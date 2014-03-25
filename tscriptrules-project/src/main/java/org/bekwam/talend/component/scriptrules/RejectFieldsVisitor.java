package org.bekwam.talend.component.scriptrules;

import java.lang.reflect.Field;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class RejectFieldsVisitor extends AbstractNodeVisitor {

	final private static String MESSAGE_CANT_SET_REASON_CODE    = "Can't set reasonCode field on rejectRow";
	final private static String MESSAGE_CANT_SET_REASON_MESSAGE = "Can't set reasonMessage field on rejectRow";
	
	final private static String FIELD_REASON_CODE    = "reasonCode";
	final private static String FIELD_REASON_MESSAGE = "reasonMessage";

	private Log logger = LogFactory.getLog(RejectFieldsVisitor.class);

	@Override
	public void visitReject(Reject reject, Result parent, Map<String, Field> rejectFieldCache) {

		if( logger.isDebugEnabled() ) {
			logger.debug("visitReject()");
		}
		
		Object rejectRow = parent.getRejectRow();
		String reasonCode = reject.getRule().getReasonCode();
		String reasonMessage = reject.getRule().getReasonMessage();
		
		if( logger.isDebugEnabled() ) {
			logger.debug("rejectRow=" + rejectRow + ", reasonCode=" + 
					reasonCode + ", reasonMessage=" + reasonMessage);
		}
		
		for( Field f : rejectFieldCache.values() ) {			
			if( FIELD_REASON_CODE.equals(f.getName()) ) {
				try {
					f.set(rejectRow, reasonCode);
				}
				catch(Exception exc) {
					if( logger.isWarnEnabled() ) {
						logger.warn(MESSAGE_CANT_SET_REASON_CODE, exc);
					}
				}
			}
			if( FIELD_REASON_MESSAGE.equals(f.getName()) ) {
				try {
					f.set(rejectRow, reasonMessage);
				}
				catch(Exception exc) {
					if( logger.isWarnEnabled() ) {
						logger.warn(MESSAGE_CANT_SET_REASON_MESSAGE, exc);
					}
				}
			}
		}
	}

	@Override
	public void visitSuccess(Success success, Result parent, Map<String, Field> rejectFieldCache) {
	
		if( logger.isDebugEnabled() ) {
			logger.debug("visitSuccess() : no-op");
		}
		
		
	}
}
