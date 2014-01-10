package com.microsoft.hsg.android.jc;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


import com.microsoft.hsg.Request;
import com.microsoft.hsg.android.HealthVaultService;
import com.microsoft.hsg.android.PersonInfo;
import com.microsoft.hsg.android.Record;
import com.microsoft.hsg.android.ShellActivity;
import com.microsoft.hsg.android.custom.wrapper.CustomHealthTypeWrapper;
import com.microsoft.hsg.android.jc.util.CustomUtil;
import com.microsoft.hsg.android.jc.util.DBHandler;
import com.microsoft.hsg.android.symptom.Condition;
import com.microsoft.hsg.android.symptom.PainScale;
import com.microsoft.hsg.request.SimpleRequestTemplate;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class CustomActivity extends Activity {

	private HealthVaultService service;
	private Record selectedRecord;
	PainScale pScale = new PainScale();
	Context context;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.custom_activity);
		pScale.setConstipationThreshold(5);
		pScale.setPainThreshold(5);
		pScale.setSleepThreshold(5);
		pScale.setFatigueThreshold(5);
		pScale.setNauseaThreshold(5);
		
		service = HealthVaultService.getInstance();
		Button startAuth = (Button) findViewById(R.id.auth);
		startAuth.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				HealthVaultService service = HealthVaultService.getInstance();
				startActivity(ShellActivity.createAppAuthIntent(
						CustomActivity.this, service.getAppId()));
			}
		});
		Button putThing = (Button) findViewById(R.id.putThing);
		putThing.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				// EditText text = (EditText) findViewById(R.id.weightInput);

				PutCustom putAction = new PutCustom(Integer.toString(pScale
						.getPainThreshold()));
				// InitializeRecords();
				putAction.execute();
			}
		});

	}

	@Override
	protected void onResume() {
		// InitializeControls();
		super.onResume();
	}

	private void InitializeControls() {
		InitializeRecords();
	}

	private void InitializeRecords() {
		new InitializeRecords().execute();
	}

	private class InitializeRecords extends AsyncTask<Void, Void, Void> {
		private Exception exception;
		private List<Record> records = new ArrayList<Record>();
		private ProgressDialog progressDialog;

		public InitializeRecords() {
			progressDialog = ProgressDialog.show(CustomActivity.this, "",
					"Please wait for few seconds...", true);
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			try {
				if (service.getConnectionStatus() == HealthVaultService.ConnectionStatus.Connected) {
					List<PersonInfo> personInfos = service.getPersonInfoList();
					for (PersonInfo personInfo : personInfos) {
						for (Record record : personInfo.getRecords()) {
							records.add(record);
						}
					}

					// if (records.size() > 0) {
					selectedRecord = records.get(0); // }
				}
			} catch (Exception e) {
				exception = e;
			}

			return null;
		}
	}

	private class PutCustom extends AsyncTask<Void, Void, Void> {
		private String painScale;
		private Exception exception;
		ProgressDialog progressDialog;

		public PutCustom(String painThreshold) {
			painScale = painThreshold;
			progressDialog = ProgressDialog.show(CustomActivity.this, "",
					"Please wait for put...", true);
		}

	

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.os.AsyncTask#doInBackground(Params[])
		 */
		protected Void doInBackground(Void... v) {
			try {
				// EditText text = (EditText) findViewById(R.id.weightInput);
				putCustom(painScale);
			} catch (Exception e) {
				exception = e;
			}

			return null;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
		 */
		@Override
		protected void onPostExecute(Void v) {
			progressDialog.dismiss();

			if (exception == null) {
				Toast.makeText(CustomActivity.this, "Custom Type Written",
						Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(CustomActivity.this,
						"An error occurred.  " + exception.getMessage(),
						Toast.LENGTH_LONG).show();
			}
		}
	}

	private void putCustom(String value) {
		/*Custom Wrapper Code
		 * CustomHealthTypeWrapper customWrapper = new CustomHealthTypeWrapper(
				PainScale.class.getSimpleName().toString(), pScale);
		pScale.setWrapper(customWrapper);*/
		Condition condition = new Condition();
			
/*		appDateTime.
		condition.setName(new CodableValue(symptom.getSymptomName()));
		condition.setStatus(new CodableValue(symptom.getSymptomValue()));
		condition.setOnsetDate(new CodableValue(symptom.getDateTime()));*/
		
		Request request = new Request();
		StringBuilder infoBuilder = new StringBuilder();
		if (CustomUtil.getInstance().isNetworkAvailable(this)) {
			PersonInfo personInfo = service.getPersonInfoList().get(0);
			Record record = personInfo.getRecords().get(0);
			SimpleRequestTemplate template = new SimpleRequestTemplate(
					service.getConnection(), record.getPersonId(),
					record.getId());
			// SimpleRequestTemplate template = new
			// SimpleRequestTemplate(service.getConnection());

			
			infoBuilder.append("<info><thing><type-id>");
			infoBuilder.append(CustomHealthTypeWrapper.TYPE);
			infoBuilder.append("</type-id><data-xml>");
		//	infoBuilder.append(customWrapper.toXml());
			infoBuilder.append("<common/></data-xml></thing></info>");

			
			request.setMethodName("PutThings");
			request.setInfo(infoBuilder.toString());
			template.makeRequest(request);
		} else {
			infoBuilder.append("<info><thing><type-id>");
			infoBuilder.append(CustomHealthTypeWrapper.TYPE);
			infoBuilder.append("</type-id><data-xml>");
		//	infoBuilder.append(customWrapper.toXml());
			infoBuilder.append("<common/></data-xml></thing></info>");

			
			request.setMethodName("PutThings");
			request.setInfo(infoBuilder.toString());
			PackageManager packMgr = getPackageManager();
			String path = getPackageName();
			try{
				PackageInfo pInfo = packMgr.getPackageInfo(path, 0);
				path = pInfo.applicationInfo.dataDir;
			}
			catch(NameNotFoundException e){
				Log.w("MYTAG", "Name not found", e);
			}
			
			DBHandler.getInstance().addRequest(request);
//			DBHandler.getInstance().deleteTemplates();
//			DBHandler.getInstance().addTemplate(null);
			// write to db
		}
	}
}
