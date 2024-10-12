package com.example.api_githubpetitions;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity2 extends AppCompatActivity implements View.OnClickListener {
    Button btnAdd;
    Button btnDel;
    Button close;
    Button btnFix;
    TextView title;
    TextView author;
    TracksService service;
    TextView output3;
    List<Track> tracks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main2);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.output2), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Intent intent = getIntent();
        ArrayList<String> ListaDeTracks =  intent.getStringArrayListExtra("TracksConId");
        tracks = setListaTracks(ListaDeTracks);
        title = findViewById(R.id.titleBox);
        author = findViewById(R.id.authorBox);
        btnAdd = findViewById(R.id.btnAdd);
        btnDel = findViewById(R.id.btnDel);
        btnFix = findViewById(R.id.btnFix);
        btnAdd.setOnClickListener(this);
        btnDel.setOnClickListener(this);
        btnFix.setOnClickListener(this);
        output3 = findViewById(R.id.output3);
        close = findViewById(R.id.close);
        close.setOnClickListener(this);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080/dsaApp/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(TracksService.class);

    }


    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btnAdd)
        {
            Track track = new Track("0",author.getText().toString(),title.getText().toString());
            PostTrack(track);
            tracks.add(track);
        }
        else if(v.getId() == R.id.btnDel)
            DeleteTrack(title.getText().toString());
        else if (v.getId() == R.id.close)
            finish();
        else if(v.getId() == R.id.btnFix)
            PutTrack(author.getText().toString(), title.getText().toString());
    }

    public void PostTrack(Track track) {
        Call<Void> call = service.postTrack(track);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                output3.setText("Song has been added");
            }

            @Override
            public void onFailure(Call<Void> call, Throwable throwable) {
                output3.setText("ERROR");
            }
        });
    }

    public void DeleteTrack(String title)
    {
        String id = null;
        int num = 0;
        for(Track track: tracks)
        {
            if(track.title.equals(title))
            {
                id = track.id;
            }
            else
                num = num + 1;
        }
        if(id != null)
        {
            Call<Void> call = service.deleteTrack(id);
            int finalNum = num;
            call.enqueue(new Callback<Void>() {

                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    output3.setText("Song has been deleted");
                    tracks.remove(finalNum);
                }

                @Override
                public void onFailure(Call<Void> call, Throwable throwable) {
                    output3.setText("ERROR");
                }
            });
        }
        else
            output3.setText("ERROR");
    }
    public List<Track> setListaTracks(List<String> tracks)
    {
        List<Track> lista = new ArrayList<>();;
        int i = 0;
        while(i < tracks.size()-2)
        {
            Track track = new Track(tracks.get(i), tracks.get(i+1), tracks.get(i+2));
            lista.add(track);
            i = i+3;
        }
        return lista;
    }
    public void PutTrack(String author, String title) {
        boolean encontrado = false;
        int num = 0;
        for(Track track: tracks)
        {
            if(track.title.equals(title))
            {
                track.singer = author;
                encontrado = true;
            }
            else if(track.singer.equals(author))
            {
                track.title = title;
                encontrado = true;
            }
            else
                num = num + 1;
        }
        if(encontrado)
        {
            int finalNum = num;
            Call<Void> call = service.putTrack(tracks.get(num));
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    output3.setText("Song has been fixed");
                }

                @Override
                public void onFailure(Call<Void> call, Throwable throwable) {
                    output3.setText("ERROR");
                }
            });
        }
        else
            output3.setText("The entered parameters do not exist");

    }
    
}

