package com.github.ollgei.base.jsonschema2pojo.rules;

import java.util.Arrays;

import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.ollgei.base.jsonschema2pojo.SpecPojoRuleFactory;
import com.sun.codemodel.JAnnotationArrayMember;
import com.sun.codemodel.JAnnotationUse;
import com.sun.codemodel.JFieldVar;
import org.jsonschema2pojo.Schema;
import org.jsonschema2pojo.rules.Rule;

/**
 *  hiberanate validator NotBlank rule.
 * {@link javax.validation.constraints.NotEmpty}
 * @author jiawei
 * @since 1.0.0
 */
public class HVNotEmptyRule implements Rule<JFieldVar, JFieldVar> {

    private final SpecPojoRuleFactory ruleFactory;

    public HVNotEmptyRule(SpecPojoRuleFactory ruleFactory) {
        this.ruleFactory = ruleFactory;
    }

    @Override
    public JFieldVar apply(String nodeName, JsonNode node, JsonNode parent, JFieldVar field, Schema currentSchema) {

        if (ruleFactory.getGenerationConfig().isIncludeJsr303Annotations()
                && node.has("notEmpty") && isApplicableType(field)) {
            if (node.get("notEmpty").asBoolean()) {
                final JAnnotationUse annotation = field.annotate(NotEmpty.class);
                if (node.has("notEmptyMessage")) {
                    annotation.param("message", node.get("notEmptyMessage").asText());
                }
                if (node.has("notEmptyGroups")) {
                    final JAnnotationArrayMember groups = annotation.paramArray("groups");
                    Arrays.asList(node.get("notEmptyGroups").asText().split(",")).stream().forEach(name -> {
                        try {
                            groups.param(Class.forName(name));
                        } catch (ClassNotFoundException e) {
                            ;
                        }
                    });
                }
            }
        }

        return field;
    }

    private boolean isApplicableType(JFieldVar field) {
        try {
            Class<?> fieldClass = Class.forName(field.type().boxify().fullName());
            return String.class.isAssignableFrom(fieldClass);
        } catch (ClassNotFoundException ignore) {
            return false;
        }
    }
}
