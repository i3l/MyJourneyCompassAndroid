//package com.microsoft.hsg.android.custom.painscale;
//
//import javax.xml.bind.annotation.XmlAccessType;
//import javax.xml.bind.annotation.XmlAccessorType;
//import javax.xml.bind.annotation.XmlElement;
//
//import com.microsoft.hsg.android.custom.HealthRecordItemCustomBase;
//import com.microsoft.hsg.thing.oxm.jaxb.dates.DateTime;
//
//@XmlAccessorType(XmlAccessType.FIELD)
//public class PainScale extends HealthRecordItemCustomBase {
//	
//	@XmlElement(required = true)
//	int painThreshold;
//	@XmlElement(required = true)
//	DateTime when;
//
//	/**
//	 * Gets the value of the painThreshold property.
//	 * 
//	 * @return possible object is {@link int }
//	 * 
//	 */
//	public int getPainThreshold() {
//		return painThreshold;
//	}
//
//	/**
//	 * Sets the value of the wrappedObject property.
//	 * 
//	 * @param value
//	 *            allowed object is {@link int }
//	 * 
//	 */
//	public void setPainThreshold(int painThreshold) {
//		this.painThreshold = painThreshold;
//	}
//
//	/**
//	 * Gets the value of the when property.
//	 * 
//	 * @return possible object is {@link DateTime }
//	 * 
//	 */
//	public DateTime getWhen() {
//		return when;
//	}
//
//	/**
//	 * Sets the value of the wrappedObject property.
//	 * 
//	 * @param value
//	 *            allowed object is {@link HealthRecordItemCustomBase }
//	 * 
//	 */
//	public void setWhen(DateTime when) {
//		this.when = when;
//	}
//
//}
package com.microsoft.hsg.android.symptom;

import java.util.Calendar;

import org.xmlpull.v1.XmlPullParser;

import com.microsoft.hsg.android.XmlUtils;
import com.microsoft.hsg.android.jc.HealthRecordItemCustomBase;
//import com.microsoft.hsg.thing.oxm.jaxb.dates.DateTime;


public class PainScale extends HealthRecordItemCustomBase {
	
	int painThreshold;
	int nauseaThreshold;
	int sleepThreshold;
	int constipationThreshold;
	int fatigueThreshold;
	//DateTime when;

	Calendar calendar;
	
	public PainScale(){
		this.calendar = Calendar.getInstance();
	}
	
    public int getNauseaThreshold() {
		return nauseaThreshold;
	}


    public void setNauseaThreshold(int nauseaThreshold) {
		this.nauseaThreshold = nauseaThreshold;
	}


    public int getSleepThreshold() {
		return sleepThreshold;
	}


    public void setSleepThreshold(int sleepThreshold) {
		this.sleepThreshold = sleepThreshold;
	}


    public int getConstipationThreshold() {
		return constipationThreshold;
	}


    public void setConstipationThreshold(int constipationThreshold) {
		this.constipationThreshold = constipationThreshold;
	}


    public int getFatigueThreshold() {
		return fatigueThreshold;
	}


    public void setFatigueThreshold(int fatigueThreshold) {
		this.fatigueThreshold = fatigueThreshold;
	}


	
	public int getPainThreshold() {
		return painThreshold;
	}

	public void setPainThreshold(int painThreshold) {
		this.painThreshold = painThreshold;
	}



	public String toXml()
    {    
        StringBuilder infoBuilder = new StringBuilder(256);
        infoBuilder.append("<PainScale painThreshold=\"");
        infoBuilder.append(painThreshold);
        infoBuilder.append("\" nauseaThreshold=\"");
        infoBuilder.append(nauseaThreshold);

        infoBuilder.append("\" sleepThreshold=\"");
        infoBuilder.append(sleepThreshold);

        infoBuilder.append("\" constipationThreshold=\"");
        infoBuilder.append(constipationThreshold);

        infoBuilder.append("\" fatigueThreshold=\"");
        infoBuilder.append(fatigueThreshold);

        infoBuilder.append("\" when=\"");
        infoBuilder.append(calendar.get(Calendar.MONTH) + 1);
        infoBuilder.append("/");
        infoBuilder.append(calendar.get(Calendar.DATE));
        infoBuilder.append("/");
        infoBuilder.append(calendar.get(Calendar.YEAR));
        infoBuilder.append(" ");
        infoBuilder.append(calendar.get(Calendar.HOUR));
        infoBuilder.append(":");
        infoBuilder.append(calendar.get(Calendar.MINUTE));
        infoBuilder.append(":");
        infoBuilder.append(calendar.get(Calendar.SECOND));
        infoBuilder.append(" ").append(calendar.get(Calendar.AM_PM)==Calendar.AM?"AM":"PM").append("\"");

        infoBuilder.append("/>");
        
        
        return infoBuilder.toString();
    }
    
    public static PainScale createFromXml(XmlPullParser parser) throws Exception
    {
        String value = "";
        int depth = parser.getDepth();
        while (parser.nextTag() == XmlPullParser.START_TAG
            && parser.getDepth() > depth) {
            if ("PainScale".equals(parser.getName())) {
                int valueDepth = parser.getDepth();
                while (parser.nextTag() == XmlPullParser.START_TAG
                        && parser.getDepth() > valueDepth) {
                    if ("painThreshold".equals(parser.getName())) {
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
        
        return new PainScale();
    }
}