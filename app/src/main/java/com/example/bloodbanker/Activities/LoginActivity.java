package com.example.bloodbanker.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.bloodbanker.R;
import com.example.bloodbanker.Utils.VolleySingleton;

import java.util.HashMap;
import java.util.Map;

import static com.example.bloodbanker.Utils.Endpoints.login_url;
import static com.example.bloodbanker.Utils.Endpoints.register_url;

public class LoginActivity extends AppCompatActivity {
    EditText numberEt, passwordEt;
    Button submitBtn;
    TextView signuptxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        numberEt=findViewById(R.id.username);
        passwordEt=findViewById(R.id.password);
        submitBtn=findViewById(R.id.Login);
        signuptxt=findViewById(R.id.signup);
        signuptxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
            }
        });
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numberEt.setError(null);
                passwordEt.setError(null);
                String number = numberEt.getText().toString();
                String password = passwordEt.getText().toString();
                if(isValid(number, password)){
                    login(number, password);

                }

            }
        });

    }
    private void login(final String number, final String password){
        StringRequest stringRequest= new StringRequest(Request.Method.POST, login_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.equals("Success")) {

                    Toast.makeText(LoginActivity.this, response , Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this,MainActivity.class));
                    LoginActivity.this.finish();
                }else{
                    Toast.makeText(LoginActivity.this, response, Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(LoginActivity.this, "Something is Wrong :(", Toast.LENGTH_SHORT).show();

                Log.d("VOLLEY",error.getMessage());

            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("password", password);
                params.put("number", number);

                return params;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);


    }
    private boolean isValid(String number, String password){
        if (number.isEmpty()){
            showmessage("Empty Mobile number");
            numberEt.setError("Empty Mobile number");
            return false;
        }else if(password.isEmpty()){
            showmessage("Please enter your password");
            passwordEt.setError("Empty password");
            return false;
        }
        return true;
    }
    private void showmessage(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
