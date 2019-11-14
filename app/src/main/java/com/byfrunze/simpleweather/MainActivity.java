package com.byfrunze.simpleweather;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    private StringBuilder url = new StringBuilder("https://api.darksky.net/forecast/f8a0038abd87cda1f75b4536b85929cc/");
    private EditText editTextLong;
    private EditText editTextLati;
    private TextView textViewInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextLati = findViewById(R.id.editTextLati);
        editTextLong = findViewById(R.id.editTextLong);
        textViewInfo = findViewById(R.id.textViewRes);

    }

    public void onClickGetRes(View view) {
        String ss = url.toString() +  editTextLati.getText().toString() + "," + editTextLong.getText().toString();
        Log.i("SS", ss);
        DownloadInfoWeather downloadInfoWeather = new DownloadInfoWeather();
                downloadInfoWeather.execute(ss);

    }

    private class DownloadInfoWeather extends AsyncTask<String, Void, String>{
        @Override
        protected String doInBackground(String... strings) {
            URL url = null;
            HttpURLConnection urlConnection = null;
            StringBuilder res = new StringBuilder();
            try {
                url = new URL(strings[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = urlConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader reader = new BufferedReader(inputStreamReader);
                String line = reader.readLine();
                while( line != null){
                    res.append(line);
                    line = reader.readLine();
                }
                return res.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if(urlConnection != null){
                    urlConnection.disconnect();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                Log.i("AS", jsonObject.toString());
                String city = jsonObject.getString("timezone");
                String time = jsonObject.getJSONObject("currently").getString("time");
                String temp = jsonObject.getJSONObject("currently").getString("temperature");
                String windSpeed = jsonObject.getJSONObject("currently").getString("windSpeed");
                textViewInfo.setText("City - " + city +
                                    "\ntime - " + time +
                                    "\ntemp - " + temp +
                                    "\nwindSpeed - " + windSpeed);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}