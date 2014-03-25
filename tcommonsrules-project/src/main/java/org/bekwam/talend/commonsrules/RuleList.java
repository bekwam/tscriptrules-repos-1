package org.bekwam.talend.commonsrules;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Carl2
 * @version 1.0
 * @created 27-Nov-2012 11:23:01 PM
 */
public class RuleList {

	private List<Rule> rules = new ArrayList<Rule>();

	public RuleList() {}
	
	public void addRule(Rule rule) {
		if( rules == null ) {
			rules = new ArrayList<Rule>();
		}
		rules.add( rule );
	}
	
	public List<Rule> getRules() {
		return rules;
	}
	
	public void setRules(List<Rule> rules) {
		this.rules = rules;
	}

	@Override
	public String toString() {
		return "RuleListObject [rules=" + rules + "]";
	}
	
	
}//end RulesList