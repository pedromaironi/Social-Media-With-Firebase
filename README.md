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
Social media consists of the publication of photos or articles to be presented in the applications feed in order that its users can interact with each other with likes, comments or chats in real time, this application implements several plugins. However, everything is related to Firebase.

## Table of Contents

- [Getting Started](#getting-started)
  - [Create Firebase Authentication to the project](#create-firebase-authentication-to-the-project)
  	- [Create Provider of authentication](#create-provider-of-authentication)
  	- [Create Provider of users](#create-provider-of-authentication)
  	- [Create Model of user](#create-model-of-user)
  - [Create a login with GoogleSignIn into the project](#create-a-login-with-googlesignIn-into-the-project)
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
### Dependecies in build.gradle(App)
For this project, We are going to use Firebase, then we must add the necessary dependencies in the build.gradle (App) then I will explain what each one is for.

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

## Create Firebase Authentication to the project
- Firebase
  - Firebase is a mobile platform created by Google, whose main function is to develop and facilitate the creation of high-quality apps quickly, in order to increase the user base and earn more money.
  - You can see the docs of Authentication from google HERE!
	- 🟠[Create Authentication with Firebase](https://github.com/pedromaironi/Social-Media-With-Firebase/blob/master/docs/FirebaseAuthentication.md)

  - So, we are going to go to our MainActivity.xml file to instantiate the Google Login button
  
## Create a login with GoogleSignIn into the project

  🟠 [Layout Main Activity](https://github.com/pedromaironi/Social-Media-With-Firebase/blob/a7a5ce81cde64f1cbe8facc5b38d360bc8c914bc/app/src/main/res/layout/activity_main.xml#L1)
  
<p align="left">
	<img src="https://i.imgur.com/upEduTL.png" alt="Login" width="250" height="500" />
</p>
	
```
	// This is a Button from Google Docs.
	<com.google.android.gms.common.SignInButton
        android:id="@+id/signInButtonGoogle"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginTop="10dp"
        android:layout_marginRight="25dp"
        android:layout_marginLeft="25dp"
        android:paddingBottom="50dp"/>
	
```

### For use this button,We will have to go to the mainActivity.java to instantiate the button.


```
	// We would have to instantiate the button with its class out of the class
	SignInButton mButtonGoogle;
    	GoogleSignInClient mGoogleSignInClient;
    	
	GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
		
	mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
	
	mButtonGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signInGoogle();
            }
        });	
```

- RequestIdToken(R.string.defaul_web_client_id: Specifies that an ID token for authenticated users is requested. Requesting an ID token requires that the server client ID be specified.
- RequestEmail(): Specifies that email info is requested by your application. Note that we don't recommend keying user by email address since email address might change. Keying user by ID is the preferable approach.
- Build(): Default Builder for **`[GoogleSignInOptions](https://developers.google.com/android/reference/com/google/android/gms/auth/api/signin/GoogleSignInOptions)`** which starts with clean configuration.

If you are trying to implement Sign In, i.e. you want to get back user identity, you can start with **`new GoogleSignInOptions(GoogleSignInOptions.DEFAULT_SIGN_IN)`**

>	[To further expand the information about GoogleSignInOptions HERE](https://developers.google.com/android/reference/com/google/android/gms/auth/api/signin/GoogleSignInOptions.Builder)_

- Here we are instanciate our button from GoogleClient. For continous we are going to create a new function called signInGoogle on the OnClick for of button called mButtonGoogle

```
     private void signInGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, REQUEST_CODE_GOOGLE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == REQUEST_CODE_GOOGLE) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
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
- PARAMETERS

RequestCode: The integer request code originally provided to startActivityForResult (), allowing you to identify who this result is coming from.
ResultCode: the integer result code returned by the child activity through its setResult ().
Data: an Intent, which can return result data to the caller (various data can be attached to Intent 'extras').

Here we have a variable called REQUEST_CODE_GOOGLE, wich is the variable that will be returned to the onActivityForResult for confirm the requestCode from any startActivityForResult that is activated when the activity ends. So, when this task:


```
	Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
```

- This asynchronous task of type <GoogleSignInAccount> returns the information of the user who is logged in. f this task completes successfully then the function will be called

```
	firebaseAuthWithGoogle(account);
	
	private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        mDialog.show();
        mAuthProvider.googleLogin(acct).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    String id = mAuthProvider.getUid();
                    checkUserExist(id);
                }
                else {
                    mDialog.dismiss();
                    // If sign in fails, display a message to the user.
                    Log.w("ERROR", "signInWithCredential:failure", task.getException());
                    Toast.makeText(MainActivity.this, "No se pudo iniciar sesion con google", Toast.LENGTH_SHORT).show();
                }
                // ...
            }
        });
    }
    
```
## Create Provider of authentication
- As you can see in this method I am passing the GoogleSignInAccount object to start session with google.

- So here we see that I have an object called mAuthProvider. Therefore, we must create a folder called providers and create a class within this folder called AuthProvider
Inside this class we are going to instantiate Firebase in this way

```
    private FirebaseAuth mAuth;

    public AuthProvider() {
        mAuth = FirebaseAuth.getInstance();
    }
```
	
```
	public Task<AuthResult> googleLogin(GoogleSignInAccount googleSignInAccount) {
        AuthCredential credential = GoogleAuthProvider.getCredential(googleSignInAccount.getIdToken(), null);
        return mAuth.signInWithCredential(credential);
    }
```

- This function is of type <AuthResult> called googleLogin and we are going to pass the object GoogleSignInAccount. 
- Inside this method we will have to create a variable of type AuthCredential to obtain the user's credentials, and then return them to the Main Activity

```
	return mAuth.signInWithCredential(credential);	
```

## Create Provider of users
- We see that we have to instantiate this class as follows:


```
	//Outside of onCreate
	AuthProvider mAuthProvider;
	//Inside of onCreate
	mAuthProvider = new AuthProvider();
```

- Now we have the object for to use in the function _firebaseAuthWithGoogle_.

```
	mAuthProvider.googleLogin(acct).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {}
```

- The addOnCompleteListener is a listener called when a Task completes.
With the onComplete method we are going to confirm that the task finished correctly, then we are going to have to create another method to confirm if the user exists so as not to have to create it again.

```
	private void checkUserExist(final String id) {
        mUsersProvider.getUser(id).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    mDialog.dismiss();
                    Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                    startActivity(intent);
                }
                else {
                    String email = mAuthProvider.getEmail();
                    User user = new User();
                    user.setEmail(email);
                    user.setId(id);
                    mUsersProvider.create(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            mDialog.dismiss();
                            if (task.isSuccessful()) {
                                Intent intent = new Intent(MainActivity.this, CompleteProfileActivity.class);
                                startActivity(intent);
                            }
                            else {
                                Toast.makeText(MainActivity.this, "No se pudo almacenar la informacion del usuario", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
```

- First, we are going to create a folder called Models to create a class called User with see follows atributes:
	
```
	private String id;
    	private String email;
    	private String username;
    	private String phone;
    	private String imageProfile;
    	private String imageCover;
    	private long timestamp;
    	private long lastConnection;
    	private boolean online;

    public User() {

    }
```

- Then, Pressing alt + insert we can have a direct access to create a constructor and the getters and setters that we are going to need.
- Again we are going to create a class inside the folder called Providers with the name UsersProviders.
- The following is to create the method create and install the CollectionReference to be able to use FireStore and create a collection of data with that user.

```
	private CollectionReference mCollection;
	  public UsersProvider() {
        mCollection = FirebaseFirestore.getInstance().collection("Users");
    }

	public Task<Void> create(User user) {
        return mCollection.document(user.getId()).set(user);
    }
```

- Then we will go to the MainActivity class to instantiate this class.

```
	// This is the same process of the instantiation of the previous provider
	UsersProvider mUsersProvider;
	mUsersProvider = new UsersProvider();

```

- So after this we see that to confirm that the user exists or not, we have a listener that confirms if the document exists.
- If the document exists then it is sent to the HomeActivity
- If it does not exist, then it is created and sent to the CompleteProfileActivity to complete the record with all your data.

```
	public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    mDialog.dismiss();
                    Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                    startActivity(intent);
                }
                else {
                    String email = mAuthProvider.getEmail();
                    User user = new User();
                    user.setEmail(email);
                    user.setId(id);
                    mUsersProvider.create(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            mDialog.dismiss();
                            if (task.isSuccessful()) {
                                Intent intent = new Intent(MainActivity.this, CompleteProfileActivity.class);
                                startActivity(intent);
                            }
                            else {
                                Toast.makeText(MainActivity.this, "No se pudo almacenar la informacion del usuario", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
```
	
## Features at a Social Media

- Feed
- Posts
- Notifications
- Profile of user
- Comments
- Likes
- Messages
- Authentication with Firebase in Google Login
- Realtime Database
- Cloud Firestore
- Storage
