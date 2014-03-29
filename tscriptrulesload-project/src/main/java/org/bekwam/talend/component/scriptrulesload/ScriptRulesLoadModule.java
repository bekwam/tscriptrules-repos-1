package com.bekwam.talend.component.scriptrulesload;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;

final public class ScriptRulesLoadModule extends AbstractModule {

	final private String jexlExpressionColumnName;
	final private String reasonCodeColumnName;
	final private String reasonMessageColumnName;
	final private boolean lenient;
	
	public ScriptRulesLoadModule(			
			   String jexlExpressionColumnName, 
			   String reasonCodeColumnName,
			   String reasonMessageColumnName,
			   boolean lenient) {
		this.jexlExpressionColumnName = jexlExpressionColumnName;
		this.reasonCodeColumnName = reasonCodeColumnName;
		this.reasonMessageColumnName = reasonMessageColumnName;
		this.lenient = lenient;
	}

	@Override
	protected void configure() {		
		bind(Boolean.class).annotatedWith(Names.named("Lenient")).toInstance(lenient);
		bind(String.class).annotatedWith(Names.named("JexlExpressionCol")).toInstance(jexlExpressionColumnName);
		bind(String.class).annotatedWith(Names.named("ReasonCodeCol")).toInstance(reasonCodeColumnName);
		bind(String.class).annotatedWith(Names.named("ReasonMessageCol")).toInstance(reasonMessageColumnName);
	}

}
