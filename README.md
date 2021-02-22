<p align="center">
  <img src="https://i.imgur.com/lbwNTNl.jpg" width="480" title="Hi I'm Quique">
</p>

<h1 align="center">
  <a href="https://github.com/pedromaironi/appChat-AndroidN">
     Social Media in Android
  </a>
</h1>

<p align="center">
  <strong>Coding to Learn, learn to coding</strong><br>
</p>

<p align="center">
  <a href="https://github.com/pedromaironi/appChat-AndroidN">
    <img src="https://img.shields.io/badge/license-MIT-blue.svg" alt="Social Media is released under the MIT license." />
  </a>
  <a href="https://twitter.com/intent/follow?screen_name=pedromaironi">
    <img src="https://img.shields.io/twitter/follow/pedromaironi.svg?label=Follow%20@pedromaironi" alt="Follow @pedromaironi" />
  </a>
</p>

## Overview
Social media consists of the publication of photos or articles to be presented in the application's feed in order that its users can interact with each other with likes, comments or chats in real time, this application implements several plugins. However, everything is related to Firebase.

## Table of Contents

- [Getting Started](#getting-started)
  - [Adding Firebase as a Dependency](#adding-firebase-as-a-dependency)
  - [Adding Firebase Authentication to the project](#adding-firebase-to-the-proyect)
  - [Basic Usage](#basic-usage)
- [Best Practices](#best-practices)
- [Features at a Social Media](#features-at-a-social-media)
  - [License](#license)
  - [Credits](#credits)
  - [Thanks](#thanks)

## Getting Started
If you want to start working to create a similar application or add features similar to that of this social network to your project, you should include the following dependencies in your project.These are all the dependencies used in this one, but if you only want to understand how a specific one works, check the index of the document. 

### Adding dependencies to your project in the build.gradle(Module App)
```
// This app have a minSdkVersion 21 and targetSdkVersion 30
dependencies {
implementation 'com.google.firebase:firebase-auth:19.1.0'
            implementation 'com.google.firebase:firebase-firestore:21.2.1'

            implementation 'com.google.android.gms:play-services-auth:17.0.0'
            implementation 'com.github.d-max:spots-dialog:1.1@aar'

            implementation 'com.google.firebase:firebase-storage:19.1.1'
            implementation 'id.zelory:compressor:2.1.0'
            implementation 'com.squareup.picasso:picasso:2.5.2'
            implementation 'com.firebaseui:firebase-ui-firestore:6.2.1'
            implementation 'com.github.smarteist:autoimageslider:1.3.7'
            implementation 'com.github.mancj:MaterialSearchBar:0.7.5'
            implementation 'com.google.firebase:firebase-messaging:20.0.0'

            implementation 'com.squareup.retrofit2:retrofit:2.4.0'
            implementation 'com.squareup.retrofit2:converter-gson:2.4.0'
            implementation 'com.squareup.retrofit2:converter-scalars:2.4.0'
}
apply plugin: 'com.google.gms.google-services'
```
### Dependecies in build.gradle(AppChat
```

buildscript {

    repositories {
        google()
        jcenter()

    }
    dependencies {
        classpath 'com.android.tools.build:gradle:4.0.2'
        classpath 'com.google.gms:google-services:4.3.5'

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

allprojects {
    repositories {
        google()
        jcenter()
        maven { url "https://jitpack.io" }
    }
}

task clean(type: Delete) {
    delete rootProject.buildDir
}

```
## Adding Firebase as a Dependency

## Adding Firebase Authentication to the project

## Basic Usage

## License

## Creadits

## Thanks
