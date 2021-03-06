package com.github.MrRogerHuang

import org.junit.Test
import org.junit.Assert.assertTrue

class Test {
    @Test fun testGradleProjectRunner() {
        val gradleProjectRunner = GradleProjectRunner()
        try {
            gradleProjectRunner.run(".", true)
            val model = gradleProjectRunner.model
            model?.classpaths?.forEach { println(it) }
            assertTrue(true)
        } catch (e: Exception) {
            e.printStackTrace()
            assertTrue(false)
        }
    }
}