plugins {
    kotlin("jvm") version "2.3.21"
    kotlin("plugin.serialization") version "2.3.21"
}

group = "backend"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // Принудительное обновление всех транзитивных модулей Netty до безопасной версии
    implementation(enforcedPlatform("io.netty:netty-bom:4.1.132.Final"))

    // Серверная часть ktor
    implementation("io.ktor:ktor-server-core-jvm:3.0.3")
    implementation("io.ktor:ktor-server-netty-jvm:3.0.3")
    implementation("io.ktor:ktor-server-content-negotiation-jvm:3.0.3")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm:3.0.3")

    // Логирование — обновлено с 1.5.6 до безопасной 1.5.32
    implementation("ch.qos.logback:logback-classic:1.5.32")

    // Exposed
    implementation("org.jetbrains.exposed:exposed-core:0.57.0")
    implementation("org.jetbrains.exposed:exposed-dao:0.57.0")
    implementation("org.jetbrains.exposed:exposed-jdbc:0.57.0")

    // Драйвер с бд
    implementation("org.postgresql:postgresql:42.7.3")

    // Управление подключением
    implementation("com.zaxxer:HikariCP:5.1.0")


    // Swagger и OpenApi
    implementation("io.ktor:ktor-server-openapi:3.0.3")
    implementation("io.ktor:ktor-server-swagger:3.0.3")

    // Тестирование
    testImplementation(kotlin("test"))
}

kotlin {
    jvmToolchain(21)
}


tasks.test {
    useJUnitPlatform()
}