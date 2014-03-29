package org.bekwam.talend.component.scriptrulesload;

import org.bekwam.talend.commonsrules.Rule;
import org.bekwam.talend.commonsrules.RuleList;
import org.bekwam.talend.commonsrules.ScriptRulesValidationException;
import org.bekwam.talend.commonsrules.ScriptRulesValidator;
import org.bekwam.talend.component.scriptrulesload.ScriptRulesLoadBean;
import org.bekwam.talend.component.scriptrulesload.schema.row1Struct;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import org.bekwam.talend.commons.Counter;

public class ScriptRulesLoadBeanTest {

	private Mockery context = new Mockery() {{
        setImposteriser(ClassImposteriser.INSTANCE);
    }};
    
    private ScriptRulesValidator validator;
    
    private ScriptRulesLoadBean loadBean;
    
    @Before
    public void init() {
    
    	validator = context.mock(ScriptRulesValidator.class);
    }
    
    @Test
    public void constructor() throws ScriptRulesValidationException {    	

    	context.checking(new Expectations() {{
			never(validator).validateRuleList(with(any(RuleList.class))); 
		}});
		
    	loadBean = new ScriptRulesLoadBean(validator, 
    									  "jexlExpression",
    									  "reasonCode",
    									  "reasonMessage",
    									  false);
    	
    	context.assertIsSatisfied();
    }
    
    @Test
    public void addRule() throws ScriptRulesValidationException {
    	row1Struct row1 = new row1Struct();
    	
    	row1.jexlExpression = "row1.field1 == 'ok'";
    	row1.reasonCode = "1";
    	row1.reasonMessage = "ok";
    	
    	Counter counter = new Counter();

    	final RuleList invalidRuleList = new RuleList();
    	
    	context.checking(new Expectations() {{
			oneOf(validator).validateRuleList(with(any(RuleList.class))); 
			will(returnValue(invalidRuleList));
		}});
		
    	loadBean = new ScriptRulesLoadBean(validator, 
				  						   "jexlExpression",
				  						   "reasonCode",
				  						   "reasonMessage",
				  						   false);
    	loadBean.addRule(row1, counter);
    	
    	context.assertIsSatisfied();
    	
    	assertNotNull( loadBean.getRuleList() );
    	assertEquals( 1, loadBean.getRuleList().getRules().size() );
    	assertEquals( "row1.field1 == 'ok'", loadBean.getRuleList().getRules().get(0).getJexlExpression());
    	assertEquals( "1", loadBean.getRuleList().getRules().get(0).getReasonCode());
    	assertEquals( "ok", loadBean.getRuleList().getRules().get(0).getReasonMessage());
    }

    @Test(expected=ScriptRulesValidationException.class)
    public void addInvalidRule() throws ScriptRulesValidationException {
    	
    	row1Struct row1 = new row1Struct();
    	
    	row1.jexlExpression = "row1.field1 == 'ok'";
    	row1.reasonCode = "1";
    	row1.reasonMessage = "ok";
    	
    	Counter counter = new Counter();

    	final RuleList invalidRuleList = new RuleList();
    	invalidRuleList.addRule( new Rule(row1.jexlExpression, 
    									  row1.reasonCode, 
    									  row1.reasonMessage) );
    	
    	context.checking(new Expectations() {{
			oneOf(validator).validateRuleList(with(any(RuleList.class))); 
			will(returnValue(invalidRuleList));
		}});
		
    	loadBean = new ScriptRulesLoadBean(validator, 
				  						   "jexlExpression",
				  						   "reasonCode",
				  						   "reasonMessage",
				  						   false);
    	loadBean.addRule(row1, counter);
    	
    	context.assertIsSatisfied();

    }

    @Test
    public void addInvalidRuleLenient() throws ScriptRulesValidationException {
    	
    	row1Struct row1 = new row1Struct();
    	
    	row1.jexlExpression = "row1.field1 == 'ok'";
    	row1.reasonCode = "1";
    	row1.reasonMessage = "ok";
    	
    	Counter counter = new Counter();

    	final RuleList invalidRuleList = new RuleList();
    	invalidRuleList.addRule( new Rule(row1.jexlExpression, 
    									  row1.reasonCode, 
    									  row1.reasonMessage) );
    	
    	context.checking(new Expectations() {{
			oneOf(validator).validateRuleList(with(any(RuleList.class))); 
			will(returnValue(invalidRuleList));
		}});
		
    	loadBean = new ScriptRulesLoadBean(validator, 
				  						   "jexlExpression",
				  						   "reasonCode",
				  						   "reasonMessage",
				  						   true);
    	loadBean.addRule(row1, counter);
    	
    	context.assertIsSatisfied();

    }
}
