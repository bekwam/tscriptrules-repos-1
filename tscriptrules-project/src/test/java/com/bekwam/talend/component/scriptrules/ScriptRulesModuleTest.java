package com.bekwam.talend.component.scriptrules;

import org.bekwam.talend.commonsrules.RuleList;
import org.bekwam.talend.component.scriptrules.ScriptRulesModule;
import org.junit.Test;

import org.bekwam.talend.commons.Connection;
import com.bekwam.talend.component.scriptrules.schema.row1Struct;
import com.bekwam.talend.component.scriptrules.schema.row3Struct;

public class ScriptRulesModuleTest {

	@SuppressWarnings("unused")
	@Test
	public void construtor() {
		
		Connection inputConn = new Connection("row1", new row1Struct());
		Connection filterConn = new Connection("row2", new row1Struct());
		Connection rejectConn = new Connection("row3", new row3Struct());
		
		RuleList ruleList = new RuleList();
		
		ScriptRulesModule module = new ScriptRulesModule(ruleList, false, inputConn, filterConn, rejectConn);
	}
}
