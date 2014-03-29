package com.bekwam.talend.component.scriptrulesload;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bekwam.talend.commonsrules.Rule;
import org.bekwam.talend.commonsrules.RuleList;
import org.bekwam.talend.commonsrules.ScriptRulesValidationException;
import org.bekwam.talend.commonsrules.ScriptRulesValidator;

import org.bekwam.talend.commons.Counter;

/**
 * ScriptRulesLoadBean is the implementation of the tScriptRulesLoad 
 * component
 * 
 * The primary JET interface is addRule() which is a row-based call that takes 
 * as input a row with fields for the rule (jexlExpression, reasonCode, 
 * reasonMessage) and a Counter object.
 * 
 * @author Carl2
 * @version 1.0
 * @created 15-Dec-2012 5:22:41 PM
 */
public final class ScriptRulesLoadBean {

	private Log logger = LogFactory.getLog(ScriptRulesLoadBean.class);

	private final ScriptRulesValidator validator;
	private final String jexlExpressionColumnName;
	private final String reasonCodeColumnName;
	private final String reasonMessageColumnName;
	private final boolean lenient;
	
	private final RuleList ruleList = new RuleList();

	/**
	 * Constructor
	 * 
	 * Expected to be invoked from Guice, but straight-up Java is allowed for
	 * unit testing
	 * 
	 * @param validator
	 * @param jexl
	 * @param inputConn
	 */
	@Inject
	public ScriptRulesLoadBean(ScriptRulesValidator validator, 				
							   @Named("JexlExpressionCol") String jexlExpressionColumnName, 
							   @Named("ReasonCodeCol") String reasonCodeColumnName,
							   @Named("ReasonMessageCol") String reasonMessageColumnName,
							   @Named("Lenient") boolean lenient)
		throws ScriptRulesValidationException {
		
		if( logger.isDebugEnabled() ) {
			logger.debug("constructor w. validator=" + validator + ", lenient=" + lenient);
		}
		
		this.validator = validator;
		
		this.jexlExpressionColumnName = jexlExpressionColumnName;
		this.reasonCodeColumnName = reasonCodeColumnName;
		this.reasonMessageColumnName = reasonMessageColumnName;
		
		this.lenient = lenient;
	}

	/**
	 * Adds a Rule to the internal data structure (ruleList), returning a 
	 * Counter of the current state of the ruleList
	 * 
	 * @param jexlExpression
	 * @param reasonCode
	 * @param reasonMessage
	 */
	public Counter addRule(Object row, Counter counter) throws ScriptRulesValidationException {
		
		if( logger.isDebugEnabled() ) {
			logger.debug("adding rule row=" + row);
		}

		try {
			String jexlExpression = BeanUtils.getProperty(row, jexlExpressionColumnName);
			String reasonCode = BeanUtils.getProperty(row, reasonCodeColumnName);
			String reasonMessage = BeanUtils.getProperty(row, reasonMessageColumnName);
		
			if( logger.isDebugEnabled() ) {
				logger.debug("adding rule jexl=" + jexlExpression + ", reasonCode=" +
					reasonCode + ", reasonMessage=" + reasonMessage);
			}
			
			RuleList tempRuleList = new RuleList();
			tempRuleList.addRule( new Rule(jexlExpression, reasonCode, reasonMessage) );
			
			RuleList invalidRuleList = validator.validateRuleList(tempRuleList);
			
			if( CollectionUtils.isEmpty( invalidRuleList.getRules() ) ) {
				ruleList.getRules().addAll( tempRuleList.getRules() );
			}
			else {
				if( lenient ) {
					System.out.println("Skipping invalid rule '" + jexlExpression + "'");
				}
				else {
					String msg = "error adding rule jexlExpression=" + jexlExpression + ", reasonCode=" 
							+ reasonCode + ", reasonMessage=" + reasonMessage;
					logger.error( msg );
					throw new ScriptRulesValidationException( msg );
				}
			}
			
		} catch (Exception exc) {
			String msg = "error adding rule row=" + row;			
			logger.error( msg, exc );
			throw new ScriptRulesValidationException( msg );
		}
		
		return new Counter( counter, 1, 0 );  // add one ok
		
	}

	/**
	 * Returns the ruleList that has been built up through addRule()
	 * 
	 * Can also return an empty object
	 * 
	 * @return
	 */
	public RuleList getRuleList(){
		return ruleList;
	}
	
	
}//end ScriptRulesLoadBean