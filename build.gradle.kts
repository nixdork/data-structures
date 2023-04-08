import org.jetbrains.kotlin.gradle.dsl.KotlinTopLevelExtension

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    `java-library`
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.task.tree)
    id("local.build")
    id("local.analysis")
}

repositories {
    mavenCentral()
}

logger.lifecycle("> JDK toolchain version: ${java.toolchain.languageVersion.get()}")
logger.lifecycle("> Kotlin version: ${extensions.findByType<KotlinTopLevelExtension>()?.coreLibrariesVersion}")

dependencies {
    implementation(gradleKotlinDsl())
    implementation(libs.kotlin.stdlib)

    testImplementation(libs.serpro.kotlin.faker)
    testImplementation(libs.bundles.kotest)

    detektPlugins(libs.arturbosch.detekt.formatting)
}
