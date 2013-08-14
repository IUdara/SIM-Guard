package com.isuru.simgrd;

import java.io.FileInputStream;
import java.io.IOException;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

public class SIMDetect extends Service {
	
	String storedSimSerial;
	String currentSimSerial;

	String FILENAME = "SimSerial";
	String PHONENO = "InformTo";
	String phoneNo;
	FileInputStream fis;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();

		initVar();

		try {

			fis = openFileInput(FILENAME);
			byte[] dataBytes = new byte[fis.available()];
			while (fis.read(dataBytes) != -1) {
				storedSimSerial = new String(dataBytes);
			}

			fis.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void initVar() {
		// TODO Auto-generated method stub
		fis = null;
		storedSimSerial = null;
		phoneNo = null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();

		Log.e("SimSerial::", storedSimSerial);

		TelephonyManager tmMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		currentSimSerial = tmMgr.getSimSerialNumber();
		Log.e("Current Sim Serial::", currentSimSerial);

		if (currentSimSerial.equals(storedSimSerial)
				|| storedSimSerial.equals("Not Assigned")) {
			Log.e("Sim Status", "Sim's not changed !!!");
			Toast.makeText(this, "Sim's not changed !!!", Toast.LENGTH_LONG)
					.show();
		} else {
			Log.e("Sim Status", "Sim's changed !!!");
			Toast.makeText(this, "Sim's changed !!!", Toast.LENGTH_LONG).show();
			try {

				fis = openFileInput(PHONENO);
				byte[] dataBytes = new byte[fis.available()];
				while (fis.read(dataBytes) != -1) {
					phoneNo = new String(dataBytes);
				}

				fis.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			this.sendSms("Sim's changed. New SIM Serial No: "
					+ currentSimSerial);
		}

		this.stopSelf();

		return Service.START_STICKY;
	}

	@Override
	public void onDestroy() {

		super.onDestroy();
		Toast.makeText(this, "Service Destroy", Toast.LENGTH_LONG).show();
	}

	private void sendSms(String message) {
		SmsManager manager = SmsManager.getDefault();
		manager.sendTextMessage(phoneNo, null, message, null, null);
	}

}
