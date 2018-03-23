package com.example.prateek.testmp;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

/**
 * Created by prateek on 3/11/18.
 */

public class CustomAdapter extends ArrayAdapter<User> {

    public ArrayList data= new ArrayList();
    FirebaseStorage storage;

    public CustomAdapter(@NonNull Context context, ArrayList<User> arrayList) {
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
//Check03
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater shreeInflater = LayoutInflater.from(getContext());
        View customView = shreeInflater.inflate(R.layout.custom_userlist_layout, parent, false);

        TextView shreeText = (TextView) customView.findViewById(R.id.shreeText);
        ImageView profile_image = (ImageView) customView.findViewById(R.id.profile_image);

        if (data.size() >= 0) {
            storage=FirebaseStorage.getInstance();
            try {
                User user = (User)data.get(position);

                String string = user.Full_Name;

                System.out.print(string);

                Log.i("FullNAmememesdd", string);

                shreeText.setText(string);
                profile_image.setImageResource(R.drawable.defaultuserimagemage);

                if(!user.profileImage.equals("")) {

                    StorageReference storageReference = storage.getReferenceFromUrl(user.profileImage);

                    Glide.with(getContext())
                            .using(new FirebaseImageLoader())
                            .load(storageReference)
                            .placeholder(R.drawable.defaultuserimagemage)
                            .into(profile_image);

                }
            }catch(Exception e){
                e.printStackTrace();
            }

        }
        else {
            shreeText.setText("No users");
        }

        return customView;

    }


}
