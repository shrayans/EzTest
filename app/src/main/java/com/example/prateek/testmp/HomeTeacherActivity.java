package com.example.prateek.testmp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class HomeTeacherActivity extends AppCompatActivity {

    String userType="Student";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_teacher);
        this.setTitle("Teacher Home");
    }

    public void createTestMethod(View view ){

           Intent intent = new Intent(HomeTeacherActivity.this,TestEntryActivity.class);
           startActivity(intent);

    }

    public void getUserList(View view){

            Intent intent = new Intent(HomeTeacherActivity.this, UserListActivity.class);
            intent.putExtra("userType", userType);
            startActivity(intent);

    }
}
