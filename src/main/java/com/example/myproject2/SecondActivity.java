package com.example.myproject2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Xfermode;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class SecondActivity extends AppCompatActivity {
    TCP_client tc;
    Bitmap bitmap;
    Bitmap reBitmap;
    Bitmap origin;
    Bitmap newreBitmap;
    private boolean move = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_second);
        final MyView m = new MyView(SecondActivity.this);
        //ImageView drawView = (ImageView)findViewById(R.id.image_draw);


        Bundle extras = getIntent().getExtras();
        byte[] byteArray = getIntent().getByteArrayExtra("image");
        bitmap = BitmapFactory.decodeByteArray(byteArray,0,byteArray.length);

        setContentView(m);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View v = inflater.inflate(R.layout.activity_draw,null);
        addContentView(v , new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,FrameLayout.LayoutParams.MATCH_PARENT));

        Button button = v.findViewById(R.id.button);
        Button button2 = v.findViewById(R.id.button_erase);



        Button button3 = v.findViewById(R.id.button_mask);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                float scale = (float)(1024/(float)bitmap.getWidth());
                int image_w = (int)(bitmap.getWidth() * scale) ;
                int image_h = (int)(bitmap.getWidth() * scale) ;
                Bitmap resize = Bitmap.createScaledBitmap(bitmap,image_w,image_h,true);
                resize.compress(Bitmap.CompressFormat.JPEG,100,stream);
                byte[] byteArray2 = stream.toByteArray();
                Intent intent = new Intent(SecondActivity.this,ChangeActivity.class);
                intent.putExtra("image1",byteArray2);
                startActivity(intent);
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tc = new SecondActivity.TCP_client();
                tc.execute(this);

            }
        });


        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                m.erase();
            }
        });


    }



    class MyView extends View {
        Paint paint = new Paint();
        Paint paint2 = new Paint();
        Path path = new Path();

        public MyView(Context context) {
            super(context);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(30f);
            paint2.setStyle(Paint.Style.FILL_AND_STROKE);
            paint2.setStrokeWidth(20f);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            //canvas.setBitmap(bitmap);
            canvas.drawBitmap(bitmap, 0, 0, null);
            reBitmap = Bitmap.createBitmap(bitmap.getWidth() ,bitmap.getHeight() , Bitmap.Config.ARGB_8888);
            newreBitmap = Bitmap.createBitmap(bitmap.getWidth() ,bitmap.getHeight() , Bitmap.Config.ARGB_8888);
            origin = Bitmap.createBitmap(bitmap.getWidth() ,bitmap.getHeight() , Bitmap.Config.ARGB_8888);
            Canvas c = new Canvas(reBitmap);
            Canvas newc = new Canvas(newreBitmap);
            Canvas oric = new Canvas(origin);

            c.drawBitmap(reBitmap,0,0,null);
            newc.drawBitmap(newreBitmap,0,0,null);
            newc.drawColor(Color.WHITE);
            c.drawBitmap(bitmap,0,0,null);
            oric.drawBitmap(bitmap,0,0,null);
            oric.drawBitmap(origin,0,0,null);
            //c.drawColor(Color.WHITE);

            if(move == false){
                canvas.drawPath(path,paint);
                c.drawPath(path,paint);
                newc.drawPath(path,paint);
            }
            if(move == true){
                canvas.drawBitmap(bitmap,0,0,null);
                c.drawColor(Color.WHITE);
                path.reset();
                //c.drawBitmap(bitmap,0,0,null);
                move = false;

            }

        }
        @Override
        public boolean onTouchEvent(MotionEvent event) {
            float x = event.getX();
            float y = event.getY();
            if(x > bitmap.getWidth()) x = bitmap.getWidth();
            if(y > bitmap.getHeight()) y = bitmap.getHeight();

            switch(event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    path.moveTo(x,y);
                    break;
                case MotionEvent.ACTION_MOVE:
                    path.lineTo(x,y);
                    break;
                case MotionEvent.ACTION_UP:
                    break;
            }
            invalidate();

            return true;
        }

        public void erase(){
            move = true;
            invalidate();
        }

    }

    public class TCP_client extends AsyncTask {
        protected String SERV_IP = "192.168.200.173";
        protected int PORT = 11411;

        protected Object doInBackground(Object[] objects) {

            try{
                Log.d("TCP","server connecting");
                InetAddress serverAddr = InetAddress.getByName(SERV_IP);
                Socket sock = new Socket(serverAddr,PORT);
                try{
                    System.out.println("데이터 찾는 중");
                    ByteArrayOutputStream bao = new ByteArrayOutputStream();
                    newreBitmap.compress(Bitmap.CompressFormat.JPEG,100,bao);
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

