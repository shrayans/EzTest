package com.example.prateek.testmp;

import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

/**
 * Created by prateek on 3/11/18.
 */

public class DBManager {

    public FirebaseStorage firebaseStorage;
    private DatabaseReference mDatabase;

    ScoreBoard scoreBoardObject;

    public ArrayList<String> arrayList= new ArrayList<>();

    public ArrayList<Object> users = new ArrayList<>();

    public ArrayList<Object> studentScoresArrayList = new ArrayList<>();

    public void getUserListFromDB(String userType,final MyCallback myCallback  ) {

        try {
            mDatabase = FirebaseDatabase.getInstance().getReference();


            mDatabase.child("users").child(userType).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    long currentChildren = 0;
                    long totalChildrenCount = dataSnapshot.getChildrenCount();

                    for (DataSnapshot ds : dataSnapshot.getChildren()) {

                        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                        String UID = currentFirebaseUser.getUid();
                        String key = ds.getKey();


                        if (!key.equals(UID)) {

                            try {
                                User user = ds.getValue(User.class);
                                Log.i("Email", user.getEmail().toString());
                                users.add(user);
                            }catch(Exception e){
                                e.printStackTrace();
                            }

                            arrayList.add(ds.child("Full_Name").getValue().toString());

                        }

                        currentChildren++;
                    }

                    try {

                        myCallback.onCallback(users);
                        Log.i("UserTypeValue*++++**", arrayList.get(1));

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.i("Status", databaseError.getMessage());
                }
            });


            //return arrayList;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addScore(double totalMarks, String test_id, String Uid){
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.child("tests").child(test_id).child("Scores").child(Uid).setValue(totalMarks);
    }

    String name;
    String marks;

    public void getScoreList(String test_id,final MyCallback myCallback){

        try{

            mDatabase.child("tests").child(test_id).child("Scores").addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    for (final DataSnapshot ds : dataSnapshot.getChildren()){

                        //DatabaseReference xDatabase=FirebaseDatabase.getInstance().getReference();
                        //name=xDatabase.child("users").child("Student").child(ds.getKey()).child("Full_Name").;
                        Log.e("UID",ds.getKey());
                         getStudentNameFromUid(ds.getKey(), new MyCallback() {
                            @Override
                            public void onCallback(ArrayList<Object> value) {

                            }

                            @Override
                            public void onCallbackString(String string) {
                                name=string;

                                marks = ds.getValue().toString();
                                Log.i("valuesOfDS",ds.getKey());
                                Log.i("Name + Marks", name + marks);

                                scoreBoardObject = new ScoreBoard(name, marks);
                                studentScoresArrayList.add(scoreBoardObject);
                                myCallback.onCallback(studentScoresArrayList);

                            }


                        });

                    }

                    Log.i("StudentScoresListSize*", studentScoresArrayList.size() + "");


                }


                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }catch (Exception e) {e.printStackTrace();}
        //Log.i("studentScoreArrayList", studentScoresArrayList.get(0).name);
        //return studentScoresArrayList;
    }

    //called in getScoreList
    public void getStudentNameFromUid(String Uid,final MyCallback myCallback) {


        Log.i("Position", "Inside getNamefromID");

        try {

            //final String[] name = new String[1];

            mDatabase.child("users").child("Student").child(Uid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    Log.e("Getting something", dataSnapshot.getKey());

                    User user = dataSnapshot.getValue(User.class);
                    Log.i("User", user.Full_Name);
                    String name = user.Full_Name;
                    Log.i("name[0]", name);
                    myCallback.onCallbackString(name);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            //return name[0];
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
        //return "";

    public void getTeacherNameFromUid(String Uid,final MyCallback myCallback) {


        Log.i("Position","Inside getNamefromID");

        try{

            //final String[] name = new String[1];
            mDatabase=FirebaseDatabase.getInstance().getReference();

            mDatabase.child("users").child("Teachers").child(Uid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    Log.e("Getting something",dataSnapshot.getKey());

                    User user = dataSnapshot.getValue(User.class);
                    Log.i("User", user.Full_Name);
                    String name = user.Full_Name;
                    Log.i("name[0]",name);
                    myCallback.onCallbackString(name);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            //return name[0];
        }catch (Exception e){e.printStackTrace();}
    }
}