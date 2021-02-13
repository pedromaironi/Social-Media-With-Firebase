package com.pedrodev.appchat.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.storage.UploadTask;
import com.pedrodev.appchat.R;
import com.pedrodev.appchat.models.User;
import com.pedrodev.appchat.providers.AuthProvider;
import com.pedrodev.appchat.providers.imageProvider;
import com.pedrodev.appchat.providers.UsersProvider;
import com.pedrodev.appchat.utils.FileUtil;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;

public class EditProfileActivity extends AppCompatActivity {

    CircleImageView mCircleImageViewBack;
    CircleImageView mCircleImageViewProfile;
    ImageView mImageViewCover;
    TextInputEditText mTextInputUsername;
    TextInputEditText mTextInputPhone;
    Button mButtonEditProfile;

    AlertDialog.Builder mBuilderSelector;
    CharSequence options[];
    private final int GALLERY_REQUEST_CODE_PROFILE = 1;
    private final int GALLERY_REQUEST_CODE_COVER = 2;
    private final int PHOTO_REQUEST_CODE_PROFILE = 3;
    private final int PHOTO_REQUEST_CODE_COVER = 4;

    // FOTO 1
    String mAbsolutePhotoPath;
    String mPhotoPath;
    File mPhotoProfileCamera;

    // FOTO 2
    String mAbsolutePhotoPath2;
    String mPhotoPath2;
    File mPhotoCoverCamera;

    File mImageProfileFile;
    File mImageCoverFile;

    String mUsername = "";
    String mPhone = "";

    AlertDialog mDialog;

    imageProvider mImageProvider;
    UsersProvider mUsersProvider;
    AuthProvider mAuthProvider;

    String mImageProfile = "";
    String mImageCover = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        mCircleImageViewBack = findViewById(R.id.CircleImageBackProfile);
        mCircleImageViewProfile = findViewById(R.id.CircleImageProfile);
        mImageViewCover = findViewById(R.id.ImageEditCover);
        mTextInputUsername = findViewById(R.id.textInputUserNameEdit_profile);
        mTextInputPhone = findViewById(R.id.textInputPhoneNumberEdit_profile);
        mButtonEditProfile = findViewById(R.id.btnUpdateProfile);

        mBuilderSelector = new AlertDialog.Builder(this);
        mBuilderSelector.setTitle("Selecciona una opcion");
        options = new CharSequence[] {"Imagen de galeria", "Tomar foto"};

        mImageProvider = new imageProvider();
        mUsersProvider = new UsersProvider();
        mAuthProvider = new AuthProvider();

        mDialog = new SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Espere un momento")
                .setCancelable(false).build();

        mButtonEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickEditProfile();
            }
        });

        mCircleImageViewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectOptionImage(1);
            }
        });

        mImageViewCover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectOptionImage(2);
            }
        });

        mCircleImageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        getUserDataProfile();
//        Snackbar.make( , "This is main activity", Snackbar.LENGTH_LONG)
//                .setAction("CLOSE", new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//
//                    }
//                })
//                .setActionTextColor(getResources().getColor(android.R.color.holo_red_light ))
//                .show();
    }

    private void getUserDataProfile(){
        mUsersProvider.getUser(mAuthProvider.getUid()).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    if (documentSnapshot.contains("username")) {
                        mUsername = documentSnapshot.getString("username");
                        mTextInputUsername.setText(mUsername);
                    }
                    if (documentSnapshot.contains("phoneNumber")) {
                        mPhone = documentSnapshot.getString("phoneNumber");
                        mTextInputPhone.setText(mPhone);
                    }
                    if (documentSnapshot.contains("image_profile")) {
                        mImageProfile = documentSnapshot.getString("image_profile");
                        if (mImageProfile != null) {
                            if (!mImageProfile.isEmpty()) {
                                Picasso.with(EditProfileActivity.this).load(mImageProfile).into(mCircleImageViewProfile);
                            }
                        }
                    }
                    if (documentSnapshot.contains("image_cover")) {
                        mImageCover = documentSnapshot.getString("image_cover");
                        if (mImageCover != null) {
                            if (!mImageCover.isEmpty()) {
                                Picasso.with(EditProfileActivity.this).load(mImageCover).into(mImageViewCover);
                            }
                        }
                    }
                }
            }

        });
    }

    public boolean isPhoneValid(String phoneNumber) {
        String expression = "/^[\\(]?[\\+]?(\\d{2}|\\d{3})[\\)]?[\\s]?((\\d{6}|\\d{8})|(\\d{3}[\\*\\.\\-\\s]){2}\\d{3}|(\\d{2}[\\*\\.\\-\\s]){3}\\d{2}|(\\d{4}[\\*\\.\\-\\s]){1}\\d{4})|\\d{8}|\\d{10}|\\d{12}$/";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(phoneNumber);
        return matcher.matches();
    }

    private void clickEditProfile() {
        mUsername = mTextInputUsername.getText().toString();
        mPhone = mTextInputPhone.getText().toString();
        if (!mUsername.isEmpty() && !mPhone.isEmpty()) {
            if(isPhoneValid(mPhone)) {
                if (mImageProfileFile != null && mImageCoverFile != null) {
                    saveImageCoverANDProfile(mImageProfileFile, mImageCoverFile);
                }
                else if (mPhotoProfileCamera != null && mPhotoCoverCamera != null) {
                    saveImageCoverANDProfile(mPhotoProfileCamera, mPhotoCoverCamera);
                } else if (mImageProfileFile != null && mPhotoProfileCamera != null) {
                    saveImageCoverANDProfile(mImageProfileFile, mPhotoProfileCamera);
                } else if (mPhotoProfileCamera != null && mImageCoverFile != null) {
                    saveImageCoverANDProfile(mPhotoProfileCamera, mImageCoverFile);
                }else if(mPhotoProfileCamera !=null){
                    saveImage(mPhotoProfileCamera, true);
                }else if(mPhotoCoverCamera !=null){
                    saveImage(mPhotoCoverCamera, false);
                }else if(mImageProfileFile !=null){
                    saveImage(mImageProfileFile, true);
                }else if(mImageCoverFile !=null){
                    saveImage(mImageCoverFile, false);
                }
                else {
                    User user = new User();
                    user.setUsername(mUsername);
                    user.setPhoneNumber(mPhone);
                    user.setImageCover(mImageCover);
                    user.setImageProfile(mImageProfile);
                    user.setId(mAuthProvider.getUid());
                    updateInfo(user);
                }
            }else{
                Toast.makeText(this, "Escribe un numero de telefono válido", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Toast.makeText(this, "Ingrese el nombre de usuario y el telefono", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveImageCoverANDProfile(File imageFile1, final File imageFile2) {
        mDialog.show();
        mImageProvider.save(EditProfileActivity.this, imageFile1).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {
                    mImageProvider.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            final String urlProfile = uri.toString();
                        }
                    });
                }
                else {
                    mDialog.dismiss();
                    Toast.makeText(EditProfileActivity.this, "Hubo error al almacenar la imagen", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void saveImage(File img, final boolean isProfileImage){
        mDialog.show();
        mImageProvider.save(EditProfileActivity.this, img).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {
                    mImageProvider.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            final String url = uri.toString();
                            User user = new User();
                            user.setUsername(mUsername);
                            user.setPhoneNumber(mPhone);
                            if (isProfileImage) {
                                user.setImageProfile(url);
                                user.setImageCover(mImageCover);
                            }
                            else {
                                user.setImageCover(url);
                                user.setImageProfile(mImageProfile);
                            }
                            user.setId(mAuthProvider.getUid());
                            updateInfo(user);
                        }
                    });
                }
                else {
                    mDialog.dismiss();
                    Toast.makeText(EditProfileActivity.this, "Hubo error al almacenar la imagen", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private void updateInfo(User user){
        if(mDialog.isShowing()){
            mDialog.show();
        }
        mUsersProvider.update(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                mDialog.dismiss();
                if (task.isSuccessful()) {
                    Toast.makeText(EditProfileActivity.this, "La informacion se actualizó correctamente", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else {
                    Toast.makeText(EditProfileActivity.this, "La informacion no se pudo actualizar", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void selectOptionImage(final int numberImage) {

        mBuilderSelector.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i == 0) {
                    if (numberImage == 1) {
                        openGallery(GALLERY_REQUEST_CODE_PROFILE);
                    }
                    else if (numberImage == 2) {
                        openGallery(GALLERY_REQUEST_CODE_COVER);
                    }
                }
                else if (i == 1){
                    if (numberImage == 1) {
                        takePhoto(PHOTO_REQUEST_CODE_PROFILE);
                    }
                    else if (numberImage == 2) {
                        takePhoto(PHOTO_REQUEST_CODE_COVER);
                    }
                }
            }
        });

        mBuilderSelector.show();

    }

    private void takePhoto(int requestCode) {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createPhotoFile(requestCode);
            } catch(Exception e) {
                Toast.makeText(this, "Hubo un error con el archivo " + e.getMessage(), Toast.LENGTH_LONG).show();
            }

            if (photoFile != null) {
                Uri photoUri = FileProvider.getUriForFile(EditProfileActivity.this, "com.pedrodev.appchat", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(takePictureIntent, requestCode);
            }
        }
    }

    private File createPhotoFile(int requestCode) throws IOException {
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File photoFile = File.createTempFile(
                new Date() + "_photo",
                ".jpg",
                storageDir
        );
        if (requestCode == PHOTO_REQUEST_CODE_PROFILE) {
            mPhotoPath = "file:" + photoFile.getAbsolutePath();
            mAbsolutePhotoPath = photoFile.getAbsolutePath();
        }
        else if (requestCode == PHOTO_REQUEST_CODE_COVER) {
            mPhotoPath2 = "file:" + photoFile.getAbsolutePath();
            mAbsolutePhotoPath2 = photoFile.getAbsolutePath();
        }
        return photoFile;
    }

    private void openGallery(int requestCode) {
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /**
         * SELECCION DE IMAGEN DESDE LA GALERIA
         */
        if (requestCode == GALLERY_REQUEST_CODE_PROFILE && resultCode == RESULT_OK) {
            try {
                mPhotoProfileCamera = null;
                mImageProfileFile = FileUtil.from(this, data.getData());
                mCircleImageViewProfile.setImageBitmap(BitmapFactory.decodeFile(mImageProfileFile.getAbsolutePath()));
            } catch(Exception e) {
                Log.d("ERROR", "Se produjo un error " + e.getMessage());
                Toast.makeText(this, "Se produjo un error " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }

        if (requestCode == GALLERY_REQUEST_CODE_COVER && resultCode == RESULT_OK) {
            try {
                mPhotoCoverCamera = null;
                mImageCoverFile = FileUtil.from(this, data.getData());
                mImageViewCover.setImageBitmap(BitmapFactory.decodeFile(mImageCoverFile.getAbsolutePath()));
            } catch(Exception e) {
                Log.d("ERROR", "Se produjo un error " + e.getMessage());
                Toast.makeText(this, "Se produjo un error " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }

        /**
         * SELECCION DE FOTOGRAFIA
         */
        if (requestCode == PHOTO_REQUEST_CODE_PROFILE && resultCode == RESULT_OK) {
            mImageProfileFile = null;
            mPhotoProfileCamera = new File(mAbsolutePhotoPath);
            Picasso.with(EditProfileActivity.this).load(mPhotoPath).into(mCircleImageViewProfile);
        }

        /**
         * SELECCION DE FOTOGRAFIA
         */
        if (requestCode == PHOTO_REQUEST_CODE_COVER && resultCode == RESULT_OK) {
            mImageCoverFile = null;
            mPhotoCoverCamera = new File(mAbsolutePhotoPath2);
            Picasso.with(EditProfileActivity.this).load(mPhotoPath2).into(mImageViewCover);
        }
    }

}
