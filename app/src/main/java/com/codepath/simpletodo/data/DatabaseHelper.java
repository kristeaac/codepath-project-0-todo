package com.codepath.simpletodo.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Collection;

import static nl.qbusict.cupboard.CupboardFactory.cupboard;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "simpleTodo.db";
    private static final int DATABASE_VERSION = 1;
    private static DatabaseHelper helper;

    static {
        cupboard().register(TodoItem.class);
    }

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        cupboard().withDatabase(db).createTables();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        cupboard().withDatabase(db).upgradeTables();
    }

    public <T> Iterable<T> readAll(Class<T> clazz) {
        Cursor cursor = cupboard().withDatabase(this.getReadableDatabase()).query(TodoItem.class).getCursor();
        return cupboard().withCursor(cursor).iterate(clazz);
    }

    public <T> boolean delete(T entity) {
        return cupboard().withDatabase(this.getWritableDatabase()).delete(entity);
    }

    public <T> void write(T entity) {
        cupboard().withDatabase(this.getWritableDatabase()).put(entity);
    }

    public <T> void write(Collection<T> entities) {
        cupboard().withDatabase(this.getWritableDatabase()).put(entities);
    }

    public synchronized static DatabaseHelper getHelper(Context context) {
        if (helper == null) {
            //context.deleteDatabase(DATABASE_NAME);
            helper = new DatabaseHelper(context);
        }
        return helper;
    }

}
