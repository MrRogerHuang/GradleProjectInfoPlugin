package com.github.MrRogerHuang

import java.io.Serializable

class DefaultModel(private val pluginClassNames: List<String>, val classpaths: List<String>) : Serializable {
    fun hasPlugin(type: Class<*>): Boolean {
        return pluginClassNames.contains(type.name)
    }
}