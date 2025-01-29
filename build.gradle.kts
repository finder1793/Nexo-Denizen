import org.apache.tools.ant.filters.ReplaceTokens

plugins {
    `java-library`
    `maven-publish`
    alias(libs.plugins.shadow)
    alias(libs.plugins.run.paper)
}

group = "com.pwing"
version = "1.0-SNAPSHOT"

repositories {
    mavenLocal()
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://maven.citizensnpcs.co/repo")
    maven("https://repo.nexomc.com/snapshots/")
}

dependencies {
    implementation("org.jetbrains:annotations:22.0.0")
    compileOnly(libs.denizen)
    compileOnly(libs.nexo)
    compileOnly(libs.paper.api)
}

java {
    disableAutoTargetJvm()
    toolchain.languageVersion = JavaLanguageVersion.of(21)
}

tasks {
    jar {
        enabled = false
    }

    shadowJar {
        archiveFileName.set("${rootProject.name}-${project.version}.jar")
        archiveClassifier.set("")

        manifest {
            attributes["Implementation-Version"] = project.version
        }

        configurations = listOf(project.configurations.shadow.get())
        minimize()
    }

    assemble {
        dependsOn(shadowJar)
    }

    withType<JavaCompile> {
        options.encoding = "UTF-8"
        options.release.set(17)
    }

    withType<Javadoc> {
        options.encoding = "UTF-8"
    }

    processResources {
        filter<ReplaceTokens>("tokens" to mapOf("version" to project.version))
        inputs.property("version", project.version)

        filesMatching("plugin.yml") {
            expand("version" to project.version)
        }
    }

    defaultTasks("build")

    val version = "1.21.1"
    val javaVersion = JavaLanguageVersion.of(21)

    val jvmArgsExternal = listOf(
        "-Dcom.mojang.eula.agree=true"
    )

    runServer {
        minecraftVersion(version)
        runDirectory.set(rootDir.resolve("run/paper/$version"))

        javaLauncher.set(project.javaToolchains.launcherFor {
            languageVersion.set(javaVersion)
        })

        jvmArgs = jvmArgsExternal
    }
}
