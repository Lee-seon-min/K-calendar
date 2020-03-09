package org.techtown.caloriecalculator;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class DataHandler extends ContentProvider { //각종 테이블의 조작
    private static final int INFO=1;
    private static  final int DAILY=2;

    private static final String DATABASE_NAME="APPDB";
    private static final String TABLE_INFO="information";
    private static final String TABLE_DAILY="daily";
    public static final Uri CONTENT_URI_INFO=Uri.parse("content://org.techtown.caloriecalculator/information");
    public static final Uri CONTENT_URI_DAILY=Uri.parse("content://org.techtown.caloriecalculator/daily");
    private SQLiteDatabase database;
    private static final UriMatcher uriMatcher=new UriMatcher(UriMatcher.NO_MATCH);
    static{
        uriMatcher.addURI("org.techtown.caloriecalculator","information",INFO);
        uriMatcher.addURI("org.techtown.caloriecalculator","daily",DAILY);
    }

    @Override
    public boolean onCreate() {
        database=getContext().openOrCreateDatabase(DATABASE_NAME, Context.MODE_PRIVATE,null);
        database.execSQL("create table if not exists "+TABLE_INFO+" ("+
                "sex int,"+
                "age int, "+
                "weight double, "+
                "height double, "+
                "activity varchar(10), "+
                "basic int,"+
                "alpha int)"
        );
        database.execSQL("create table if not exists "+TABLE_DAILY+" ("+
                "thisdate varchar(15),"+
                "morning int, "+
                "lunch int, "+
                "evening int, "+
                "snack int, "+
                "sum int)"
        );

        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor cursor=null;
        switch(uriMatcher.match(uri)){
            case INFO:
                cursor=database.query(TABLE_INFO,projection,selection,selectionArgs,null,null,sortOrder);
                break;
            case DAILY:
                cursor=database.query(TABLE_DAILY,projection,selection,selectionArgs,null,null,sortOrder);
                break;
        }
        if(cursor!=null)
            cursor.setNotificationUri(getContext().getContentResolver(),uri);
        return cursor; //null일수도 아닐수도
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        long id = -1;
        int target=0;
        switch (uriMatcher.match(uri)){
            case INFO:
                id=database.insert(TABLE_INFO,null,values);
                target=1;
                break;
            case DAILY:
                id=database.insert(TABLE_DAILY,null,values);
                target=2;
                break;
        }
        if(id>0){
            switch(target){
                case 1:
                    Uri _uri1= ContentUris.withAppendedId(CONTENT_URI_INFO,id);
                    getContext().getContentResolver().notifyChange(_uri1,null);
                    return _uri1;
                case 2:
                    Uri _uri2= ContentUris.withAppendedId(CONTENT_URI_DAILY,id);
                    getContext().getContentResolver().notifyChange(_uri2,null);
                    return _uri2;
            }
        }
        return null;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        int count=0;
        switch(uriMatcher.match(uri)){
            case INFO:
                count=database.update(TABLE_INFO,values,selection,selectionArgs);
                break;
            case DAILY:
                count=database.update(TABLE_DAILY,values,selection,selectionArgs);
                break;
            default:
                return -1; //오류시
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return count;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int count=0;
        switch(uriMatcher.match(uri)){
            case INFO:
                count=database.delete(TABLE_INFO,selection,selectionArgs);
                break;
            case DAILY:
                count=database.delete(TABLE_DAILY,selection,selectionArgs);
                break;
                default:
                    return -1;
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return count;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch(uriMatcher.match(uri)){
            case INFO:
                return "vnd.android.cursor.dir/information";
            case DAILY:
                return "vnd.android.cursor.dir/daily";
        }
        return null;
    }
}
