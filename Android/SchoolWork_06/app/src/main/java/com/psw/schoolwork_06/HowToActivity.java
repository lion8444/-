package com.psw.schoolwork_06;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class HowToActivity extends AppCompatActivity {
    private Button btnTest;
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.a);
        btnTest= (Button)findViewById(R.id.btnImg);
        Toast.makeText(this.getApplicationContext(), "사용방법 버튼 클릭", Toast.LENGTH_SHORT).show();

        btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"테스트 버튼 클릭",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
