package com.github.MrRogerHuang;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.plugins.JavaPluginConvention;
import org.gradle.api.tasks.SourceSet;
import org.gradle.tooling.provider.model.ToolingModelBuilder;
import org.gradle.tooling.provider.model.ToolingModelBuilderRegistry;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class GradleProjectInfoPlugin implements Plugin<Project> {
    private final ToolingModelBuilderRegistry registry;

    @Inject
    public GradleProjectInfoPlugin(ToolingModelBuilderRegistry registry) {
        this.registry = registry;
    }

    @Override
    public void apply(Project project) {
        registry.register(new CustomToolingModelBuilder());
    }

    private static class CustomToolingModelBuilder implements ToolingModelBuilder {
        @Override
        public boolean canBuild(String modelName) {
            return modelName.equals(GradleProjectInfo.class.getName());
        }

        @Override
        public Object buildAll(String modelName, Project project) {
            List<String> pluginClassNames = new ArrayList<String>();

            for(Plugin plugin : project.getPlugins()) {
            pluginClassNames.add(plugin.getClass().getName());
        }

            List<String> classpaths = new ArrayList<>();
            project.getConvention().getPlugin(JavaPluginConvention.class).getSourceSets().findByName(SourceSet.MAIN_SOURCE_SET_NAME)
                    .getRuntimeClasspath().forEach(file -> classpaths.add(file.getAbsolutePath()));


            return new DefaultModel(pluginClassNames, classpaths);
        }
    }
}
