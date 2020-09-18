package com.example.myproject2;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static java.lang.Thread.sleep;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int PICK_FROM_CAMERA = 0;
    private static final int PICK_FROM_ALBUM = 1;
    Bitmap img;
    TCP_client tc;
    public static File[] mfile;
    int degree = 0;
    private static final String TAG = "MainActivity";
    private ImageView ivPreview;
    private Uri filePath;
    GridView gridView;
    Uri path[] = new Uri[7];

    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btChoose = (Button) this.findViewById(R.id.bt_choose);
        Button btUpload = (Button) this.findViewById(R.id.bt_upload);
        Button btDown = (Button) this.findViewById(R.id.bt_download);
        ivPreview = (ImageView) findViewById(R.id.iv_preview);
        btChoose.setOnClickListener(this);
        btUpload.setOnClickListener(this);
        btDown.setOnClickListener(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Log.d("My Tag", "권한 설정 완료");
            } else {
                Log.d("MY Tag", "권한 설정 요청");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d("My Tag", "onRequestPermissionsResult");
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
            Log.d("My Tag", "Permission: " + permissions[0] + "was " + grantResults[0]);
        }
    }

    public void doTakePhotoAction() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, PICK_FROM_CAMERA);
        }
    }

    public void doTakeAlbumAction() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, PICK_FROM_ALBUM);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case PICK_FROM_ALBUM: {
                if (resultCode == RESULT_OK && requestCode == PICK_FROM_ALBUM) {
                    filePath = data.getData();

                    Log.d(TAG, "uri:" + String.valueOf(filePath));
                    try {
                        String imagePath = uri_path(filePath);

                        //Uri 파일을 Bitmap으로 만들어서 ImageView에 집어 넣는다.
                        InputStream in = getContentResolver().openInputStream(data.getData());
                        img = BitmapFactory.decodeStream(in);
                        ExifInterface exif = new ExifInterface(imagePath);
                        if (exif != null) {
                            int exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                                    -1);
                            if (exifOrientation != -1) {
                                switch (exifOrientation) {
                                    case ExifInterface.ORIENTATION_ROTATE_90:
                                        degree = 90;
                                        break;

                                    case ExifInterface.ORIENTATION_ROTATE_180:
                                        degree = 180;
                                        break;

                                    case ExifInterface.ORIENTATION_ROTATE_270:
                                        degree = 270;
                                        break;
                                }
                            }
                        }
                        img = rotateImage(img, degree);
                        ivPreview.setImageBitmap(img);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
            case PICK_FROM_CAMERA: {
                if (resultCode == RESULT_OK && requestCode == PICK_FROM_CAMERA && data.hasExtra("data")) {
                    Bundle extras = data.getExtras();
                    img = (Bitmap) extras.get("data");

                    ivPreview.setImageBitmap(img);

                }
            }
        }
    }

    public void onClick(View v) {
        if (v.getId() == R.id.bt_choose) {
            DialogInterface.OnClickListener cameraListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    doTakePhotoAction();
                }
            };
            DialogInterface.OnClickListener albumListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    doTakeAlbumAction();
                }
            };
            DialogInterface.OnClickListener cancelListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            };

            new AlertDialog.Builder(this)
                    .setTitle("업로드할 이미지 선택")
                    .setPositiveButton("사진촬영", cameraListener)
                    .setNeutralButton("앨범선택", albumListener)
                    .setNegativeButton("취소", cancelListener)
                    .show();
        } else if (v.getId() == R.id.bt_upload) {
            tc = new TCP_client();
            tc.execute(this);
        } else if (v.getId() == R.id.bt_download) {
            //Download();
            openImagesActivity();

        }
    }

    //upload the file
    private void upload() {
        //업로드할 파일이 있으면 수행
        if (filePath != null) {
            //업로드 진행 Dialog 보이기
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("업로드중...");
            progressDialog.show();

            //storage
            FirebaseStorage storage = FirebaseStorage.getInstance();
            //Unique한 파일명을 만들자.
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMHH_mmss");
            Date now = new Date();
            String filename = "file" + ".jpg";
            //storage 주소와 폴더 파일명을 지정해 준다.
            StorageReference storageRef = storage.getReferenceFromUrl("gs://myproject-919cd.appspot.com").child("images2/" + filename);
            final String url = "gs://myproject-919cd.appspot.com/images2/" + filename;
            //올라가거라..
            storageRef.putFile(filePath)
                    //성공시
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss(); //업로드 진행 Dialog 상자 닫기
                            Toast.makeText(getApplicationContext(), "업로드 완료!", Toast.LENGTH_SHORT).show();
                            Toast.makeText(getApplicationContext(), "DB에 업로드 완료!", Toast.LENGTH_SHORT).show();
                        }
                    })
                    //실패시
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "업로드 실패!", Toast.LENGTH_SHORT).show();
                        }
                    })
                    //진행중
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            @SuppressWarnings("VisibleForTests") //이걸 넣어 줘야 아랫줄에 에러가 사라진다. 넌 누구냐?
                                    double progress = (100 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                            //dialog에 진행률을 퍼센트로 출력해 준다
                            progressDialog.setMessage("Uploaded " + ((int) progress) + "% ...");
                        }
                    });
        } else {
            Toast.makeText(getApplicationContext(), "파일을 먼저 선택하세요.", Toast.LENGTH_SHORT).show();
        }
    }


    public class TCP_client extends AsyncTask {
        protected String SERV_IP = "192.168.200.173";
        protected int PORT = 11410;

        @Override
        protected Object doInBackground(Object[] objects) {

            try {
                Log.d("TCP", "server connecting");
                InetAddress serverAddr = InetAddress.getByName(SERV_IP);
                Socket sock = new Socket(serverAddr, PORT);
                try {
                    System.out.println("데이터 찾는 중");
                    String str = uri_path(filePath);
                    File file = new File(str);

                    DataInputStream dis = new DataInputStream(new FileInputStream(file));
                    DataOutputStream dos = new DataOutputStream(sock.getOutputStream());

                    long fileSize = file.length();
                    byte[] buf = new byte[1024];

                    long totalReadBytes = 0;
                    int readBytes;
                    System.out.println("데이터 찾기 끝");

                    while ((readBytes = dis.read(buf)) > 0) {
                        System.out.println("while");
                        dos.write(buf, 0, readBytes);
                        totalReadBytes += readBytes;
                    }
                    System.out.println("데이터 보내기 끝 직전");
                    dos.close();
                    System.out.println("데이터 끝");
                    Toast.makeText(MainActivity.this,"다 보냈습니다",Toast.LENGTH_LONG).show();

                } catch (Exception e) {
                    Log.d("TCP", "don't send message");
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

    private String uri_path(Uri uri) {
        String res = null;
        String[] image_data = {MediaStore.Images.Media.DATA};
        Cursor cur = getContentResolver().query(uri, image_data, null, null, null);
        if (cur.moveToFirst()) {
            int col = cur.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cur.getString(col);
        }
        cur.close();
        return res;
    }

    private void openImagesActivity() {
        Intent intent = new Intent(this, ImagesActivity.class);
        startActivity(intent);
    }


    public Bitmap rotateImage(Bitmap src, float degree) {
        // Matrix 객체 생성
        Matrix matrix = new Matrix();
        // 회전 각도 셋팅
        matrix.postRotate(degree);
        // 이미지와 Matrix 를 셋팅해서 Bitmap 객체 생성
        return Bitmap.createBitmap(src, 0, 0, src.getWidth(),
                src.getHeight(), matrix, true);
    }

    public void SaveBitmapToFileCache(Bitmap bitmap, String strFilePath) {
        File fileCacheItem = new File(strFilePath);
        OutputStream out = null;
        try {
            fileCacheItem.createNewFile();
            out = new FileOutputStream(fileCacheItem);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
