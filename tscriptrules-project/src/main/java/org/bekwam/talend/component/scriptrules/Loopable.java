package org.bekwam.talend.component.scriptrules;

import java.lang.reflect.Field;
import java.util.Map;

public interface Loopable {

	abstract public void accept(AbstractNodeVisitor visitor, Result parent, Map<String, Field> rejectFieldCache);

}
