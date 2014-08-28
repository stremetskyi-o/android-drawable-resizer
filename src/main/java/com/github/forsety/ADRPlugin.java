package com.github.forsety;

import com.android.build.gradle.AppExtension;
import com.android.build.gradle.AppPlugin;
import com.android.build.gradle.LibraryExtension;
import com.android.build.gradle.api.BaseVariant;
import com.android.build.gradle.tasks.MergeResources;
import com.github.forsety.task.ResizeDrawablesTask;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

import java.util.HashMap;

/**
 * Created by Forsety on 27.08.2014.
 */
public class ADRPlugin implements Plugin<Project> {

    @Override
    public void apply(Project target) {
        target.getExtensions().create("adr", ADRExtension.class);
        target.afterEvaluate(project -> {
            target.getExtensions().findByType(ADRExtension.class).validate();
            if (target.getPlugins().hasPlugin(AppPlugin.class))
                target.getExtensions().findByType(AppExtension.class).getApplicationVariants().whenObjectAdded(appVariant -> setup(target, appVariant));
            else if (target.getPlugins().hasPlugin(AppPlugin.class))
                target.getExtensions().findByType(LibraryExtension.class).getLibraryVariants().whenObjectAdded(libraryVariant -> setup(target, libraryVariant));
            else
                throw new RuntimeException("No android plugin found");
        });
    }

    private void setup(Project target, BaseVariant variant) {
        MergeResources mergeResourcesTask = variant.getMergeResources();
        String resizeTaskName = generateTaskName("resize", variant, "Drawables");
        HashMap<String, Object> args = new HashMap<>();
        args.put("type", ResizeDrawablesTask.class);
        args.put("description", "This task resizes current build variant drawables and saves them in project build directory");
        ResizeDrawablesTask resizeTask = (ResizeDrawablesTask) target.task(args, resizeTaskName);
        resizeTask.setBuildVariant(variant);
        mergeResourcesTask.dependsOn(resizeTask);
    }

    public static String generateTaskName(String prefix, BaseVariant variant, String postfix) {
        return prefix + variant.getName().substring(0, 1).toUpperCase() + variant.getName().substring(1) + postfix;
    }

}
