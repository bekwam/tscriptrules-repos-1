package org.bekwam.talend.commonsrules;

import org.bekwam.talend.commonsrules.Rule;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class RuleTest {

	@Test
	public void getters() {
		
		Rule r = new Rule("script", "code", "message");
		assertEquals( "script", r.getJexlExpression() );
		assertEquals( "code", r.getReasonCode() );
		assertEquals( "message", r.getReasonMessage() );
	}
}
