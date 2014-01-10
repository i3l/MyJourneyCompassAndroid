package com.microsoft.hsg.android.jc.util;

import java.util.ArrayList;
import java.util.ListIterator;

import com.microsoft.hsg.Request;
import com.microsoft.hsg.android.HealthVaultService;
import com.microsoft.hsg.android.PersonInfo;
import com.microsoft.hsg.android.Record;
import com.microsoft.hsg.android.jc.MainActivity;
import com.microsoft.hsg.request.SimpleRequestTemplate;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class ConnectivityService extends Service {
	private BroadcastReceiver connectivityReceiver;
	private HealthVaultService service;
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Log.w("ConnectivityService-Log", "onCreate()");
	}
	
	

	@Override
	public void onDestroy() {
		super.onDestroy();
		// Do not forget to unregister the receiver!!!
		this.unregisterReceiver(this.connectivityReceiver);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		final IntentFilter theFilter = new IntentFilter();
		theFilter
				.addAction(android.net.ConnectivityManager.CONNECTIVITY_ACTION);

		Log.w("ConnectivityService-Log", "onStartCommand()");
		this.connectivityReceiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				ConnectivityManager connectivityMgr = (ConnectivityManager) context
						.getSystemService(Context.CONNECTIVITY_SERVICE);

				Log.w("ConnectivityService-BroadcastReceiver", "onReceive()");
				if (connectivityMgr == null) {
					return;
				} else if (connectivityMgr.getActiveNetworkInfo() != null
						&& connectivityMgr.getActiveNetworkInfo().isConnected()) {
					Log.w("ConnectivityService-BroadcastReceiver", "connected");
					ArrayList<Request> requests = DBHandler.getInstance()
							.getRequests();
					service = HealthVaultService.getInstance();
					service.connect(getApplicationContext());
					SimpleRequestTemplate template = null;
					if (service.getConnectionStatus() == HealthVaultService.ConnectionStatus.Connected){
						PersonInfo personInfo = service.getPersonInfoList().get(0);
						Record record = personInfo.getRecords().get(0);
						template = new SimpleRequestTemplate(
								service.getConnection(), record.getPersonId(),
								record.getId());
					}
					if (template != null) {

						if (requests != null) {
							ListIterator<Request> iter = requests
									.listIterator();
							while (iter.hasNext()) {
								Request currentRequest = iter.next();
								template.makeRequest(currentRequest);
							}
						}
					}

				}
			}
		};
		// Registers the receiver so that your service will listen for
		// broadcasts
		this.registerReceiver(this.connectivityReceiver, theFilter);
		 Toast.makeText(this, "Started!", Toast.LENGTH_LONG);
		return super.onStartCommand(intent, flags, startId);
	}
}
