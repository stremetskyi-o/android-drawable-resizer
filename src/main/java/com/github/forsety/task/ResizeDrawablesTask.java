package com.github.forsety.task;

import com.android.build.gradle.api.BaseVariant;
import com.github.forsety.ADRExtension;
import com.github.forsety.Density;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

/**
 * Created by Forsety on 27.08.2014.
 */
public class ResizeDrawablesTask extends DefaultTask {

    private BaseVariant variant;

    @TaskAction
    public void resize() {
        if (variant == null)
            throw new RuntimeException("No build variant was specified");
        ADRExtension adr = getProject().getExtensions().findByType(ADRExtension.class);
        Density baseDensity = adr.getBaseDensityInfo();
        List<File> inputResDirs = getInputDirs();
        List<File> outputResDirs = getOutputDirs(inputResDirs);
        Pattern drawableFolderPattern = Pattern.compile("^drawable-.*" + baseDensity.getQualifierName() + ".*$");
        for (int i = adr.getMinDensityInfo().ordinal(); i < adr.getBaseDensityInfo().ordinal(); i++) {
            Density currentDensity = Density.values()[i];
            if (currentDensity == Density.TVDPI && !adr.isGenerateTvDpi())
                continue;
            IntStream.range(0, inputResDirs.size()).forEach(j -> {
                File inputResDir = inputResDirs.get(j);
                File outputResDir = outputResDirs.get(j);
                Arrays.asList(inputResDir.listFiles((file, s) -> drawableFolderPattern.matcher(s).matches())).forEach(intputDrawableDir -> {
                    File outputDrawableDir = new File(outputResDir, intputDrawableDir.getName().replace(baseDensity.getQualifierName(), currentDensity.getQualifierName()));
                    if (!outputDrawableDir.exists())
                        outputDrawableDir.mkdirs();
                });
            });
        }
    }

    private List<File> getInputDirs() {
        List<File> dirs = new LinkedList<>();
        variant.getSourceSets().forEach(provider -> provider.getResDirectories().forEach(resources -> {
            if (resources.exists())
                dirs.add(resources);
        }));
        return dirs;
    }

    private List<File> getOutputDirs(List<File> inputDirs) {
        List<File> dirs = new ArrayList<>(inputDirs.size());
        File baseOutputDir = new File(getProject().getBuildDir(), "adr");
        File projectDir = getProject().getProjectDir();
        inputDirs.forEach(dir -> dirs.add(new File(baseOutputDir, dir.getAbsolutePath().replace(projectDir.getAbsolutePath(), ""))));
        return dirs;
    }

    public void setBuildVariant(BaseVariant variant) {
        this.variant = variant;
    }

}
