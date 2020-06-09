package com.example.bloodbanker.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.bloodbanker.R;
import com.example.bloodbanker.Utils.VolleySingleton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.android.volley.Request.*;
import static com.example.bloodbanker.Utils.Endpoints.login_url;
import static com.example.bloodbanker.Utils.Endpoints.register_url;

public class RegisterActivity extends AppCompatActivity {
        private EditText nameEt,countyEt,exprienceEt,passwordEt,mobileEt;
        private Button joinBtn;

        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
            nameEt= findViewById(R.id.name);
            countyEt= findViewById(R.id.city);
            exprienceEt=findViewById(R.id.barberLevel);
            passwordEt=findViewById(R.id.bankpwd);
            mobileEt=findViewById(R.id.mobile);
            joinBtn=findViewById(R.id.joinBtn);

            joinBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String name, county1, experience, password, mobile;

                    name= nameEt.getText().toString();
                    county1=countyEt.getText().toString();
                    experience=exprienceEt.getText().toString();
                    password=passwordEt.getText().toString();
                    mobile=mobileEt.getText().toString();

                    if(isValid(name, county1, experience, password, mobile)){

                        register(name, county1, experience, password, mobile);
                        showmessage(name+"\n"+county1+"\n"+experience+"\n"+password+"\n"+mobile);
                    }


                }
            });
        }
    private void register(final String name, final String county1, final String experience, final String password,
                          final String mobile){
        StringRequest stringRequest= new StringRequest(Method.POST, register_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.equals("Success")) {
                    PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("county",county1).apply();

                    Toast.makeText(RegisterActivity.this, response , Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegisterActivity.this,MainActivity.class));
                    RegisterActivity.this.finish();
                }else{
                    Toast.makeText(RegisterActivity.this, response, Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(RegisterActivity.this, "Something is Wrong :(", Toast.LENGTH_SHORT).show();

                Log.d("VOLLEY",error.getMessage());

            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("name", name);
                params.put("county", county1);
                params.put("exprience", experience);
                params.put("password", password);
                params.put("number", mobile);

                return params;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);

    }
    public boolean isValid(String name, String county, String experience, String password, String mobile){
        List<String> valid_experience = new ArrayList<>();
        valid_experience.add("1");
        valid_experience.add("2");
        valid_experience.add("3");
        valid_experience.add("4");
        valid_experience.add("5");
        valid_experience.add("6");
        valid_experience.add("7");
        valid_experience.add("8");
        if (name.isEmpty()){
            showmessage("Name is empty");
            return false;

        }else if(county.isEmpty()){
            showmessage("County HQ is required");
            return false;
        }else if(!valid_experience.contains(experience)){
            showmessage("Not Within Experience range"+valid_experience);
            return false;
        }else if(mobile.length() !=10){
            showmessage("Invalid mobile number");
            return false;
        }
        return true;
    }

    private void showmessage(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();

    }
}
