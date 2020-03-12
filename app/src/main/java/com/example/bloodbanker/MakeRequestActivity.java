package com.example.bloodbanker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.PermissionChecker;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.bumptech.glide.Glide;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

public class MakeRequestActivity extends AppCompatActivity {
    EditText messageText;
    TextView ImageText;
    ImageView imageView;
    Button postBtn;
    Uri imageUri;

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
          startActivityForResult(intent,101);

    }
    private void permissions(){
        if ( PermissionChecker.checkCallingOrSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE)
            != PermissionChecker.PERMISSION_GRANTED){
            requestPermissions(new String[]{READ_EXTERNAL_STORAGE},401);
        }else{
            //permission is already granted
            pickImage();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==401){
            if(grantResults[0]==PermissionChecker.PERMISSION_GRANTED){
                //permission was granted
                pickImage();
            }else {
                //No permission yet
                showmessage("Permission Declined");
            }
        }
    }

    private void  uploadRequest(String message){
        //code to upload message

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==101 && requestCode==RESULT_OK){
            if(data!=null){
               imageUri= data.getData();
               Glide.with(getApplicationContext()).load(imageUri).into(imageView);
            }else{
                showmessage("Something went wrong");
            }

        }
    }

    private boolean isValid(){
        if(messageText.getText().toString().isEmpty()){
            showmessage("Message shouldnt be empty");
            return false;
        }

        return true;
    }
    private void showmessage(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
