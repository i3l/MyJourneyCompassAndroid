package com.microsoft.hsg.android.jc.util;

import java.util.Calendar;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class CustomUtil {
	
	private static CustomUtil instance = null;
	
	private CustomUtil(){
		//Singleton Pattern implementation
	}
	
	public static CustomUtil getInstance(){
		if(instance == null){
			return new CustomUtil();
		}
		return instance;
	}

	/**
	 * Check the hardware if there are connected to Internet or not.
	 * This method gets the list of all available networks and if any one of 
	 * them is connected returns true. If none is connected returns false.
	 * 
	 * @param context {@link Context} of the app.
	 * @return <code>true</code> if connected or <code>false</code> if not
	 */
	public boolean isNetworkAvailable(Context context) {
	    boolean available = false;
	    try {
	        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	        if (connectivity != null) {
	            NetworkInfo[] info = connectivity.getAllNetworkInfo();
	            if (info != null) {
	                for (int i = 0; i < info.length; i++) {
	                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
	                        available = true;
	                    }
	                }
	            }
	        }
	        if (available == false) {
	            NetworkInfo wiMax = connectivity.getNetworkInfo(6);

	            if (wiMax != null && wiMax.isConnected()) {
	                available = true;
	            }
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return available;
	}
	
/*	public ApproxDateTime getApproxDateTime(){
		ApproxDateTime result = new ApproxDateTime();
		StructuredApproxDate structAppDate = new StructuredApproxDate();
		structAppDate.setDate(getApproxDateFromCalendar());
		structAppDate.setTime(Time.fromCalendar(Calendar.getInstance()));
		result.setStructured(structAppDate);
		return result;		
	}
	
	private ApproxDate getApproxDateFromCalendar(){
		Calendar calendar = Calendar.getInstance();
		ApproxDate appDate = new ApproxDate();
		appDate.setD(calendar.get(Calendar.DATE));
		appDate.setM(calendar.get(Calendar.MONTH)+1);
		appDate.setY(calendar.get(Calendar.YEAR));
		return appDate;
	}*/
}
