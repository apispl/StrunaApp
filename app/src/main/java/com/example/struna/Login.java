package com.example.struna;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Login extends AppCompatActivity {

    private EditText name, email, password;
    private Button rejestruj;
    private URL url;
    private volatile int status;
    private InputStream inputStream;
    private String message;
    private String userId;
    private String value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        rejestruj = (Button) findViewById(R.id.zarejestruj1);

        rejestruj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    sendPut();
                } catch (InterruptedException | ExecutionException e) {
                    System.out.println("sendPost - PROBLEM!");
                }
                System.out.println("Status odpowiedzi to:" + status);
                System.out.println(message);
                System.out.println(userId);
                System.out.println(value);

                switch (message){
                    case "User created!":
                        Toast toast = Toast.makeText(getApplicationContext(), "Zarejestrowano użytkownika!",Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER,0,0);
                        toast.show();
                        startActivity(new Intent(getApplicationContext(), Register.class));
                        break;
                    case "Validation failed.":
                        Toast toast1 = Toast.makeText(getApplicationContext(), "Dane nie poprawne!!!", Toast.LENGTH_SHORT);
                        toast1.setGravity(Gravity.CENTER,0,0);
                        toast1.show();
                        break;
                    case "Please enter a valid email.":
                        Toast toast2 = Toast.makeText(getApplicationContext(), "Dane nie poprawne!!! Sprawdź email!", Toast.LENGTH_SHORT);
                        toast2.setGravity(Gravity.CENTER,0,0);
                        toast2.show();
                        break;
                    case "Email address exists!":
                        Toast toast3 = Toast.makeText(getApplicationContext(), "Adres e-mail: " + value + " Jest już zarejestrowany!", Toast.LENGTH_LONG);
                        toast3.setGravity(Gravity.CENTER,0,0);
                        toast3.show();
                        startActivity(new Intent(getApplicationContext(), Register.class));
                        break;
                }

            }

        });

    }

    public void sendPut() throws InterruptedException, ExecutionException {

        ExecutorService executor = Executors.newSingleThreadExecutor();

        Callable<Integer> callable = new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                url = new URL("http://185.204.216.100:8080/auth/signup");
                JSONObject jsonParam = new JSONObject();

                jsonParam.put("email", email.getText().toString());
                jsonParam.put("password", password.getText().toString());
                jsonParam.put("name", name.getText().toString());
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("PUT");
                conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                conn.setRequestProperty("Accept", "application/json");
                conn.setDoOutput(true);
                conn.setDoInput(true);
                sendJSONrequest(conn,jsonParam);
                System.out.println(status);
                whichInput(status,conn);
                readInput(inputStream,status);
                return status;
            }
        };
        Future<Integer> future = executor.submit(callable);
        future.get();
        executor.shutdown();
    }

    public int sendJSONrequest(HttpURLConnection inConn, JSONObject jsonRequest) throws IOException {
        DataOutputStream os = new DataOutputStream(inConn.getOutputStream());
        os.writeBytes(jsonRequest.toString());
        status = inConn.getResponseCode();
        return status;
    }

    public InputStream whichInput(int stat, HttpURLConnection inConn) throws IOException {
        if (stat < HttpURLConnection.HTTP_BAD_REQUEST) {
            inputStream = inConn.getInputStream();
            return inputStream;
        }
        if (stat == 422) {
            inputStream = inConn.getErrorStream();
            return inputStream;
        } else {
            inputStream = inConn.getErrorStream();
            return inputStream;
        }
    }
    public void readInput(InputStream inpt, int stat) throws IOException, JSONException {
        BufferedReader br = new BufferedReader(new InputStreamReader(inpt));
        StringBuilder builder = new StringBuilder();
        String output;
        while ((output = br.readLine()) != null) {
            builder.append(output);
        }
        System.out.println(builder);
        if (stat < HttpURLConnection.HTTP_BAD_REQUEST) {
            JSONObject json = new JSONObject(builder.toString());
            message = json.getString("message");
            userId = json.getString("userId");
        }
        if (stat == 422) {
            JSONObject json = new JSONObject(builder.toString());
            message = json.getJSONArray("data").getJSONObject(0).getString("msg");
            value = json.getJSONArray("data").getJSONObject(0).getString("value");
        }
    }
}