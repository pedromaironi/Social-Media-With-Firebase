package com.pedrodev.appchat.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.storage.UploadTask;
import com.pedrodev.appchat.R;
import com.pedrodev.appchat.models.Post;
import com.pedrodev.appchat.providers.AuthProvider;
import com.pedrodev.appchat.providers.imageProvider;
import com.pedrodev.appchat.providers.postProvider;
import com.pedrodev.appchat.utils.FileUtil;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;


public class PostActivity extends AppCompatActivity {

    private final int REQUEST_GALLERY_CODE = 104;
    private final int PHOTO_REQUEST_CODE = 100;
    // No private
    File mImageFile;

    private Button mButtonPost;

    private TextInputEditText mTextInputTitle;
    private TextInputEditText mTextInputDescription;
    private ImageView mImageViewPc;
    private ImageView mImageViewNotice;
    private ImageView mImageViewGames;
    private ImageView mImagePost;

    private postProvider mPostProvider;
    private imageProvider mImageProvider;
    private AuthProvider mAuthProvider;

    private String mTitle = "";
    private String mDescription = "";
    private String mCategory = "";
    private TextView mTextViewCategory;
    CircleImageView mcircleImageView;
    AlertDialog.Builder mBuilderSelector;
    CharSequence options[];
    private AlertDialog mDialog;

    // Foto 1
    String mAbsolutePhotoPath;
    String mPhotoPath;
    File mPhotoFile;

    // Foto 2;
    String mAbsolutePhotoPath2;
    String mPhotoPath2;
    File mPhotoFile2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        mBuilderSelector = new AlertDialog.Builder(this);
        mBuilderSelector.setTitle("Selecciona una opción");
        options = new CharSequence[] {"Imagen de galería", "Tomar foto"};
        // Class | Provider
        mImageProvider = new imageProvider();
        mPostProvider = new postProvider();
        mAuthProvider = new AuthProvider();

        // ImageViews
        mImageViewGames = findViewById(R.id.imageViewGames);
        mImageViewPc = findViewById(R.id.imageViewPc);
        mImageViewNotice = findViewById(R.id.imageViewNotice);
        mImagePost = findViewById(R.id.imageViewPost);

        // InputEditText
        mTextInputDescription = findViewById(R.id.textInputDescription);
        mTextInputTitle = findViewById(R.id.textInputTitle);
        mTextViewCategory = findViewById(R.id.TextViewCategory);
        mButtonPost = findViewById(R.id.btnPost);


        mcircleImageView = findViewById(R.id.CircleImageBackFragment);


        mcircleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mDialog = new SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Espere un momento")
                .setCancelable(false).build();

        mButtonPost.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                ClickPost();
            }

        });

        mImagePost.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                int numImage = 1;
                selectOptionImage(numImage);
            }
        });

        mImageViewPc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCategory = "PC";
                mTextViewCategory.setText(mCategory);
            }
        });

        mImageViewNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCategory = "NEWS";
                mTextViewCategory.setText(mCategory);
             }
        });

        mImageViewGames.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCategory = "GAMES";
                mTextViewCategory.setText(mCategory);

            }
        });
    }

    private void selectOptionImage(final int numImage) {
        mBuilderSelector.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i == 0) {
                    if(numImage == 1){
                        openGallery(PHOTO_REQUEST_CODE);
                    }/*else if(numImage == 2){
                        openGallery(PHOTO_REQUEST_CODE2);
                    }*/
                } else if (i == 1){
                    if(numImage == 1) {
                        takePhoto(PHOTO_REQUEST_CODE);
                    }/* else if(numImage == 2){
                        takePhoto(PHOTO_REQUEST_CODE2);
                    }
                    */
                }
            }
        });

        mBuilderSelector.show();
    }

    private void takePhoto(int requestCode) {
        Intent intentPhoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(intentPhoto.resolveActivity(getPackageManager())!=null){
            File photoFile = null;
            try {
                photoFile = createPhotoFile(requestCode);
            }catch (Exception e){
                Log.e("Error", " Hubo un error en el archivo");
            }
            if (photoFile != null){
               Uri photoUri = FileProvider.getUriForFile(PostActivity.this, "com.pedrodev.appchat", photoFile);
               intentPhoto.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
               startActivityForResult(intentPhoto, requestCode);
            }
        }
    }



    private File createPhotoFile( int requestCode) throws IOException {
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            File photoFile = File.createTempFile(
                    new Date() + "_photo",
                    ".jpg",
                    storageDir);
            if(requestCode == PHOTO_REQUEST_CODE){
                mPhotoPath = "file:" + photoFile.getAbsolutePath();
                mAbsolutePhotoPath = photoFile.getAbsolutePath();
            }/*else if(requestCode == PHOTO_REQUEST_CODE2){
            mPhotoPath2 = "file:" + photoFile.getAbsolutePath();
            mAbsolutePhotoPath2 = photoFile.getAbsolutePath();
            }*/

            return photoFile;
    }

    private void saveImage(File imageFile1) {
        mDialog.show();
        mImageProvider.save(PostActivity.this, imageFile1).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if(task.isSuccessful()){
                    mImageProvider.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String url = uri.toString();
                            Post post = new Post();
                            post.setImage(url);
                            post.setTitle(mTitle);
                            post.setDescription(mDescription);
                            post.setCategory(mCategory);
                            post.setIdUser(mAuthProvider.getUid());

                            mPostProvider.save(post).addOnCompleteListener(new OnCompleteListener<Void>(){
                                @Override
                                public void onComplete(@NonNull Task<Void> taskSave) {
                                    mDialog.dismiss();
                                    if (taskSave.isSuccessful()) {
                                        clearForm();
                                        Toast.makeText(PostActivity.this, "La informacion se almaceno correctamente!", Toast.LENGTH_SHORT).show();
                                    }else {
                                        Toast.makeText(PostActivity.this, "No se pudo almacenar la informaciön!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                        }
                    });
                }else{
                    mDialog.dismiss();
                    Toast.makeText(PostActivity.this, "La imagen no pudo guardarse correctamente!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    private void ClickPost() {
        mDialog.show();
        mTitle = mTextInputTitle.getText().toString();
        mDescription = mTextInputDescription.getText().toString();
        if (!mTitle.isEmpty() && !mDescription.isEmpty() && !mCategory.isEmpty()) {
            // Selected image from gallery
            if(mImageFile != null){
                 saveImage(mImageFile);
                 mDialog.dismiss();
                // Selected image from camera
             }else if(mPhotoFile != null){
                saveImage(mPhotoFile);
                mDialog.dismiss();
            }
             else{
                 Toast.makeText(PostActivity.this, "Debes seleccionar una imagen", Toast.LENGTH_SHORT).show();
                mDialog.dismiss();
            }
        }else{
            Toast.makeText(PostActivity.this, "Completa los campos correctamente", Toast.LENGTH_SHORT).show();
            mDialog.dismiss();
        }
    }


    private void clearForm() {
        mTextInputTitle.setText("");
        mTextInputDescription.setText("");
        mTextViewCategory.setText("");
        mImagePost.setImageResource(R.drawable.ic_camera);
        mDescription = "";
        mTitle = "";
        mCategory= "";
        mImageFile= null;

    }

    private void openGallery( int requestCode) {
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/");
        startActivityForResult(galleryIntent,requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Action user equals Select image from gallery AND The image selected resul ok
        /*
         * Seleccion de imagen desde galeria
         */
        if (requestCode == REQUEST_GALLERY_CODE && resultCode == RESULT_OK){
            try {
                // Confirm photo from gallery
                mPhotoFile = null;
                mImageFile = FileUtil.from(this, data.getData());
                mImagePost.setImageBitmap(BitmapFactory.decodeFile(mImageFile.getAbsolutePath()));
            }catch(Exception e){
                Log.d("Error","Se produjo un error " + e.getMessage());
                Toast.makeText(this, "Se produjo un error", Toast.LENGTH_SHORT).show();
            }
        }

        /*
            * SELECCION DE FOTOGRAFIA
         */
        if (requestCode == PHOTO_REQUEST_CODE && resultCode == RESULT_OK){
            // Lib import on gradle
            // Confirm Photo from camera
            mImageFile = null;
            mPhotoFile = new File(mAbsolutePhotoPath);
           Picasso.with(PostActivity.this).load(mPhotoPath).into(mImagePost);
        }

        /*
        if (requestCode == PHOTO_REQUEST_CODE2 && resultCode == RESULT_OK){
            // Lib import on gradle
            mImageFile = null;
            mPhotoFile = new File(mAbsolutePhotoPath);
            Picasso.with(PostActivity.this).load(mPhotoPath2).into(mImagePost2);
        }
        */
    }
}