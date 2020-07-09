package com.github.MrRogerHuang

fun main() {
    val gradleProjectRunner = GradleProjectRunner()
    try {
        gradleProjectRunner.run(".")
        val model = gradleProjectRunner.model
        model?.classpaths?.forEach { println(it) }
        println("Done!")
    } catch (e: Exception) {
        e.printStackTrace()
    }
}