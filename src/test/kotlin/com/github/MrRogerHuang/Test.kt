package com.github.MrRogerHuang

import org.junit.Test
import org.junit.Assert.assertTrue

class Test {
    @Test fun testGradleProjectRunner() {
        val gradleProjectRunner = GradleProjectRunner()
        gradleProjectRunner.run("./test", true)
        assertTrue(true)
    }
}