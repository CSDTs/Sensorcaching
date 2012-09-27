package edu.rpi.communitysensors.android;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class BluetoothActivity extends Activity {
	private TextView sensorbtname;
	
	private BluetoothAdapter mBluetoothAdapter;
	private BluetoothSocket mSocket;
	private BluetoothDevice mDevice;
	private OutputStream mOutputStream;
	private InputStream mInputStream;
	private Thread workerThread;
	private byte[] readBuffer;
	private int readBufferPosition;
	private int counter;
	private volatile boolean stopWorker;
	private DataStoreHelper dsh;
	private SQLiteDatabase db;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bluetooth);
		
		Button connectButton = (Button) findViewById(R.id.b_butconnect);
		Button uploadButton = (Button) findViewById(R.id.b_butupload);
		sensorbtname = (TextView) findViewById(R.id.b_textsensorname);
		
		dsh = new DataStoreHelper(getApplicationContext());
		db = dsh.getWritableDatabase();
		
		//ConnectButton
		//Connect to the Bluetooth Device
		connectButton.setOnClickListener(ConnectBT);
	}
	
	private OnClickListener ConnectBT = new OnClickListener() {

		@Override
		public void onClick(View v) {
			try {
				findBT();
				openBT();
				closeBT();
			}
			catch (IOException ex) {}			
		}		
	};
	
	private void findBT() {
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if(mBluetoothAdapter == null) {
			sensorbtname.setText("Not connected");
			Toast.makeText(this, "No bluetooth devices found", Toast.LENGTH_SHORT).show();
		}
		if(!mBluetoothAdapter.isEnabled()) {
			Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableBluetooth, 0);
		}
		Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
		String devicename = "";
		if(pairedDevices.size() > 0) {
			for(BluetoothDevice device : pairedDevices){
				devicename = device.getName();
				mDevice = device;
				break;
			}
		}
		sensorbtname.setText("Connected to " + devicename);
		Toast.makeText(this, "Bluetooth device found", Toast.LENGTH_SHORT).show();
	}
	private void openBT() throws IOException {
		UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb"); //Standard SerialPortService ID
        mSocket = mDevice.createRfcommSocketToServiceRecord(uuid);        
        mSocket.connect();
        mOutputStream = mSocket.getOutputStream();
        mInputStream = mSocket.getInputStream();
        
        downloadBT();
        
        Toast.makeText(this, "Downloading Data", Toast.LENGTH_SHORT).show();
	}
	private void downloadBT() {
		
		final Handler handler = new Handler();
		final byte deliminter = 10; // This defines the end of character
									// This is the ASCII code for a newline character
		stopWorker = false;
		readBufferPosition = 0;
		readBuffer = new byte[1024];
		workerThread = new Thread(new Runnable () {
			public void run() {
				while(!Thread.currentThread().isInterrupted() && !stopWorker) {
					try {
						int bytesAvailable = mInputStream.available();
						if(bytesAvailable > 0) {
							byte[] packetBytes = new byte[bytesAvailable];
							mInputStream.read(packetBytes);
							for(int i = 0; i < bytesAvailable; i++) {
								byte b = packetBytes[i];
								if(b == deliminter) {
									byte[] encodedBytes = new byte[readBufferPosition];
									System.arraycopy(readBuffer, 0, encodedBytes, 0, encodedBytes.length);
                                    final String data = new String(encodedBytes, "US-ASCII");
                                    readBufferPosition = 0;
                                    
                                    handler.post(new Runnable() {
                                        public void run() {
                                            saveData(data);
                                        }
                                    });
                                }
								else {
                                    readBuffer[readBufferPosition++] = b;
                                }
							}
						}
					}
					catch (IOException ex)  {
	                    stopWorker = true;
	                }
				}
			}
		});
		workerThread.start();
	}
	
	private void saveData(String data) {
		Log.d("SAVE_DATA", data);
	}
	
	private void closeBT() throws IOException {
		stopWorker = true;
		mOutputStream.close();
		mInputStream.close();
		mSocket.close();
		db.close();
		dsh.close();
		Toast.makeText(this, "Bluetooth Closed", Toast.LENGTH_SHORT).show();
	}
}
