package com.soma.nunu.oneto;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.util.ArrayList;
import java.util.Random;

public class Game extends Activity implements View.OnClickListener, Runnable {

    private Button btn_start = null;            // 시작
    private Button btns[] = new Button[25];     // 숫자 리스트
    private TextView Time   = null;             // 시간
    private TextView Next   = null;
    private TextView Next_num   = null;         // 다음 숫자
    private Dialog input_dlg = null;            // 팝업
    private StopWatch   Swatch        = null;   // 스탑워치
    private int Num[] = new int[25];            // 숫자 리스트
    private int Order = 1;
    private boolean State = false;              // 시작 상태
    private String strTime2=null;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);


        SetBinddingButtons();                   // 액티비티 버튼 바인딩

        Order = 1;
        State = false;                          // 게임시작 X

        // 시간 텍스트 세팅
        Time = (TextView)findViewById(R.id.time);
        Time.setText("00:00:00");
        Time.setTextColor(Color.RED);

        // 다음 숫자 세팅
        Next = (TextView)findViewById(R.id.next);
        Next.setVisibility(View.GONE);
        Next_num = (TextView)findViewById(R.id.next_num);
        Next_num.setVisibility(View.GONE);
        Next_num.setText("1");

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    //숫자 배열 초기화
    public void initNumberArr(int nStartNum){
        for(int i=0; i< 25; i++){
            Num[i] = i+ nStartNum;
        }
    }

    //숫자 랜덤 섞기
    public void shakeNumber(){

        int x   = 0;
        int y   = 0;
        int temp = 0;

        Random _ran = new Random();

        for(int i=0; i< 100; i++){
            x = _ran.nextInt(24);
            y = _ran.nextInt(24);

            if(x == y) continue;

            temp = Num[x];
            Num[x] = Num[y];
            Num[y] = temp;
        }
    }

    public void run() {

        // 게임 시작 상태일때만
        while(State)
        {

            handler.sendEmptyMessage(0);

            try{
                Thread.sleep(50);
            }catch(Exception ig){
                ig.printStackTrace();
            }
        }
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {

            double ell = Swatch.getFormatF();
            String strTime = String.format("%02d:%02d:%02d", (int) (ell / 60),
                    (int)(ell% 60), (int)((ell *100) % 100));
            strTime2 = String.format("%02d%02d%02d", (int) (ell / 60),
                    (int)(ell% 60), (int)((ell *100) % 100));
            Time.setText(strTime);
            super.handleMessage(msg);

        }
    };

    public void onSucc(){
        ShowDialog();
    }

    public void ShowDialog(){
        input_dlg = new Dialog(this);
        input_dlg.setContentView(R.layout.pop_up);
        input_dlg.setTitle("Success!");
        ((EditText)input_dlg.findViewById(R.id.editName)).setOnKeyListener(on_key);;
        input_dlg.show();
    }

    public View.OnKeyListener on_key = new View.OnKeyListener(){

        public boolean onKey(View v, int keyCode, KeyEvent event) {

            if(event.getAction() == KeyEvent.ACTION_DOWN ){
                if(keyCode == KeyEvent.KEYCODE_ENTER){
                    String url = "http://52.78.100.250:8080/soma/insert.jsp";
                    HttpClient http = new DefaultHttpClient();

                    try{
                        String playerName = ((EditText)input_dlg.findViewById(R.id.editName)).getText().toString();
                        //String strTime = Time.getText().toString();

                        if(playerName == null || playerName.length() == 0){
                            playerName = "Unknown Player";
                        }

                        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                        nameValuePairs.add(new BasicNameValuePair("name", playerName));
                        nameValuePairs.add(new BasicNameValuePair("time", strTime2));

                        HttpParams params = http.getParams();
                        HttpConnectionParams.setConnectionTimeout(params, 5000);
                        HttpConnectionParams.setSoTimeout(params, 5000);

                        HttpPost httpPost = new HttpPost(url);
                        UrlEncodedFormEntity entityRequest = new UrlEncodedFormEntity(nameValuePairs, "EUC-KR");

                        httpPost.setEntity(entityRequest);

                        HttpResponse responsePost = http.execute(httpPost);
                        HttpEntity resEntity = responsePost.getEntity();

                        Swatch.reset();
                    }catch(Exception ig){

                    }

                    input_dlg.dismiss();
                    InitValue();

                    intent = new Intent(getBaseContext(), intro.class);
                    startActivityForResult(intent, 1);

                    return true;
                }
            }

            return false;
        }

    };


    private void SetBinddingButtons() {

        //스타트 버튼 설정
        btn_start = (Button) findViewById(R.id.btn_start);
        btn_start.setOnClickListener(this);

        //시간 텍스트 설정
        Time = (TextView)findViewById(R.id.time);
        Time.setText("00:00:00");
        Time.setTextColor(Color.RED);

        //스탑워치 실행
        Swatch = new StopWatch();

        for (int i = 0; i < 25; i++) {
            btns[i] = (Button) findViewById(R.id.btn1 + i);
            btns[i].setOnClickListener(this);
            btns[i].setVisibility(View.INVISIBLE);
            btns[i].setTextSize(20);
        }


    }

    public void InitValue(){
        Order = 1;
        State = false;
        //btn_start.setText("Start");
        Time.setText("00:00:00");
    }

    public void onClick(View v) {

        // 스타트 버튼 클릭 시
        if(v == btn_start){

            // 게임이 진행되고 있다면
            if(!State){
                InitValue();
                Swatch.stop();
                Swatch.reset();
            }

            // 상태를 게임 실행으로 바꾸어 줌
            State = !State;

            if(State) {

                // 배열 생성 후 섞기
                initNumberArr(1);
                shakeNumber();

                for(int i=0; i< 5; i++){
                    for(int j=0; j< 5; j++){
                        btns[i*5 + j].setVisibility(View.VISIBLE);
                        btns[i*5 + j].layout(i*60+6, j*60, i*60+6+60, j*60+60);
                        btns[i*5 + j].setText("" + Num[i*5 + j]);
                        btns[i*5 + j].setGravity(Gravity.CENTER_HORIZONTAL |Gravity.CENTER_VERTICAL);
                    }
                }

                Next.setVisibility(View.VISIBLE);
                Next_num.setVisibility(View.VISIBLE);
                btn_start.setBackground(getDrawable(R.drawable.stop));

                new Thread(this).start();
                Swatch.start();

                // 데이터 섞음
                initNumberArr(26);
                shakeNumber();
            }

            // 상태에 따라서 버튼 글자 설정
            btn_start.setBackground(State?getDrawable(R.drawable.stop):getDrawable(R.drawable.play));
        }

        else{
            int x = Integer.parseInt(((Button)v).getText().toString());

            if(x == Order){
                if(Order >= 26){
                    ((Button)v).setVisibility(View.INVISIBLE);
                }
                else{
                    ((Button)v).setText(""+ Num[25 - Order]);
                }

                Next_num.setText(""+(Order+1));
                Order++;
            }

            if(Order == 51)
            {
                State = false;
                Next_num.setText("1");
                btn_start.setBackground(getDrawable(R.drawable.play));
                Swatch.stop();
                onSucc();
            }
        }
    }
}