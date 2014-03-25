package com.bekwam.talend.component.scriptrules;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bekwam.talend.commonsrules.Rule;
import org.bekwam.talend.component.scriptrules.AbstractNodeVisitor;
import org.bekwam.talend.component.scriptrules.Loopable;
import org.bekwam.talend.component.scriptrules.Reject;
import org.bekwam.talend.component.scriptrules.RejectFieldsVisitor;
import org.bekwam.talend.component.scriptrules.Result;
import org.bekwam.talend.component.scriptrules.Success;
import org.junit.Test;

import org.bekwam.talend.commons.Counter;
import com.bekwam.talend.component.scriptrules.schema.row1Struct;
import com.bekwam.talend.component.scriptrules.schema.row3Struct;

public class RejectFieldsVisitorTest extends AbstractAbstractNodeVisitorTest {

	@Override
	public AbstractNodeVisitor getAbstractNodeVisitor() {		
		return new RejectFieldsVisitor();
	}

	@Test
	public void visitReject() throws SecurityException, NoSuchFieldException {
		
		Rule r = new Rule("row1.field1 == 'ok'", "1", "field1 must be ok");
		
		Reject reject = new Reject(r);
		
		List<Loopable> loopables = new ArrayList<Loopable>();
		loopables.add( reject );
		
		row1Struct row1 = new row1Struct();
		row1Struct row2 = new row1Struct();
		row3Struct row3 = new row3Struct();  // reject row
	
		Counter counter = new Counter();
		
		Result result = new Result(row1, row2, row3, loopables, counter);
		
		Map<String, Field> rejectFieldCache = new HashMap<String, Field>();
		rejectFieldCache.put("field1", row3.getClass().getField("field1"));
		rejectFieldCache.put("reasonCode", row3.getClass().getField("reasonCode"));
		rejectFieldCache.put("reasonMessage", row3.getClass().getField("reasonMessage"));
		
		getAbstractNodeVisitor().visitReject(reject, result, rejectFieldCache);
		
		assertEquals( "1", row3.getReasonCode() );
		assertEquals( "field1 must be ok", row3.getReasonMessage() );
	}
	
	@Test
	public void noReasonCode() throws SecurityException, NoSuchFieldException {
		
		Rule r = new Rule("row1.field1 == 'ok'", "1", "field1 must be ok");
		
		Reject reject = new Reject(r);
		
		List<Loopable> loopables = new ArrayList<Loopable>();
		loopables.add( reject );
		
		row1Struct row1 = new row1Struct();
		row1Struct row2 = new row1Struct();
		row1Struct row3 = new row1Struct();  // no reasonCode, reasonMessage
	
		Counter counter = new Counter();
		
		Result result = new Result(row1, row2, row3, loopables, counter);
		
		Map<String, Field> rejectFieldCache = new HashMap<String, Field>();
		rejectFieldCache.put("field1", row3.getClass().getField("field1"));
		
		getAbstractNodeVisitor().visitReject(reject, result, rejectFieldCache);
	
		// if here, successful test
	}
	
	@Test
	public void success() throws SecurityException, NoSuchFieldException {
		
		Success success = new Success();
		
		List<Loopable> loopables = new ArrayList<Loopable>();
		loopables.add( success );
		
		row1Struct row1 = new row1Struct();
		row1Struct row2 = new row1Struct();
		row3Struct row3 = new row3Struct();  // reject row
	
		Counter counter = new Counter();
		
		Result result = new Result(row1, row2, row3, loopables, counter);
		
		Map<String, Field> rejectFieldCache = new HashMap<String, Field>();
		rejectFieldCache.put("field1", row3.getClass().getField("field1"));
		rejectFieldCache.put("reasonCode", row3.getClass().getField("reasonCode"));
		rejectFieldCache.put("reasonMessage", row3.getClass().getField("reasonMessage"));
		
		getAbstractNodeVisitor().visitSuccess(success, result, rejectFieldCache);
	
		// no-op
	}
}
