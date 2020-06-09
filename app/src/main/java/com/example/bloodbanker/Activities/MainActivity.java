            package com.example.bloodbanker.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.bloodbanker.Adapters.RequestAdapter;
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
import static com.example.bloodbanker.Utils.Endpoints.login_url;

            public class MainActivity extends AppCompatActivity {
                private RecyclerView recyclerView;
                private List<RequestDataModel> requestDataModelList;
                private RequestAdapter requestAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView make_request=findViewById(R.id.make_req);
        make_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MakeRequestActivity.class));
            }
        });
        requestDataModelList = new ArrayList<>();
        Toolbar toolbar= findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                if(item.getItemId() == R.id.search_btn){

                    //open search Activity
                    startActivity(new Intent(MainActivity.this,SearchActivity.class));
                }

                return false;
            }
        });
        recyclerView = findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        requestAdapter = new RequestAdapter(requestDataModelList,this);
        recyclerView.setAdapter(requestAdapter);
        populateHomePage();
        TextView pick_location = findViewById(R.id.choose_loc);
        String location =  PreferenceManager.getDefaultSharedPreferences(this).getString("city","no_city_found");
        if(!location.equals("no_city_found")){
            pick_location.setText(location);
        }
    }
    private void populateHomePage(){
       final String county= PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("city","no_city");

        StringRequest stringRequest= new StringRequest(Request.Method.POST, get_request_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson= new Gson();
                Type type= new TypeToken<List<RequestDataModel>>(){}.getType();
                List<RequestDataModel> dataModels=gson.fromJson(response,type);
                requestDataModelList.addAll(dataModels);
                requestAdapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(MainActivity.this, "Something is Wrong :(", Toast.LENGTH_SHORT).show();

                Log.d("VOLLEY",error.getMessage());

            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("county",county);

                return params;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);


    }

                @Override
                public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search,menu);
                    return true;
                }
            }
