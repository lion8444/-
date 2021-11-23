package com.psw.schoolwork_06;

import static java.lang.Thread.sleep;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//public class OrderRecord extends AppCompatActivity {
//    private Button btnTest;
//    private TextView textView2,textView3,textView4,textView5;
//    private int shared=0;
//
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.a);
//
//        //a레이아웃은 테스트 페이지
//        // RealConnect에서 받은 사진도 함께 저장하도록 수정?..?....
//        btnTest= (Button)findViewById(R.id.btnTest);
//        textView2 = (TextView)findViewById(R.id.textView2);
//        textView3 = (TextView)findViewById(R.id.textView3);
//        textView4 = (TextView)findViewById(R.id.textView4);
//        textView5 = (TextView)findViewById(R.id.textView5);
//        Toast.makeText(this.getApplicationContext(), "주문기록 클릭", Toast.LENGTH_SHORT).show();
//
//
//        btnTest.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(getApplicationContext(),"테스트 버튼 클릭",Toast.LENGTH_SHORT).show();
//
//            }
//        });
//    }
//
//    //이 페이지를 클릭하면 데이터를 가져올수 있지만 마지막에 작성된 데이터만 가져온다.
//    //만약 배달 정보를 2번 입력하고 이 페이지를 한번만 클릭했다면 마지막에 입력한 정보만 출력된다.
//    //https://siadaddy-cordinglife.tistory.com/12 참고
//    @Override
//    public void onStart() {
//        super.onStart();
//        SharedPreferences sharedData1= getSharedPreferences("display1", MODE_PRIVATE);    // display1 이름의 기본모드 설정, 만약 test key값이 있다면 해당 값을 불러옴.
//        String inputText1 = sharedData1.getString("share1",""); //SharedPreferences를 제할
//        textView2.setText(inputText1);
//
//
//        SharedPreferences sharedData2= getSharedPreferences("display2", MODE_PRIVATE);
//        String inputText2 = sharedData2.getString("share2","");
//        textView3.setText(inputText2);
//
//        SharedPreferences sharedData3= getSharedPreferences("display3", MODE_PRIVATE);
//        String inputText3 = sharedData3.getString("share3","");
//        textView4.setText(inputText3);
//
//        SharedPreferences sharedData4= getSharedPreferences("display4", MODE_PRIVATE);
//        String inputText4 = sharedData4.getString("share4","");
//        textView5.setText(inputText4);
//        shared=0;
//
//    }
//
//}

public class OrderRecord extends AppCompatActivity {
    final FirebaseFirestore dbList = FirebaseFirestore.getInstance();
    private ListView listview;
    private String dbText;
    private TextView text0,text1,text2,text3,text4,text5,text6,text7,text8,text9,text10;
    private TextView text11,text12,text13,text14,text15,text16,text17,text18,text19,text20;
    private TextView textLine0,textLine1;
    private Button btn;
    private Button btn2;
    private String str;
    private String str0,str1,str2,str3,str4,str5,str6,str7,str8,str9,str10 ;
    private String str11,str12,str13,str14,str15,str16,str17,str18,str19,str20 ;
    private int acount;
//    ArrayList<String> arrayList = new ArrayList();
//    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
//            android.R.layout.simple_list_item_1, arrayList);
    String name[] = {"음식점","배달코드","요청사항","도착예정시간"};
    String[] strSplit;
    String[] time;
    String strPlus;

    ArrayList<String> arrayList;
    ArrayAdapter<String> adapter;

    ArrayList<String> IntegerList;
    HashMap<Integer,String> save;
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview); // a에서 activity_listview 변경
        listview = (ListView)findViewById(R.id.listview);
        arrayList = new ArrayList();
        save = new HashMap<>();
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, arrayList);
        listview.setAdapter(adapter);





        CheckTypesTask task = new CheckTypesTask();
        task.execute();
    }
    private class CheckTypesTask extends AsyncTask<Void, Void, Void> {
        ProgressDialog asyncDialog = new ProgressDialog(
                OrderRecord.this);
        @Override
        protected void onPreExecute() {
            asyncDialog.setCancelable(false);
            asyncDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            // show dialog
            asyncDialog.show();
            //프로그래스 종료하는 방법
            asyncDialog.dismiss();


            DocumentReference docRef0= dbList.collection("order").document("info0");
            docRef0.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            str0=document.getData().toString();
                            str0 = str0.substring(10,str0.length()-1);
                            strSplit = str0.split("&");
                            str0=strSplit[0];
                            str1=strSplit[1];
                            str2=strSplit[2];
                            str3=strSplit[3];
                            str=str0+"\n"+str1+"\n"+str2+"\n"+str3;
                            arrayList.add(str);
                            Collections.sort(arrayList,Collections.reverseOrder());
                            listview.setAdapter(adapter);
                        } else {
                        }
                    } else {
                    }
                }
            });
            DocumentReference docRef1= dbList.collection("order").document("info1");
            docRef1.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            str0=document.getData().toString();
                            str0 = str0.substring(10,str0.length()-1);
                            strSplit = str0.split("&");
                            str0=strSplit[0];
                            str1=strSplit[1];
                            str2=strSplit[2];
                            str3=strSplit[3];
                            str=str0+"\n"+str1+"\n"+str2+"\n"+str3;
                            arrayList.add(str);
                            Collections.sort(arrayList,Collections.reverseOrder());
                            listview.setAdapter(adapter);
                        } else {
                        }
                    } else {
                    }
                }
            });
            DocumentReference docRef2= dbList.collection("order").document("info2");
            docRef2.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            str0=document.getData().toString();
                            str0 = str0.substring(10,str0.length()-1);
                            strSplit = str0.split("&");
                            str0=strSplit[0];
                            str1=strSplit[1];
                            str2=strSplit[2];
                            str3=strSplit[3];
                            str=str0+"\n"+str1+"\n"+str2+"\n"+str3;
                            arrayList.add(str);
                            Collections.sort(arrayList,Collections.reverseOrder());
                            listview.setAdapter(adapter);
                        } else {
                        }
                    } else {
                    }
                }
            });
            DocumentReference docRef3= dbList.collection("order").document("info3");
            docRef3.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            str0=document.getData().toString();
                            str0 = str0.substring(10,str0.length()-1);
                            strSplit = str0.split("&");
                            str0=strSplit[0];
                            str1=strSplit[1];
                            str2=strSplit[2];
                            str3=strSplit[3];
                            str=str0+"\n"+str1+"\n"+str2+"\n"+str3;
                            arrayList.add(str);
                            Collections.sort(arrayList,Collections.reverseOrder());
                            listview.setAdapter(adapter);
                        } else {
                        }
                    } else {
                    }
                }
            });
            DocumentReference docRef4= dbList.collection("order").document("info4");
            docRef4.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            str0=document.getData().toString();
                            str0 = str0.substring(10,str0.length()-1);
                            strSplit = str0.split("&");
                            str0=strSplit[0];
                            str1=strSplit[1];
                            str2=strSplit[2];
                            str3=strSplit[3];
                            str=str0+"\n"+str1+"\n"+str2+"\n"+str3;
                            arrayList.add(str);
                            Collections.sort(arrayList,Collections.reverseOrder());
                            listview.setAdapter(adapter);
                        } else {
                        }
                    } else {
                    }
                }
            });
            DocumentReference docRef5= dbList.collection("order").document("info5");
            docRef5.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            str0=document.getData().toString();
                            str0 = str0.substring(10,str0.length()-1);
                            strSplit = str0.split("&");
                            str0=strSplit[0];
                            str1=strSplit[1];
                            str2=strSplit[2];
                            str3=strSplit[3];
                            str=str0+"\n"+str1+"\n"+str2+"\n"+str3;
                            arrayList.add(str);
                            Collections.sort(arrayList,Collections.reverseOrder());
                            listview.setAdapter(adapter);
                        } else {
                        }
                    } else {
                    }
                }
            });
            DocumentReference docRef6= dbList.collection("order").document("info6");
            docRef6.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            str0=document.getData().toString();
                            str0 = str0.substring(10,str0.length()-1);
                            strSplit = str0.split("&");
                            str0=strSplit[0];
                            str1=strSplit[1];
                            str2=strSplit[2];
                            str3=strSplit[3];
                            str=str0+"\n"+str1+"\n"+str2+"\n"+str3;
                            arrayList.add(str);
                            Collections.sort(arrayList,Collections.reverseOrder());
                            listview.setAdapter(adapter);
                        } else {
                        }
                    } else {
                    }
                }
            });
            DocumentReference docRef7= dbList.collection("order").document("info7");
            docRef7.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            str0=document.getData().toString();
                            str0 = str0.substring(10,str0.length()-1);
                            strSplit = str0.split("&");
                            str0=strSplit[0];
                            str1=strSplit[1];
                            str2=strSplit[2];
                            str3=strSplit[3];
                            str=str0+"\n"+str1+"\n"+str2+"\n"+str3;
                            arrayList.add(str);
                            Collections.sort(arrayList,Collections.reverseOrder());
                            listview.setAdapter(adapter);
                        } else {
                        }
                    } else {
                    }
                }
            });
            DocumentReference docRef8= dbList.collection("order").document("info8");
            docRef8.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            str0=document.getData().toString();
                            str0 = str0.substring(10,str0.length()-1);
                            strSplit = str0.split("&");
                            str0=strSplit[0];
                            str1=strSplit[1];
                            str2=strSplit[2];
                            str3=strSplit[3];
                            str=str0+"\n"+str1+"\n"+str2+"\n"+str3;
                            arrayList.add(str);
                            Collections.sort(arrayList,Collections.reverseOrder());
                            listview.setAdapter(adapter);
                        } else {
                        }
                    } else {
                    }
                }
            });
            DocumentReference docRef9= dbList.collection("order").document("info9");
            docRef9.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            str0=document.getData().toString();
                            str0 = str0.substring(10,str0.length()-1);
                            strSplit = str0.split("&");
                            str0=strSplit[0];
                            str1=strSplit[1];
                            str2=strSplit[2];
                            str3=strSplit[3];
                            str=str0+"\n"+str1+"\n"+str2+"\n"+str3;
                            arrayList.add(str);
                            Collections.sort(arrayList,Collections.reverseOrder());
                            listview.setAdapter(adapter);
                        } else {
                        }
                    } else {
                    }
                }
            });
            DocumentReference docRef10= dbList.collection("order").document("info10");
            docRef10.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            str0=document.getData().toString();
                            str0 = str0.substring(10,str0.length()-1);
                            strSplit = str0.split("&");
                            str0=strSplit[0];
                            str1=strSplit[1];
                            str2=strSplit[2];
                            str3=strSplit[3];
                            str=str0+"\n"+str1+"\n"+str2+"\n"+str3;
                            arrayList.add(str);
                            Collections.sort(arrayList,Collections.reverseOrder());
                            listview.setAdapter(adapter);
                        } else {
                        }
                    } else {
                    }
                }
            });
            DocumentReference docRef11= dbList.collection("order").document("info11");
            docRef11.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            str0=document.getData().toString();
                            str0 = str0.substring(10,str0.length()-1);
                            strSplit = str0.split("&");
                            str0=strSplit[0];
                            str1=strSplit[1];
                            str2=strSplit[2];
                            str3=strSplit[3];
                            str=str0+"\n"+str1+"\n"+str2+"\n"+str3;
                            arrayList.add(str);
                            Collections.sort(arrayList,Collections.reverseOrder());
                            listview.setAdapter(adapter);
                        } else {
                        }
                    } else {
                    }
                }
            });
            DocumentReference docRef12= dbList.collection("order").document("info12");
            docRef12.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            str0=document.getData().toString();
                            str0 = str0.substring(10,str0.length()-1);
                            strSplit = str0.split("&");
                            str0=strSplit[0];
                            str1=strSplit[1];
                            str2=strSplit[2];
                            str3=strSplit[3];
                            str=str0+"\n"+str1+"\n"+str2+"\n"+str3;
                            arrayList.add(str);
                            Collections.sort(arrayList,Collections.reverseOrder());
                            listview.setAdapter(adapter);
                        } else {
                        }
                    } else {
                    }
                }
            });
            DocumentReference docRef13= dbList.collection("order").document("info13");
            docRef13.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            str0=document.getData().toString();
                            str0 = str0.substring(10,str0.length()-1);
                            strSplit = str0.split("&");
                            str0=strSplit[0];
                            str1=strSplit[1];
                            str2=strSplit[2];
                            str3=strSplit[3];
                            str=str0+"\n"+str1+"\n"+str2+"\n"+str3;
                            arrayList.add(str);
                            Collections.sort(arrayList,Collections.reverseOrder());
                            listview.setAdapter(adapter);
                        } else {
                        }
                    } else {
                    }
                }
            });
            DocumentReference docRef14= dbList.collection("order").document("info14");
            docRef14.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            str0=document.getData().toString();
                            str0 = str0.substring(10,str0.length()-1);
                            strSplit = str0.split("&");
                            str0=strSplit[0];
                            str1=strSplit[1];
                            str2=strSplit[2];
                            str3=strSplit[3];
                            str=str0+"\n"+str1+"\n"+str2+"\n"+str3;
                            arrayList.add(str);
                            Collections.sort(arrayList,Collections.reverseOrder());
                            listview.setAdapter(adapter);
                        } else {
                        }
                    } else {
                    }
                }
            });
            DocumentReference docRef15= dbList.collection("order").document("info15");
            docRef15.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            str0=document.getData().toString();
                            str0 = str0.substring(10,str0.length()-1);
                            strSplit = str0.split("&");
                            str0=strSplit[0];
                            str1=strSplit[1];
                            str2=strSplit[2];
                            str3=strSplit[3];
                            str=str0+"\n"+str1+"\n"+str2+"\n"+str3;
                            arrayList.add(str);
                            Collections.sort(arrayList,Collections.reverseOrder());
                            listview.setAdapter(adapter);
                        } else {
                        }
                    } else {
                    }
                }
            });
            DocumentReference docRef16= dbList.collection("order").document("info16");
            docRef16.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            str0=document.getData().toString();
                            str0 = str0.substring(10,str0.length()-1);
                            strSplit = str0.split("&");
                            str0=strSplit[0];
                            str1=strSplit[1];
                            str2=strSplit[2];
                            str3=strSplit[3];
                            str=str0+"\n"+str1+"\n"+str2+"\n"+str3;
                            arrayList.add(str);
                            Collections.sort(arrayList,Collections.reverseOrder());
                            listview.setAdapter(adapter);
                        } else {
                        }
                    } else {
                    }
                }
            });
            DocumentReference docRef17= dbList.collection("order").document("info17");
            docRef17.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            str0=document.getData().toString();
                            str0 = str0.substring(10,str0.length()-1);
                            strSplit = str0.split("&");
                            str0=strSplit[0];
                            str1=strSplit[1];
                            str2=strSplit[2];
                            str3=strSplit[3];
                            str=str0+"\n"+str1+"\n"+str2+"\n"+str3;
                            arrayList.add(str);
                            Collections.sort(arrayList,Collections.reverseOrder());
                            listview.setAdapter(adapter);
                        } else {
                        }
                    } else {
                    }
                }
            });
            DocumentReference docRef18= dbList.collection("order").document("info18");
            docRef18.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            str0=document.getData().toString();
                            str0 = str0.substring(10,str0.length()-1);
                            strSplit = str0.split("&");
                            str0=strSplit[0];
                            str1=strSplit[1];
                            str2=strSplit[2];
                            str3=strSplit[3];
                            str=str0+"\n"+str1+"\n"+str2+"\n"+str3;
                            arrayList.add(str);
                            Collections.sort(arrayList,Collections.reverseOrder());
                            listview.setAdapter(adapter);
                        } else {
                        }
                    } else {
                    }
                }
            });
            DocumentReference docRef19= dbList.collection("order").document("info19");
            docRef19.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            str0=document.getData().toString();
                            str0 = str0.substring(10,str0.length()-1);
                            strSplit = str0.split("&");
                            str0=strSplit[0];
                            str1=strSplit[1];
                            str2=strSplit[2];
                            str3=strSplit[3];
                            str=str0+"\n"+str1+"\n"+str2+"\n"+str3;
                            arrayList.add(str);
                            Collections.sort(arrayList,Collections.reverseOrder());
                            listview.setAdapter(adapter);
                        } else {
                        }
                    } else {
                    }
                }
            });
            DocumentReference docRef20= dbList.collection("order").document("info20");
            docRef20.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            str0=document.getData().toString();
                            str0 = str0.substring(10,str0.length()-1);
                            strSplit = str0.split("&");
                            str0=strSplit[0];
                            str1=strSplit[1];
                            str2=strSplit[2];
                            str3=strSplit[3];
                            str=str0+"\n"+str1+"\n"+str2+"\n"+str3;
                            arrayList.add(str);
                            Collections.sort(arrayList,Collections.reverseOrder());
                            listview.setAdapter(adapter);
                        } else {
                        }
                    } else {
                    }
                }
            });
            super.onPreExecute();

        }
        // 백그라운드에서 실행
        @Override
        protected Void doInBackground(Void... arg0) {
            return null;
        }
        // 백그라운드가 모두 끝난 후 실행
        @Override
        protected void onPostExecute(Void result) {
            asyncDialog.dismiss();
            super.onPostExecute(result);
        }
    }
}