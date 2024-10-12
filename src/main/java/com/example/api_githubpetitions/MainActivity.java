package com.example.api_githubpetitions;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    TextView output;
    Button btn;
    Button btn2;
    TracksService service;
    int j = 0;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    List<Track> tracks;
    List<String> tracksConId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.output2), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        j = 1;
        output = findViewById(R.id.output);
        btn = findViewById(R.id.btn1);
        btn.setOnClickListener(this);
        btn2 = findViewById(R.id.pon);
        btn2.setOnClickListener(this);



        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080/dsaApp/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(TracksService.class);

        MuestraCanciones();
    }
    @Override
    public void onClick(View V) {
        if(V.getId() == R.id.btn1)
            MuestraCanciones();
        else if(V.getId() == R.id.pon)
        {
            Intent intent = new Intent(MainActivity.this, MainActivity2.class);
            intent.putStringArrayListExtra("TracksConId", (ArrayList<String>) tracksConId);
            startActivity(intent);
        }
    }
    public void MuestraCanciones(){
        Call<List<Track>> call = service.listTracks();
        call.enqueue(new Callback<List<Track>>() {
            @Override
            public void onResponse(Call<List<Track>> call, Response<List<Track>> response) {
                if (response.isSuccessful() && response.body() != null)
                {
                    tracks = response.body();
                    List<String> input = new ArrayList<>();
                    input = getList(tracks);
                    tracksConId = getListConId(tracks);
                    mAdapter = new MyAdapter(input);
                    recyclerView.setAdapter(mAdapter);
                }
                else
                {
                    output.setText("ERROR en la API");
                }
            }
            @Override
            public void onFailure(Call<List<Track>> call, Throwable throwable) {
                String text = call.toString();
                output.setText(call.toString());
                j = j+1;
            }
        });
    }

    public List<String> getList(List<Track> tracks)
    {
        List<String> respuesta = new ArrayList<>();
        for (Track track:tracks)
        {
            String title = track.title;
            String singer = track.singer;
            respuesta.add(title);
            respuesta.add(singer);
        }
        return respuesta;
        
    }
    public List<String> getListConId(List<Track> tracks)
    {
        List<String> respuesta = new ArrayList<>();
        for (Track track:tracks)
        {
            String title = track.title;
            String singer = track.singer;
            String id = track.id;
            respuesta.add(id);
            respuesta.add(singer);
            respuesta.add(title);
        }
        return respuesta;
    }
}
