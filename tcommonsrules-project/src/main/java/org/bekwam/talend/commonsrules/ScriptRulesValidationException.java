package org.bekwam.talend.commonsrules;

/**
 * @author Carl2
 * @version 1.0
 * @created 18-Nov-2012 11:08:41 AM
 */
public class ScriptRulesValidationException extends Exception {

	private static final long serialVersionUID = 1L;

	public ScriptRulesValidationException(){

	}

	/**
	 * 
	 * @param message
	 */
	public ScriptRulesValidationException(String message){
		super(message);
	}
	
}//end ScriptRulesValidationException