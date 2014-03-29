package org.bekwam.talend.component.scriptrules;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.jexl2.Expression;
import org.apache.commons.jexl2.JexlContext;
import org.apache.commons.jexl2.JexlEngine;
import org.apache.commons.jexl2.MapContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bekwam.talend.commonsrules.Rule;
import org.bekwam.talend.commonsrules.RuleList;
import org.bekwam.talend.commonsrules.ScriptRulesValidationException;
import org.bekwam.talend.commonsrules.ScriptRulesValidator;

import org.bekwam.talend.commons.Connection;
import org.bekwam.talend.commons.Counter;

/**
 * @author Carl2
 * @version 1.0
 * @created 28-Oct-2012 7:54:13 PM
 */
final public class ScriptRulesBean {

	private Log logger = LogFactory.getLog(ScriptRulesBean.class);
	
	final private JexlEngine jexl;
	
	final private RuleList ruleList;
	
	final private RejectFieldsVisitor rejectFieldsVisitor;
	
	final private boolean runAllMode;  // not recommended as a production setting	
	
	// class / name pairs
	final private Connection inputConn;
	final private Connection filterConn;
	final private Connection rejectConn;

	final private static String MESSAGE_INVALID_RULES           = "Error creating rulesBean with invalidRules=";
	final private static String MESSAGE_INVALID_CONN            = "Error creating rulesBean with invalid conn=";
	
	private Map<String, Expression> exprCache = new HashMap<String, Expression>();
	
	/**
	 * Constructor
	 */
	@Inject
	public ScriptRulesBean(ScriptRulesValidator validator, 
						   JexlEngine jexl, 
						   RejectFieldsVisitor rejectFieldsVisitor,
						   RuleList ruleList, 
						   @Named("RunAllMode") Boolean runAllMode,
						   @Named("Input") Connection inputConn, 
						   @Named("Filter") Connection filterConn, 
						   @Named("Reject") Connection rejectConn) throws ScriptRulesValidationException {
		
		if( logger.isDebugEnabled() ) {
			logger.debug("Constructor; validator=" + validator + ", jexl=" + 
						jexl + ", ruleList=" + ruleList + ", inputConn=" + 
						inputConn + ", filterConn=" + filterConn + 
						", rejectConn=" + rejectConn + ", runAllMode=" + runAllMode + ", rejectFieldsVisitor=" + rejectFieldsVisitor);
		}

		RuleList invalidRuleList = validator.validateRuleList(ruleList);
		
		if( CollectionUtils.isNotEmpty(invalidRuleList.getRules()) ) {
			logger.error(MESSAGE_INVALID_RULES + invalidRuleList);
			throw new ScriptRulesValidationException(MESSAGE_INVALID_RULES + invalidRuleList);
		}
		
		if( !validator.isValid(inputConn) ) {
			logger.error(MESSAGE_INVALID_CONN + "inputConn");
			throw new ScriptRulesValidationException(MESSAGE_INVALID_CONN + "inputConn");
		}
		
		this.jexl = jexl;
		this.ruleList = ruleList;
		this.inputConn = inputConn;
		this.filterConn = filterConn;
		this.rejectConn = rejectConn;
		this.runAllMode = runAllMode;
		this.rejectFieldsVisitor = rejectFieldsVisitor;
		
		for( Rule r : ruleList.getRules() ) {
			exprCache.put( r.getJexlExpression(), this.jexl.createExpression(r.getJexlExpression()) );
		}	
		
		if( inputConn != null && inputConn.getConnClass() != null ) {
			fillFieldCache( inputConn.getConnClass(), inputFieldCache );
		}

		if( filterConn != null && filterConn.getConnClass() != null ) {
			fillFieldCache( filterConn.getConnClass(), filterFieldCache );
		}

		if( rejectConn != null && rejectConn.getConnClass() != null ) {
			fillFieldCache( rejectConn.getConnClass(), rejectFieldCache );
		}

	}

	private Map<String, Field> inputFieldCache = new HashMap<String, Field>();
	private Map<String, Field> filterFieldCache = new HashMap<String, Field>();
	private Map<String, Field> rejectFieldCache = new HashMap<String, Field>();
	
	private void fillFieldCache(Class<?> clazz, Map<String, Field> cache) {
		for( Field f : clazz.getDeclaredFields() ) {
			if( !f.isSynthetic() && Modifier.isPublic(f.getModifiers()) && !Modifier.isStatic(f.getModifiers())) {
				cache.put( f.getName(), f );
			}
		}
	}
	
	private List<Loopable> evaluate(final Object inputRow) {
		
		if( logger.isDebugEnabled() ) {
			logger.debug("in evaluate()");
		}
		
		List<Loopable> rejects = new ArrayList<Loopable>();

		if (CollectionUtils.isNotEmpty(ruleList.getRules())) {
			
			if( logger.isDebugEnabled() ) {
				logger.debug("there are rules to process");
			}
			
			JexlContext context = new MapContext();
			context.set(inputConn.getConnName(), inputRow);
			context.set("input_row", inputRow);  // alias

			for (Rule rule : ruleList.getRules()) {
				Expression e = exprCache.get(rule.getJexlExpression());
				Object o = e.evaluate(context);
				if (!((Boolean) o)) {
					if (logger.isDebugEnabled()) {
						logger.debug("rejecting rule=" + rule.getJexlExpression());
					}
					Reject reject = new Reject(rule);
					rejects.add(reject);
					
					//
					// Only care about first reject in runAllMode
					//
					if( !runAllMode ) break;
				}
			}
		}
		return rejects;
	}
	
	public Result process(final Object inputRow, 
							    final Object filterRow, 
							    final Object rejectRow,
							    final Counter counter) {

		if( logger.isDebugEnabled() ) {
			logger.debug("process with inputRow=" + inputRow + ", " +
					"filterRow=" + filterRow + ", rejectRow=" + rejectRow);
		}
		
		//
		// Run jexl on input
		//
		List<Loopable> rejects = evaluate(inputRow);
		
		//
		// Set up output flow objects and counter
		//
		Result result = null;
		if( CollectionUtils.isEmpty(rejects) ) {

			Object fr = null;
			
			if( filterConn != null && filterConn.isDefined() ) { 
				try {
					fr = filterConn.getConnClass().newInstance();
				} catch (Exception exc) {
					if( logger.isWarnEnabled() ) {
						logger.warn("cannot copy filter conn", exc);
					}
					
				}
				copyToFilterConn(inputRow, fr);
			}
			
			List<Loopable> successList = new ArrayList<Loopable>();
			successList.add( new Success() );
			
			result = new Result(inputRow, fr, null, successList, new Counter(counter, 1,0));	
			
		} else {
			
			if( logger.isDebugEnabled() ) {
				logger.debug("there are rejects");
			}
			
			Object rr = null;

			if( rejectConn != null && rejectConn.isDefined() ) {
				try {
					rr = rejectConn.getConnClass().newInstance();
				} catch (Exception exc) {
					if( logger.isWarnEnabled() ) {
						logger.warn("cannot copy reject conn", exc);
					}
					
				}
				copyToRejectConn(inputRow, rr);
			}

			// rejects.size will be 1 if runAllMode is set
			result = new Result(inputRow, null, rr, rejects, new Counter(counter, 0, rejects.size()));										
		}
		
		return result;
	}
	
	private void copyToConn(Object sourceRow, 
							  Map<String, Field> sourceClassCache, 
							  Object targetRow,
							  Map<String, Field> targetClassCache) {
		
		if( logger.isDebugEnabled() ) {
			logger.debug("copyToFilterConn()");
		}
		
		if( logger.isDebugEnabled() ) {
			logger.debug("sourceRow=" + sourceRow + ", sourceClassCache=" + sourceClassCache +
					", targetRow=" + targetRow + ", targetClassCache=" +
					targetClassCache);
		}
		
		try {
			for( Field f : sourceClassCache.values() ) {
				
				//
				// BeanUtils doesn't work with non-JavaBean structs that
				// Talend might generate (ex, a field "My_Field")
				//
				
//				Object val = BeanUtils.getProperty(sourceRow, f.getName());
				Object val = f.get(sourceRow);
				if( logger.isDebugEnabled() ) {
					logger.debug("val=" + val);
				}
				Field target_f = targetClassCache.get(f.getName());
				if( logger.isDebugEnabled() ) {
					logger.debug("target_f=" + target_f);
				}
				target_f.set(targetRow, val);
			}
		} catch(Exception exc) {
			if( logger.isWarnEnabled() ) {
				logger.warn("cannot copy field", exc);
			}
		}
	}

	private void copyToFilterConn(Object inputRow, Object filterRow) {
		copyToConn(inputRow, inputFieldCache, filterRow, filterFieldCache);
	}
	
	private void copyToRejectConn(Object inputRow, Object rejectRow) {
		copyToConn(inputRow, inputFieldCache, rejectRow, rejectFieldCache);
	}

	public void setRejectFields(Result parent, Loopable loopable) {
		loopable.accept(rejectFieldsVisitor, parent, rejectFieldCache);
	}
	
}//end ScriptRulesBean