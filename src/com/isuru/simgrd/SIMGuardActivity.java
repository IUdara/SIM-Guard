package com.isuru.simgrd;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

public class SIMGuardActivity extends Activity implements OnClickListener {

	ToggleButton save;
	Button set;
	EditText toInform;
	String FILENAME = "SimSerial";
	String CONFIGNAME = "Configuration";
	String PHONENO = "InformTo";
	String isOn = "false";
	FileOutputStream fos;
	FileInputStream fis;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		try {

			fis = openFileInput(CONFIGNAME);
			byte[] dataBytes = new byte[fis.available()];
			while (fis.read(dataBytes) != -1) {
				isOn = new String(dataBytes);
			}

			fis.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		initVars();

		save.setOnClickListener(this);
		set.setOnClickListener(this);

		try {
			fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);
			fos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			fos = openFileOutput(PHONENO, Context.MODE_PRIVATE);
			fos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void initVars() {
		// TODO Auto-generated method stub
		save = (ToggleButton) findViewById(R.id.tbChange);
		set = (Button) findViewById(R.id.bSet);
		toInform = (EditText) findViewById(R.id.etPhone);
		if (isOn.equals("true")) {
			save.setChecked(true);
		} else {
			save.setChecked(false);
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		String storedSimSerial = "Not Assigned";
		switch (v.getId()) {
		case R.id.tbChange: {
			if (save.isChecked()) {
				TelephonyManager telephoneMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
				storedSimSerial = telephoneMgr.getSimSerialNumber();
				isOn = "true";
			} else {
				isOn = "false";
			}

			try {
				fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);
				fos.write(storedSimSerial.getBytes());
				fos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			try {
				fos = openFileOutput(CONFIGNAME, Context.MODE_PRIVATE);
				fos.write(isOn.getBytes());
				fos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		}

		case R.id.bSet: {
			String phoneNo = toInform.getText().toString();

			if (phoneNo.length() == 10 && phoneNo.startsWith("0")) {
				try {
					fos = openFileOutput(PHONENO, Context.MODE_PRIVATE);
					fos.write(phoneNo.getBytes());
					fos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Toast.makeText(this, "Phone number saved.", Toast.LENGTH_LONG)
						.show();
			} else {
				Toast.makeText(this, "Enter a valid phone number",
						Toast.LENGTH_LONG).show();
			}
			break;
		}

		}
	}

	@Override
	public boolean onCreateOptionsMenu(android.view.Menu menu) {
		// TODO Auto-generated method stub
		super.onCreateOptionsMenu(menu);
		MenuInflater popUp = getMenuInflater();
		popUp.inflate(R.menu.popup, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.iExit:
			finish();
			break;
		default:
			break;
		}
		return true;
	}
}