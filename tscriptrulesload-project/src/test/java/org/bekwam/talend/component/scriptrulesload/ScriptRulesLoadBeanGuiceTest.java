package org.bekwam.talend.component.scriptrulesload;

import org.apache.commons.collections.CollectionUtils;
import org.bekwam.talend.commonsrules.ScriptRulesValidationException;
import org.bekwam.talend.component.scriptrulesload.ScriptRulesLoadBean;
import org.bekwam.talend.component.scriptrulesload.ScriptRulesLoadModule;
import org.bekwam.talend.component.scriptrulesload.schema.row1Struct;

import org.bekwam.talend.commons.Counter;
import com.google.inject.Guice;
import com.google.inject.Injector;

public class ScriptRulesLoadBeanGuiceTest {

	public static void main(String[] args) throws ScriptRulesValidationException {
		
		Injector injector = Guice.createInjector(
				new ScriptRulesLoadModule("jexlExpression", "reasonCode", "reasonMessage", false)
				);
		
		ScriptRulesLoadBean loadBean = injector.getInstance(ScriptRulesLoadBean.class);
		
		Counter counter1 = new Counter();
		
		row1Struct row1 = new row1Struct();
		
		row1.jexlExpression = "row1.field1 == 'ok'";
		row1.reasonCode = "1";
		row1.reasonMessage = "field1 ok";
		
		Counter counter2 = loadBean.addRule(row1, counter1);
		
		System.out.println("Rule added..." + CollectionUtils.isNotEmpty(loadBean.getRuleList().getRules()) );
		System.out.println("Counter..." + counter2);
	}
}
