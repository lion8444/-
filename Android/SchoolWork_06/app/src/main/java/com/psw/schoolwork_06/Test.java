package com.psw.schoolwork_06;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.w3c.dom.Text;

import java.io.IOException;
//https://developer.android.com/s/results?hl=ko&q=%ED%81%AC%EB%A1%A4%EB%A7%81
//크롤링
//https://wonpaper.tistory.com/111
//크롤링 좋은 예시
//https://todaycode.tistory.com/5
//셀레니움 크롤링
//https://seungsulee.tistory.com/24
//모바일에서는 셀레니움 크롤링이 불가능 하다
//https://developer.android.com/studio/write/app-link-indexing?hl=ko
//https://best421.tistory.com/83
//안드로이드 앱 링크 추가
//데이터를 가져올수 있는 방법인듯...
//https://m.blog.naver.com/wisestone2007/221006801354
//ui automator
public class Test extends AppCompatActivity {
    String text;
    TextView textView;
    TextView testc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);

        testc = (TextView) findViewById(R.id.testc);

        final Bundle bundle = new Bundle();

        new Thread(){
            @Override
            public void run() {
                Document doc = null;
                try {
//                    doc = Jsoup.connect("https://mc.coupang.com/ssr/desktop/order/list").get();
//                    Elements contents = doc.select("div.abukv2-1 fpOWPc");
//                    text +=contents.text();
//                    contents = doc.select("span.sc-755zt3-0 lcvccu-0 crvMkb lkVlhg");
//                    text +=contents.text();
//                    contents = doc.select("span.sc-755zt3-1 sc-8q24ha-1 kGEiDX gJmRLG");
//                    text +=contents.text();
//
//                    bundle.putString("texts", text);                               //핸들러를 이용해서 Thread()에서 가져온 데이터를 메인 쓰레드에 보내준다.
//                    Message msg = handler.obtainMessage();
//                    msg.setData(bundle);
//                    handler.sendMessage(msg);

                    doc = Jsoup.connect("https://weather.naver.com/").get();
                    Elements contents = doc.select("strong.location_name");          //회차 id값 가져오기
                    text += contents.text();
                    contents = doc.select("strong.current");
                    text +=contents.text();
                    contents = doc.select("span.weather.before_slash");
                    text +=contents.text();

         //           text += doc.select("#bnusNo").text();                   //보너스 번호 contents 변수를 사용하지 않고 가져오는 방법

                    bundle.putString("texts", text);                               //핸들러를 이용해서 Thread()에서 가져온 데이터를 메인 쓰레드에 보내준다.
                    Message msg = handler.obtainMessage();
                    msg.setData(bundle);
                    handler.sendMessage(msg);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
  //          textView.setText(bundle.getString("numbers"));                      //이런식으로 View를 메인 쓰레드에서 뿌려줘야한다.
            testc.setText(bundle.getString("texts"));
        }
    };
}

