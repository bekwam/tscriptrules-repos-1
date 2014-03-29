package com.bekwam.talend.component.scriptrules.schema;

/**
 * A three-field structure used in unit tests to mimic a reject schema
 * 
 * @author Carl2
 *
 */
public class row3Struct {

	public String field1;
	public String reasonCode;
	public String reasonMessage;
	
	public row3Struct() {}

	public row3Struct(String field1, String reasonCode, String reasonMessage) {
		super();
		this.field1 = field1;
		this.reasonCode = reasonCode;
		this.reasonMessage = reasonMessage;
	}

	public String getField1() {
		return field1;
	}
	public void setField1(String field1) {
		this.field1 = field1;
	}
	public String getReasonCode() {
		return reasonCode;
	}
	public void setReasonCode(String reasonCode) {
		this.reasonCode = reasonCode;
	}
	public String getReasonMessage() {
		return reasonMessage;
	}
	public void setReasonMessage(String reasonMessage) {
		this.reasonMessage = reasonMessage;
	}
}
