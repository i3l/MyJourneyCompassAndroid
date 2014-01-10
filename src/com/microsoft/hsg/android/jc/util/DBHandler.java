package com.microsoft.hsg.android.jc.util;

import java.io.IOException;
import java.util.ArrayList;

//import android.content.Context;
//import android.content.SharedPreferences;
//import android.content.SharedPreferences.Editor;
import android.util.Log;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.config.EmbeddedConfiguration;
import com.microsoft.hsg.Request;
//import com.microsoft.hsg.request.SimpleRequestTemplate;

public class DBHandler {

	private static DBHandler instance;
	private ObjectContainer dbRequestContainer;
//	private ObjectContainer dbTemplateContainer;
	private static String directory;
	
	
	public String getDirectory() {
		return directory;
	}

	public void setDirectory(String directory) {
		this.directory = directory;
	}

	private DBHandler(){
		//Singleton
	}

	public static DBHandler getInstance() {
		if(instance != null)
			return instance;
		
		instance = new DBHandler();
		return instance;
	}

//	private void openTemplateDb(){
//		dbTemplateContainer = Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(), directory+Constants.REQ_TEMPLATE_FILE_NAME);
//	}
//	
//	private void closeTemplateDb(){
//		dbTemplateContainer.close();
//	}

	private void openRequestDb(){
		if (dbRequestContainer == null || dbRequestContainer.ext().isClosed()) 
			dbRequestContainer = Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(), directory+Constants.REQUEST_DB_FILE_NAME);
	}
	
	private void closeRequestDb(){
		if (dbRequestContainer != null)
			dbRequestContainer.close();
	}
	
	public ArrayList<Request> getRequests(){
		openRequestDb();
		ArrayList<Request> result = new ArrayList<Request>();
		//Request req = new Request();
		ObjectSet<Request> objectList = dbRequestContainer.queryByExample(null);
		Log.d("DBHandler",":getRequest:");
		while(objectList.hasNext()){
			result.add(objectList.next());
		}
		closeRequestDb();
		return result;
	}
	
	public void deleteRequests(){
		openRequestDb();
		//Request req = new Request();
		ObjectSet<Request> objectList = dbRequestContainer.queryByExample(null);
		while(objectList.hasNext()){
			Request r = (Request)objectList.next();
			dbRequestContainer.delete(r);
		}
		closeRequestDb();
	}
	
	public void deleteRequest(Request entry){
		openRequestDb();
		ObjectSet<Request> objectList = dbRequestContainer.queryByExample(entry);
		while(objectList.hasNext()){
			Request r = (Request)objectList.next();
			dbRequestContainer.delete(r);
		}
		closeRequestDb();
	}
	
	public void addRequest(Request entry){
		openRequestDb();
		Log.w("DBHandler",":addRequest:: "+entry.getMethodName());
		dbRequestContainer.store(entry);
		dbRequestContainer.commit();
		closeRequestDb();
		
	}
	
//	public void addTemplate(SimpleRequestTemplate entry){
//		openTemplateDb();
//		dbTemplateContainer.store(entry);
//		dbTemplateContainer.commit();
//		closeTemplateDb();
//	}
//	
//	public void deleteTemplates(){
//		openTemplateDb();
//		ObjectSet<Request> objectList = dbTemplateContainer.queryByExample(null);
//		while(objectList.hasNext()){
//			Request r = (Request)objectList.next();
//			dbTemplateContainer.delete(r);
//		}
//		closeTemplateDb();
//	}
//	public SimpleRequestTemplate getTemplate(){
//		openTemplateDb();
//		SimpleRequestTemplate result = null;
//		ObjectSet<SimpleRequestTemplate> objectList = dbTemplateContainer.queryByExample(null);
//		if(objectList != null){
//			result = (SimpleRequestTemplate)objectList.next();
//		}
//		closeTemplateDb();
//		return result;
//	}
	
	private EmbeddedConfiguration dbConfig() throws IOException {
		
		EmbeddedConfiguration configuration = Db4oEmbedded.newConfiguration();
		
		configuration.common().objectClass(Request.class).objectField("name").indexed(true);
		
		configuration.common().objectClass(Request.class).cascadeOnUpdate(true);
		
		configuration.common().objectClass(Request.class).cascadeOnActivate(true);
		
		return configuration;
		
		}
}
