package com.example.prateek.testmp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ProfileActivity extends AppCompatActivity {

    ImageView user_profile_photo;
    TextView user_profile_name;
    TextView user_profile_email,user_profile_short_bio;


    FirebaseStorage storage;
    User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        storage=FirebaseStorage.getInstance();

        getRefrence();

        Intent intent=getIntent();

       user =(User) intent.getSerializableExtra("userObject");


        if(!user.profileImage.equals("")) {

            StorageReference storageReference = storage.getReferenceFromUrl(user.profileImage);

//            Glide.with(this)
//                    .using(new FirebaseImageLoader())
//                    .load(storageReference)
//
//                    .placeholder(R.drawable.defaultuserimagemage)
//                    .into(user_profile_photo);

            Glide.with(this).using(new FirebaseImageLoader()).load(storageReference).asBitmap().centerCrop().into(new BitmapImageViewTarget(user_profile_photo) {
                @Override
                protected void setResource(Bitmap resource) {
                    ClassLoader context;
                    RoundedBitmapDrawable circularBitmapDrawable =
                            RoundedBitmapDrawableFactory.create(getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    user_profile_photo.setImageDrawable(circularBitmapDrawable);
                }
            });

        }

        user_profile_name.setText(user.Full_Name);
        user_profile_email.setText(user_profile_email.getText()+user.email);
        user_profile_short_bio.setText(user.userType);
        Log.i("Profile #####",user.UID);

        user_profile_email.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                try{
                    Intent intent=new Intent(Intent.ACTION_SEND);

                    String[] recipients={user.email.toLowerCase()};
                    intent.putExtra(Intent.EXTRA_EMAIL, recipients);
                    //intent.putExtra(Intent.EXTRA_SUBJECT,"Subject text here...");
                    //intent.putExtra(Intent.EXTRA_TEXT,"Body of the content here...");
                    //intent.putExtra(Intent.EXTRA_CC,"mailcc@gmail.com");
                    intent.setType("text/html");
                    intent.setPackage("com.google.android.gm");
                    startActivity(Intent.createChooser(intent, "Send mail"));
                }catch (Exception e){

                }
                return true;
            }
        });
    }



//    public void openGmail(View view){
//       try{
//            Intent intent=new Intent(Intent.ACTION_SEND);
//
//            String[] recipients={user.email.toLowerCase()};
//            intent.putExtra(Intent.EXTRA_EMAIL, recipients);
//            //intent.putExtra(Intent.EXTRA_SUBJECT,"Subject text here...");
//            //intent.putExtra(Intent.EXTRA_TEXT,"Body of the content here...");
//            //intent.putExtra(Intent.EXTRA_CC,"mailcc@gmail.com");
//            intent.setType("text/html");
//            intent.setPackage("com.google.android.gm");
//            startActivity(Intent.createChooser(intent, "Send mail"));
//        }catch (Exception e){
//
//       }}

    void getRefrence(){

        user_profile_photo=(ImageView)findViewById(R.id.user_profile_photo);
        user_profile_name=(TextView)findViewById(R.id.user_profile_name);
        user_profile_email=(TextView)findViewById(R.id.user_profile_email);
        user_profile_short_bio=(TextView)findViewById(R.id.user_profile_short_bio);

    }
}
