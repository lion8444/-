package com.psw.schoolwork_06;

import android.app.TabActivity;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.text.SimpleDateFormat;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

//https://webnautes.tistory.com/995
@SuppressWarnings("deprecation")
public class RealConnect extends TabActivity {
    // onActivityResult의 RequestCode 식별자
    private static final int REQUEST_NEW_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;
    final static int BT_MESSAGE_READ = 2;

    // 블루투스 사용 객체
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothDevice bluetoothDevice;
    private Set<BluetoothDevice> bluetoothDeviceSet;
    private BluetoothSocket bluetoothSocket;

    public static InputStream inputStream;
    public static OutputStream outputStream;

    // xml 객체
    private ListView listView_pairing_devices; // 페어링 된 디바이스 리스트뷰 객체
    private int passCurrentPage=0;

    // 일반 객체
    private String device_name;
    private Button button_setting_menu;
    private Button btnOpen;
    private Button btnClose;
    private String sendText;
    private TabHost tabHost;
    private TextView connectName;
    private TextView current;
    private TextView textConnect;


    //이미지 테스트를 위한 객체
    private TextView testText;
    //    private Button btnTest;
    private ImageView recImg;
    private ImageView fireImg;
    private Button btnNext;
    private Button btnPre;
    private int imgs[]={R.drawable.goo,R.drawable.img1,R.drawable.img2,R.drawable.img3,};
    private int imgCount=0;
    private Button btnImg;

    //배송 정보 입력을 위한 객체
    private EditText edtData1;
    private EditText edtData2;
    private EditText edtData3;
    private EditText edtData4;
    private Button btnInput;
    private String data1,data2,data3,data4;
    private SimpleDateFormat mFormat = new SimpleDateFormat("M/d/HH/mm/ss");


    static int bCurrent;

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    DatabaseReference firebase = databaseReference.child("wrok");

    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference = storage.getReference();
    StorageReference pathReference = storageReference.child("photo");

    final FirebaseFirestore db = FirebaseFirestore.getInstance();
    public int count=0;
    int recvCount;
    int intCount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab);

        tabHost = getTabHost();

        TabHost.TabSpec Spec1 = tabHost.newTabSpec("TAG1").setIndicator("연결하기");
        Spec1.setContent(R.id.connect);
        tabHost.addTab(Spec1);
        TabHost.TabSpec Spec2 = tabHost.newTabSpec("TAG2").setIndicator("연결됨");
        Spec2.setContent(R.id.communication);
        tabHost.addTab(Spec2);
        TabHost.TabSpec Spec3 = tabHost.newTabSpec("TAG3").setIndicator("정보입력");
        Spec3.setContent(R.id.inputData);
        tabHost.addTab(Spec3);
        TabHost.TabSpec Spec4 = tabHost.newTabSpec("TAG4").setIndicator("사진");
        Spec4.setContent(R.id.receiveImg);
        tabHost.addTab(Spec4);

        tabHost.setCurrentTab(1);


        btnOpen = (Button)findViewById(R. id.btnOpen);
        btnClose = (Button)findViewById(R.id.btnClose);
        button_setting_menu = (Button)findViewById(R.id.button_setting_menu);
        connectName=(TextView)findViewById(R.id.connectName);
        current = (TextView)findViewById(R.id.current);
        textConnect = (TextView)findViewById(R.id.textConnect);

        //이미지를 위한 테스트
        //     btnTest = (Button)findViewById(R.id.btnImg);
//        testText=(TextView)findViewById(R.id.testText);
//        btnNext = (Button)findViewById(R.id.btnNext);
//        btnPre = (Button)findViewById(R.id.btnPre);
//        recImg= (ImageView)findViewById(R.id.recImg);
        btnImg = (Button)findViewById(R.id.btnImg);
        fireImg= (ImageView)findViewById(R.id.fireImg);
        //배송 정보
        edtData1 = (EditText)findViewById(R.id.edtData1);
        edtData2 = (EditText)findViewById(R.id.edtData2);
//        edtData3 = (EditText)findViewById(R.id.edtData3);
        edtData4 = (EditText)findViewById(R.id.edtData4);
        btnInput = (Button)findViewById(R.id.btnInput);

        edtData1.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        edtData1.setInputType(InputType.TYPE_CLASS_TEXT);
        edtData2.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        edtData2.setInputType(InputType.TYPE_CLASS_TEXT);
//        edtData3.setImeOptions(EditorInfo.IME_ACTION_NEXT);
//        edtData3.setInputType(InputType.TYPE_CLASS_TEXT);
        edtData4.setImeOptions(EditorInfo.IME_ACTION_DONE);
        edtData4.setInputType(InputType.TYPE_CLASS_TEXT);



        Map<String,Object> user = new HashMap<>();

        btnInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                data1=edtData1.getText().toString();
                data2=edtData2.getText().toString();
                data4=edtData4.getText().toString();
                String orderInfo;
                String ordercode;
                String ordertext;
                String ordertime;
                orderInfo = data1;
                ordercode = data2;
                ordertext = data4;
                user.clear();
                user.put("가게이름",orderInfo);
                user.put("배달코드",ordercode);
                user.put("요청사항",ordertext);
                String info= "info";
                count = recvCount;
                saveCount(count);
                String strCount= info+String.valueOf(recvCount);
                if(recvCount==20){
                    recvCount=1;
                }
                Date date = new Date();
                String time = mFormat.format(date);
                String[] timeSplit =time.split("/");
                int intTime = Integer.parseInt(timeSplit[0])+Integer.parseInt(timeSplit[1])+
                        Integer.parseInt(timeSplit[2])+Integer.parseInt(timeSplit[3])
                        +Integer.parseInt(timeSplit[4]);
                //              String strTime = String.valueOf(intTime);
                String strTime = timeSplit[0]+"/"+timeSplit[1]+"  ("+timeSplit[2]
                        +":"+timeSplit[3]+":"+timeSplit[4]+")";

                db.collection("product").document("상품1")
                        .set(user)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(getApplicationContext(), "배달통에 주문 정보를 입력했습니다", Toast.LENGTH_SHORT).show();

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(),"다시 입력해주세요",Toast.LENGTH_SHORT).show();
                            }
                        });

                if(data4.equals("")){
                    ordertext=" ";
                }
                String strOrder = strTime+"&"+orderInfo+"&"+ordercode+"&"+ordertext;
                user.clear();
                user.put("strOrder",strOrder);
                db.collection("order").document(strCount)
                        .set(user)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                count++;
                                saveCount(count);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(),"다시 입력해주세요",Toast.LENGTH_SHORT).show();
                            }
                        });
                edtData1.setText(null);
                edtData2.setText(null);
                edtData4.setText(null);

                saveCount(count);


            }

        });



        //OPEN버튼을 누르면 문자 a를 보내고
        //아두이노에서 문자 a를 받음
        btnOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //밑에 내용과 같은거
//                try {
//                    write("a");
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
                sendText="a";
                try{
                    outputStream.write(sendText.getBytes());
                    current.setText("배달통이 열렸습니다");
                }catch (Exception e){
                    e.printStackTrace();
                }finally {

                }
            }
        });

        //CLOSE 누르면 문자 b를 보내고
        //아두이노에서 문자 b를 받음
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendText="b";
                try{
                    outputStream.write(sendText.getBytes());
                    current.setText("배달통이 닫혔습니다");
                }catch (Exception e){
                    e.printStackTrace();
                }
                finally {

                }
            }
        });

        //이미지 테스트 페이지
        btnImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(pathReference==null){
                    Toast.makeText(getApplicationContext(), "이미지가 없습니다", Toast.LENGTH_SHORT).show();
                }else{
                    StorageReference submitPorfile = storageReference.child("photo/img1.png");
                    submitPorfile.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Glide.with(RealConnect.this).load(uri).into(fireImg);
                            Toast.makeText(getApplicationContext(), "사진을 가져왔습니다", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });


        //
        //
        //여기서부터 블루투스 연결하는 코드
        //
        //
        //

        // 리스트 뷰 객체와 xml id 연결
        listView_pairing_devices = (ListView)findViewById(R.id.listview_pairing_devices);

        // "디바이스 신규 등록" 버튼 클릭 이벤트
        button_setting_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 블루투스 설정 화면 띄워주기
                Intent intent = new Intent(Settings.ACTION_BLUETOOTH_SETTINGS);
                // 종료 후 onActivityResult 호출, requestCode가 넘겨짐
                startActivityForResult(intent, REQUEST_NEW_DEVICE);
            }
        });
        // 블루투스 활성화 확인 함수 호출
        checkBluetooth();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            // 블루투스 설정화면 종료 시
            case REQUEST_NEW_DEVICE :
                checkBluetooth();
                break;
            // 블루투스 활성화 선택 종료 시
            case REQUEST_ENABLE_BT :
                tabHost.setCurrentTab(0);
                // 활성화 버튼을 눌렀을 때
                if(resultCode == RESULT_OK) {
                    selectDevice();
                }
                // 취소 버튼을 눌렀을 때
                else if(resultCode == RESULT_CANCELED) {
                    Toast.makeText(getApplicationContext(), "블루투스를 활성화 하지 않아 앱을 종료합니다.", Toast.LENGTH_LONG).show();
                    finish();
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    // 블루투스 활성화 확인 함수
    public void checkBluetooth() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        // 단말기가 블루투스를 지원하지 않을 때
        if(bluetoothAdapter == null) {
            Toast.makeText(getApplicationContext(), "단말기가 블루투스를 지원하지 않습니다.", Toast.LENGTH_LONG).show();
            finish();
        }
        // 블루투스를 지원할 때
        else {
            // 블루투스가 활성화 상태
            if(bluetoothAdapter.isEnabled()) {
                selectDevice();
            }
            else {
                // 블루투스를 활성화
                Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(intent, REQUEST_ENABLE_BT);
            }
        }
    }

    // 블루투스 선택 함수
    //adapter.notifyDataSetChanged(); 를 사용하면 페어링된 항목들이 변화 할때 변경된 내용을 보여주는거 같음
    //https://ywook.tistory.com/13 참고 사이트
    public void selectDevice() {
        // 페어링 된 디바이스 목록을 불러옴
        bluetoothDeviceSet = bluetoothAdapter.getBondedDevices();
        // 리스트를 만듬
        List<String> list = new ArrayList<>();
        for(BluetoothDevice bluetoothDevice : bluetoothDeviceSet) {
            list.add(bluetoothDevice.getName());
        }
        // 어레이어뎁터로 리스트뷰에 리스트를 생성
        final ArrayAdapter arrayAdapter = new ArrayAdapter(RealConnect.this, android.R.layout.simple_list_item_1, list);
        listView_pairing_devices.setAdapter(arrayAdapter);
        listView_pairing_devices.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            // 리스트 뷰의 아이템을 클릭했을 때
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                device_name = arrayAdapter.getItem(position).toString();
                // 프로그레스 다이얼로그 생성
                CheckTypesTask task = new CheckTypesTask();
                task.execute();
            }
        });
    }

    // 클릭 된 디바이스와 연결하는 함수
    public void connectDevice(String deviceName) {
        // 블루투스 연결 할 디바이스를 찾음
        for(BluetoothDevice device : bluetoothDeviceSet) {
            if(deviceName.equals(device.getName())) {
                bluetoothDevice = device;
                break;
            }
        }
        // UUID 생성
        UUID uuid = java.util.UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
        try {
            // 블루투스 소켓 생성
            bluetoothSocket = bluetoothDevice.createInsecureRfcommSocketToServiceRecord(uuid);
            bluetoothSocket.connect();
            // 데이터 받기 위해 인풋스트림 생성
            inputStream = bluetoothSocket.getInputStream();
            outputStream = bluetoothSocket.getOutputStream();
            bCurrent=1;
            ConnectedBluetoothThread c = new ConnectedBluetoothThread(bluetoothSocket);
            c.start();
        }
        catch (Exception e) {
            e.printStackTrace();
            // 쓰레드에서 UI처리를 위한 핸들러
            Message msg = handler.obtainMessage();
            handler.sendMessage(msg);
            handler.sendEmptyMessage(-1);
        }
    }
    private class ConnectedBluetoothThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedBluetoothThread(BluetoothSocket socket) {
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;
            handler.sendEmptyMessage(1);
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), "소켓 연결 중 오류가 발생했습니다.", Toast.LENGTH_LONG).show();
            }
            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }
        public void run() {
            byte[] buffer = new byte[1024];
            int bytes;

            while(true) {
                try {
                    bytes = inputStream.available();
                    if (bytes != 0) {
                        SystemClock.sleep(100);
                        bytes = inputStream.available();
                        bytes = inputStream.read(buffer, 0, bytes);
                        handler.obtainMessage(BT_MESSAGE_READ, bytes, -1, buffer).sendToTarget();
                    }
                } catch (IOException e) {
                    break;
                }finally {
                }
            }
        }
        public void write(String str) {
            byte[] bytes = str.getBytes();
            try {
                mmOutStream.write(bytes);
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), "데이터 전송 중 오류가 발생했습니다.", Toast.LENGTH_LONG).show();
            }
        }
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), "소켓 해제 중 오류가 발생했습니다.", Toast.LENGTH_LONG).show();
            }
        }
    }
    public void write(String str) throws IOException {
        byte[] bytes = str.getBytes();
        try {
            outputStream.write(bytes);
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), "데이터 전송 중 오류가 발생했습니다.", Toast.LENGTH_LONG).show();
        }finally {
        }
    }
    private class recv extends Thread {
        public void run() {
            byte[] buffer = new byte[1024];
            int bytes;
            while (true) {
                try {
                    bytes = inputStream.available();
                    if (bytes != 0) {
                        SystemClock.sleep(100);
                        bytes = inputStream.available();
                        bytes = inputStream.read(buffer, 0, bytes);
                        handler.obtainMessage(BT_MESSAGE_READ, bytes, -1, buffer).sendToTarget();
                    }
                } catch (IOException e) {
                    break;
                } finally {

                }
            }
        }
    }

    // 핸들러 선언
    // 앱 종료 버튼을 눌러 종료 해도 연결은 유지 되지만
    // device_name이 초기화 되어서 연결 됬다는 메시지가 안나옴  수정해야함
    final Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if(msg.what==1) {
                Toast.makeText(getApplicationContext(), device_name + "과/와 연결되었습니다.", Toast.LENGTH_SHORT).show();
                SharedPreferences dName= getSharedPreferences("name", MODE_PRIVATE); //display1 이름의 기본모드 설장
                SharedPreferences.Editor nameSave= dName.edit(); // SharedPreferences를 제어할 editor1 선언
                nameSave.putString("nameSave1",device_name); // 키,벨류 형식으로 저장
                nameSave.commit(); //저장
                connectName.setText(device_name + "과/와 연결되었습니다");
                textConnect.setText(device_name + "과/와 연결되었습니다");
                passCurrentPage=1;
                tabHost.setCurrentTab(passCurrentPage);
            }
            if(msg.what==-1){
                Toast.makeText(getApplicationContext(), device_name + "블루투스 연결을 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                connectName.setText(device_name + "과/와 다시 연결해주세요");
            }

        }
    };
    // 프로그레스 다이얼로그 생성
    private class CheckTypesTask extends AsyncTask<Void, Void, Void> {
        ProgressDialog asyncDialog = new ProgressDialog(
                RealConnect.this);
        @Override
        protected void onPreExecute() {
            asyncDialog.setCancelable(false);
            asyncDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            asyncDialog.setMessage(device_name + "과/와 연결중입니다.");
            // show dialog
            asyncDialog.show();
            //프로그래스 종료하는 방법
//            asyncDialog.dismiss();
            super.onPreExecute();
        }
        // 백그라운드에서 실행
        @Override
        protected Void doInBackground(Void... arg0) {
            connectDevice(device_name);
//            connectName.setText(device_name + "과/와 연결되었습니다");
            return null;
        }
        // 백그라운드가 모두 끝난 후 실행
        @Override
        protected void onPostExecute(Void result) {
            asyncDialog.dismiss();
            super.onPostExecute(result);
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences sharedData1= getSharedPreferences("infoCountx", MODE_PRIVATE); //display1 키값이 있다면 값을 불러옴
        recvCount = sharedData1.getInt("countax",0);
        if(bCurrent==1){
            SharedPreferences nameData= getSharedPreferences("name", MODE_PRIVATE); //display1 키값이 있다면 값을 불러옴
            String nameData1 = nameData.getString("nameSave1","");
            connectName.setText(nameData1 + "과/와 연결되었습니다");
            textConnect.setText(nameData1 + "과/와 연결되었습니다");
            recv re = new recv();
            re.start();
        }
        else
            return;

    }

    @Override
    protected void onStop() {
        //  saveCount(count);
        SharedPreferences sharedCount= getSharedPreferences("infoCountx", MODE_PRIVATE); //display1 이름의 기본모드 설장
        SharedPreferences.Editor editorCountx= sharedCount.edit(); // SharedPreferences를 제어할 editor1 선언
        editorCountx.putInt("countax", count); // 키,벨류 형식으로 저장
        editorCountx.commit(); //저장


        super.onStop();
    }

    private void saveCount(int count){

        if(count==21){
            count=0;
        }
        SharedPreferences sharedCount= getSharedPreferences("infoCountx", MODE_PRIVATE); //display1 이름의 기본모드 설장
        SharedPreferences.Editor editorCountx= sharedCount.edit(); // SharedPreferences를 제어할 editor1 선언
        editorCountx.putInt("countax", count); // 키,벨류 형식으로 저장
        editorCountx.commit(); //저장

        SharedPreferences sharedData1= getSharedPreferences("infoCountx", MODE_PRIVATE); //display1 키값이 있다면 값을 불러옴
        recvCount = sharedData1.getInt("countax",0);
        count = recvCount;

    }
}