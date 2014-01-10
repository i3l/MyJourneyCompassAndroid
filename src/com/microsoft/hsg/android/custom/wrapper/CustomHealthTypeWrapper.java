//**
// * 
// */
//package com.microsoft.hsg.android.custom.wrapper;
//
//import javax.xml.bind.annotation.XmlAccessType;
//import javax.xml.bind.annotation.XmlAccessorType;
//
//import com.microsoft.hsg.android.custom.HealthRecordItemCustomBase;
//import com.microsoft.hsg.thing.oxm.jaxb.application.AppSpecific;
//import com.microsoft.hsg.thing.oxm.jaxb.dates.DateTime;
//
///**
// * @author Vimal
// * 
// */
//
//@XmlAccessorType(XmlAccessType.FIELD)
//public class CustomHealthTypeWrapper extends AppSpecific {
//
//	final String nullObjectTypeName = "NULLOBJECT";
//
//	public String wrappedObjectTypeName;
//
//	private HealthRecordItemCustomBase wrappedObject = null;
//
//	public CustomHealthTypeWrapper(String wrappedObjectTypeName,
//			HealthRecordItemCustomBase wrappedObject) {
//		super();
//		this.wrappedObjectTypeName = wrappedObjectTypeName;
//		this.wrappedObject = wrappedObject;
//	}
//
//	public CustomHealthTypeWrapper() {
//		// TODO Auto-generated constructor stub
//	}
//
//	/**
//	 * Gets the value of the wrappedObjectTypeName property.
//	 * 
//	 * @return possible object is {@link String }
//	 * 
//	 */
//	public String getWrappedObjectTypeName() {
//		return wrappedObjectTypeName;
//	}
//
//	/**
//	 * Sets the value of the wrappedObjectTypeName property.
//	 * 
//	 * @param value
//	 *            allowed object is {@link String }
//	 * 
//	 */
//	public void setWrappedObjectTypeName(String wrappedObjectTypeName) {
//		this.wrappedObjectTypeName = wrappedObjectTypeName;
//	}
//
//	/**
//	 * Gets the value of the wrappedObject property.
//	 * 
//	 * @return possible object is {@link Object }
//	 * 
//	 */
//	public HealthRecordItemCustomBase getWrappedObject() {
//		return wrappedObject;
//	}
//
//	/**
//	 * Sets the value of the wrappedObject property.
//	 * 
//	 * @param value
//	 *            allowed object is {@link HealthRecordItemCustomBase }
//	 * 
//	 */
//	public void setWrappedObject(HealthRecordItemCustomBase wrappedObject) {
//		this.wrappedObject = wrappedObject;
//	}
//
//}
package com.microsoft.hsg.android.custom.wrapper;

import java.util.Calendar;

import org.xmlpull.v1.XmlPullParser;

import com.microsoft.hsg.android.XmlUtils;
import com.microsoft.hsg.android.jc.HealthRecordItemCustomBase;

public class CustomHealthTypeWrapper {
	public static final String nullObjectTypeName = "NULLOBJECT";
	public static final String TYPE = "a5033c9d-08cf-4204-9bd3-cb412ce39fc0";

	public String wrappedObjectTypeName;
	private Calendar calendar;
	private HealthRecordItemCustomBase wrappedObject = null;

	public CustomHealthTypeWrapper(String wrappedObjectTypeName,
			HealthRecordItemCustomBase wrappedObject) {
		super();
		this.calendar = Calendar.getInstance();
		this.wrappedObjectTypeName = wrappedObjectTypeName;
		if(this.wrappedObject == null){
			this.wrappedObject = wrappedObject;
		}
	}

	public String getWrappedObjectTypeName() {
		return wrappedObjectTypeName;
	}

	public void setWrappedObjectTypeName(String wrappedObjectTypeName) {
		this.wrappedObjectTypeName = wrappedObjectTypeName;
	}

	public HealthRecordItemCustomBase getWrappedObject() {
		return wrappedObject;
	}

	public void setWrappedObject(HealthRecordItemCustomBase wrappedObject) {
		this.wrappedObject = wrappedObject;
	}

	

	
	
	
	
	 public String toXml()
	    {    
	        StringBuilder infoBuilder = new StringBuilder(256);
	        infoBuilder.append("<app-specific><format-appid>Custom</format-appid><format-tag>");
	        if(wrappedObject != null )
	        	if(wrappedObjectTypeName != null)
	        		infoBuilder.append(wrappedObjectTypeName);
	        	else
	        		infoBuilder.append(nullObjectTypeName);
	        infoBuilder.append("</format-tag><when><date><y>");
	        infoBuilder.append(calendar.get(Calendar.YEAR));
	        infoBuilder.append("</y><m>");
	        infoBuilder.append(calendar.get(Calendar.MONTH) + 1);
	        infoBuilder.append("</m><d>");
	        infoBuilder.append(calendar.get(Calendar.DATE));
	        infoBuilder.append("</d></date><time><h>");
	        infoBuilder.append(calendar.get(Calendar.HOUR_OF_DAY));
	        infoBuilder.append("</h><m>");
	        infoBuilder.append(calendar.get(Calendar.MINUTE));
	        infoBuilder.append("</m><s>");
	        infoBuilder.append(calendar.get(Calendar.SECOND));
	        infoBuilder.append("</s></time></when><summary></summary>");
	        if(wrappedObject != null){
	        	infoBuilder.append("<CustomType>");
	        	infoBuilder.append(wrappedObject.toXml());
	        	infoBuilder.append("</CustomType>");
	        }
	       infoBuilder.append("</app-specific>");
	        
	        return infoBuilder.toString();
	    }
	    
	    public static void createFromXml(XmlPullParser parser) throws Exception
	    {
	        String value = "";
	        int depth = parser.getDepth();
	        while (parser.nextTag() == XmlPullParser.START_TAG
	            && parser.getDepth() > depth) {
	            if ("value".equals(parser.getName())) {
	                int valueDepth = parser.getDepth();
	                while (parser.nextTag() == XmlPullParser.START_TAG
	                        && parser.getDepth() > valueDepth) {
	                    if ("kg".equals(parser.getName())) {
	                        value = parser.nextText();
	                    }
	                    else {
	                        XmlUtils.skipSubTree(parser);
	                    }
	                }
	            }
	            else {
	                XmlUtils.skipSubTree(parser);
	            }
	        }
	        
	        
	    }
	
	
	
}
