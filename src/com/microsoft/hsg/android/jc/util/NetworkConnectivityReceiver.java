package com.microsoft.hsg.android.jc.util;

import java.io.InputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Scanner;

import com.microsoft.hsg.Request;
import com.microsoft.hsg.android.HealthVaultService;
import com.microsoft.hsg.android.PersonInfo;
import com.microsoft.hsg.android.Record;
import com.microsoft.hsg.android.jc.SymptomActivity;
import com.microsoft.hsg.request.SimpleRequestTemplate;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class NetworkConnectivityReceiver extends BroadcastReceiver {
	private HealthVaultService service;

	@Override
	public void onReceive(Context context, Intent intent) {
		ConnectivityManager connectivityMgr = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		if (connectivityMgr == null) {
			Log.w("NetworkConnectivityReceiver", "onReceive() returned");
			return;
		} else if (connectivityMgr.getActiveNetworkInfo() != null
				&& connectivityMgr.getActiveNetworkInfo().isConnected()) {
			Log.d("NetworkConnectivityReceiver",
					"onReceive() SendOfflineRequest");
			SendOfflineRequest sendRequests = new SendOfflineRequest(context);
			sendRequests.execute();
		}

	}

	public boolean inetAddr() {

		boolean x1 = false;

		try {
			Socket s = new Socket("utcnist.colorado.edu", 37);
			s.setSoTimeout(5000);
			
			InputStream i = s.getInputStream();

			Scanner scan = new Scanner(i);

			while (scan.hasNextLine()) {

				System.out.println(scan.nextLine());
				x1 = true;
			}
		} catch (Exception e) {

			x1 = false;
		}

		return x1;

	}

	private class SendOfflineRequest extends AsyncTask<Void, Void, Void> {
		//private String painScale;
		//private Exception exception;
		private Context context;

		public SendOfflineRequest(Context context) {
			this.context = context;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.os.AsyncTask#doInBackground(Params[])
		 */
		protected Void doInBackground(Void... v) {
			try {

				service = HealthVaultService.getInstance();
				service.connect(context);
				SimpleRequestTemplate template = null;
				if (service.getConnectionStatus() == HealthVaultService.ConnectionStatus.Connected) {
					ArrayList<Request> requests = DBHandler.getInstance()
							.getRequests();
					if (requests.size() <= 0) return null;
					
					int cnt=0;
					Log.d("NetworkConnectivityReceiver", "HV connected");
					while (!inetAddr()) {
						cnt++;
						if (cnt >= 3) {
							Log.d("NetworkConnectivityReceiver", "NetworkConnectivityReceiver Canceled");
							return null;
						}
					}
					PersonInfo personInfo = service.getPersonInfoList().get(0);
					Record record = personInfo.getRecords().get(0);
					template = new SimpleRequestTemplate(
							service.getConnection(), record.getPersonId(),
							record.getId());
					Log.w("NetworkConnectivityReceiver", "record details : "
							+ record.getName());

					if (template != null) {
						if (requests != null && requests.size() > 0) {
							ListIterator<Request> iter = requests
									.listIterator();
							Log.w("NetworkConnectivityReceiver",
									"request count : " + requests.size());
							while (iter.hasNext()) {
								Request currentRequest = iter.next();
								template.makeRequest(currentRequest);
								// DBHandler.getInstance().deleteRequest(currentRequest);
							}
						}
						DBHandler.getInstance().deleteRequests();
					}
				}
			} catch (Exception e) {
				//exception = e;
				Log.w("NetworkConnectivityReceiver", "Error Sending Data:"+e.getMessage());
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

		}
	}

}
