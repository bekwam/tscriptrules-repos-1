package org.bekwam.talend.commons;


/**
 * A class representing a Talend connection (input, filter, or reject)
 * 
 * @author Carl2
 * @version 1.0
 * @created 26-Nov-2012 7:43:38 PM
 */
final public class Connection {

	final private String connName;
	final private Class<?> connClass;
	
	public Connection(String connName, Object connObj) {
		super();
		this.connName = connName;
		if( connObj != null ) {
			this.connClass = connObj.getClass();
		}
		else {
			this.connClass = null;
		}
	}
	
	public String getConnName() {
		return connName;
	}
	public Class<?> getConnClass() {
		return connClass;
	}
	
	public boolean isDefined() { return connClass != null; }
	
	@Override
	public String toString() {
		return "Connection [connName=" + connName + ", connClass=" + connClass
				+ "]";
	}
	
}//end Connection