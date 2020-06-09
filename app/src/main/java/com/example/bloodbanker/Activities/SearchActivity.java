package com.example.bloodbanker.Activities;

import androidx.appcompat.app.AppCompatActivity;

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
import com.example.bloodbanker.DataModels.RequestDataModel;
import com.example.bloodbanker.R;
import com.example.bloodbanker.Utils.VolleySingleton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.bloodbanker.Utils.Endpoints.get_request_url;
import static com.example.bloodbanker.Utils.Endpoints.search_results_url;

public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        final EditText et_blood_group, et_county;
        et_blood_group = findViewById(R.id.et_blood_group);
        et_county = findViewById(R.id.et_city);
        Button submit_button = findViewById(R.id.submit_button);
        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String blood_group = et_blood_group.getText().toString();
                String county = et_county.getText().toString();
                if (isValid(blood_group, county)) {
                    get_search_reasults(blood_group, county);
                }
            }
        });
    }

    private void get_search_reasults(final String blood_group, final String county) {

        StringRequest stringRequest= new StringRequest(Request.Method.POST, search_results_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //json code
                Intent intent = new Intent(SearchActivity.this,SearchResults.class);
                intent.putExtra("county",county);
                intent.putExtra("exprience",blood_group);
                intent.putExtra("json",response);
                startActivity(intent);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(SearchActivity.this, "Something is Wrong :(", Toast.LENGTH_SHORT).show();

                Log.d("VOLLEY",error.getMessage());

            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("county",county);
                params.put("exprience",blood_group);

                return params;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    private boolean isValid(String blood_group, String county) {
        List<String> valid_experience = new ArrayList<>();
        valid_experience.add("1");
        valid_experience.add("2");
        valid_experience.add("3");
        valid_experience.add("4");
        valid_experience.add("5");
        valid_experience.add("6");
        valid_experience.add("7");
        valid_experience.add("8");
        if (county.isEmpty()) {
            showmessage("Name is empty");
            return false;

        } else if (!valid_experience.contains(blood_group)) {
            showmessage("Not Within Experience range" + valid_experience);
            return false;
        }
        return true;

    }

    private void showmessage(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();

    }
}
