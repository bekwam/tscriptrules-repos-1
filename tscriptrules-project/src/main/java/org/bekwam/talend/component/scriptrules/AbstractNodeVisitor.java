package org.bekwam.talend.component.scriptrules;

import java.lang.reflect.Field;
import java.util.Map;

abstract public class AbstractNodeVisitor {

	abstract public void visitSuccess(Success success, Result parent, Map<String, Field> rejectFieldCache);
	abstract public void visitReject(Reject reject, Result parent, Map<String, Field> rejectFieldCache);
}
