## base-swagger-annotator
swagger for jsonschema2pojo
## 使用
```text
<plugin>
    <groupId>org.jsonschema2pojo</groupId>
    <artifactId>jsonschema2pojo-maven-plugin</artifactId>
    <dependencies>
        <dependency>
            <groupId>com.github.ollgei.base</groupId>
            <artifactId>base-jsonschema2pojo-library</artifactId>
            <version>1.0.0.RC1</version>
        </dependency>
        <dependency>
            <groupId>javax.validation</groupId>
            <artifactId>validation-api</artifactId>
            <version>2.0.1.Final</version>
        </dependency>
    </dependencies>
    <configuration>
        <sourceType>jsonschema</sourceType>
        <sourceDirectory>${basedir}/../fd-json-schema/statemachine</sourceDirectory>
        <targetPackage>com.lunz.fin.fd.statemachine.pojo</targetPackage>
        <targetLanguage>java</targetLanguage>
        <annotationStyle>jackson2</annotationStyle>
        <includeAdditionalProperties>false</includeAdditionalProperties>
        <includeJsr303Annotations>true</includeJsr303Annotations>
        <serializable>true</serializable>
        <classNameSuffix>Dto</classNameSuffix>
        <customAnnotator>com.github.ollgei.base.jsonschema2pojo.annotator.SwaggerAnnotator</customAnnotator>
        <customRuleFactory>com.github.ollgei.base.jsonschema2pojo.SpecPojoRuleFactory</customRuleFactory>
    </configuration>
    <executions>
        <execution>
            <goals>
                <goal>generate</goal>
            </goals>
        </execution>
    </executions>
</plugin>

```