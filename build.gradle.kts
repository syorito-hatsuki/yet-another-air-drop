import org.jetbrains.kotlin.gradle.dsl.JvmTarget

val javaVersion = JavaVersion.VERSION_21
val minecraftVersion: String by project
val modVersion: String by project

plugins {
    id("fabric-loom")
    kotlin("jvm")
    kotlin("plugin.serialization")
    id("com.modrinth.minotaur")
}

base {
    archivesName.set("yet-another-air-drop-$modVersion-$minecraftVersion")
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

modrinth {
    token.set(System.getenv("MODRINTH_TOKEN"))
    projectId.set("yet-another-air-drop")
    versionName.set("Yet Another Air Drop $modVersion")
    versionNumber.set(modVersion)
    versionType.set("release")
    uploadFile.set(tasks.remapJar)
    gameVersions.addAll("1.21.4")
    loaders.add("fabric")
    changelog.set(rootProject.file("CHANGELOG.md").readText())
    dependencies {
        required.project("fabric-api", "fabric-language-kotlin")
        embedded.project("ducky-updater-lib", "modmenu-badges-lib")
    }
}

tasks {
    named("modrinth").configure {
        doLast {
            val uploadTask = this@configure as com.modrinth.minotaur.TaskModrinthUpload
            @Suppress("UnstableApiUsage")
            val uploadInfo = uploadTask.uploadInfo ?: return@doLast
            val url = "https://modrinth.com/mod/yet-another-air-drop/version/${uploadInfo.id}"
            println(url)
            rootProject.file("build/modrinth_url.txt").writeText(url)
        }
    }

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
        filesMatching("fabric.mod.json") {
            expand(mutableMapOf("version" to modVersion))
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
