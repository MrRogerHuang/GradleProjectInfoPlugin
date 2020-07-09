package com.github.MrRogerHuang

import groovy.text.GStringTemplateEngine
import org.gradle.tooling.GradleConnector
import org.gradle.tooling.ProjectConnection

import java.io.File
import java.io.Reader
import java.util.*


fun readResource(scriptName: String): Reader {
    return ClassLoader.getSystemResourceAsStream(scriptName).bufferedReader()
}

class GradleProjectRunner {
    var model: GradleProjectInfo? = null

    private fun saveToTempFile(text: String, deleteOnExit: Boolean = false): File {
        val config = File.createTempFile("temp_", ".gradle")
        if (deleteOnExit) {
            config.deleteOnExit()
        }

        config.bufferedWriter().use { writer ->
            writer.write(text)
        }

        return config
    }

    fun run(projectDirectory: String, testMode: Boolean = false) {
        val connector = GradleConnector.newConnector()
        val projectFile = File(projectDirectory)
        // Get plugin version.
        var props = Properties()
        val propsReader = readResource("gradle.properties")
        props.load(propsReader)
        val version = props.getProperty("version")
        var bindMap = mapOf("version" to version)

        if (testMode) {
            bindMap = bindMap.plus("pluginRootDir" to projectFile.canonicalPath)
        }

        // Replace ${version}.
        val renderedInitGradleReader = readResource("init.gradle")
        val renderedInitGradle = GStringTemplateEngine().createTemplate(renderedInitGradleReader).make(bindMap).toString()

        var gradleScript = saveToTempFile(renderedInitGradle)
        connector.forProjectDirectory(projectFile.absoluteFile)

        var connection: ProjectConnection? = null

        try {
            connection = connector.connect()
            val customModelBuilder = connection.model(GradleProjectInfo::class.java)
            customModelBuilder.addArguments("-I", gradleScript.absolutePath)
            model = customModelBuilder.get()
        } finally {
            connection?.close()
        }
    }
}