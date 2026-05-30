package com.codefactory.appstripe.serenity.runners;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

import static io.cucumber.junit.platform.engine.Constants.GLUE_PROPERTY_NAME;
import static io.cucumber.junit.platform.engine.Constants.PLUGIN_PROPERTY_NAME;
import static io.cucumber.junit.platform.engine.Constants.FILTER_TAGS_PROPERTY_NAME;

/**
 * Runner principal para pruebas E2E con Serenity BDD + Cucumber (JUnit 5).
 * <p>
 * Spring Boot se inicia vía Cucumber {@code @BeforeAll} en {@code CommonSteps},
 * porque {@code @SpringBootTest} no es compatible con JUnit 5 {@code @Suite}.
 * <p>
 * Comandos de ejecución recomendados:
 * <pre>
 *   mvn clean test -Dtest=SerenityTestRunner
 *   mvn clean verify
 *   mvn serenity:aggregate
 * </pre>
 */
@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features")
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "com.codefactory.appstripe.serenity.steps")
@ConfigurationParameter(key = PLUGIN_PROPERTY_NAME, value = "net.serenitybdd.cucumber.core.plugin.SerenityReporter, pretty, json:target/serenity-reports/cucumber.json")
@ConfigurationParameter(key = FILTER_TAGS_PROPERTY_NAME, value = "not @ignored")
public class SerenityTestRunner {
    // Clase de configuración pura — el runner ejecuta los features via JUnit 5 @Suite.
    // Spring Boot se inicia desde CommonSteps con @BeforeAll de Cucumber.
}
