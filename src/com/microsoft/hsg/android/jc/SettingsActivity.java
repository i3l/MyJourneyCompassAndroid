package com.microsoft.hsg.android.jc;

import java.io.File;

import com.microsoft.hsg.android.HealthVaultFileSettings;
import com.microsoft.hsg.android.HealthVaultService;
import com.microsoft.hsg.android.PersonInfo;
import com.microsoft.hsg.android.Record;
import com.microsoft.hsg.android.HealthVaultService.ConnectionStatus;
import com.microsoft.hsg.android.HealthVaultSettings;
import com.microsoft.hsg.android.jc.util.AppConfiguration;
import com.microsoft.hsg.android.jc.util.CustomUtil;
import com.microsoft.hsg.android.jc.util.DBHandler;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources.NotFoundException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class SettingsActivity extends Activity {

	// private HealthVaultService hvService;
	private Button symptomsButton;
	private Button reportButton;
	Button disconnectButton;
	HealthVaultService hvService;

	private static String patientName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		Log.w("SettingsActivity", "onCreate");

		setContentView(R.layout.settings_activity);
		hvService = HealthVaultService.getInstance();
		if (hvService == null) {
			hvService = MainActivity.setHeathVaultInstance(this);
		}

		disconnectButton = (Button) findViewById(R.id.disconnectButton);
		symptomsButton = (Button) findViewById(R.id.symptom);
		reportButton = (Button) findViewById(R.id.report);
		disconnectButton.setOnClickListener(new View.OnClickListener() {
			private ProgressDialog progress;

			public void onClick(View view) {
				if (hvService.getConnectionStatus() != ConnectionStatus.Connected) {
					progress = ProgressDialog.show(SettingsActivity.this, "",
							"Please wait...", true);

					new AsyncTask<Context, Void, Intent>() {
						private Exception exception;

						protected Intent doInBackground(Context... context) {
							try {
								return hvService.connect(SettingsActivity.this);
							} catch (Exception e) {
								exception = e;
							}
							return null;
						}

						@Override
						protected void onPostExecute(Intent intent) {
							progress.dismiss();
							if (exception != null) {
								Toast.makeText(SettingsActivity.this,
										exception.getMessage(),
										Toast.LENGTH_LONG).show();
							} else {
								if (intent != null) {
									startActivity(intent);
								} else {
									Toast.makeText(
											SettingsActivity.this,
											"Critical Error Occurred. Please Try Again",
											Toast.LENGTH_LONG).show();
									hvService = MainActivity
											.setHeathVaultInstance(SettingsActivity.this);
								}
							}
						}
					}.execute();
				} else {
					SettingsActivity.this.Confirm();
				}
			}
		});
	}

	public void Confirm() {
		AlertDialog dialog = new AlertDialog.Builder(this).create();
		dialog.setTitle(getResources().getString(R.string.hv_dc_warning));
		dialog.setMessage(getResources().getString(R.string.hv_dc_msg));
		dialog.setCancelable(false);
		dialog.setButton(DialogInterface.BUTTON_POSITIVE, getResources()
				.getString(R.string.hv_dc_YES),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int buttonId) {
						disconnectButton
								.setText("Connect to Microsoft¨ HealthVault¨");
						hvService.reset();
						CookieSyncManager.createInstance(SettingsActivity.this);
						CookieManager cookieManager = CookieManager
								.getInstance();
						cookieManager.removeAllCookie();
						hvService = MainActivity
								.setHeathVaultInstance(SettingsActivity.this);
						Toast.makeText(
								SettingsActivity.this,
								"Device Disconnected.\nYou have to connect again to track your symptoms.",
								Toast.LENGTH_LONG).show();
					}
				});
		dialog.setButton(DialogInterface.BUTTON_NEGATIVE, getResources()
				.getString(R.string.hv_dc_NO),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int buttonId) {
						Toast.makeText(SettingsActivity.this,
								"Disconnect Canceled", Toast.LENGTH_LONG)
								.show();
					}
				});
		dialog.setIcon(android.R.drawable.ic_dialog_alert);
		dialog.show();
	}

	@Override
	protected void onResume() {
		InitializeControls();
		super.onResume();
	}

	public void loadSymptomActivity(View arg) {
		if (hvService.getConnectionStatus() == ConnectionStatus.Connected) {
			Intent intent = new Intent(SettingsActivity.this,
					SymptomActivity.class);
			SettingsActivity.this.startActivity(intent);
		} else {
			Toast.makeText(
					SettingsActivity.this,
					"You are not connected to HealthVault.\nPlease connect to HealthVault before you begin symptom tracking.",
					Toast.LENGTH_LONG).show();
		}
	}

	public void loadReportActivity(View arg) {
		if (hvService.getConnectionStatus() == ConnectionStatus.Connected) {
			Intent intent = new Intent(SettingsActivity.this,
					ReportActivity.class);
			SettingsActivity.this.startActivity(intent);
		} else {
			Toast.makeText(
					SettingsActivity.this,
					"You are not connected to HealthVault.\nPlease connect to HealthVault before you begin symptom reporting.",
					Toast.LENGTH_LONG).show();
		}
	}

	private void InitializeControls() {
		// TextView msg = (TextView) findViewById(R.id.welcomeText);
		Log.w("SettingsActivity", "InitalizeControls");

		if (CustomUtil.getInstance().isNetworkAvailable(this)) {
			switch (hvService.getConnectionStatus()) {
			case Connected:
				disconnectButton
						.setText("Disconnect from Microsoft¨ HealthVault¨");
				new AsyncTask<Context, Void, Intent>() {
					private Exception exception;

					protected Intent doInBackground(Context... context) {
						try {
							PersonInfo personInfo = hvService
									.getPersonInfoList().get(0);
							Record record = personInfo.getRecords().get(0);
							patientName = (record.getName());
						} catch (Exception e) {
							exception = e;
						}
						return null;
					}

					@Override
					protected void onPostExecute(Intent intent) {
						if (exception != null) {
							Toast.makeText(
									SettingsActivity.this,
									"Getting Name Failed: "
											+ exception.getMessage(),
									Toast.LENGTH_LONG).show();
						}
					}
				}.execute();

				break;
			case NotConnected:
				disconnectButton.setText("Connect to Microsoft¨ HealthVault¨");
				break;
			}
		} else {
			Toast.makeText(
					SettingsActivity.this,
					"Notice: Network is not available.",
					Toast.LENGTH_LONG).show();
		}
	}

	public static String getPatientName() {
		return patientName;
	}
}
