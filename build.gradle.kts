import org.jetbrains.kotlin.gradle.dsl.JvmTarget

val javaVersion = JavaVersion.VERSION_21
val minecraftVersion: String by project
val modVersion: String by project
val mavenGroup: String by project
val modId: String by project

plugins {
    id("fabric-loom")
    kotlin("jvm")
    kotlin("plugin.serialization")
}

base {
    archivesName.set("$modId-$modVersion-$minecraftVersion")
}

repositories {
    maven("https://api.modrinth.com/maven") {
        content {
            includeGroup("maven.modrinth")
        }
    }
}

loom {
    accessWidenerPath = File("src/main/resources/yet-another-air-drop.accesswidener")
}

dependencies {
    minecraft("com.mojang", "minecraft", minecraftVersion)

    val yarnMappings: String by project
    mappings("net.fabricmc", "yarn", yarnMappings, null, "v2")

    val loaderVersion: String by project
    modImplementation("net.fabricmc", "fabric-loader", loaderVersion)

    val fabricVersion: String by project
    modImplementation("net.fabricmc.fabric-api", "fabric-api", fabricVersion)

    val fabricKotlinVersion: String by project
    modImplementation("net.fabricmc", "fabric-language-kotlin", fabricKotlinVersion)

    val modMenuBadgesLibVersion: String by project
    include(modImplementation("maven.modrinth", "modmenu-badges-lib", modMenuBadgesLibVersion))

    val duckyUpdaterLibVersion: String by project
    include(modImplementation("maven.modrinth", "ducky-updater-lib", duckyUpdaterLibVersion))

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")
}

tasks {
    withType<JavaCompile> {
        options.encoding = "UTF-8"
        sourceCompatibility = javaVersion.toString()
        targetCompatibility = javaVersion.toString()
        options.release.set(javaVersion.toString().toInt())
    }

    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_21)
        }
    }

    jar {
        from("LICENSE")
    }

    processResources {
        val modName: String by project
        val modDescription: String by project

        filesMatching("fabric.mod.json") {
            expand(
                mutableMapOf(
                    "modId" to modId,
                    "modName" to modName,
                    "modVersion" to modVersion,
                    "modDescription" to modDescription,
                    "minecraftVersion" to minecraftVersion,
                )
            )
        }
        filesMatching("yetanotherairdrop.mixins.json") {
            expand(mutableMapOf("modId" to modId))
        }
    }

    java {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(javaVersion.toString()))
        }
        sourceCompatibility = javaVersion
        targetCompatibility = javaVersion
        withSourcesJar()
    }
}
