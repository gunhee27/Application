package com.example.myproject2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class ChangeActivity extends AppCompatActivity {
    ImageView iv_done,imageView;
    TCP_client tc;
    Bitmap bitmap2;
    Bitmap bitmap3;
    String filepath;
    File mfile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change);
        iv_done = (ImageView)findViewById(R.id.iv_done);
        imageView = (ImageView)findViewById(R.id.imageView);
        Button button = findViewById(R.id.bt_change);
        Button button2 = findViewById(R.id.bt_get);
        filepath = Environment.getDataDirectory() + "/temp/";

        Bundle extras2 = getIntent().getExtras();
        byte[] byteArray2 = getIntent().getByteArrayExtra("image1");
        bitmap2 = BitmapFactory.decodeByteArray(byteArray2,0,byteArray2.length);
        imageView.setImageBitmap(bitmap2);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //SaveBitmapToFileCache(bitmap2,filepath);
                tc = new ChangeActivity.TCP_client();
                tc.execute(this);
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Download();
            }
        });

    }

    private void Download(){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        final String filename;
        final StorageReference storageRef = storage.getReferenceFromUrl("gs://myproject-919cd.appspot.com").child("result2/");
        StorageReference pathReference = storageRef.child("0000_output.png");
        mfile = new File(this.getExternalFilesDir(null), "0000_output.png");
        try{
            //final File file = new File(this.getExternalFilesDir(null), filename[i1]);
            final FileDownloadTask fileDownloadTask = pathReference.getFile(mfile);
            fileDownloadTask.addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    iv_done.setImageURI(Uri.fromFile(mfile));
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


    private void SaveBitmapToFileCache(Bitmap bitmap, String strFilePath) {

        File fileCacheItem = new File(strFilePath);
        OutputStream out = null;

        try
        {
            fileCacheItem.createNewFile();
            out = new FileOutputStream(fileCacheItem);

            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                out.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    public class TCP_client extends AsyncTask {
        protected String SERV_IP = "192.168.200.173";
        protected int PORT = 11412;

        protected Object doInBackground(Object[] objects) {

            try{
                Log.d("TCP","server connecting");
                InetAddress serverAddr = InetAddress.getByName(SERV_IP);
                Socket sock = new Socket(serverAddr,PORT);
                try{
                    System.out.println("데이터 찾는 중");
                    ByteArrayOutputStream bao = new ByteArrayOutputStream();
                    bitmap2.compress(Bitmap.CompressFormat.JPEG,100,bao);
                    byte[] ba = bao.toByteArray();
                    String ba1 = Base64.encodeToString(ba,Base64.DEFAULT);

                    DataOutputStream dos = new DataOutputStream(sock.getOutputStream());

                    long totalReadBytes = 0;
                    int readBytes;
                    System.out.println("데이터 찾기 끝");

                    dos.write(ba,0,ba.length);


                    System.out.println("데이터 보내기 끝 직전");
                    dos.close();
                    System.out.println("데이터 끝");
                    Toast.makeText(getApplicationContext(),"다 보내졌습니다",Toast.LENGTH_LONG).show();

                } catch (Exception e) {
                    Log.d("TCP","don't send message");
                    e.printStackTrace();
                }

            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

    }

}

