package com.example.architectureroomdb;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;


// DB Class

@Database(entities = {Note.class}, version = 1)
public abstract class NoteDB extends RoomDatabase {

    private static NoteDB instance;

    public abstract NoteDao noteDao();

    public static synchronized NoteDB getInstance(Context ctx) {
        if (instance == null) {
            instance = Room.databaseBuilder(ctx.getApplicationContext(),
                    NoteDB.class, "note_database")
                    //method when dont need to handle migration (Si no fuera asi, debieramos llamar ".addMigration(MIGRATION new)")
                    .fallbackToDestructiveMigration()
                    //callback agregado para llamar al oncreate de mas abajo (optional)
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

    // crea callback para que sea llamado en OnCreate de la BD (optional)
    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDBAsyncTask(instance).execute();
        }
    };

    //Async to populate the DB (optional)
    private static class PopulateDBAsyncTask extends AsyncTask<Void, Void, Void>{
        private NoteDao noteDao;

        public PopulateDBAsyncTask(NoteDB db) {
            this.noteDao = db.noteDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            noteDao.insert(new Note("Title 1", "descripcion 1", 1));
            noteDao.insert(new Note("Title 2", "descripcion 2", 2));
            noteDao.insert(new Note("Title 3", "descripcion 3", 3));

            return null;
        }
    }
}
