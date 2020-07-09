# Notice
This Gradle plugin can be used to load a Gradle project information to a standalone Java or Kotlin program. At this time, it supports to acquire two kinds of information: 
1. Does a Gradle project contain some one Gradle plugin.
2. What classpaths a Java / Kotlin Gradle project uses.

This plugin mainly demonstrates how to acquire a Gradle project information by Gradle Tooling APIs. Anyone can fork the Git repo to extend it for acquiring more information from a Gradle project. However, the author might not do that.

# Test the plugin
`./gradlew test`

# Publish the plugin
`./gradlew publishPlugins`

# Run the example
`./gradlew :example:run`

# Info on plugins.gradle.org
https://plugins.gradle.org/plugin/com.github.MrRogerHuang.GradleProjectInfo