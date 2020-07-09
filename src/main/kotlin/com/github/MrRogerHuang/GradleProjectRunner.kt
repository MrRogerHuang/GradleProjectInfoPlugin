package com.github.MrRogerHuang

import org.gradle.tooling.GradleConnector
import org.gradle.tooling.ProjectConnection

import java.io.File

fun copyResource(scriptName: String, target: File) {
    target.bufferedWriter().use { configWriter ->
        ClassLoader.getSystemResourceAsStream(scriptName).bufferedReader().use { configReader ->
            configReader.copyTo(configWriter)
        }
    }
}

class GradleProjectRunner {
    var model: GradleProjectInfo? = null

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
            //copyResource("gradle.properties", File(gradleScript.parent + "/gradle.properties"))
        } else {
            gradleScript = File(projectDirectory + "/init.gradle")
        }
        connector.forProjectDirectory(projectFile.absoluteFile)
        var connection: ProjectConnection? = null
        
        try {
            connection = connector.connect()
            val customModelBuilder = connection.model(GradleProjectInfo::class.java)
            customModelBuilder.addArguments("-I", gradleScript.absolutePath)
            model = customModelBuilder.get()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        finally {
            connection?.close()
        }
    }
}