package com.github.forsety.task;

import com.android.build.gradle.api.BaseVariant;
import com.android.build.gradle.tasks.MergeResources;
import com.android.ide.common.res2.ResourceSet;
import com.github.forsety.ADRExtension;
import com.github.forsety.BatchDrawableResizer;
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
        List<BatchDrawableResizer> resizers = new LinkedList<>();
        Pattern drawableFolderPattern = Pattern.compile("^(drawable|mipmap)-.*" + baseDensity.getQualifierName() + ".*$");
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
                    BatchDrawableResizer resizer = new BatchDrawableResizer(intputDrawableDir, outputDrawableDir, ((double) currentDensity.getValue()) / baseDensity.getValue());
                    resizer.start();
                    resizers.add(resizer);
                });
            });
        }

        // Add output res dirs to resource merger
        MergeResources mergeTask = variant.getMergeResources();
        List<ResourceSet> mergeSets = mergeTask.getInputResourceSets();
        ResourceSet adrSet = new ResourceSet(variant.getName()+ "ADR");
        adrSet.addSources(outputResDirs);
        mergeSets.add(adrSet);
        mergeTask.setInputResourceSets(mergeSets);

        try {
            for (BatchDrawableResizer resizer : resizers)
                resizer.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        int totalDrawablesResized = 0;
        for (BatchDrawableResizer resizer : resizers)
            totalDrawablesResized += resizer.getResizedDrawablesCount();
        setDidWork(totalDrawablesResized > 0);
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
