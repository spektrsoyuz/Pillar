plugins {
    id("java")
    id("com.gradleup.shadow") version "9.0.0-beta2"
    id("io.freefair.lombok") version "8.12.1"
}

group = "com.spektrsoyuz"
version = "1.0"

repositories {
    mavenCentral()
    maven {
        name = "papermc-repo"
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
    maven {
        name = "sonatype"
        url = uri("https://oss.sonatype.org/content/groups/public/")
    }
    maven {
        url = uri("https://repo.extendedclip.com/content/repositories/placeholderapi/")
    }
}

dependencies {
    implementation("com.zaxxer:HikariCP:6.2.1")
    implementation("redis.clients:jedis:5.2.0")
    implementation("org.spongepowered:configurate-hocon:4.1.2")
    compileOnly("io.papermc.paper:paper-api:1.21.4-R0.1-SNAPSHOT")
    compileOnly("me.clip:placeholderapi:2.11.6")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

tasks {
    shadowJar {
        archiveClassifier.set("")
    }
    build {
        dependsOn(shadowJar)
    }
}