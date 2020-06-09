package com.example.bloodbanker.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.widget.TextView;

import com.example.bloodbanker.Adapters.RequestAdapter;
import com.example.bloodbanker.Adapters.SearchAdapter;
import com.example.bloodbanker.DataModels.Donor;
import com.example.bloodbanker.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SearchResults extends AppCompatActivity {

    List<Donor> donorList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        donorList= new ArrayList<>();
        String json;
        String county,exprience;
        Intent intent = getIntent();
        json = intent.getStringExtra("json");
        county = intent.getStringExtra("county");
        exprience = intent.getStringExtra("exprience");
        TextView heading = findViewById(R.id.heading);
        String str= "Attendants in" + county + "With" + exprience+ "Years of experience";
        heading.setText(str);
        Gson gson = new Gson();
        Type type = new TypeToken<List<Donor>>(){

        }.getType();
        List<Donor> dataModels = gson.fromJson(json,type);
        if (dataModels !=null && dataModels.isEmpty()){
            heading.setText("No Donors Yet");
        }else if(dataModels !=null) {
            donorList.addAll(dataModels);
        }
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);

        SearchAdapter searchAdapter= new SearchAdapter(donorList, getApplicationContext());
        recyclerView.setAdapter(searchAdapter);



    }
}
