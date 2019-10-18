package com.example.architectureroomdb;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    /*
No creamos un nuevo VM, sinó instanciariamos uno en cada actividad.
Android le pedimos un VM y el framework se encarga de crear uno o de darnos uno existente
 */
    private NoteViewModel noteViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        final NoteAdapter adapter = new NoteAdapter();
        recyclerView.setAdapter(adapter);


        /**
        le pasamos el scope para que siga su lifecycle = "of(this)"
        le pasamos de que tipo tiene que ser el VM = "get(NoteViewModel.class)"
         */
        noteViewModel = ViewModelProviders.of(this).get(NoteViewModel.class);
        noteViewModel.getAllNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                //update the RecyclerView
                adapter.setNotes(notes);
                Toast.makeText(MainActivity.this, "OnChange", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
