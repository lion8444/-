package com.psw.schoolwork_06;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    public Button button_connect_bluetooth;
    public Button btton_order_record;
    public Button button_how_to;
    public Button button_exit;
    public Button btnC;
    final FirebaseFirestore db = FirebaseFirestore.getInstance();
    final DocumentReference db2 = db.collection("photo").document("picture");
    String photo;
    Map<String,Object> photo1 = new HashMap<>();
    ConnectivityManager connectivityManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button_connect_bluetooth = (Button) findViewById(R.id.button_connect_bluetooth);
        btton_order_record = (Button) findViewById(R.id.btton_order_record);
 //       button_how_to = (Button) findViewById(R.id.button_how_to);
        button_exit = (Button) findViewById(R.id.button_exit);
  //      btnC = (Button) findViewById(R.id.btnC);
        checkInternetState();



        db2.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    return;
                }
                if (snapshot != null && snapshot.exists()) {
                    String test = String.valueOf(snapshot.getData());
                    //    Toast.makeText(getApplicationContext(),"변경감지",Toast.LENGTH_SHORT).show();
                    if(test.equals("{current=1}")||test.equals("{current=0}")){
                        db2.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        photo=document.getData().toString();
                                        if(photo.equals("{current=1}")){
                                            show();
                                        }
                                    } else {

                                    }
                                } else {
                                }
                            }
                        });
                    }


                } else {
                }
            }
        });

// 노액션바 설정
        button_connect_bluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onStart();
                Intent intent = new Intent(MainActivity.this, RealConnect.class);
                startActivity(intent);
                //SharedPreferences를 이용해 블루투스 연결이 된 이후 부터는
                //텝레이아웃의 2번쨰 페이지를 보여주도록 수정 예정
            }
        });

//        button_how_to.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this, HowToActivity.class);
//                startActivity(intent);
//            }
//        });

        btton_order_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, OrderRecord.class);
                startActivity(intent);
            }
        });

        button_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 종료 확인 다이얼로그(대화창) 생성
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, android.R.style.Theme_DeviceDefault_Light_Dialog);
                builder.setMessage("정말로 앱을 종료하시겠습니까?").setTitle("앱 종료")
                        .setPositiveButton("아니오", new DialogInterface.OnClickListener() {
                            // 아니오 버튼을 눌렀을 때
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 아무 작동없이 함수를 종료
                                return;
                            }
                        })
                        .setNeutralButton("예", new DialogInterface.OnClickListener() {
                            // 예 버튼을 눌렀을 때
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 앱을 종료
                                finish();
                            }
                        })
                        .setCancelable(false).show();
            }
        });
//        btnC.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(MainActivity.this, Test.class);
//                startActivity(intent);
//            }
//        });
    }
    private void show() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "default");
        builder.setSmallIcon(R.drawable.push1);
        builder.setContentTitle("배달통에 음식이 도착했어요");
        builder.setContentText("확인하기");

        Intent intent = new Intent(this, Intro.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(pendingIntent);

//        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(),
//                R.drawable.push1);
//        builder.setLargeIcon(largeIcon);

//        builder.setColor(Color.BLUE);

        Uri ringtoneUri = RingtoneManager.getActualDefaultRingtoneUri(this,
                RingtoneManager.TYPE_NOTIFICATION);

        builder.setSound(ringtoneUri);

        long[] vibrate = {0, 100, 200, 300};
        builder.setVibrate(vibrate);
        builder.setAutoCancel(true);

        NotificationManager manager = (NotificationManager) getSystemService((NOTIFICATION_SERVICE));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            manager.createNotificationChannel(new NotificationChannel("default", "기본채널",
                    NotificationManager.IMPORTANCE_DEFAULT));
        }
        manager.notify(1, builder.build());

        photo1.clear();
        photo1.put("current","0");
        db.collection("photo").document("picture")
                .set(photo1)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });
    }
    private void checkInternetState(){
        connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        assert connectivityManager !=null;
        if(!(connectivityManager.getActiveNetworkInfo()!=null && connectivityManager.getActiveNetworkInfo().isConnected())){
            new android.app.AlertDialog.Builder(this)
                    .setMessage("네트워크를 다시 연결해주세요").setTitle("네트워크 연결 실패")
                    .setCancelable(false)
                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intentConfirm = new Intent();
                            intentConfirm.setAction("android.settings.WIFI_SETTINGS");
                            startActivity(intentConfirm);
                        }
                    }).show();
        }
    }
}

