package org.bekwam.talend.component.scriptrules;

import org.bekwam.talend.commonsrules.RuleList;

import org.bekwam.talend.commons.Connection;
import com.google.inject.AbstractModule;
import com.google.inject.name.Names;

final public class ScriptRulesModule extends AbstractModule {

	private final RuleList ruleList;
	private final Boolean runAllMode;
	private final Connection inputConn;
	private final Connection filterConn;
	private final Connection rejectConn;
	
	public ScriptRulesModule(RuleList ruleList, 
						     Boolean runAllMode,
			   				 Connection inputConn, 
			   				 Connection filterConn, 
			   				 Connection rejectConn) {
		this.ruleList = ruleList;
		this.runAllMode = runAllMode;
		this.inputConn = inputConn;
		this.filterConn = filterConn;
		this.rejectConn = rejectConn;
	}
	
	@Override
	protected void configure() {		
		bind(RuleList.class).toInstance(ruleList);		
		bind(Boolean.class).annotatedWith(Names.named("RunAllMode")).toInstance(runAllMode);
		bind(Connection.class).annotatedWith(Names.named("Input")).toInstance(inputConn);
		bind(Connection.class).annotatedWith(Names.named("Filter")).toInstance(filterConn);
		bind(Connection.class).annotatedWith(Names.named("Reject")).toInstance(rejectConn);
	}

}
