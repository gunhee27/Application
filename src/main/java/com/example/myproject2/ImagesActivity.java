package com.example.myproject2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class ImagesActivity extends AppCompatActivity implements View.OnClickListener{
    public static File[] mfile;
    ImageView iv_preview;
    ImageView iv_preview2;
    ImageView iv_preview3;
    ImageView iv_preview4;
    ImageView iv_preview5;
    ImageView iv_preview6;

    Bitmap bitmap;
    Bitmap bitmap2;
    Bitmap bitmap3;
    Bitmap bitmap4;
    Bitmap bitmap5;
    Bitmap bitmap6;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images);
        iv_preview = (ImageView) findViewById(R.id.iv_preview);
        iv_preview2 = (ImageView) findViewById(R.id.iv_preview2);
        iv_preview3 = (ImageView) findViewById(R.id.iv_preview3);
        iv_preview4 = (ImageView) findViewById(R.id.iv_preview4);
        iv_preview5 = (ImageView) findViewById(R.id.iv_preview5);
        iv_preview6 = (ImageView) findViewById(R.id.iv_preview6);
        iv_preview.setOnClickListener(this);
        iv_preview2.setOnClickListener(this);
        iv_preview3.setOnClickListener(this);
        iv_preview4.setOnClickListener(this);
        iv_preview5.setOnClickListener(this);
        iv_preview6.setOnClickListener(this);

        Download();

    }

    private void Download(){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        final String filename[] = new String[6];
        mfile = new File[6];
        final StorageReference storageRef = storage.getReferenceFromUrl("gs://myproject-919cd.appspot.com").child("faces2/");
        StorageReference pathReference = storageRef.child("sample_0.jpg");
        mfile[0] = new File(this.getExternalFilesDir(null), "sample_0.jpg");
        try{
            //final File file = new File(this.getExternalFilesDir(null), filename[i1]);
            final FileDownloadTask fileDownloadTask = pathReference.getFile(mfile[0]);
            fileDownloadTask.addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    iv_preview.setImageURI(Uri.fromFile(mfile[0]));
                    BitmapDrawable d = (BitmapDrawable)iv_preview.getDrawable();
                    bitmap = d.getBitmap();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Log.e("zzz","fail");
                }
            }).addOnProgressListener(new OnProgressListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onProgress(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    //Toast.makeText(getApplicationContext(), "진행중 ", Toast.LENGTH_SHORT).show();
                }
            });
        } catch(Exception e){
            e.printStackTrace();
        }
        StorageReference pathReference2 = storageRef.child("sample_1.jpg");
        mfile[1] = new File(this.getExternalFilesDir(null), "sample_1.jpg");
        try{
            //final File file = new File(this.getExternalFilesDir(null), filename[i1]);
            final FileDownloadTask fileDownloadTask = pathReference2.getFile(mfile[1]);
            fileDownloadTask.addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    iv_preview2.setImageURI(Uri.fromFile(mfile[1]));
                    Drawable d1 = iv_preview2.getDrawable();
                    bitmap2 = ((BitmapDrawable)d1).getBitmap();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Log.e("zzz","fail");
                }
            }).addOnProgressListener(new OnProgressListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onProgress(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    //Toast.makeText(getApplicationContext(), "진행중 ", Toast.LENGTH_SHORT).show();
                }
            });
        } catch(Exception e){
            e.printStackTrace();
        }
        StorageReference pathReference3 = storageRef.child("sample_2.jpg");
        mfile[2] = new File(this.getExternalFilesDir(null), "sample_2.jpg");
        try{
            //final File file = new File(this.getExternalFilesDir(null), filename[i1]);
            final FileDownloadTask fileDownloadTask = pathReference3.getFile(mfile[2]);
            fileDownloadTask.addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    iv_preview3.setImageURI(Uri.fromFile(mfile[2]));
                    Drawable d2 = iv_preview3.getDrawable();
                    bitmap3 = ((BitmapDrawable)d2).getBitmap();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Log.e("zzz","fail");
                }
            }).addOnProgressListener(new OnProgressListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onProgress(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    //Toast.makeText(getApplicationContext(), "진행중 ", Toast.LENGTH_SHORT).show();
                }
            });
        } catch(Exception e){
            e.printStackTrace();
        }
        StorageReference pathReference4 = storageRef.child("sample_3.jpg");
        mfile[3] = new File(this.getExternalFilesDir(null), "sample_3.jpg");
        try{
            //final File file = new File(this.getExternalFilesDir(null), filename[i1]);
            final FileDownloadTask fileDownloadTask = pathReference4.getFile(mfile[3]);
            fileDownloadTask.addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    iv_preview4.setImageURI(Uri.fromFile(mfile[3]));
                    Drawable d3 = iv_preview4.getDrawable();
                    bitmap4 = ((BitmapDrawable)d3).getBitmap();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Log.e("zzz","fail");
                }
            }).addOnProgressListener(new OnProgressListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onProgress(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    //Toast.makeText(getApplicationContext(), "진행중 ", Toast.LENGTH_SHORT).show();
                }
            });
        } catch(Exception e){
            e.printStackTrace();
        }
        StorageReference pathReference5 = storageRef.child("sample_4.jpg");
        mfile[4] = new File(this.getExternalFilesDir(null), "sample_4.jpg");
        try{
            //final File file = new File(this.getExternalFilesDir(null), filename[i1]);
            final FileDownloadTask fileDownloadTask = pathReference5.getFile(mfile[4]);
            fileDownloadTask.addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    iv_preview5.setImageURI(Uri.fromFile(mfile[4]));
                    Drawable d4 = iv_preview5.getDrawable();
                    bitmap5 = ((BitmapDrawable)d4).getBitmap();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Log.e("zzz","fail");
                }
            }).addOnProgressListener(new OnProgressListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onProgress(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    //Toast.makeText(getApplicationContext(), "진행중 ", Toast.LENGTH_SHORT).show();
                }
            });
        } catch(Exception e){
            e.printStackTrace();
        }
        StorageReference pathReference6 = storageRef.child("sample_5.jpg");
        mfile[5] = new File(this.getExternalFilesDir(null), "sample_5.jpg");
        try{
            //final File file = new File(this.getExternalFilesDir(null), filename[i1]);
            final FileDownloadTask fileDownloadTask = pathReference6.getFile(mfile[5]);
            fileDownloadTask.addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    iv_preview6.setImageURI(Uri.fromFile(mfile[5]));
                    BitmapDrawable d5 = (BitmapDrawable)iv_preview6.getDrawable();
                    bitmap6 = ((BitmapDrawable)d5).getBitmap();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Log.e("zzz","fail");
                }
            }).addOnProgressListener(new OnProgressListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onProgress(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    //Toast.makeText(getApplicationContext(), "진행중 ", Toast.LENGTH_SHORT).show();
                }
            });
        } catch(Exception e){
            e.printStackTrace();
        }

    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_preview) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            float scale = (float)(1024/(float)bitmap.getWidth());
            int image_w = (int)(bitmap.getWidth() * scale);
            int image_h = (int)(bitmap.getWidth() * scale);
            Bitmap resize = Bitmap.createScaledBitmap(bitmap,image_w,image_h,true);
            resize.compress(Bitmap.CompressFormat.JPEG,100,stream);
            byte[] byteArray = stream.toByteArray();
            Intent intent = new Intent(ImagesActivity.this, SecondActivity.class);
            intent.putExtra("image",byteArray);
            startActivity(intent);
        } else if (v.getId() == R.id.iv_preview2) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            float scale = (float)(1024/(float)bitmap2.getWidth());
            int image_w = (int)(bitmap2.getWidth() * scale);
            int image_h = (int)(bitmap2.getWidth() * scale);
            Bitmap resize = Bitmap.createScaledBitmap(bitmap2,image_w,image_h,true);
            resize.compress(Bitmap.CompressFormat.JPEG,100,stream);
            byte[] byteArray = stream.toByteArray();
            Intent intent = new Intent(ImagesActivity.this,SecondActivity.class);
            intent.putExtra("image",byteArray);
            startActivity(intent);
        } else if (v.getId() == R.id.iv_preview3) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            float scale = (float)(1024/(float)bitmap3.getWidth());
            int image_w = (int)(bitmap3.getWidth() * scale);
            int image_h = (int)(bitmap3.getWidth() * scale);
            Bitmap resize = Bitmap.createScaledBitmap(bitmap3,image_w,image_h,true);
            resize.compress(Bitmap.CompressFormat.JPEG,100,stream);
            byte[] byteArray = stream.toByteArray();
            Intent intent = new Intent(ImagesActivity.this,SecondActivity.class);
            intent.putExtra("image",byteArray);
            startActivity(intent);
        }
        else if (v.getId() == R.id.iv_preview4) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            float scale = (float)(1024/(float)bitmap4.getWidth());
            int image_w = (int)(bitmap4.getWidth() * scale);
            int image_h = (int)(bitmap4.getWidth() * scale);
            Bitmap resize = Bitmap.createScaledBitmap(bitmap4,image_w,image_h,true);
            resize.compress(Bitmap.CompressFormat.JPEG,100,stream);
            byte[] byteArray = stream.toByteArray();
            Intent intent = new Intent(ImagesActivity.this,SecondActivity.class);
            intent.putExtra("image",byteArray);
            startActivity(intent);
        }
        else if (v.getId() == R.id.iv_preview5) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            float scale = (float)(1024/(float)bitmap5.getWidth());
            int image_w = (int)(bitmap5.getWidth() * scale);
            int image_h = (int)(bitmap5.getWidth() * scale);
            Bitmap resize = Bitmap.createScaledBitmap(bitmap5,image_w,image_h,true);
            resize.compress(Bitmap.CompressFormat.JPEG,100,stream);
            byte[] byteArray = stream.toByteArray();
            Intent intent = new Intent(ImagesActivity.this,SecondActivity.class);
            intent.putExtra("image",byteArray);
            startActivity(intent);
        }
        else if (v.getId() == R.id.iv_preview6) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            float scale = (float)(1024/(float)bitmap6.getWidth());
            int image_w = (int)(bitmap6.getWidth() * scale);
            int image_h = (int)(bitmap6.getWidth() * scale);
            Bitmap resize = Bitmap.createScaledBitmap(bitmap6,image_w,image_h,true);
            resize.compress(Bitmap.CompressFormat.JPEG,100,stream);
            byte[] byteArray = stream.toByteArray();
            Intent intent = new Intent(ImagesActivity.this,SecondActivity.class);
            intent.putExtra("image",byteArray);
            startActivity(intent);
        }
    }
}
