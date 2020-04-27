package com.lion.tom.projectdb.Classes;

import com.google.firebase.database.FirebaseDatabase;

public class Application extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}