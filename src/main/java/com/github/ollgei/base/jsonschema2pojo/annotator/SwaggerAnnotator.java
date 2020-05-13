package com.github.ollgei.base.jsonschema2pojo.annotator;

import com.fasterxml.jackson.databind.JsonNode;
import com.sun.codemodel.JAnnotationUse;
import com.sun.codemodel.JClass;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JFieldVar;
import com.sun.codemodel.JNullType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.jsonschema2pojo.AbstractAnnotator;
import org.jsonschema2pojo.GenerationConfig;

/**
 * swagger.
 * @author ollgei
 * @since 1.0.0
 */
public class SwaggerAnnotator extends AbstractAnnotator {

    private static final String NODE_PROPERTY_SWAGGER_ENABLED= "swaggerEnabled";
    private static final String NODE_PROPERTY_DESCRIPTION = "description";
    private static final String NODE_PROPERTY_REQUIRED = "required";
    private static final String NODE_PROPERTY_EXAMPLE = "example";
    private static final String NODE_PROPERTY_ALLOWABLE_VALUES = "allowableValues";
    private static final String ANNOTATION_PARAM_NAME = "name";
    private static final String ANNOTATION_PARAM_VALUE = "value";
    private static final String ANNOTATION_PARAM_REQUIRED = "required";
    private static final String ANNOTATION_PARAM_DESCRIPTION = "description";
    private static final String ANNOTATION_PARAM_ALLOWABLE_VALUES = "allowableValues";
    private static final String ANNOTATION_PARAM_EXAMPLE = "example";


    public SwaggerAnnotator(GenerationConfig generationConfig) {
        super(generationConfig);
    }

    @Override
    public void typeInfo(JDefinedClass clazz, JsonNode node) {
        if (node.has(NODE_PROPERTY_SWAGGER_ENABLED) && node.get(NODE_PROPERTY_SWAGGER_ENABLED).asBoolean(false)) {
            JAnnotationUse annotation = clazz.annotate(ApiModel.class);
            if (node.has(NODE_PROPERTY_DESCRIPTION)) {
                annotation.param(ANNOTATION_PARAM_DESCRIPTION, node.get(NODE_PROPERTY_DESCRIPTION).asText());
            }
        }
    }

    @Override
    public void propertyField(JFieldVar field, JDefinedClass clazz, String propertyName, JsonNode node) {
        if (!modelClazzAnnotation(clazz)) {
            return;
        }

        JAnnotationUse annotation =
                modelPropertyAnnotation(field).param(ANNOTATION_PARAM_NAME, propertyName);
        if (node.has(NODE_PROPERTY_DESCRIPTION)) {
            annotation.param(ANNOTATION_PARAM_VALUE, node.get(NODE_PROPERTY_DESCRIPTION).asText());
        }
        if (node.has(NODE_PROPERTY_REQUIRED)) {
            annotation.param(ANNOTATION_PARAM_REQUIRED, node.get(NODE_PROPERTY_REQUIRED).asBoolean());
        }

        if (node.has(NODE_PROPERTY_ALLOWABLE_VALUES)) {
            modelPropertyAnnotation(field).param(ANNOTATION_PARAM_ALLOWABLE_VALUES,
                    node.get(NODE_PROPERTY_ALLOWABLE_VALUES).asText());
        }

        if (node.has(NODE_PROPERTY_EXAMPLE)) {
            modelPropertyAnnotation(field).param(ANNOTATION_PARAM_EXAMPLE,
                    node.get(NODE_PROPERTY_EXAMPLE).asText());
        }

    }

    private boolean modelClazzAnnotation(JDefinedClass clazz) {
        JClass annotationClass = clazz.owner().ref(ApiModel.class);
        if (annotationClass == null) {
            return false;
        }
        if (annotationClass instanceof JNullType) {
            return false;
        }
        for (JAnnotationUse annotationUse : clazz.annotations()) {
            if (annotationUse.getAnnotationClass().isAssignableFrom(annotationClass)) {
                return true;
            }
        }
        return false;
    }

    private JAnnotationUse modelPropertyAnnotation(JFieldVar field) {
        JClass annotationClass = field.type().owner().ref(ApiModelProperty.class);
        if (annotationClass == null) {
            return field.annotate(ApiModelProperty.class);
        }
        for (JAnnotationUse annotationUse : field.annotations()) {
            if (annotationUse.getAnnotationClass().isAssignableFrom(annotationClass)) {
                return annotationUse;
            }
        }
        return field.annotate(ApiModelProperty.class);
    }

    @Override
    public boolean isAdditionalPropertiesSupported() {
        return false;
    }

}
