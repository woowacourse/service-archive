import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "2.2.6.RELEASE"
	id("io.spring.dependency-management") version "1.0.9.RELEASE"
	kotlin("jvm") version "1.3.71"
	kotlin("plugin.spring") version "1.3.71"
	kotlin("plugin.jpa") version "1.3.71"
}

group = "camp.nextstep"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_1_8
extra["junit-jupiter.version"] = "5.4.2"

repositories {
	mavenCentral()
}

dependencies {
	// Spring
	implementation("org.springframework.boot:spring-boot-starter")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")

	// Log
	implementation("net.logstash.logback:logstash-logback-encoder:6.1")
	runtimeOnly("net.rakugakibox.spring.boot:logback-access-spring-boot-starter:2.7.1")

	// Kotlin
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("io.github.microutils:kotlin-logging:1.5.4")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.9.0")

	// Test
	testImplementation("org.junit.jupiter:junit-jupiter")
	testImplementation("org.springframework.boot:spring-boot-starter-test") {
		exclude(group = "junit")
	}
	testImplementation("org.assertj:assertj-core:3.12.2")

	// Databases
	implementation("org.hibernate:hibernate-java8")
	runtimeOnly("com.h2database:h2")
	runtimeOnly("mysql:mysql-connector-java:8.0.17")
}

tasks.withType<Test> {
	useJUnitPlatform()
	jvmArgs("-Djava.security.egd=file:/dev/./urandom", "-Dspring.config.location=classpath:/config/")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "1.8"
	}
}
