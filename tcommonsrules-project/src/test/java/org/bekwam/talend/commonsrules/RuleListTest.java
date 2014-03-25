package org.bekwam.talend.commonsrules;

import org.bekwam.talend.commonsrules.Rule;
import org.bekwam.talend.commonsrules.RuleList;
import org.junit.Test;
import static org.junit.Assert.assertNotNull;

public class RuleListTest {

	@Test
	public void addToNull() {
		RuleList ruleList = new RuleList();
		ruleList.setRules( null );
		ruleList.addRule( new Rule("script", "code", "message") );
		assertNotNull( ruleList.getRules() );
		assertNotNull( ruleList.toString() );
	}
}
