package com.github.MrRogerHuang

interface GradleProjectInfo {
    fun hasPlugin(type: Class<*>?): Boolean
    val classpaths: List<String?>?
}