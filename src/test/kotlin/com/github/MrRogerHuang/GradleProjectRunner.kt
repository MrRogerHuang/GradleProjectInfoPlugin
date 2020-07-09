package com.github.MrRogerHuang

import org.gradle.api.plugins.JavaPlugin
import org.gradle.tooling.GradleConnector
import org.gradle.tooling.ProjectConnection
import com.github.MrRogerHuang.GradleProjectInfo

import java.io.File
import org.junit.Assert.assertTrue

fun copyResource(scriptName: String, target: File) {
    target.bufferedWriter().use { configWriter ->
        ClassLoader.getSystemResourceAsStream(scriptName).bufferedReader().use { configReader ->
            configReader.copyTo(configWriter)
        }
    }
}

class GradleProjectRunner {
    private fun gradleScriptToTempFile(scriptName: String, deleteOnExit: Boolean = false): File {
        val config = File.createTempFile(scriptName, ".gradle")
        if (deleteOnExit) {
            config.deleteOnExit()
        }

        copyResource(scriptName, config)

        return config
    }

    fun run(projectDirectory: String, testMode: Boolean = false) {
        val connector = GradleConnector.newConnector()
        var gradleScript: File?
        val projectFile = File(projectDirectory)
        if (!testMode) {
            gradleScript = gradleScriptToTempFile("init.gradle")
            // Copy to the same temp path.
            copyResource("gradle.properties", File(gradleScript.parent + "/gradle.properties"))
        } else {
            gradleScript = File(projectDirectory + "/init.gradle")
        }
        connector.forProjectDirectory(projectFile.absoluteFile)
        var connection: ProjectConnection? = null
        
        try {
            connection = connector.connect()
            val customModelBuilder = connection.model(GradleProjectInfo::class.java)
            customModelBuilder.addArguments("-b", gradleScript.absolutePath)
            val model = customModelBuilder.get()
            val classpaths = model.classpaths
            println("Classpaths of project ${projectFile.canonicalPath}:")
            classpaths?.forEach { println(it) }
        } catch (e: Exception) {
            e.printStackTrace()
            assertTrue(false)
        }
        finally {
            connection?.close()
        }
    }
}