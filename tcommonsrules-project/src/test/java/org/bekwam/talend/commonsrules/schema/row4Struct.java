package org.bekwam.talend.commonsrules.schema;

/**
 * A two-field schema used in unit tests
 * 
 * @author Carl2
 *
 */
public class row4Struct {

	private String field1;
	private String field2;
	
	
	public row4Struct() {
		super();
	}

	public row4Struct(String field1, String field2) {
		super();
		this.field1 = field1;
		this.field2 = field2;
	}
	
	public String getField1() {
		return field1;
	}
	public void setField1(String field1) {
		this.field1 = field1;
	}
	public String getField2() {
		return field2;
	}
	public void setField2(String field2) {
		this.field2 = field2;
	}
	
	
}
