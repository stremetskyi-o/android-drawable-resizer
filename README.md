Android Drawable Resizer Gradle Plugin
======================================

This Gradle plugin will automatically resize Android drawables for lower screen densities, handling 
build types, flavors, source sets mapping and all your qualifiers. No more headache from sizing tons 
of images and storing them in your repository. Drawables are generated only when source drawable has 
changed and stored under the project 'build' folder.

Usage
-----

* From local Maven repository
    1. Import this project to your Android Studio and run 'install' gradle task. After this project 
        will be available from your local Maven repository.
    2. Add this to your Android project 'build.gradle' file
    ```groovy
    buildscript {
        repositories {
            mavenLocal()
        }
        dependencies {
            classpath 'com.github.forsety:adr:0.1'
        }
    }
    
    apply plugin: 'adr'
    ```

_Note: Download and install [JDK8](http://www.oracle.com/technetwork/java/javase/downloads/index.html) if you haven't already.
Also set it as project default JDK and don't worry, your Android projects will build just fine._ 

Configuration
-------------

In your 'build.gradle' file add:
    ```groovy
    adr {
        minDensity "mdpi"
        baseDensity "xxhdpi"
        generateTvDpi false
    }
    ```
Example shows default configuration, so if it suits your needs - you don't need configuration at all.