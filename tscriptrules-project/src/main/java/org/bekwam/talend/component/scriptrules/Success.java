package org.bekwam.talend.component.scriptrules;

import java.lang.reflect.Field;
import java.util.Map;

final public class Success implements Loopable {
	
	public void accept(AbstractNodeVisitor visitor, Result parent, Map<String, Field> rejectFieldCache) {
		visitor.visitSuccess(this, parent, rejectFieldCache);
	}

}
