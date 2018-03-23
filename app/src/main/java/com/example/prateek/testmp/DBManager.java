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

    public ArrayList<String> arrayList= new ArrayList<>();

    public ArrayList<User> users = new ArrayList<>();


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
                        String key = (String) ds.getKey();


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
}