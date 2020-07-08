package com.github.MrRogerHuang;
import java.util.List;

public interface GradleProjectInfo {
    boolean hasPlugin(Class type);
    List<String> getClasspaths();
}
