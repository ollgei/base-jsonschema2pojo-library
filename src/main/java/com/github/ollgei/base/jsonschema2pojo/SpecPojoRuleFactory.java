package com.github.ollgei.base.jsonschema2pojo;

import com.github.ollgei.base.jsonschema2pojo.rules.HVNotBlankRule;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JFieldVar;
import org.jsonschema2pojo.rules.Rule;
import org.jsonschema2pojo.rules.RuleFactory;

/**
 * rule factory.
 * 
 * @author jiawei
 * @since 1.0.0
 */
public class SpecPojoRuleFactory extends RuleFactory {

    @Override
    public Rule<JDefinedClass, JDefinedClass> getPropertyRule() {
        return new SpecPojoPropertyRule(this);
    }

    public Rule<JFieldVar, JFieldVar> getHVNotBlankRule() {
        return new HVNotBlankRule(this);
    }
}
