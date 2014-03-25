package org.bekwam.talend.commonsrules;

import org.apache.commons.jexl2.JexlEngine;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.bekwam.talend.commons.Connection;
import com.google.inject.Inject;

/**
 * This class validates a ScriptRulesBean, making sure that the arguments
 * provided by Talend JET are sufficient.
 * 
 * @author Carl2
 * @version 1.0
 * @created 28-Oct-2012 7:54:13 PM
 */
public class ScriptRulesValidator {

	private Log logger = LogFactory.getLog(ScriptRulesValidator.class);

	final private JexlEngine jexl;
	
	/**
	 * Constructor
	 * 
	 * @param jexl
	 */
	@Inject
	public ScriptRulesValidator(JexlEngine jexl) {
		this.jexl = jexl;
	}
	
	/**
	 * Determines whether or not a Connection object is valid
	 * @param conn
	 * @return
	 */
	public boolean isValid(Connection conn) {
		if( logger.isDebugEnabled() ) {
			logger.debug("isValid; conn=" + conn);
		}
		return StringUtils.isNotEmpty(conn.getConnName()) && conn.getConnClass() != null;
	}
	
	/**
	 * Determines whether or not a set of rules is valid
	 * 
	 * Returns a list of invalidRules back to the caller.  The list will never
	 * be null.  An empty list indicates that no rule were invalid.
	 * 
	 * @param rules
	 * @return
	 */
	public RuleList validateRuleList(RuleList ruleList) {
		
		RuleList invalidRuleList = new RuleList();
		for( Rule r : ruleList.getRules() ) {
			try {
				jexl.createExpression( r.getJexlExpression() );
			}
			catch(Exception exc) {
				if( logger.isInfoEnabled() ) {
					logger.info("rule=" + r.getJexlExpression() + " is not valid");
				}
				invalidRuleList.addRule( r );
			}
		}
		return invalidRuleList;
	}

}//end ScriptRulesValidator