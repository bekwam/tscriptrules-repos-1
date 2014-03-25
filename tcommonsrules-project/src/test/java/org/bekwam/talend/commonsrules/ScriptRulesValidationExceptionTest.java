package org.bekwam.talend.commonsrules;

import org.bekwam.talend.commonsrules.ScriptRulesValidationException;
import org.junit.Test;

public class ScriptRulesValidationExceptionTest {

	@Test
	public void constructor() {
		new ScriptRulesValidationException("msg");
	}
}
