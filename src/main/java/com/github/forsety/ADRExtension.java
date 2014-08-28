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

    public void validate() {
        Density baseDensityInfo;
        Density minDensityInfo;
        try {
            baseDensityInfo = Density.valueOf(baseDensity.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidConfigurationException("\"" + baseDensity + "\" is not valid base density");
        }
        try {
            minDensityInfo = Density.valueOf(minDensity.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidConfigurationException("\"" + minDensity + "\" is not valid min density");
        }
        if (minDensityInfo.compareTo(baseDensityInfo) >= 0)
            throw new InvalidConfigurationException("Min density must be less than base density");
    }
}
