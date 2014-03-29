package org.bekwam.talend.component.scriptrulesload;

import org.bekwam.talend.component.scriptrulesload.ScriptRulesLoadModule;
import org.junit.Test;


public class ScriptRulesLoadModuleTest {

	@SuppressWarnings("unused")
	@Test
	public void constructor() {		
		ScriptRulesLoadModule module = new ScriptRulesLoadModule("jexlExpression", "reasonCode", "reasonMessage", false);
	}
}
