package com.example.struna;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class StartList extends AppCompatActivity {

    private InputStream inputStream;
    private URL url;
    private String stringRequest;
    String token;
    ListView listView;
    String songURL;
    ArrayList<String> mID = new ArrayList<String>();
    ArrayList<String> mTitle = new ArrayList<String>();
    ArrayList<String> mAuthor = new ArrayList<String>();
    ArrayList<String> imageUrl = new ArrayList<String>();
    ArrayList<String> trackUrl = new ArrayList<String>();
    ArrayList<Bitmap> mImages = new ArrayList<Bitmap>();
    ArrayList<String> musicStorage = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_list);

        listView = findViewById(R.id.listView);
        token = getIntent().getStringExtra("USER_TOKEN");
        System.out.println(token);
        try {
            sendGET();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        MyAdapter adapter = new MyAdapter(this, mTitle, mAuthor, mImages);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0){
                    Toast.makeText(StartList.this,"Dobry wybór - 1!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getBaseContext(), Player.class);
                    System.out.println(musicStorage.get(position));
                    intent.putExtra("Choosen_Song", songURL = musicStorage.get(position));
                    intent.putExtra("Background_Song", mImages.get(position));
                    startActivity(intent);
                }
                if (position == 1){
                    Toast.makeText(StartList.this,"Dobry wybór - 2!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getBaseContext(), Player.class);
                    intent.putExtra("Choosen_Song", songURL = musicStorage.get(position));
                    intent.putExtra("Background_Song", mImages.get(position));
                    startActivity(intent);
                }
                if (position == 2){
                    Toast.makeText(StartList.this,"Dobry wybór - 3!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getBaseContext(), Player.class);
                    intent.putExtra("Choosen_Song", songURL = musicStorage.get(position));
                    intent.putExtra("Background_Song", mImages.get(position));
                    startActivity(intent);
                }
                if (position == 3){
                    Toast.makeText(StartList.this,"Dobry wybór - 4!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getBaseContext(), Player.class);
                    intent.putExtra("Choosen_Song", songURL = musicStorage.get(position));
                    intent.putExtra("Background_Song", mImages.get(position));
                    startActivity(intent);
                }
                if (position == 4){
                    Toast.makeText(StartList.this,"Dobry wybór - 5!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getBaseContext(), Player.class);
                    intent.putExtra("Choosen_Song", songURL = musicStorage.get(position));
                    intent.putExtra("Background_Song", mImages.get(position));
                    startActivity(intent);
                }
                if (position == 5){
                    Toast.makeText(StartList.this,"Dobry wybór - 6!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getBaseContext(), Player.class);
                    intent.putExtra("Choosen_Song", songURL = musicStorage.get(position));
                    intent.putExtra("Background_Song", mImages.get(position));
                    startActivity(intent);
                }
                if (position == 6){
                    Toast.makeText(StartList.this,"Dobry wybór - 7!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getBaseContext(), Player.class);
                    intent.putExtra("Choosen_Song", songURL = musicStorage.get(position));
                    intent.putExtra("Background_Song", mImages.get(position));
                    startActivity(intent);
                }
                if (position == 7){
                    Toast.makeText(StartList.this,"Dobry wybór - 8!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getBaseContext(), Player.class);
                    intent.putExtra("Choosen_Song", songURL = musicStorage.get(position));
                    intent.putExtra("Background_Song", mImages.get(position));
                    startActivity(intent);
                }
                if (position == 8){
                    Toast.makeText(StartList.this,"Dobry wybór - 9!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getBaseContext(), Player.class);
                    intent.putExtra("Choosen_Song", songURL = musicStorage.get(position));
                    intent.putExtra("Background_Song", mImages.get(position));
                    startActivity(intent);
                }
            }
        });

    }
    class MyAdapter extends ArrayAdapter<String>{
        Context context;
        ArrayList<String> rTitle;
        ArrayList<Bitmap> rImages;
        ArrayList<String> rAuthor;


        MyAdapter (Context c, ArrayList<String> title, ArrayList<String> author, ArrayList<Bitmap> imgs){
            super(c,R.layout.row, R.id.textView1,title);
            this.context = c;
            this.rTitle = title;
            this.rAuthor = author;
            this.rImages = imgs;

        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.row, parent, false);
            ImageView images = row.findViewById(R.id.image);
            TextView myTitle = row.findViewById(R.id.textView1);
            TextView myDescription = row.findViewById(R.id.textView2);

            images.setImageBitmap(rImages.get(position));
            myTitle.setText(rTitle.get(position));
            myDescription.setText(rAuthor.get(position));
            return row;
        }
    }
    public void sendGET() throws InterruptedException, ExecutionException {

        ExecutorService executor = Executors.newSingleThreadExecutor();

        Callable<Integer> callable = new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                url = new URL("http://185.204.216.100:8080/library/tracks?page=1&perPage=8");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                stringRequest = "Bearer " + token;
                conn.setRequestProperty("Authorization", stringRequest);
                conn.setRequestProperty("Accept", "application/json");
                conn.setDoOutput(false);
                conn.setDoInput(true);
                System.out.println(stringRequest);
                System.out.println(conn.getResponseCode());
                System.out.println(conn.getResponseMessage());
                inputStream = conn.getInputStream();
                readInput(inputStream);
                sendGETimgs(imageUrl);
                setMusicURL(trackUrl);
                return null;
            }
        };
        Future<Integer> future = executor.submit(callable);
        future.get();
        executor.shutdown();
    }
    public void readInput(InputStream inpt) throws IOException, JSONException {
        BufferedReader br = new BufferedReader(new InputStreamReader(inpt));
        StringBuilder builder = new StringBuilder();
        String output;
        while ((output = br.readLine()) != null) {
            builder.append(output);
        }
        System.out.println(builder);
        JSONObject json = new JSONObject(builder.toString());
        for (int i = 0; i < 8; i++) {
            mID.add(json.getJSONArray("track").getJSONObject(i).getString("_id"));
            mTitle.add(json.getJSONArray("track").getJSONObject(i).getString("title"));
            mAuthor.add(json.getJSONArray("track").getJSONObject(i).getString("author"));
            trackUrl.add(json.getJSONArray("track").getJSONObject(i).getString("trackUrl"));
            imageUrl.add(json.getJSONArray("track").getJSONObject(i).getString("imageUrl"));
        }

    }
    public Bitmap resizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }
    public void sendGETimgs(ArrayList<String> imgUrl) throws IOException {
        URL ur;
        Bitmap x;

        for (int i = 0; i < imgUrl.size(); i++) {
            ur = new URL("http://185.204.216.100:8080/" + imgUrl.get(i));
            HttpURLConnection conn = (HttpURLConnection) ur.openConnection();
            conn.setRequestMethod("GET");
            stringRequest = "Bearer " + token;
            conn.setRequestProperty("Authorization", stringRequest);
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoInput(true);
            conn.connect();
            InputStream input = conn.getInputStream();
            x = BitmapFactory.decodeStream(input);
            Bitmap bcgImage = resizedBitmap(x,200);
            mImages.add(bcgImage);
        }
    }
    public void setMusicURL(ArrayList<String> trcUrl) throws IOException {
        String ur;
        for (int i = 0; i < trcUrl.size(); i++) {
            ur = "http://185.204.216.100:8080/" + trcUrl.get(i);
            musicStorage.add(ur);
            System.out.println(musicStorage.get(i));
        }
    }

}
