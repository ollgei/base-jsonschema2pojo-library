package com.github.ollgei.base.jsonschema2pojo;

import com.fasterxml.jackson.databind.JsonNode;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JFieldVar;
import org.jsonschema2pojo.Schema;
import org.jsonschema2pojo.rules.PropertyRule;

/**
 * spec property rule.
 * @author jiawei
 * @since 1.0.0
 */
public class SpecPojoPropertyRule extends PropertyRule {

    private final SpecPojoRuleFactory specPojoRuleFactory;

    public SpecPojoPropertyRule(SpecPojoRuleFactory specPojoRuleFactory) {
        super(specPojoRuleFactory);
        this.specPojoRuleFactory = specPojoRuleFactory;
    }

    @Override
    public JDefinedClass apply(String nodeName, JsonNode node, JsonNode parent, JDefinedClass jclass, Schema schema) {
        super.apply(nodeName, node, parent, jclass, schema);
        final String propertyName = specPojoRuleFactory.getNameHelper().getPropertyName(nodeName, node);
        final JFieldVar field = jclass.fields().get(propertyName);

        specPojoRuleFactory.getHVNotBlankRule().apply(nodeName, node, parent, field, schema);

        specPojoRuleFactory.getHVNotEmptyRule().apply(nodeName, node, parent, field, schema);

        return jclass;
    }
}
