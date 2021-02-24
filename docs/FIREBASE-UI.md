## Firebase UI

- FirebaseUI is an open-source library for Android that allows you to quickly connect common UI elements to Firebase APIs.

## Usage
FirebaseUI has separate modules for using Firebase Realtime Database, Cloud Firestore, Firebase Auth, and Cloud Storage. To get started, see the individual instructions for each module:

FirebaseUI Auth
FirebaseUI Firestore
FirebaseUI Database
FirebaseUI Storage

Installation
FirebaseUI is published as a collection of libraries separated by the Firebase API they target. Each FirebaseUI library has a transitive dependency on the appropriate Firebase SDK so there is no need to include those separately in your app.

In your app/build.gradle file add a dependency on one of the FirebaseUI libraries.

```
dependencies {
    // FirebaseUI for Firebase Realtime Database
    implementation 'com.firebaseui:firebase-ui-database:7.1.1'

    // FirebaseUI for Cloud Firestore
    implementation 'com.firebaseui:firebase-ui-firestore:7.1.1'

    // FirebaseUI for Firebase Auth
    implementation 'com.firebaseui:firebase-ui-auth:7.1.1'

    // FirebaseUI for Cloud Storage
    implementation 'com.firebaseui:firebase-ui-storage:7.1.1'
}

```
If you're including the firebase-ui-auth dependency, there's a little more setup required.

After the project is synchronized, we're ready to start using Firebase functionality in our app.

## Sample app
There is a sample app in the app/ directory that demonstrates most of the features of FirebaseUI. 
Load the project in Android Studio and run it on your Android device to see a demonstration.
Before you can run the sample app, you must create a project in the Firebase console. 
Add an Android app to the project, and copy the generated google-services.json file into the app/ directory. 
Also enable anonymous authentication for the Firebase project, since some components of the sample app requires it.
If you encounter a version incompatibility error between Android Studio and Gradle while trying to run the sample app, try disabling the Instant Run feature of Android Studio.
Alternatively, update Android Studio and Gradle to their latest versions.
A note on importing the project using Android Studio: Using 'Project from Version Control' will not automatically link the project with Gradle (issue #1349).
When doing so and opening any build.gradle.kts file, an error shows up: Project 'FirebaseUI-Android' isn't linked with Gradle. To resolve this issue, please git checkout the project manually and import with Import from external model.
