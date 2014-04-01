package org.bekwam.talend.component.scriptrules;

import java.util.List;

import org.bekwam.talend.commons.Connection;
import org.bekwam.talend.commonsrules.RuleList;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Names;

/**
 * Guice module for tScriptRules Component
 * 
 * @author Carl2
 *
 */
final public class ScriptRulesModule extends AbstractModule {

	private final RuleList ruleList;
	private final Boolean runAllMode;
	private final Connection inputConn;
	private final Connection filterConn;
	private final Connection rejectConn;
	private final List<String> routineClassNames;
	
	public ScriptRulesModule(RuleList ruleList, 
		     				 Boolean runAllMode,
		     				 Connection inputConn, 
		     				 Connection filterConn, 
		     				 Connection rejectConn,
		     				 List<String> routineClassNames) {
		this.ruleList = ruleList;
		this.runAllMode = runAllMode;
		this.inputConn = inputConn;
		this.filterConn = filterConn;
		this.rejectConn = rejectConn;
		this.routineClassNames = routineClassNames;
	}

	@Override
	protected void configure() {		
		bind(RuleList.class).toInstance(ruleList);		
		bind(Boolean.class).annotatedWith(Names.named("RunAllMode")).toInstance(runAllMode);
		bind(Connection.class).annotatedWith(Names.named("Input")).toInstance(inputConn);
		bind(Connection.class).annotatedWith(Names.named("Filter")).toInstance(filterConn);
		bind(Connection.class).annotatedWith(Names.named("Reject")).toInstance(rejectConn);
		bind(new TypeLiteral<List<String>>() {}).toInstance(routineClassNames);
	}

}
