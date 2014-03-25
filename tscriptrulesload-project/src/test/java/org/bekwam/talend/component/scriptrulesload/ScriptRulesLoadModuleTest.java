package org.bekwam.talend.component.scriptrulesload;

import org.junit.Test;

import com.bekwam.talend.component.scriptrulesload.ScriptRulesLoadModule;

public class ScriptRulesLoadModuleTest {

	@SuppressWarnings("unused")
	@Test
	public void constructor() {		
		ScriptRulesLoadModule module = new ScriptRulesLoadModule("jexlExpression", "reasonCode", "reasonMessage", false);
	}
}
