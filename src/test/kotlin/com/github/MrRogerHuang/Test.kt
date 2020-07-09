package com.github.MrRogerHuang

import org.junit.Test
import org.junit.Assert.assertTrue

class Test {
    @Test fun testGradleProjectRunner() {
        println("Hi!------------------------------")
        val gradleProjectRunner = GradleProjectRunner()
        gradleProjectRunner.run(".")
        assertTrue(true)
    }
}