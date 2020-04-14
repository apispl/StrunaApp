package com.example.struna;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Register extends AppCompatActivity {

    private EditText email1, password1;
    private Button login;
    private URL url;
    int status;
    private InputStream inputStream;
    private String token;
    private String name;
    private String message;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        email1 = findViewById(R.id.email1);
        password1 = findViewById(R.id.password1);
        login = (Button) findViewById(R.id.login);
        final RequestQueue queue = Volley.newRequestQueue(this);
        login.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                try {
                    sendPOST();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
                System.out.println("Status odpowiedzi to:" + status);
                System.out.println(message);
                System.out.println(token);

                System.out.println(name);

                if (message == null){
                    Toast toast = Toast.makeText(getApplicationContext(), "Zalogowano poprawnie!",Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                    Intent intent = new Intent(getBaseContext(), StartList.class);
                    intent.putExtra("USER_TOKEN", token);
                    startActivity(intent);
                }
                else{
                    switch (message){
                        case "User doesn't exist":
                            Toast toast1 = Toast.makeText(getApplicationContext(), "Użytkownik nie istnieje, zarejestruj się!", Toast.LENGTH_SHORT);
                            toast1.setGravity(Gravity.CENTER,0,0);
                            toast1.show();
                            break;
                        case "Wrong password!":
                            Toast toast2 = Toast.makeText(getApplicationContext(), "Niepoprawne hasło!", Toast.LENGTH_SHORT);
                            toast2.setGravity(Gravity.CENTER,0,0);
                            toast2.show();
                            break;
                    }
                }


            }
        });
    }
    public void sendPOST() throws InterruptedException, ExecutionException {

        ExecutorService executor = Executors.newSingleThreadExecutor();

        Callable<Integer> callable = new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                url = new URL("http://185.204.216.100:8080/auth/login");
                JSONObject jsonParam = new JSONObject();

                jsonParam.put("email", email1.getText().toString());
                jsonParam.put("password", password1.getText().toString());
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
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
            System.out.println("Input stream NORMAL");
            return inputStream;
        }
        if (stat == 401) {
            inputStream = inConn.getErrorStream();
            System.out.println("Input stream <HTTP_BAD_REQUEST");
            return inputStream;
        } else {
            inputStream = inConn.getErrorStream();
            System.out.println("Nieznany błąd!");
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
            token = json.getString("token");
            name = json.getString("userName");
            System.out.println(token);
            System.out.println(name);

        }
        if (stat == 401) {
            JSONObject json = new JSONObject(builder.toString());
            message = json.getString("message");
            System.out.println(message);
        }
    }

}
