package com.example.prateek.testmp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeStudentActivity extends AppCompatActivity {

    ListView testListView;
    private DatabaseReference mDatabase;

    ArrayList<String> testName=new ArrayList<>();
    ArrayList<String> testUID=new ArrayList<>();
    ArrayList<String> fullTestUID = new ArrayList<>();

    String test_uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_student);
        this.setTitle("Home Student");

        testListView=findViewById(R.id.TestListView);
        mDatabase = FirebaseDatabase.getInstance().getReference();


        try{


            mDatabase.child("tests").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    for (DataSnapshot ds : dataSnapshot.getChildren()) {

                        test_uid=ds.getKey().toString();
                        fullTestUID.add(test_uid);
                        String[] strings=test_uid.split("\\*");

                        Log.i("TestName===",strings[0]);
                        Log.i("TestName===",strings[1]);

                        testName.add(strings[0]);
                        testUID.add(strings[1]);

                    }

                    ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1,testName);

                    testListView.setAdapter(listAdapter);


                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


        }catch(Exception e){
            e.printStackTrace();
        }

        onTestClick();

    }

    private void onTestClick() {

        testListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                try {
                    Intent intent = new Intent(HomeStudentActivity.this, OngoingTestActivity.class);

                    intent.putExtra("test_uid", fullTestUID.get(position));

                    Log.e("msg",fullTestUID.get(position));

                    startActivity(intent);

                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });
    }


    //menu code
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_testing, menu);

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {

            case R.id.profile:
                //setLanguage("English");
                //Log.i("Selected","English");

                return true;
            case R.id.logout:
                //setLanguage("Hindi");
                FirebaseAuth.getInstance().signOut();
                Intent intent=new Intent(this,MainActivity.class);
                startActivity(intent);
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
    }
}
