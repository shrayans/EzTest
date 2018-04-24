package com.example.prateek.testmp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeTeacherActivity extends AppCompatActivity {

    String userType="Student";
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_teacher);
        this.setTitle("Teacher Home");
        mDatabase = FirebaseDatabase.getInstance().getReference();
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
                mDatabase.child("users").child("Teachers").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User user=dataSnapshot.getValue(User.class);
                        Intent intent=new Intent(getApplicationContext(), ProfileActivity.class);
                        intent.putExtra("userObject",(User)user);
                        startActivity(intent);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                return true;
            case R.id.logout:

                FirebaseAuth.getInstance().signOut();
                Intent intent=new Intent(this,MainActivity.class);
                startActivity(intent);

                finishAffinity();


                return true;
            default:
                return false;
        }
    }
}
