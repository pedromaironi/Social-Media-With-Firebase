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
- Firebase
  - Firebase is a mobile platform created by Google, whose main function is to develop and facilitate the creation of high-quality apps quickly, in order to increase the user base and earn more money.
  
### Create Authentication with Firebase
:fire: Firebase Authentication provides backend services, easy-to-use SDKs, and ready made UI libraries to authenticate users to your app. Supports authentication using passwords, phone numbers, popular federated identity providers like google, facebook and twitter, and much more.

### Implementation

:fire: In the file called (build.graddle) we will have the code block called dependecies. Thus, we will have to instantiate the following implementations.
```
dependencies {
	implementation platform('com.google.firebase:firebase-bom:26.4.0')
	implementation 'com.google.firebase:firebase-auth'
}
```
### Add Google play services

:fire: In the build.gradletop level file of your project, make sure the Google Maven repository is included:
```
allprojects {
    repositories {
        google()

        // If you're using a version of Gradle lower than 4.1, you must instead use:
        // maven {
        //     url 'https://maven.google.com'
        // }
    }
}
```
Then at your file build.gradlelevel build.gradle, declare Google Play services as a dependency:

```
apply plugin: 'com.android.application'
    ...

    dependencies {
        implementation 'com.google.android.gms:play-services-auth:19.0.0'
    }
```

Set up a Google API console project
To set up a Google API Console project, click the button below and specify the package name of your application when prompted. You will also need to provide the SHA-1 hash of your signing certificate. See Authentication of your client for more information.

### Get the OAuth 2.0 client ID from your backend server
If your application authenticates with a backend server or accesses Google APIs from your backend server , you must obtain the OAuth 2.0 client ID that was created for your server. To find the OAuth 2.0 client ID: UID

1. Open the [Credentials page](https://console.developers.google.com/apis/credentials)

    in the API Console.

2. The client ID of the **web application**

type is the OAuth 2.0 client ID of your backend server.

If you are using Google Login with an app or site that communicates with a backend server, you may need to identify the user who is currently logged into the server. To do this securely, after a user successfully logs in, send the user identification token to your server using HTTPS. Then, on the server, verify the integrity of the ID token and use the user information contained in the token to establish a session or create a new account.

Pass this client ID to the method requestIdTokenor requestServerAuthCodewhen you create the object GoogleSignInOptions.

## Next steps

Now that you've set up a Google API Console project and set up your Android Studio project, you can [integrate Google Login](https://developers.google.com/identity/sign-in/android/sign-in) into your app.

# Integration of Google login in your Android application

To integrate Google Sign-In into your Android app, set up Google Sign-In and add a button to your app layout that starts the sign-in flow.

## Before you start

[Set up a Google API Console project and set up your Android Studio project](https://developers.google.com/identity/sign-in/android/start-integrating) .

## Configure Google Login and GoogleSignInClient Object

1. In the method **`onCreate`**of their activity **`onCreate`**to **`onCreate`**, configure the **`onCreate`**session Google to request user data required by your application. For example, to configure Google login to request user IDs and basic profile information, create an object **`[GoogleSignInOptions](https://developers.google.com/android/reference/com/google/android/gms/auth/api/signin/GoogleSignInOptions.Builder#GoogleSignInOptions.Builder())`**with the parameter **`DEFAULT_SIGN_IN`**. To also request the email addresses of the users, create the object **`GoogleSignInOptions`**with the option **`requestEmail`**.


```java
    // Configure sign-in to request the user's ID, email address, and basic
    // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
    GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
    .requestEmail()
    .build();
```

2. Then, also in the method onCreateof its activity onCreateto onCreatecreate an object GoogleSignInClient with the options you specified.

```java
// Build a GoogleSignInClient with the options specified by gso.
mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
```

## Check if there is a user who is logged in

In the method **`onStart`**your activity, check if a user has already **`onStart`**in your application with Google.

`// Check for existing Google Sign In account, if the user is already signed in// the GoogleSignInAccount will be non-null.GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);updateUI(account);`

If you **`GoogleSignIn.getLastSignedInAccount`**return an object **`GoogleSignInAccount`**(instead of **`null`**), the user has already **`GoogleSignIn.getLastSignedInAccount`** **`GoogleSignInAccount`**in your application with Google. Update your UI accordingly, that is, hide the login button, start your main activity, or whatever is appropriate for your application.

If it **`GoogleSignIn.getLastSignedInAccount`**returns **`null`**, the user has not yet **`GoogleSignIn.getLastSignedInAccount`**in their application with Google. Update your UI to show the Google login button.

## Add the Google login button to your app

1. **`[SignInButton](https://developers.google.com/android/reference/com/google/android/gms/common/SignInButton)`**

    ![https://developers.google.com/identity/images/btn_google_signin_light_normal_web.png](https://developers.google.com/identity/images/btn_google_signin_light_normal_web.png)

    Add

    in your application layout:

    `<com.google.android.gms.common.SignInButton android:id="@+id/sign_in_button" android:layout_width="wrap_content" android:layout_height="wrap_content" />`

2. **Optional** : If you are using the default login button graphic instead of providing your own login button assets, you can customize the size of the button with the method **`[setSize](https://developers.google.com/android/reference/com/google/android/gms/common/SignInButton#setSize(int))`**.

    `// Set the dimensions of the sign-in button.SignInButton signInButton = findViewById(R.id.sign_in_button);signInButton.setSize(SignInButton.SIZE_STANDARD);`

3. In Android activity (for example, in method **`onCreate`**), register **`OnClickListener`**your button for **`OnClickListener`**session as user when you **`OnClickListener`**click on it:

    `findViewById(R.id.sign_in_button).setOnClickListener(this);`

## Start the login flow

1. **`onClick[getSignInIntentgetSignInIntent](https://developers.google.com/android/reference/com/google/android/gms/auth/api/signin/GoogleSignInClient#getSignInIntent())startActivityForResult`**

    ![https://developers.google.com/identity/sign-in/android/images/choose-account.png](https://developers.google.com/identity/sign-in/android/images/choose-account.png)

    In

    the activity method , handle the

    login buttons by creating a login intent with the method

    and starting the intent with

    ```
    @Override
    public void onClick(View v) {    
      switch (v.getId()) 
      {        
      case R.id.sign_in_button:            
        signIn();            
      break;        
      // ...    
      }}
    ```

    ```
    private void signIn() {    
    Intent signInIntent = mGoogleSignInClient.getSignInIntent();    
    startActivityForResult(signInIntent, RC_SIGN_IN);}
    ```

    ### Instanciate the listener for signInWithGoogle() method

    ```java
    mButtonLoginGoogle.setOnClickListener(new View.OnClickListener(){
        @Override
        public void onClick(View view){
            signInWithGoogle();
        }
    });

    private void signInWithGoogle() {
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, REQUEST_CODE_GOOGLE);
        }
    ```

    **REMEMBER create the varible for the Code Request. In this case REQUEST_CODE_GOOGLE = 1012;** This code will be used in the onStartActivityResult to confirm that the requestCode is the same.

    Once the user GoogleSignInAccount, you can get a GoogleSignInAccount object for the user in the activity's onActivityResult method.

    ```java
    @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
            if (requestCode == REQUEST_CODE_GOOGLE) {
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                try {
                    // Google Sign In was successful, authenticate with Firebase
                    //Log.d("ERROR", "firebaseAuthWithGoogle:" + account.getId());
                    GoogleSignInAccount account = task.getResult(ApiException.class);
                    firebaseAuthWithGoogle(account);
                } catch (ApiException e) {
                    // Google Sign In failed, update UI appropriately
                    Log.w("ERROR", "Google sign in failed", e);
                    // ...
                }
            }
        }
    ```

    The GoogleSignInAccount object contains information about the user that GoogleSignInAccount, such as the user's name.

    ```
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            updateUI(null);
        }
    }
    ```
    
## Adding Firebase Authentication to the project

## Basic Usage

## License

## Creadits

## Thanks
