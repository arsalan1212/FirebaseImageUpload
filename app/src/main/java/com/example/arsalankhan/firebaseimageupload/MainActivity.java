package com.example.arsalankhan.firebaseimageupload;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {

    StorageReference storageReference;
    private static final int PICK_IMAGE_CODE=2;
    private static final int CAPTURE_IMAGE_CODE=1;
    ProgressDialog progessDialog;
    private ImageView imageview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progessDialog=new ProgressDialog(this);
        storageReference= FirebaseStorage.getInstance().getReference();
        imageview= (ImageView) findViewById(R.id.imageView);


    }

    public void CaptureImage(View view){
        Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,CAPTURE_IMAGE_CODE);
    }

    //btn click
    public void PickImage(View view){

        Intent intent=new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,PICK_IMAGE_CODE);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        progessDialog.setMessage("Please wait, while Image upload");
        progessDialog.show();

        if(requestCode==PICK_IMAGE_CODE && resultCode==RESULT_OK){
            Uri uri=data.getData();
            StorageReference filePath=storageReference.child("photo").child(uri.getLastPathSegment());
            filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Toast.makeText(MainActivity.this, "Image Uploaded", Toast.LENGTH_SHORT).show();
                    progessDialog.dismiss();

                }
            });
        }else if(requestCode==CAPTURE_IMAGE_CODE && resultCode==RESULT_OK){
            Uri uri=data.getData();
            StorageReference filePath=storageReference.child("Capture").child(uri.getLastPathSegment());
            filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progessDialog.dismiss();
                    Toast.makeText(MainActivity.this, "Image Uploaded", Toast.LENGTH_SHORT).show();
                    Picasso.with(MainActivity.this).load(taskSnapshot.getDownloadUrl()).into(imageview);
                }
            });
        }
    }
}
