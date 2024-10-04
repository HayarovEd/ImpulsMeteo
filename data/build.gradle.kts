plugins {
    alias(libs.plugins.jetbrains.kotlin.jvm)
    alias(libs.plugins.jetbrains.kotlin.serialization)
}

dependencies {
    implementation(project(":domain"))

    //datastore
    api(libs.androidx.datastore.preferences)

    //ktor
    //ktor
    api(libs.ktor.client.core)
    api(libs.ktor.client.okhttp)
    api(libs.ktor.client.content.negotiation)
    api(libs.ktor.serialization.kotlinx.json)
    api(libs.ktor.client.logging)
    api(libs.ktor.client.andriod)
}