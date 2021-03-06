package com.bekwam.talend.component.scriptrules;

import java.util.ArrayList;
import java.util.List;

import org.bekwam.talend.commonsrules.Rule;
import org.bekwam.talend.commonsrules.RuleList;
import org.bekwam.talend.component.scriptrules.ScriptRulesBean;
import org.bekwam.talend.component.scriptrules.ScriptRulesModule;

import org.bekwam.talend.commons.Connection;
import org.bekwam.talend.commons.Counter;
import com.bekwam.talend.component.scriptrules.schema.row1Struct;
import com.bekwam.talend.component.scriptrules.schema.row3Struct;
import com.google.inject.Guice;
import com.google.inject.Injector;

public class ScriptRulesPerfTest {

	/**
	 * A functional test the verifies the wiring
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

		long start = System.currentTimeMillis();
		
		RuleList ruleList = new RuleList();
		ruleList.addRule(new Rule("row1.field1 == 'ok'", "1", "ok test"));
		
		Connection inputConn = new Connection("row1", new row1Struct());
		Connection filterConn = new Connection("row2", new row1Struct());
		Connection rejectConn = new Connection("row3", new row3Struct());
		
		Injector injector = Guice.createInjector(new ScriptRulesModule(ruleList, true, inputConn, filterConn, rejectConn, routineClassNames, Boolean.FALSE, Boolean.FALSE));
		
		ScriptRulesBean rulesBean = injector.getInstance(ScriptRulesBean.class);

		row1Struct inputRow = new row1Struct("ok");
		row1Struct filterRow = new row1Struct();
		row3Struct rejectRow = new row3Struct();
		
		Counter counter = new Counter();

		for( int i=0; i<1000000; i++ ) {
			rulesBean.process(inputRow, filterRow, rejectRow, counter);
		}
		
		long end = System.currentTimeMillis();
		
		System.out.println("main() completed in " + (end-start) + " ms");
	}

}
