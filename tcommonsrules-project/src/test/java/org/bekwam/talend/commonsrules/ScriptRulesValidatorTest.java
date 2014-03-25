package org.bekwam.talend.commonsrules;

import org.apache.commons.jexl2.JexlEngine;
import org.bekwam.talend.commonsrules.Rule;
import org.bekwam.talend.commonsrules.RuleList;
import org.bekwam.talend.commonsrules.ScriptRulesValidator;
import org.bekwam.talend.commonsrules.schema.row1Struct;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.bekwam.talend.commons.Connection;



/**
 * @author Carl2
 * @version 1.0
 * @created 28-Oct-2012 7:47:02 AM
 */
public class ScriptRulesValidatorTest {

	private Mockery context = new Mockery() {{
        setImposteriser(ClassImposteriser.INSTANCE);
    }};
    
    private JexlEngine jexl;
    private ScriptRulesValidator validator;
    
	/**
	 * 
	 * @exception Exception
	 */
    @Before
	public void setUp() throws Exception{
		jexl = context.mock(JexlEngine.class);
		validator = new ScriptRulesValidator(jexl);
	}

	/**
	 * 
	 * @exception Exception
	 */
    @After
	public void tearDown()
	  throws Exception{
	}

	/**
	 * Valid connection
	 */
    @Test
	public final void testIsValidConnectionOk() {
		row1Struct row1 = new row1Struct();
		Connection conn = new Connection("row1", row1);
		assertTrue( validator.isValid(conn) );
	}

	/**
	 * Missing name
	 */
    @Test
	public final void testIsValidConnectionMissingName() {
		row1Struct row1 = new row1Struct();
		Connection conn = new Connection(null, row1);
		assertFalse( validator.isValid(conn) );
	}

	/**
	 * Missing class
	 */
    @Test
	public final void testIsValidConnectionMissingClass() {
		Connection conn = new Connection("ro1", null);
		assertFalse( validator.isValid(conn) );
	}

	/**
	 * Valid rule
	 */
    @Test
	public final void testGoodRule() {
		
		final String jexlExpression = "row1.field1 == 'ok'";
		
		RuleList ruleList = new RuleList();
		ruleList.addRule( new Rule(jexlExpression, "reasonCode", "reasonMessage"));
		
		context.checking(new Expectations() {{
			oneOf(jexl).createExpression(jexlExpression);
		}});

		RuleList invalidRuleList = validator.validateRuleList( ruleList );
		
		context.assertIsSatisfied();
		
		assertNotNull( invalidRuleList );
		assertEquals( 0, invalidRuleList.getRules().size() );
	}
	
	/**
	 * Invalid single rule
	 */
    @Test
	public final void testBadRule() {
		
		final String jexlExpression = "row1.field1 == 'ok";  // leaves off end single quote
		
		RuleList ruleList = new RuleList();
		ruleList.addRule( new Rule(jexlExpression, "reasonCode", "reasonMessage"));
		
		final Exception exc = new Exception("exception");
		
		context.checking(new Expectations() {{
			oneOf(jexl).createExpression(jexlExpression);
			will(throwException(exc));
		}});

		RuleList invalidRuleList = validator.validateRuleList( ruleList );
		
		context.assertIsSatisfied();

		assertNotNull( invalidRuleList );
		assertEquals( 1, invalidRuleList.getRules().size() );
		assertEquals( jexlExpression, invalidRuleList.getRules().get(0).getJexlExpression() );
	}
	
	/**
	 * More than one invalid rule
	 */
    @Test
	public final void testMultipleBadRules() {
		
		final String jexlExpression1 = "row1.field1 == 'ok";  // leaves off end single quote
		final String jexlExpression2 = ".row1 != null"; // invalid token before 'row1'
		
		RuleList ruleList = new RuleList();
		ruleList.addRule( new Rule(jexlExpression1, "reasonCode1", "reasonMessage1"));
		ruleList.addRule( new Rule(jexlExpression2, "reasonCode2", "reasonMessage2"));
		
		final Exception exc = new Exception("exception");
		
		context.checking(new Expectations() {{
			oneOf(jexl).createExpression(jexlExpression1);
			will(throwException(exc));

			oneOf(jexl).createExpression(jexlExpression2);
			will(throwException(exc));

		}});

		RuleList invalidRuleList = validator.validateRuleList( ruleList );
		
		context.assertIsSatisfied();

		assertNotNull( invalidRuleList );
		assertEquals( 2, invalidRuleList.getRules().size() );
		assertEquals( jexlExpression1, invalidRuleList.getRules().get(0).getJexlExpression() );
		assertEquals( jexlExpression2, invalidRuleList.getRules().get(1).getJexlExpression() );

	}
}//end ScriptRulesValidatorTest