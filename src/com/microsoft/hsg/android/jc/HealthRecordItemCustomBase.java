/**
 * 
 */
package com.microsoft.hsg.android.jc;

import com.microsoft.hsg.android.custom.wrapper.CustomHealthTypeWrapper;

/**
 * @author Vimal
 *
 */
public abstract class HealthRecordItemCustomBase {

	protected CustomHealthTypeWrapper wrapper;
	
	protected CustomHealthTypeWrapper getWrapper() {
		return wrapper;
	}

	protected void setWrapper(CustomHealthTypeWrapper wrapper) {
		this.wrapper = wrapper;
	}

	public abstract String toXml();
	//public abstract void parseXml();
}
