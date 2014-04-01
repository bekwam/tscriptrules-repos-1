package com.bekwam.talend.component.scriptrules;

import java.util.ArrayList;
import java.util.List;

import org.bekwam.talend.commons.Connection;
import org.bekwam.talend.commons.Counter;
import org.bekwam.talend.commonsrules.Rule;
import org.bekwam.talend.commonsrules.RuleList;
import org.bekwam.talend.component.scriptrules.Result;
import org.bekwam.talend.component.scriptrules.ScriptRulesBean;
import org.bekwam.talend.component.scriptrules.ScriptRulesModule;

import com.bekwam.talend.component.scriptrules.schema.row1Struct;
import com.bekwam.talend.component.scriptrules.schema.row3Struct;
import com.google.inject.Guice;
import com.google.inject.Injector;

public class ScriptRulesFunctionalTest {

	/**
	 * A functional test the verifies the wiring and what happens if the 
	 * component is called twice in a row
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		List<String> routineClassNames = new ArrayList<String>();
		
		routineClassNames.add( "DataOperation" );
		routineClassNames.add( "Mathematical" );
		routineClassNames.add( "Numeric" );
		routineClassNames.add( "Relational" );
		routineClassNames.add( "StringHandling" );
		routineClassNames.add( "TalendDataGenerator" );
		routineClassNames.add( "TalendDate" );
		routineClassNames.add( "TalendString" );		

		RuleList ruleList = new RuleList();
		ruleList.addRule(new Rule("row1.field1 == 'ok'", "1", "ok test"));
		
		row1Struct row1 = new row1Struct();
		row1Struct row2 = new row1Struct();
		row3Struct row3 = new row3Struct();
		
		row1Struct sampleRow2 = new row1Struct();
		row3Struct sampleRow3 = new row3Struct();

		row1.setField1("ok");
		
		for( int i=0; i<2; i++ ) {
			
			row1.setField1("ok");
			
			Connection inputConn = new Connection("row1", row1);
			Connection filterConn = new Connection("row2", sampleRow2);
			Connection rejectConn = new Connection("row3", sampleRow3);
			
			Injector injector = Guice.createInjector(new ScriptRulesModule(ruleList, true, inputConn, filterConn, rejectConn, routineClassNames));
			
			ScriptRulesBean rulesBean = injector.getInstance(ScriptRulesBean.class);
	
			Counter counter = new Counter();
	
			Result result = rulesBean.process(row1, row2, row3, counter);
		
			System.out.println("iteration: " + i );
			System.out.println("Rejects test passed..." + result.success());
			System.out.println("Filter row test passed..." + ((row1Struct)result.getFilterRow()).field1.equals("ok"));
			System.out.println("Reject row test passed..." + (result.getRejectRow() == null ) );
			System.out.println("Counter object test passed..." + 
					 ((result.getCounter().getNumLines()==1) && (result.getCounter().getNumLinesOk()==1) && (result.getCounter().getNumLinesReject()==0) ));
			
			row2 = null;
			row3 = null;
		}
	}

}
