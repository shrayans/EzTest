package com.example.prateek.testmp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;



import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import static android.support.v4.content.ContextCompat.startActivity;

/**
 * Created by prateek on 3/11/18.
 */

public class CustomAdapter extends ArrayAdapter<Object> {

    public ArrayList data= new ArrayList();
    FirebaseStorage storage;

    public CustomAdapter(@NonNull Context context, ArrayList<Object> arrayList) {


        super(context,R.layout.custom_userlist_layout, arrayList);
        data=arrayList;
        Log.i("Size of Array",arrayList.size()+"");
    }

    @Override
    public int getCount() {
        if(data.size()<=0){
            System.out.print(0+"");
        }

        return super.getCount();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        final User user;

        LayoutInflater shreeInflater = LayoutInflater.from(getContext());
        View customView = shreeInflater.inflate(R.layout.custom_userlist_layout, parent, false);

        TextView userTextView = (TextView) customView.findViewById(R.id.shreeText);
        final ImageView profile_image = (ImageView) customView.findViewById(R.id.profile_image);

        if (data.size() >= 0) {
            storage=FirebaseStorage.getInstance();
            try {
                user = (User)data.get(position);

                String string = user.Full_Name;

                userTextView.setText(string);
                profile_image.setImageResource(R.drawable.defaultuserimagemage);

                if(!user.profileImage.equals("")) {

                    StorageReference storageReference = storage.getReferenceFromUrl(user.profileImage);
//
//                    Glide.with(getContext())
//                            .using(new FirebaseImageLoader())
//                            .load(storageReference)
//
//                            .placeholder(R.drawable.defaultuserimagemage)
//                            .into(profile_image);

                    Glide.with(getContext()).using(new FirebaseImageLoader()).load(storageReference).asBitmap().centerCrop().into(new BitmapImageViewTarget(profile_image) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            ClassLoader context;
                            RoundedBitmapDrawable circularBitmapDrawable =
                                    RoundedBitmapDrawableFactory.create(getContext().getResources(), resource);
                            circularBitmapDrawable.setCircular(true);
                            profile_image.setImageDrawable(circularBitmapDrawable);
                        }
                    });

                }
            }catch(Exception e){
                e.printStackTrace();
            }

        }
        else {
            userTextView.setText("No users");
        }


        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        return customView;

    }

    void goToProfileActivity(){

    }

}
