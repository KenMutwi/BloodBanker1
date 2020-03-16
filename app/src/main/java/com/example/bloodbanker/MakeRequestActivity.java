package com.example.bloodbanker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.PermissionChecker;
import androidx.preference.PreferenceManager;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.StringRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;
import com.bumptech.glide.Glide;
import com.example.bloodbanker.Utils.Endpoints;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

public class MakeRequestActivity extends AppCompatActivity {
    EditText messageText;
    TextView ImageText;
    ImageView imageView;
    Button postBtn;
   Uri imageUri;


    private Bitmap bitmap;
   public static final int  PICK_IMAGE_REQUEST=22;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_request);
        AndroidNetworking.initialize(getApplicationContext());
        messageText = findViewById(R.id.heartfelt);
        ImageText=findViewById(R.id.choosetext);
        imageView=findViewById(R.id.myimage);
        postBtn=findViewById(R.id.post_btn);
        postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isValid()){
                    // code to upload the post

                    uploadRequest(messageText.getText().toString());
                }

            }
        });
        ImageText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //code to select an image
                permissions();

            }
        });

    }
    private void pickImage(){
        Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
         intent.setType("image/*");
          startActivityForResult(intent,401);

    }
    private void permissions(){
        if ( PermissionChecker.checkCallingOrSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE)
            != PermissionChecker.PERMISSION_GRANTED){
            requestPermissions(new String[]{READ_EXTERNAL_STORAGE},401);
        }else{
            //permission is already granted
            //pickImage();
            showFileChooser();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==401){
            if(grantResults[0]==PermissionChecker.PERMISSION_GRANTED){
                //permission was granted
                //pickImage();
                showFileChooser();
            }else {
                //No permission yet
                showmessage("Permission Declined");
            }
        }
    }

    private void  uploadRequest(String message){
        //code to upload message
        String path = "";
        try{
            path= getPath(imageUri);
        }catch (URISyntaxException e){
            showmessage("wrong uri");
        }
        String number= PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("number","12345");

        AndroidNetworking.upload(Endpoints.upload_url)
                .addMultipartFile("file",new File(path))
                .addQueryParameter("message",message)
                .addQueryParameter("number", number)
                .setPriority(Priority.HIGH)
                .build()
                .setUploadProgressListener(new UploadProgressListener() {
                    @Override
                    public void onProgress(long bytesUploaded, long totalBytes) {
                        // do anything with progress
                        long progress=(bytesUploaded/totalBytes)*100;
                        ImageText.setText(String.valueOf(progress));
                        ImageText.setOnClickListener(null);
                    }
                })
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        showmessage(response);
                        ImageText.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                permissions();
                            }
                        });

                    }

                    @Override
                    public void onError(ANError anError) {
                        showmessage(anError.getMessage());

                    }
                });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==101 && requestCode==RESULT_OK && data!=null && data.getData() !=null){
            if(data.getData()!=null){
             imageUri= data.getData();
             try{
                 bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                 imageView.setImageBitmap(bitmap);
             }catch (IOException e){

             }
           //  Glide.with(getApplicationContext()).load(imageUri).into(imageView);
           }else{
             showmessage("Something went wrong");
          }

        }
    }
    private void showFileChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"select picture"),PICK_IMAGE_REQUEST);


    }

    private boolean isValid(){
        if(messageText.getText().toString().isEmpty()){
            showmessage("Message shouldnt be empty");
            return false;
        }else if(imageUri==null){
            showmessage("Pick Photo");
            return false;
        }

        return true;
    }
    private void showmessage(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }


    @SuppressLint("NewApi")
    private String getPath(Uri uri) throws URISyntaxException {
        final boolean needToCheckUri = Build.VERSION.SDK_INT >= 19;
        String selection = null;
        String[] selectionArgs = null;
        // Uri is different in versions after KITKAT (Android 4.4), we need to
        // deal with different Uris.
        if (needToCheckUri && DocumentsContract.isDocumentUri(getApplicationContext(), uri)) {
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                return Environment.getExternalStorageDirectory() + "/" + split[1];
            } else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                uri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
            } else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("image".equals(type)) {
                    uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                selection = "_id=?";
                selectionArgs = new String[]{
                        split[1]
                };
            }
        }
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {
                    MediaStore.Images.Media.DATA
            };
            Cursor cursor = null;
            try {
                cursor = getContentResolver()
                        .query(uri, projection, selection, selectionArgs, null);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }


    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }


    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }


    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    }
