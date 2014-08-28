package com.github.forsety;

/**
 * Created by Forsety on 28.08.2014.
 */
public class ADRExtension {

    private String baseDensity = Density.XXHDPI.name();
    private String minDensity = Density.MDPI.name();
    private boolean generateTvDpi = false;

    public String getBaseDensity() {
        return baseDensity;
    }

    public Density getBaseDensityInfo() {
        return Density.valueOf(baseDensity.toUpperCase());
    }

    public void setBaseDensity(String baseDensity) {
        this.baseDensity = baseDensity;
    }

    public String getMinDensity() {
        return minDensity;
    }

    public Density getMinDensityInfo() {
        return Density.valueOf(minDensity.toUpperCase());
    }

    public void setMinDensity(String minDensity) {
        this.minDensity = minDensity;
    }

    public boolean isGenerateTvDpi() {
        return generateTvDpi;
    }

    public void setGenerateTvDpi(boolean generateTvDpi) {
        this.generateTvDpi = generateTvDpi;
    }
}
