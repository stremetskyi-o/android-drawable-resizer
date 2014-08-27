package com.github.forsety.task;

import com.android.build.gradle.api.BaseVariant;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;

import java.io.File;

/**
 * Created by Forsety on 27.08.2014.
 */
public class ResizeDrawablesTask extends DefaultTask {

    private BaseVariant variant;

    @TaskAction
    public void resize() {
        if (variant == null)
            throw new RuntimeException("No build variant was specified");
        File outputDir = new File(getProject().getBuildDir(), "adr" + File.separator + variant.getName());
        if (!outputDir.exists())
            outputDir.mkdirs();
    }

    public void setBuildVariant(BaseVariant variant) {
        this.variant = variant;
    }

}
