plugins {
    id("org.jetbrains.kotlin.jvm")
    id("org.jetbrains.kotlin.kapt")
    id("org.jetbrains.kotlin.plugin.allopen")

    id("org.springframework.boot") version "2.7.5"
    id("io.spring.dependency-management") version "1.0.15.RELEASE"
    id("org.jetbrains.kotlin.plugin.spring")
    id("org.jetbrains.kotlin.plugin.jpa")
    id("com.palantir.docker") version "0.34.0"
    id("jacoco")
}

group = "mis055"
version = "1.0.0-SNAPSHOT"

repositories {
    mavenCentral()
}


configurations.all {
    exclude("org.springframework.boot", "spring-boot-starter-logging")
}

dependencies {

    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-log4j2")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-amqp")

//    implementation("org.springframework.boot:spring-boot-starter-security")


    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    // db section
    implementation("org.flywaydb:flyway-core")
    runtimeOnly("org.postgresql:postgresql")

    implementation("org.zalando:problem-spring-web-starter:0.27.0")

    // openapi
    implementation("org.springdoc:springdoc-openapi-ui:1.6.12")

    // mapstruct
    kapt("org.mapstruct:mapstruct-processor:1.5.1.Final")
    implementation("org.mapstruct:mapstruct:1.5.1.Final")

    // tests dependencies
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(module = "mockito-core")
        exclude(module = "org.hamcrest")
    }
    testImplementation("org.springframework.security:spring-security-test")
    testImplementation("com.willowtreeapps.assertk:assertk-jvm:0.25")
    testImplementation("io.mockk:mockk:1.13.2")
    testImplementation("com.ninja-squad:springmockk:3.1.1")

    testImplementation("org.testcontainers:junit-jupiter:1.17.6")
    testImplementation("org.testcontainers:postgresql:1.17.6")
    testRuntimeOnly("com.h2database:h2")

}

kapt {
    correctErrorTypes = true
    arguments {
        // add default component for mapstruct to generated @Injectable classes
        arg("mapstruct.defaultComponentModel", "spring")
    }
}

tasks {
    compileKotlin {
        kotlinOptions {
            jvmTarget = JavaVersion.VERSION_11.toString()
            // interface default method
            freeCompilerArgs += "-Xjvm-default=all"
        }
    }
    compileTestKotlin {
        kotlinOptions {
            jvmTarget = JavaVersion.VERSION_11.toString()
        }
    }
}

docker {
    name = "misecius-app:latest"
    tag("name", "misecius")

    buildArgs(mapOf("name" to "misecius"))
    copySpec.from("build/libs").into("distribution")
    pull(true)
    setDockerfile(file("Dockerfile"))

}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.check {
    dependsOn(tasks.jacocoTestCoverageVerification)
}

tasks.jacocoTestReport {
    dependsOn(tasks.test) // tests are required to run before generating the report
    reports {
        xml.required.set(true)
        csv.required.set(false)
        html.outputLocation.set(layout.buildDirectory.dir("sites"))
    }
}

tasks.jacocoTestCoverageVerification {
    dependsOn(tasks.jacocoTestReport)
    violationRules {
        rule {
            limit {
                minimum = "0.5".toBigDecimal()
            }
        }
    }
}

// testing logging
tasks.test {
    useJUnitPlatform()
    val errorTests = mutableListOf<String>()
    testLogging {
        showStandardStreams = true
        exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
        events("skipped", "failed", "passed", "STANDARD_OUT", "STANDARD_ERROR")
    }
    afterTest(
        KotlinClosure2<TestDescriptor, TestResult, Unit>(
            { desc, result ->
                if (result.resultType == org.gradle.api.tasks.testing.TestResult.ResultType.FAILURE) {
                    errorTests.add("${desc.className}:${desc.name}")
                }
            }
        )
    )
    afterSuite(
        KotlinClosure2<TestDescriptor, TestResult, Unit>(
            { desc, result ->
                if (desc.parent == null) {
                    println("------------------------------------------------")
                    println("\nTest result: ${result.resultType}")
                    println(
                        "Test summary: ${result.testCount} tests, " +
                                "${result.successfulTestCount} succeeded, " +
                                "${result.failedTestCount} failed, " +
                                "${result.skippedTestCount} skipped"
                    )
                    println()
                    if (errorTests.isNotEmpty()) {
                        System.err.println("\u001B[31;4m" + "Failure tests:")
                        println("\u001B[0m")
                        System.err.println("\u001B[31m" + errorTests.joinToString(separator = System.lineSeparator()))
                    }
                }
            }
        )
    )
    finalizedBy(tasks.jacocoTestReport)
}
