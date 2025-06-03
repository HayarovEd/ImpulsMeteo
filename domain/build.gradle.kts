plugins {
    alias(libs.plugins.jetbrains.kotlin.jvm)
    alias(libs.plugins.jetbrains.kotlin.serialization)
}




dependencies {
    implementation(libs.kotlinx.coroutines.android)
    api(libs.kotlinx.datetime)
    implementation(libs.ktor.serialization.kotlinx.json)
}