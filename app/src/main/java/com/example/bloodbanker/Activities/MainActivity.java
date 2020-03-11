            package com.example.bloodbanker.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.bloodbanker.Adapters.RequestAdapter;
import com.example.bloodbanker.DataModels.RequestDataModel;
import com.example.bloodbanker.R;

import java.util.ArrayList;
import java.util.List;

            public class MainActivity extends AppCompatActivity {
                private RecyclerView recyclerView;
                private List<RequestDataModel> requestDataModelList;
                private RequestAdapter requestAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
    }
    private void populateHomePage(){
        RequestDataModel requestDataModel= new RequestDataModel("I love you sweet Purity Gakii Gikunda","https://www.pexels.com/photo/affection-afterglow-backlit-blur-556667/");
        requestDataModelList.add(requestDataModel);
        requestDataModelList.add(requestDataModel);
        requestDataModelList.add(requestDataModel);
        requestDataModelList.add(requestDataModel);
        requestDataModelList.add(requestDataModel);
        requestAdapter.notifyDataSetChanged();

    }

                @Override
                public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search,menu);
                    return true;
                }
            }
