package com.github.MrRogerHuang

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginConvention
import org.gradle.api.tasks.SourceSet
import org.gradle.tooling.provider.model.ToolingModelBuilder
import org.gradle.tooling.provider.model.ToolingModelBuilderRegistry
import java.io.File
import java.util.*
import java.util.function.Consumer
import javax.inject.Inject

class GradleProjectInfoPlugin @Inject constructor(private val registry: ToolingModelBuilderRegistry) : Plugin<Project?> {
    override fun apply(project: Project?) {
        registry.register(CustomToolingModelBuilder())
    }

    private class CustomToolingModelBuilder : ToolingModelBuilder {
        override fun canBuild(modelName: String): Boolean {
            return modelName == GradleProjectInfo::class.java.name
        }

        override fun buildAll(modelName: String, project: Project): Any {
            val pluginClassNames: MutableList<String> = ArrayList()
            for (plugin in project.plugins) {
                pluginClassNames.add(plugin.javaClass.name)
            }
            val classpaths: MutableList<String> = ArrayList()
            project.convention.getPlugin(JavaPluginConvention::class.java).sourceSets
                    .findByName(SourceSet.MAIN_SOURCE_SET_NAME)?.runtimeClasspath?.forEach(Consumer {
                        file: File -> classpaths.add(file.absolutePath)
                    })
            return DefaultModel(pluginClassNames, classpaths)
        }
    }

}