package com.example.prateek.testmp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class MainActivity extends AppCompatActivity {

    public FirebaseAuth firebaseAuth;
    private DatabaseReference mDatabase;
    EditText UserEmailEditText;
    EditText PasswordEditText;
    ProgressDialog progressDialog;


    public void logIn(View view ){

        firebaseAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        int tag=  Integer.valueOf((String) view.getTag());

        String userType=null;
        final Intent intent;


        if(tag==0){

                    userType="Teachers";
                    intent=new Intent(MainActivity.this,HomeTeacherActivity.class);

            }else{

                    userType="Student";
                    intent=new Intent(MainActivity.this,HomeStudentActivity.class);

                }

        final String x=userType;

        //getting parameters

        String email = UserEmailEditText.getText().toString();
        String password= PasswordEditText.getText().toString();

        //checking parameters
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"Please enter email",Toast.LENGTH_LONG).show();
            return;
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"Please enter password",Toast.LENGTH_LONG).show();
            return;
        }

        progressDialog.setMessage("Loggin you in...");
        progressDialog.show();

        //checking for user authantication
        firebaseAuth.signInWithEmailAndPassword(email,password) .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                //checking if success
                if (task.isSuccessful()) {

                    //Toast.makeText(MainActivity.this, "LogIn Successfull", Toast.LENGTH_LONG).show();

                    //checking the entry of UID in firebase db in "user" child
                    mDatabase.child("users").child(x).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            long currentChildren=0;
                            long totalChildrenCount=dataSnapshot.getChildrenCount();

                            for(DataSnapshot ds : dataSnapshot.getChildren()) {
                                String key = (String) ds.getKey();
                                FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
                                String UID=currentFirebaseUser.getUid();

                                if(key.equals(UID)){

                                    Toast.makeText(MainActivity.this, "Details Matched !", Toast.LENGTH_LONG).show();
                                    //Intent intent = new Intent(MainActivity.this, TestEntryActivity.class);
                                    startActivity(intent);
                                    break;
                                }

                                currentChildren++;

                                if(currentChildren>=totalChildrenCount){

                                    Toast.makeText(MainActivity.this, "Wrong Details", Toast.LENGTH_LONG).show();
                                    break;

                                     }

                                }

                            }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                } else {
                    //display some message here
                    Toast.makeText(MainActivity.this, "Did you enter correct details?", Toast.LENGTH_LONG).show();
                }
                progressDialog.dismiss();
            }
        });


    }


    public void goToSignUpActicity(View view ){

        Intent intent = new Intent(this, SignUp.class);
        startActivity(intent);

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //if user is already login
        if(FirebaseAuth.getInstance().getCurrentUser()!=null){
            Intent intent = new Intent(MainActivity.this, HomeTeacherActivity.class);
            startActivity(intent);
        }

        UserEmailEditText=(EditText) findViewById(R.id.UserEmailEditText);
        PasswordEditText=(EditText) findViewById(R.id.PasswordEditText);
        progressDialog = new ProgressDialog(this);

        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{android.Manifest.permission.CAMERA},
                1);




    }
}
//FirebaseAuth.getInstance().getCurrentUser()
