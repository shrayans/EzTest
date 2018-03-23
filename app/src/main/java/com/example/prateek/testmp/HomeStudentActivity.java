package com.example.prateek.testmp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_student);
        this.setTitle("Home Student");

        testListView=(ListView)findViewById(R.id.TestListView);
        mDatabase = FirebaseDatabase.getInstance().getReference();


        try{


            mDatabase.child("tests").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    for (DataSnapshot ds : dataSnapshot.getChildren()) {

                        String test_uid=ds.getKey().toString();
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



    }

}
