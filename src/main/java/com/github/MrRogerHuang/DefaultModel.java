package com.github.MrRogerHuang;

import java.io.Serializable;
import java.util.List;

public class DefaultModel implements Serializable {
    private final List<String> pluginClassNames;
    private final List<String> classpaths;
    
    public DefaultModel(List<String> pluginClassNames, List<String> classpaths) {
        this.pluginClassNames = pluginClassNames;
        this.classpaths = classpaths;
    }
    
    public boolean hasPlugin(Class type) {              
        return pluginClassNames.contains(type.getName());
    }
    
    public List<String> getClasspaths() { return classpaths; }
}
