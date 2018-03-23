package com.example.prateek.testmp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Timer;

public class UserListActivity extends AppCompatActivity {

    DBManager dbManager = new DBManager();

    ArrayList<User> userArrayList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
        try {
            methodF();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void methodF(){

        Intent intent = getIntent();
        final String userType = intent.getStringExtra("userType");
        //Log.i("UserType**********",userType);

        String[] items = {"Shree", "Shrayans", "abc", "kamlesh", "vaidik", "raju"};
        Log.i("UserTypeValueFrom****", userType);


        try {
            dbManager.getUserListFromDB(userType, new MyCallback() {
                @Override
                public void onCallback(ArrayList<User> value) {

                    userArrayList = value;

                    callMe();
                }

            });


           // Log.i("ArraySize", userArrayList.size() + "");

           // Log.i("UserTypeValue-----", userArrayList.get(1));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void callMe() {

        try {

            Log.i("Size Of Array",userArrayList.size()+"");
            final User user = userArrayList.get(0);

            Log.i("FullNAME", user.Full_Name);
            Log.i("ProfileImage",user.profileImage);


            ArrayAdapter arrayAdapter = new CustomAdapter(this, userArrayList);

            ListAdapter shreeAdapter = new CustomAdapter(this, userArrayList);
            ListView shreeListView = (ListView) findViewById(R.id.userListView);

            shreeListView.setAdapter(shreeAdapter);

            shreeListView.setOnItemClickListener(

                    new AdapterView.OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                            User user1=(User) adapterView.getItemAtPosition(i);
                            String item = user1.Full_Name;
                            Toast.makeText(UserListActivity.this, item, Toast.LENGTH_SHORT).show();
                        }
                    }

            );
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}