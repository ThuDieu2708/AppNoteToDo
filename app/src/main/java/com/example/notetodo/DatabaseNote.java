package com.example.notetodo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class DatabaseNote extends SQLiteOpenHelper {

    Context context;

    // tạo cơ sở dữ liệu
    private static final String nameDB = "MyNotes";
    private static final int versionDB = 1;
    // tạo bảng
    private static final String nameTable = "mynotes";
    private static final String ColumnId = "id";
    private static final String ColumnTitle = "title";
    private static final String ColumnContent = "content";

    public DatabaseNote(@Nullable Context context) {
        super(context, nameDB, null, versionDB);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String queryy = "CREATE TABLE " + nameTable +
                "(" + ColumnId + " INTEGER PRIMARY KEY," + ColumnTitle + " TEXT," + ColumnContent + " TEXT" + ")";
        //thực hiện truy vấn
        sqLiteDatabase.execSQL(queryy);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + nameTable);
        onCreate(sqLiteDatabase);
    }
    void addNote(String title, String content){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(ColumnTitle, title);
        cv.put(ColumnContent, content);
        long resValue = db.insert(nameTable, null, cv);
        if(resValue == -1){
            Toast.makeText(context, "Data Not Added", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(context, "Data Added Successfully", Toast.LENGTH_SHORT).show();
        }
    }
    Cursor readAllData(){
        String query = "SELECT * FROM " + nameTable;
        SQLiteDatabase db = this.getReadableDatabase();
        // tạo 1 con trỏ cursor ban đầu bằng null
        Cursor cursor = null;
        if(db != null){
            cursor = db.rawQuery(query, null); //Chạy SQL được cung cấp và trả về Con trỏ trên tập kết quả.
        }
        return cursor;
    }

    public void deleteAllNote(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + nameTable;
        db.execSQL(query);
    }

    public void updateNote(String title, String content, String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cValue = new ContentValues();
        cValue.put(ColumnTitle, title);
        cValue.put(ColumnContent, content);

        long result = db.update(nameTable, cValue, "id=?", new String[]{id});
        if(result == -1){
            Toast.makeText(context, "Faild", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "Update Successful", Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteItem(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(nameTable, "id=?", new String[]{id});
        if(result == -1){
            Toast.makeText(context, "Item not Delete", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "Xóa thành công Item", Toast.LENGTH_SHORT).show();
        }
    }
}
