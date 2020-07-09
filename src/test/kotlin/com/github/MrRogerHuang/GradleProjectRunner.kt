package com.github.MrRogerHuang

import org.gradle.api.plugins.JavaPlugin
import org.gradle.tooling.GradleConnector
import org.gradle.tooling.ProjectConnection
import com.github.MrRogerHuang.GradleProjectInfo

import java.io.File

class GradleProjectRunner {
    private fun gradleScriptToTempFile(scriptName: String, deleteOnExit: Boolean = false): File {
        val config = File.createTempFile("classpath", ".gradle")
        if (deleteOnExit) {
            config.deleteOnExit()
        }

        //LOG.debug("Creating temporary gradle file {}", config.absolutePath)

        config.bufferedWriter().use { configWriter ->
                ClassLoader.getSystemResourceAsStream(scriptName).bufferedReader().use { configReader ->
                configReader.copyTo(configWriter)
            }
        }

        return config
    }

    fun run(projectDirectory: String) {
        val connector = GradleConnector.newConnector()
        val gradleScript = gradleScriptToTempFile("init.gradle")
        val projectPath = File(projectDirectory)
        connector.forProjectDirectory(projectPath.absoluteFile)
        var connection: ProjectConnection? = null
        
        try {
            connection = connector.connect()
            connection.newBuild().forTasks("hello").addArguments("-b", gradleScript.absolutePath).addArguments("-Dorg.gradle.debug=true").addArguments("--debug-jvm").run()
            val customModelBuilder = connection.model(GradleProjectInfo::class.java)
            customModelBuilder.forTasks("hello")
            customModelBuilder.addArguments("-Dorg.gradle.debug=true")
            customModelBuilder.addArguments("-b", gradleScript.absolutePath)
            val model = customModelBuilder.get()
            val classpaths = model.classpaths
            classpaths?.forEach { println(it) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        finally {
            connection?.close()
        }
    }
}