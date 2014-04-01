package com.bekwam.talend.component.scriptrules;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.jexl2.JexlEngine;
import org.bekwam.talend.commons.Connection;
import org.bekwam.talend.commons.Counter;
import org.bekwam.talend.commonsrules.Rule;
import org.bekwam.talend.commonsrules.RuleList;
import org.bekwam.talend.commonsrules.ScriptRulesValidationException;
import org.bekwam.talend.commonsrules.ScriptRulesValidator;
import org.bekwam.talend.component.scriptrules.Reject;
import org.bekwam.talend.component.scriptrules.RejectFieldsVisitor;
import org.bekwam.talend.component.scriptrules.Result;
import org.bekwam.talend.component.scriptrules.ScriptRulesBean;
import org.bekwam.talend.component.scriptrules.Success;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;

import com.bekwam.talend.component.scriptrules.schema.row1Struct;
import com.bekwam.talend.component.scriptrules.schema.row4Struct;
import com.bekwam.talend.component.scriptrules.schema.row5Struct;
import com.bekwam.talend.component.scriptrules.schema.row6Struct;


/**
 * @author Carl2
 * @version 1.0
 * @created 28-Oct-2012 7:47:02 AM
 */
public class ScriptRulesBeanTest extends junit.framework.TestCase {

	private Mockery context = new Mockery() {{
        setImposteriser(ClassImposteriser.INSTANCE);
    }};
    
    private ScriptRulesValidator validator;
    private JexlEngine jexl;
    private RejectFieldsVisitor rejectFieldsVisitor;
    private List<String> routineClassNames = new ArrayList<String>();
    
    public ScriptRulesBeanTest() {}
    
	/**
	 * 
	 * @exception Exception
	 */
	protected void setUp()
	  throws Exception{
		super.setUp();
		validator = context.mock(ScriptRulesValidator.class);
		jexl = new JexlEngine();  // mock this?
		rejectFieldsVisitor = context.mock(RejectFieldsVisitor.class);
		
		routineClassNames.add( "DataOperation" );
		routineClassNames.add( "Mathematical" );
		routineClassNames.add( "Numeric" );
		routineClassNames.add( "Relational" );
		routineClassNames.add( "StringHandling" );
		routineClassNames.add( "TalendDataGenerator" );
		routineClassNames.add( "TalendDate" );
		routineClassNames.add( "TalendString" );		
	}

	/**
	 * 
	 * @exception Exception
	 */
	protected void tearDown()
	  throws Exception{
		super.tearDown();
	}

	/**
	 * Tests first form of constructor : input conn only
	 */
	final public void testConstructorInputOnly() throws ScriptRulesValidationException {
		
		final RuleList ruleList = new RuleList();
		
		row1Struct row1 = new row1Struct();
		final Connection inputConn = new Connection("row1", row1);
				
		final RuleList invalidRuleList = new RuleList();
		
		context.checking(new Expectations() {{
			oneOf(validator).isValid(inputConn); will(returnValue(true));
			oneOf(validator).validateRuleList(ruleList); will(returnValue(invalidRuleList));
		}});

		ScriptRulesBean rulesBean = new ScriptRulesBean(validator, jexl, rejectFieldsVisitor, ruleList, false, inputConn, null, null, routineClassNames);

		context.assertIsSatisfied();
		
		assertNotNull( rulesBean );
	}
	
	/**
	 * Tests second form of constructor : input and filter
	 */
	final public void testConstructorInputAndFilter() throws ScriptRulesValidationException {
		
		final RuleList ruleList = new RuleList();
		
		row1Struct row1 = new row1Struct();
		row1Struct row2 = new row1Struct();

		final Connection inputConn = new Connection("row1", row1);
		final Connection filterConn = new Connection("row2", row2);
		
		final RuleList invalidRuleList = new RuleList();
		
		context.checking(new Expectations() {{
			oneOf(validator).isValid(inputConn); will(returnValue(true));
			oneOf(validator).validateRuleList(ruleList); will(returnValue(invalidRuleList));
		}});

		ScriptRulesBean rulesBean = new ScriptRulesBean(validator, jexl, rejectFieldsVisitor, ruleList, false, inputConn, filterConn, null, routineClassNames);
		
		context.assertIsSatisfied();

		assertNotNull( rulesBean );
	}
	

	/**
	 * Tests third form of constructor : input, filter, and reject
	 */
	final public void testConstructorInputFilterAndReject() throws ScriptRulesValidationException {
		
		final RuleList ruleList = new RuleList();
		
		row1Struct row1 = new row1Struct();
		row1Struct row2 = new row1Struct();
		row1Struct row3 = new row1Struct();
		
		final Connection inputConn = new Connection("row1", row1);
		final Connection filterConn = new Connection("row2", row2);
		final Connection rejectConn = new Connection("row3", row3);
		
		final RuleList invalidRuleList = new RuleList();
		
		context.checking(new Expectations() {{
			oneOf(validator).isValid(inputConn); will(returnValue(true));

			oneOf(validator).validateRuleList(ruleList); will(returnValue(invalidRuleList));
		}});

		ScriptRulesBean rulesBean = new ScriptRulesBean(validator, jexl, rejectFieldsVisitor, ruleList, false, inputConn, filterConn, rejectConn, routineClassNames);
		
		context.assertIsSatisfied();

		assertNotNull( rulesBean );
	}
	
	/**
	 * Tests a bad rule in an inputConn-only constructor'
	 * 
	 * Will log an error message
	 */
	@SuppressWarnings("unused")
	final public void testBadRule() {
		
		final RuleList ruleList = new RuleList();
		
		Rule rule = new Rule("row1.field1 = 'carl", "1", "ok");
		ruleList.addRule( rule );
		
		row1Struct row1 = new row1Struct();
		final Connection inputConn = new Connection("row1", row1);
		
		final RuleList invalidRuleList = new RuleList();
		invalidRuleList.addRule( rule );
		
		context.checking(new Expectations() {{
			oneOf(validator).validateRuleList(ruleList); will(returnValue(invalidRuleList));
		}});

		try {
			ScriptRulesBean rulesBean = new ScriptRulesBean(validator, jexl, rejectFieldsVisitor, ruleList, false, inputConn, null, null, routineClassNames);
			fail();
		}
		catch(ScriptRulesValidationException exc) {
			
		}
			
		context.assertIsSatisfied();
	}
	
	final public void testEval() throws ScriptRulesValidationException {
		
		final String jexlExpression1 = "row1.field1 == 'ok'";
		final String jexlExpression2 = "row1.field1 != null";
		
		final RuleList ruleList = new RuleList();
		
		ruleList.addRule( new Rule(jexlExpression1, "1", "ok") );
		ruleList.addRule( new Rule(jexlExpression2, "2", "not null") );
		
		row1Struct row1 = new row1Struct();
		row1Struct row2 = new row1Struct();
		row1Struct row3 = new row1Struct();

		row1.setField1("ok");
		
		final Connection inputConn = new Connection("row1", row1);
		final Connection filterConn = new Connection("row2", row2);
		final Connection rejectConn = new Connection("row3", row3);

		final RuleList invalidRuleList = new RuleList();

		context.checking(new Expectations() {{
			
			oneOf(validator).validateRuleList(ruleList); will(returnValue(invalidRuleList));
			
			oneOf(validator).isValid(inputConn); will(returnValue(true));
		}});

		ScriptRulesBean rulesBean = new ScriptRulesBean(validator, jexl, rejectFieldsVisitor, ruleList, false, inputConn, filterConn, rejectConn, routineClassNames);
		
		Result result = rulesBean.process(row1, row2, row3, new Counter());

		context.assertIsSatisfied();
		
		assertNotNull( result );
		
		assertNotNull( result.getLoopables() );
		assertEquals( 1, result.getLoopables().size() );
		assertTrue( result.getLoopables().get(0) instanceof Success );
		
		assertEquals( 1, result.getCounter().getNumLines() );
		assertEquals( 1, result.getCounter().getNumLinesOk() );
		assertEquals( 0, result.getCounter().getNumLinesReject() );
		
		assertNotNull( result.getInputRow() );
		assertNotNull( result.getFilterRow() );
		assertNull( result.getRejectRow() );
	}

	final public void testEvalFail() throws ScriptRulesValidationException {
		
		final String jexlExpression = "row1.field1 == 'ok'";
		
		final RuleList ruleList = new RuleList();
		
		Rule rule = new Rule(jexlExpression, "1", "ok");
		ruleList.addRule( rule );
		
		row1Struct row1 = new row1Struct();
		row1Struct row2 = new row1Struct();
		row1Struct row3 = new row1Struct();

		row1.setField1("fail");
		
		final Connection inputConn = new Connection("row1", row1);
		final Connection filterConn = new Connection("row2", row2);
		final Connection rejectConn = new Connection("row3", row3);

		final RuleList invalidRuleList = new RuleList();

		context.checking(new Expectations() {{
			
			oneOf(validator).validateRuleList(ruleList); will(returnValue(invalidRuleList));
			
			oneOf(validator).isValid(inputConn); will(returnValue(true));
		}});

		ScriptRulesBean rulesBean = new ScriptRulesBean(validator, jexl, rejectFieldsVisitor, ruleList, false, inputConn, filterConn, rejectConn, routineClassNames);
		
		Result result = rulesBean.process(row1, row2, row3, new Counter());

		context.assertIsSatisfied();
		
		assertNotNull( result );
		
		assertNotNull( result.getLoopables() );
		assertEquals( 1, result.getLoopables().size() );
		assertEquals( jexlExpression, ((Reject)result.getLoopables().get(0)).getRule().getJexlExpression());
		
		assertEquals( 1, result.getCounter().getNumLines() );
		assertEquals( 0, result.getCounter().getNumLinesOk() );
		assertEquals( 1, result.getCounter().getNumLinesReject() );
		
		assertNotNull( result.getInputRow() );
		assertNull( result.getFilterRow() );
		assertNotNull( result.getRejectRow() );

	}

	final public void testEvalSeveralOk() throws ScriptRulesValidationException {
		
		final String jexlExpression = "row1.field1 == 'ok'";
		
		final RuleList ruleList = new RuleList();
		
		Rule rule = new Rule(jexlExpression, "1", "ok");
		ruleList.addRule( rule );
		
		row1Struct row1 = new row1Struct();
		row1Struct row2 = new row1Struct();
		row1Struct row3 = new row1Struct();

		row1.setField1("ok");
		
		final Connection inputConn = new Connection("row1", row1);
		final Connection filterConn = new Connection("row2", row2);
		final Connection rejectConn = new Connection("row3", row3);

		final RuleList invalidRuleList = new RuleList();

		context.checking(new Expectations() {{
			
			oneOf(validator).validateRuleList(ruleList); will(returnValue(invalidRuleList));
			
			oneOf(validator).isValid(inputConn); will(returnValue(true));
		}});

		ScriptRulesBean rulesBean = new ScriptRulesBean(validator, jexl, rejectFieldsVisitor, ruleList, false, inputConn, filterConn, rejectConn, routineClassNames);
		
		Result result = rulesBean.process(row1, row2, row3, new Counter());

		context.assertIsSatisfied();
		
		assertNotNull( result );
		
		assertNotNull( result.getLoopables() );
		assertEquals( 1, result.getLoopables().size() );
		assertTrue( result.getLoopables().get(0) instanceof Success );
		
		assertEquals( 1, result.getCounter().getNumLines() );
		assertEquals( 1, result.getCounter().getNumLinesOk() );
		assertEquals( 0, result.getCounter().getNumLinesReject() );
		
		assertNotNull( result.getInputRow() );
		assertNotNull( result.getFilterRow() );
		assertNull( result.getRejectRow() );
	}
	
	final public void testMultipleFailRunAll() throws ScriptRulesValidationException {
		
		final String jexlExpression1 = "row1.field1 == 'ok'";
		final String jexlExpression2 = "row1.field2 == 'ok'";
		
		final RuleList ruleList = new RuleList();
		
		ruleList.addRule( new Rule(jexlExpression1, "1", "field1 ok") );
		ruleList.addRule( new Rule(jexlExpression2, "2", "field2 ok") );
		
		row4Struct row1 = new row4Struct("fail", "fail");
		row4Struct row2 = new row4Struct();
		row5Struct row3 = new row5Struct();

		final Connection inputConn = new Connection("row1", row1);
		final Connection filterConn = new Connection("row2", row2);
		final Connection rejectConn = new Connection("row3", row3);

		final RuleList invalidRuleList = new RuleList();

		context.checking(new Expectations() {{
			
			oneOf(validator).validateRuleList(ruleList); will(returnValue(invalidRuleList));
			
			oneOf(validator).isValid(inputConn); will(returnValue(true));
		}});

		ScriptRulesBean rulesBean = new ScriptRulesBean(validator, jexl, rejectFieldsVisitor, ruleList, true, inputConn, filterConn, rejectConn, routineClassNames);
		
		Result result = rulesBean.process(row1, row2, row3, new Counter());

		context.assertIsSatisfied();
		
		assertNotNull( result );
		
		assertNotNull( result.getLoopables() );
		assertEquals( 2, result.getLoopables().size() );
		assertTrue( result.getLoopables().get(0) instanceof Reject );
		assertTrue( result.getLoopables().get(1) instanceof Reject );
		
		assertEquals( 2, result.getCounter().getNumLines() );
		assertEquals( 0, result.getCounter().getNumLinesOk() );
		assertEquals( 2, result.getCounter().getNumLinesReject() );
		
		assertNotNull( result.getInputRow() );
		assertNull( result.getFilterRow() );
		assertNotNull( result.getRejectRow() );
	}
	
	final public void testMultipleFailProdMode() throws ScriptRulesValidationException {
		
		final String jexlExpression1 = "row1.field1 == 'ok'";
		final String jexlExpression2 = "row1.field2 == 'ok'";
		
		final RuleList ruleList = new RuleList();
		
		ruleList.addRule( new Rule(jexlExpression1, "1", "field1 ok") );
		ruleList.addRule( new Rule(jexlExpression2, "2", "field2 ok") );
		
		row4Struct row1 = new row4Struct("fail", "fail");
		row4Struct row2 = new row4Struct();
		row5Struct row3 = new row5Struct();

		final Connection inputConn = new Connection("row1", row1);
		final Connection filterConn = new Connection("row2", row2);
		final Connection rejectConn = new Connection("row3", row3);

		final RuleList invalidRuleList = new RuleList();

		context.checking(new Expectations() {{
			
			oneOf(validator).validateRuleList(ruleList); will(returnValue(invalidRuleList));
			
			oneOf(validator).isValid(inputConn); will(returnValue(true));
		}});

		ScriptRulesBean rulesBean = new ScriptRulesBean(validator, jexl, rejectFieldsVisitor, ruleList, false, inputConn, filterConn, rejectConn, routineClassNames);
		
		Result result = rulesBean.process(row1, row2, row3, new Counter());

		context.assertIsSatisfied();
		
		assertNotNull( result );
		
		assertNotNull( result.getLoopables() );
		assertEquals( 1, result.getLoopables().size() );
		assertTrue( result.getLoopables().get(0) instanceof Reject );
		
		assertEquals( 1, result.getCounter().getNumLines() );
		assertEquals( 0, result.getCounter().getNumLinesOk() );
		assertEquals( 1, result.getCounter().getNumLinesReject() );
		
		assertNotNull( result.getInputRow() );
		assertNull( result.getFilterRow() );
		assertNotNull( result.getRejectRow() );
	}
	
	/**
	 * Tests an evaluation with a schema using fields that don't start with
	 * a lower case letter
	 * 
	 * @throws ScriptRulesValidationException
	 */
	final public void testEvalUpper() throws ScriptRulesValidationException {
		
		final String jexlExpression1 = "row1.My_Field1 == 'ok'";
		final String jexlExpression2 = "row1.My_Field1 != null";
		
		final RuleList ruleList = new RuleList();
		
		ruleList.addRule( new Rule(jexlExpression1, "1", "ok") );
		ruleList.addRule( new Rule(jexlExpression2, "2", "not null") );
		
		row6Struct row1 = new row6Struct();
		row6Struct row2 = new row6Struct();

		row1.My_Field1 = "ok";
		
		final Connection inputConn = new Connection("row1", row1);
		final Connection filterConn = new Connection("row2", row2);

		final RuleList invalidRuleList = new RuleList();

		context.checking(new Expectations() {{
			
			oneOf(validator).validateRuleList(ruleList); will(returnValue(invalidRuleList));
			
			oneOf(validator).isValid(inputConn); will(returnValue(true));
		}});

		ScriptRulesBean rulesBean = new ScriptRulesBean(validator, jexl, rejectFieldsVisitor, ruleList, false, inputConn, filterConn, null, routineClassNames);
		
		Result result = rulesBean.process(row1, row2, null, new Counter());

		context.assertIsSatisfied();
		
		assertNotNull( result );
		
		assertNotNull( result.getLoopables() );
		assertEquals( 1, result.getLoopables().size() );
		assertTrue( result.getLoopables().get(0) instanceof Success );
		
		assertEquals( 1, result.getCounter().getNumLines() );
		assertEquals( 1, result.getCounter().getNumLinesOk() );
		assertEquals( 0, result.getCounter().getNumLinesReject() );
		
		assertNotNull( result.getInputRow() );
		assertNotNull( result.getFilterRow() );
		assertNull( result.getRejectRow() );
	}
	
}//end ScriptRulesBeanTest