package com.soma.nunu.oneto;

import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;
import android.widget.ListView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by user on 2016-07-21.
 */
public class search_list extends Activity {
    int num=1;
    ListView listview;
    private ArrayList<user> users = new ArrayList<user>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_list);
        listview= (ListView)findViewById(R.id.list);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        String result = requestRank();
        //JsonToJava(result);

        try {
            JSONArray jarray = new JSONArray(result);   // JSONArray 생성
            for(int i=0; i < jarray.length(); i++){
                JSONObject jObject = jarray.getJSONObject(i);  // JSONObject 추출
                String name = jObject.getString("name");
                String time = jObject.getString("time");
                String Snum = String.valueOf(num);

                users.add(new user(Snum,name,time));
                num++;
            }
            num=1;
        } catch (JSONException e) {
            e.printStackTrace();
        }



        listview.setAdapter(new DataAdapter(this, users));

    }


    private String requestRank() {
        String url = "http://52.78.100.250:8080/soma/list.jsp";
        HttpClient http = new DefaultHttpClient();
        String result = null;


        try{
            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

            HttpParams params = http.getParams();
            HttpConnectionParams.setConnectionTimeout(params, 5000);
            HttpConnectionParams.setSoTimeout(params, 5000);

            HttpPost httpPost = new HttpPost(url);
            UrlEncodedFormEntity entityRequest = new UrlEncodedFormEntity(nameValuePairs, "EUC-KR");

            httpPost.setEntity(entityRequest);

            HttpResponse responsePost = http.execute(httpPost);
            HttpEntity resEntity = responsePost.getEntity();

            result = EntityUtils.toString(resEntity);

        }
        catch(Exception ig){

        }
        return result;
    }


}
