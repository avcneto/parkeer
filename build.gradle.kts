plugins {
    java
    war
    id("org.springframework.boot") version "3.1.3"
    id("io.spring.dependency-management") version "1.1.3"
}

group = "com.parkeer"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
    implementation("dev.miku:r2dbc-mysql:0.8.2.RELEASE")
    implementation("io.r2dbc:r2dbc-pool:1.0.1.RELEASE")
    implementation("com.mysql:mysql-connector-j:8.1.0")
    implementation("org.springframework.boot:spring-boot-starter-data-redis-reactive")
    implementation("redis.clients:jedis:5.0.1")
    implementation("javax.persistence:javax.persistence-api:2.2")
    implementation("org.flywaydb:flyway-core")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springdoc:springdoc-openapi-webflux-core:1.7.0")
    implementation("org.springdoc:springdoc-openapi-webflux-ui:1.7.0")
    implementation("org.springframework:spring-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    compileOnly("org.projectlombok:lombok")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    annotationProcessor("org.projectlombok:lombok")
    providedRuntime("org.springframework.boot:spring-boot-starter-tomcat")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.projectreactor:reactor-test")
    implementation("org.flywaydb:flyway-mysql:9.22.3")
    implementation("org.springframework.security:spring-security-crypto:6.1.5")
    implementation("org.hibernate:hibernate-core:6.4.0.CR1")
    implementation("org.hibernate:hibernate-java8:5.0.3.Final")
    implementation("org.hibernate:hibernate-validator:8.0.0.Final")

}

tasks.withType<Test> {
    useJUnitPlatform()
}

//// Trecho de codigo para fazer teste de carga e não utrapassar o limite do recurso estipulado.
//tasks.withType<JavaExec> {
//    jvmArgs = listOf("-Xmx2g") // Define 2 GB como limite de memória
//}
//
//tasks.withType<JavaExec> {
//    // Configura o número máximo de threads do pool de threads
//    systemProperty("java.util.concurrent.ForkJoinPool.common.parallelism", "4")
//}