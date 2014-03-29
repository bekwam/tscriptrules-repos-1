package com.bekwam.talend.component.scriptrules;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bekwam.talend.commonsrules.Rule;
import org.bekwam.talend.component.scriptrules.AbstractNodeVisitor;
import org.bekwam.talend.component.scriptrules.Loopable;
import org.bekwam.talend.component.scriptrules.Reject;
import org.bekwam.talend.component.scriptrules.Result;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Test;

import org.bekwam.talend.commons.Counter;
import com.bekwam.talend.component.scriptrules.schema.row1Struct;
import com.bekwam.talend.component.scriptrules.schema.row3Struct;

public class RejectTest {

	private Mockery context = new Mockery() {{
        setImposteriser(ClassImposteriser.INSTANCE);
    }};
	
    @Test
	public void accept() throws SecurityException, NoSuchFieldException {

    	Rule r = new Rule("jexlExpression", "reasonCode", "reasonMessage");
    	
    	final Reject reject = new Reject(r);
    	
    	List<Loopable> loopables = new ArrayList<Loopable>();
    	loopables.add(reject);
    	
    	final AbstractNodeVisitor visitor = context.mock(AbstractNodeVisitor.class);
    	
    	row1Struct row1 = new row1Struct();
    	row1Struct row2 = new row1Struct();
    	row3Struct row3 = new row3Struct();
    	
    	Counter counter = new Counter();
    	
    	final Result result = new Result(row1, row2, row3, loopables, counter);
    	
		final Map<String, Field> rejectFieldCache = new HashMap<String, Field>();
		rejectFieldCache.put("field1", row3.getClass().getField("field1"));
		rejectFieldCache.put("reasonCode", row3.getClass().getField("reasonCode"));
		rejectFieldCache.put("reasonMessage", row3.getClass().getField("reasonMessage"));

		context.checking(new Expectations() {{
			oneOf(visitor).visitReject(reject, result, rejectFieldCache);
		}});
		
		reject.accept(visitor, result, rejectFieldCache);
		
		context.assertIsSatisfied();
	}
}
