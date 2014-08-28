package com.github.forsety;

/**
 * Created by Forsety on 27.08.2014.
 */
public enum Density {

    LDPI(120),
    MDPI(160),
    TVDPI(213),
    HDPI(240),
    XHDPI(320),
    XXHDPI(480),
    XXXHDPI(640);

    private String qualifierName;
    private int density;

    Density(int density) {
        this.density = density;
        qualifierName = name().toLowerCase();
    }

    public int getValue() {
        return density;
    }

    public String getQualifierName() {
        return qualifierName;
    }

}
