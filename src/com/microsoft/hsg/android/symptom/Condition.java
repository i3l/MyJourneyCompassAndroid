package com.microsoft.hsg.android.symptom;

import java.util.Calendar;

public class Condition {

	public static final String TYPE = "7ea7a1f9-880b-4bd4-b593-f5660f20eda8";
	private String name;
	private String status;
	private Calendar calendar;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Calendar getCalendar() {
		return calendar;
	}

	public void setCalendar(Calendar calendar) {
		this.calendar = calendar;
	}

	public Condition() {
		super();
		calendar = Calendar.getInstance();
	}

	public String toXml() {
		final Calendar c = Calendar.getInstance();
		
		StringBuilder infoBuilder = new StringBuilder(256);
		infoBuilder.append("<condition><name><text>");
		infoBuilder.append(name);
		infoBuilder.append("</text></name><onset-date><structured><date><y>");
		infoBuilder.append(c.get(Calendar.YEAR));
		infoBuilder.append("</y><m>");
		infoBuilder.append(c.get(Calendar.MONTH) + 1);
		infoBuilder.append("</m><d>");
		infoBuilder.append(c.get(Calendar.DATE));
		infoBuilder.append("</d></date><time><h>");
		infoBuilder.append(c.get(Calendar.HOUR_OF_DAY));
		infoBuilder.append("</h><m>");
		infoBuilder.append(c.get(Calendar.MINUTE));
		infoBuilder.append("</m><s>");
		infoBuilder.append(c.get(Calendar.SECOND));
		infoBuilder.append("</s><f>");
		infoBuilder.append(c.get(Calendar.MILLISECOND));
		infoBuilder.append("</f></time></structured></onset-date><status><text>");
		infoBuilder.append(status);
		infoBuilder.append("</text></status></condition>");

		return infoBuilder.toString();
	}

}
