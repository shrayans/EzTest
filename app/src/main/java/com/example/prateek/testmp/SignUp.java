package com.example.prateek.testmp;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;


public class SignUp extends AppCompatActivity {

    EditText EmailEditText;
    EditText FullName;
    EditText PasswordEditText;
    EditText RePasswordEditText,ValidationString;
    public Button AsATeacher,AsAStudent;
    public RelativeLayout UserTypeLayout;
    public ConstraintLayout constraintLayout;

    //private Uri filePath;

    ImageView ProfileImageView;
    private Bitmap bitmap;
    private File destination = null;
    private InputStream inputStreamImg;
    private String imgPath = null;
    private final int PICK_IMAGE_CAMERA = 1, PICK_IMAGE_GALLERY = 2;
    private Uri filePath;


    ProgressDialog progressDialog;

    public FirebaseAuth firebaseAuth;
    private DatabaseReference mDatabase;
    private FirebaseStorage firebaseStorage;
    StorageReference storageReference;


    public int UserType;
    public String userTypeString=null;



    public void toMainActivity(View view ){

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

    }

    public void setUserType(View view){

       int tag=  Integer.valueOf((String) view.getTag());
       System.out.print(tag+"\n\n");


        if(tag==0){

           UserType=0;
           userTypeString="Teachers";


        }else {

           UserType=1;
           userTypeString="Student";


        }

        UserTypeLayout.setVisibility(View.INVISIBLE);
        constraintLayout.setVisibility(View.VISIBLE);

    }

    public void signUp(View view){

        final String email=EmailEditText.getText().toString().trim();
        final String Fullname=FullName.getText().toString().trim();
        String password=PasswordEditText.getText().toString().trim();
        String rePassword=RePasswordEditText.getText().toString().trim();
//        String key= ValidationString.getText().toString().trim();

        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"Please enter email",Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"Please enter password",Toast.LENGTH_LONG).show();
            return;
        }
        if(TextUtils.isEmpty(Fullname)){
            Toast.makeText(this,"Please enter Full Name",Toast.LENGTH_LONG).show();
            return;
        }
        if(TextUtils.isEmpty(rePassword)){
            Toast.makeText(this,"Please re-enter Password",Toast.LENGTH_LONG).show();
            return;
        }

        if(!password.equals(rePassword)){
            Toast.makeText(this,"Re-entered Password doesn't match",Toast.LENGTH_LONG).show();
            return;
        }
//        if(TextUtils.isEmpty(key)){
//            Toast.makeText(this,"Please enter private key",Toast.LENGTH_LONG).show();
//            return;
//        }
        final Intent intent;

        if(UserType==0){
            //userTypeString="Teachers";
            intent=new Intent(SignUp.this,HomeTeacherActivity.class);
//            if(!key.equals("654321")){
//                Toast.makeText(this,"Private Key doesn,t match ! Try Aganin",Toast.LENGTH_LONG).show();
//                return;
//            }
        }
        else
        {
            //userTypeString="Student";
            intent=new Intent(SignUp.this,HomeStudentActivity.class);
//            if(!key.equals("12345")){
//                Toast.makeText(this,"Private Key doesn,t match ! Try Aganin",Toast.LENGTH_LONG).show();
//                return;
//            }
        }


        progressDialog.setMessage("Registering Please Wait...");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //checking if success
                        if (task.isSuccessful()) {
                            User user = new User(email,Fullname,firebaseAuth.getCurrentUser().getUid().toString(),userTypeString);

                            mDatabase.child("users").child(userTypeString).child(firebaseAuth.getCurrentUser().getUid().toString()).setValue(user);
                            Toast.makeText(SignUp.this, "Database Updated", Toast.LENGTH_LONG).show();

                            Log.i("Signup", "Successful");

                            progressDialog.dismiss();

                            if(filePath != null)
                            {
                                final ProgressDialog progressDialog = new ProgressDialog(SignUp.this);
                                progressDialog.setTitle("Uploading...");
                                progressDialog.show();

                                StorageReference ref = storageReference.child("images/"+ firebaseAuth.getCurrentUser().getUid().toString());
                                ref.putFile(filePath)
                                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                            @Override
                                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                progressDialog.dismiss();
                                                String URL= taskSnapshot.getMetadata().getDownloadUrl().toString();
                                                System.out.println(URL);

                                                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();


                                                mDatabase.child("users").child(userTypeString).child(firebaseAuth.getCurrentUser().getUid().toString()).child("profileImage").setValue(URL);


                                                Toast.makeText(SignUp.this, "Uploaded", Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                progressDialog.dismiss();
                                                Toast.makeText(SignUp.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                            @Override
                                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                                double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                                        .getTotalByteCount());
                                                progressDialog.setMessage("Uploaded "+(int)progress+"%");
                                            }
                                        });
                                startActivity(intent);
                            }else {
                                startActivity(intent);
                            }


                            //Intent intent = new Intent(SignUp.this, TestEntryActivity.class);




                        } else {
                            //display some message here
                            Toast.makeText(SignUp.this, "Registration Error", Toast.LENGTH_LONG).show();
                        }

                    }
                });



    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        firebaseAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        firebaseStorage=FirebaseStorage.getInstance();
        storageReference=firebaseStorage.getReference();

        progressDialog = new ProgressDialog(this);


       getRefrence();


    }

    public void getRefrence(){

        ProfileImageView=(ImageView) findViewById(R.id.ProfileImageView);
        EmailEditText = (EditText) findViewById(R.id.EmailEditText);
        FullName = (EditText) findViewById(R.id.FullName);
        PasswordEditText = (EditText) findViewById(R.id.PasswordEditText);
        RePasswordEditText = findViewById(R.id.RePasswordEditText);
        UserTypeLayout=(RelativeLayout)findViewById(R.id.UserTypeLayout);
        constraintLayout=(ConstraintLayout)findViewById(R.id.ConstraintLayout);
        AsAStudent=(Button)findViewById(R.id.AsAStudent);
        AsATeacher=(Button)findViewById(R.id.AsATeacher);
       // ValidationString=(EditText) findViewById(R.id.ValidationString);

    }



    public void getProfilePhoto(View view){

        selectImage();

    }

    private void selectImage() {
        try {
            PackageManager pm = getPackageManager();
            int hasPerm = pm.checkPermission(android.Manifest.permission.CAMERA, getPackageName());
            if (hasPerm == PackageManager.PERMISSION_GRANTED) {
                final CharSequence[] options = {"Take Photo", "Choose From Gallery","Cancel"};
                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
                builder.setTitle("Select Option");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (options[item].equals("Take Photo")) {
                            dialog.dismiss();
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(intent, PICK_IMAGE_CAMERA);
                        } else if (options[item].equals("Choose From Gallery")) {
                            dialog.dismiss();
                            Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(pickPhoto, PICK_IMAGE_GALLERY);
                        } else if (options[item].equals("Cancel")) {
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();
            } else
                Toast.makeText(this, "Camera Permission error", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Camera Permission error", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        inputStreamImg = null;
        if (requestCode == PICK_IMAGE_CAMERA) {
            try {
                Uri selectedImage = data.getData();
                bitmap = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 20, bytes);

                Log.e("Activity", "Pick from Camera::>>> ");

                System.out.println(Environment.getExternalStorageDirectory().toString());

               String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
                destination = new File(Environment.getExternalStorageDirectory() + "/" +
                        getString(R.string.app_name), "IMG_"+timeStamp + ".jpg");
                FileOutputStream fo;
                try {
                    destination.createNewFile();
                    fo = new FileOutputStream(destination);
                    fo.write(bytes.toByteArray());
                    fo.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }


                filePath=data.getData();
                Log.i("Image Filepath======",filePath.toString());

                imgPath = destination.getAbsolutePath();
                ProfileImageView.setImageBitmap(bitmap);

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == PICK_IMAGE_GALLERY) {
            Uri selectedImage = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
                Log.e("Activity", "Pick from Gallery::>>> ");

                imgPath = getRealPathFromURI(selectedImage);
                destination = new File(imgPath.toString());
                ProfileImageView.setImageBitmap(bitmap);
                filePath=data.getData();
                Log.i("Image Filepath======",filePath.toString());


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Audio.Media.DATA};
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }


}