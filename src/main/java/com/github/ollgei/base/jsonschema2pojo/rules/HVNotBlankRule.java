package com.github.ollgei.base.jsonschema2pojo.rules;

import java.util.Arrays;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.ollgei.base.jsonschema2pojo.SpecPojoRuleFactory;
import com.sun.codemodel.JAnnotationArrayMember;
import com.sun.codemodel.JAnnotationUse;
import com.sun.codemodel.JFieldVar;
import org.jsonschema2pojo.Schema;
import org.jsonschema2pojo.rules.Rule;

/**
 *  hiberanate validator notEmpty rule.
 * @author jiawei
 * @since 1.0.0
 */
public class HVNotBlankRule implements Rule<JFieldVar, JFieldVar> {

    private final SpecPojoRuleFactory ruleFactory;

    public HVNotBlankRule(SpecPojoRuleFactory ruleFactory) {
        this.ruleFactory = ruleFactory;
    }

    @Override
    public JFieldVar apply(String nodeName, JsonNode node, JsonNode parent, JFieldVar field, Schema currentSchema) {

        if (ruleFactory.getGenerationConfig().isIncludeJsr303Annotations()
                && node.has("notBlank") && isApplicableType(field)) {
            if (node.get("notBlank").asBoolean()) {
                JAnnotationUse annotation = field.annotate(NotBlank.class);
                if (node.has("notBlank_message")) {
                    annotation.param("message", node.get("notBlank_message").asText());
                }
                if (node.has("notBlank_groups")) {
                    final JAnnotationArrayMember groups = annotation.paramArray("groups");
                    Arrays.asList(node.get("notBlank_groups").asText().split(",")).stream().forEach(name -> {
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
