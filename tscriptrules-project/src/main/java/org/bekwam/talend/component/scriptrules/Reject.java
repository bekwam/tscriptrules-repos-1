package org.bekwam.talend.component.scriptrules;

import java.lang.reflect.Field;
import java.util.Map;

import org.bekwam.talend.commonsrules.Rule;


/**
 * @author Carl2
 * @version 1.0
 * @created 15-Nov-2012 7:25:52 PM
 */
final public class Reject implements Loopable {

	final private Rule rule;
	
	public Reject(Rule rule) {
		this.rule = rule;
	}

	public Rule getRule() {
		return rule;
	}
	
	public void accept(AbstractNodeVisitor visitor, Result parent, Map<String, Field> rejectFieldCache) {
		visitor.visitReject(this, parent, rejectFieldCache);
	}
	
}//end RejectObject