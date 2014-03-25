package org.bekwam.talend.commonsrules;

/**
 * @author Carl2
 * @version 1.0
 * @created 15-Nov-2012 7:39:30 PM
 */
final public class Rule {

	final private String jexlExpression;
	final private String reasonCode;
	final private String reasonMessage;

	public Rule(String jexlExpression, String reasonCode, String reasonMessage){
		this.jexlExpression = jexlExpression;
		this.reasonCode = reasonCode;
		this.reasonMessage = reasonMessage;
	}
	
	public String getJexlExpression() {
		return jexlExpression;
	}

	public String getReasonCode() {
		return reasonCode;
	}
	
	public String getReasonMessage() {
		return reasonMessage;
	}
	
	@Override
	public String toString() {
		return "jexlExpression=" + jexlExpression + ", reasonCode=" + reasonCode +
		", reasonMessage=" + reasonMessage;
	}


}//end Rule