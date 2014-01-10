package com.microsoft.hsg.android.jc;

import java.io.File;

import com.microsoft.hsg.android.HealthVaultFileSettings;
import com.microsoft.hsg.android.HealthVaultService;
import com.microsoft.hsg.android.HealthVaultSettings;
import com.microsoft.hsg.android.jc.util.AppConfiguration;
import com.microsoft.hsg.android.jc.util.DBHandler;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {
	HealthVaultService service;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Log.w("MainActivity", "onCreate");

		setContentView(R.layout.activity_main);

		File f = this.getDir("request", Context.MODE_PRIVATE);
		DBHandler.getInstance().setDirectory(f.getPath());
		Log.w("MainActivity", f.getPath());

		if (service == null) {
			service = setHeathVaultInstance(this);
		}

		if (!checkIfFirstLaunch(this)) {
			Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
			startActivity(intent);
		} else {
			Intent intent = new Intent(MainActivity.this, SymptomActivity.class);
			startActivity(intent);
		}
	}

	private boolean checkIfFirstLaunch(Context context) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		boolean first = prefs.getBoolean("first", false);
		if (!first) {
			final SharedPreferences.Editor editor = prefs.edit();
			editor.putBoolean("first", true);
			editor.commit();
		}
		return first;
	}

	public static HealthVaultService setHeathVaultInstance(Context ctx) {
		HealthVaultSettings settings = new HealthVaultFileSettings(ctx);
		settings.setMasterAppId("91b3dec1-f96f-4240-8d31-16620b1ab36f");
		// settings.setAppId("b8b45670-ed69-465c-8fab-41ec95e12b45");
		settings.setServiceUrl("https://platform.healthvault.com/platform/wildcat.ashx");
		settings.setShellUrl("https://account.healthvault.com");

		HealthVaultService.initialize(settings);

		return HealthVaultService.getInstance();
	}

}
