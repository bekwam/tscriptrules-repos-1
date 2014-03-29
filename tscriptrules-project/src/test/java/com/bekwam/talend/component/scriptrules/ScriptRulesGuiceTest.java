package com.bekwam.talend.component.scriptrules;

import org.bekwam.talend.commonsrules.Rule;
import org.bekwam.talend.commonsrules.RuleList;
import org.bekwam.talend.component.scriptrules.Result;
import org.bekwam.talend.component.scriptrules.ScriptRulesBean;
import org.bekwam.talend.component.scriptrules.ScriptRulesModule;

import org.bekwam.talend.commons.Connection;
import org.bekwam.talend.commons.Counter;
import com.bekwam.talend.component.scriptrules.schema.row1Struct;
import com.bekwam.talend.component.scriptrules.schema.row3Struct;
import com.google.inject.Guice;
import com.google.inject.Injector;

public class ScriptRulesGuiceTest {

	/**
	 * A functional test the verifies the wiring
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		RuleList ruleList = new RuleList();
		ruleList.addRule(new Rule("row1.field1 == 'ok'", "1", "ok test"));
		
		row1Struct row1 = new row1Struct();
		row1Struct row2 = new row1Struct();
		row3Struct row3 = new row3Struct();
		
		row1.setField1("ok");
		
		Connection inputConn = new Connection("row1", row1);
		Connection filterConn = new Connection("row2", row2);
		Connection rejectConn = new Connection("row3", row3);
		
		Injector injector = Guice.createInjector(new ScriptRulesModule(ruleList, true, inputConn, filterConn, rejectConn));
		
		ScriptRulesBean rulesBean = injector.getInstance(ScriptRulesBean.class);

		Counter counter = new Counter();

		Result result = rulesBean.process(row1, row2, row3, counter);
		
		System.out.println("Rejects test passed..." + result.success());
		System.out.println("Filter row test passed..." + ((row1Struct)result.getFilterRow()).field1.equals("ok"));
		System.out.println("Reject row test passed..." + (result.getRejectRow() == null ) );
		System.out.println("Counter object test passed..." + 
				 ((result.getCounter().getNumLines()==1) && (result.getCounter().getNumLinesOk()==1) && (result.getCounter().getNumLinesReject()==0) ));
	}

}
