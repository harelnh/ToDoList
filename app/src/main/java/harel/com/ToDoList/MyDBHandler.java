package harel.com.ToDoList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

/**
 * Created by harel on 10/13/2016.
 */
public class MyDBHandler extends SQLiteOpenHelper {

    private static final String TAG = "Lab-UserInterface";
    private static final int DB_VERSION = 1;

    private static final String DB_NAME = "todo.db";
    private static final String TABLE_NAME = "table_1";
    private static final String COLUMN_POSITION = "_position";
    private static final String COLUMN_TITLE = "_title";
    private static final String COLUMN_TEXTITSELF = "_textItself";
    private static final String COLUMN_DATE = "_date";
    private static String query =    "CREATE TABLE " +TABLE_NAME + "(" +
            COLUMN_POSITION + " INTEGER, " +
            COLUMN_TITLE + " TEXT, " +
            COLUMN_TEXTITSELF + " TEXT, " +
            COLUMN_DATE + " TEXT " +
            ");";
    private static String query_update = "CREATE TRIGGER trigger_compress_play_order" +
            " AFTER DELETE ON " + TABLE_NAME + " BEGIN " +
            " UPDATE " + TABLE_NAME +
            " SET " + COLUMN_POSITION + " = " + COLUMN_POSITION + " - 1" +
            " WHERE " + COLUMN_POSITION + " > OLD." + COLUMN_POSITION + "; END;";
    public MyDBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DB_NAME, factory, DB_VERSION);
    }
//     private static final String CREATE_TABLE_TODO = "CREATE TABLE "
//                + TABLE_TODO + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_TODO
//                + " TEXT," + KEY_STATUS + " INTEGER," + KEY_CREATED_AT
//                + " DATETIME" + ")";
//     
    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(query);
     //   db.execSQL(query_update);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    db.execSQL("DROP_TABLE_IF_EXIST" + TABLE_NAME);
        onCreate(db);
    }

    //adding a new row to the table
    public void add_toDoItem (ToDoItem toDoItem, int pos){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues ();
        values.put(COLUMN_TITLE,toDoItem.get_title());
        values.put(COLUMN_TEXTITSELF,toDoItem.get_textItself());
        values.put(COLUMN_DATE,ToDoItem.FORMAT.format(toDoItem.get_date()));
        values.put(COLUMN_POSITION,pos);

        db.insert(TABLE_NAME, null, values);
        String query =  "SELECT * FROM " + TABLE_NAME + " WHERE 1";
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
            String bebe = cursor.getString(cursor.getColumnIndex(COLUMN_TITLE));
            log("bye" + bebe);

        log("added");
        db.close();

    }

    //delete a row from database
    public void remove_note (int pos){
        final int position = pos + 1 ;
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME + " WHERE " + COLUMN_POSITION + " = '" + position + "'");
        db.execSQL("UPDATE " + TABLE_NAME + " SET " + COLUMN_POSITION + " = "
        + COLUMN_POSITION + " - 1 WHERE " + COLUMN_POSITION + " > '" + position + "'" );
        log("not here though");
        db.close();

        // dont we need here a close command?
    }

    public void update_note (int position, ToDoItem toDoItem){
        SQLiteDatabase db = getWritableDatabase();
        int  pos = position + 1;
        ContentValues values = new ContentValues ();
        values.put(COLUMN_TITLE,toDoItem.get_title());
        values.put(COLUMN_TEXTITSELF,toDoItem.get_textItself());
        values.put(COLUMN_DATE,ToDoItem.FORMAT.format(toDoItem.get_date()));
        String where = "_position = ?";
        String[] whereArgs = new String[] {Long.toString(pos)};

        db.update(TABLE_NAME, values, where, whereArgs);
        log(getTableAsString(db,TABLE_NAME));
        //db.update(TABLE_NAME, values, COLUMN_POSITION + "='" + pos + "'" , null);

    }
    // useful for debugging..
    public String databaseToString(){
        String dbString = "";
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE 1";// why not leave out the WHERE  clause?
        //Cursor points to a location in your results
        Cursor recordSet = db.rawQuery(query, null);
        //Move to the first row in your results
        recordSet.moveToFirst();
        //Position after the last row means the end of the results
        while (!recordSet.isAfterLast()) {
            // null could happen if we used our empty constructor
            if (recordSet.getString(recordSet.getColumnIndex(COLUMN_TEXTITSELF)) != null) {
                dbString += recordSet.getString(recordSet.getColumnIndex(COLUMN_TEXTITSELF));
                dbString += "\n";
            }
            recordSet.moveToNext();
        }
        db.close();
        return dbString;

    }

    public void loadItems (List<ToDoItem> mItems){

        SQLiteDatabase db = getWritableDatabase();
        String query =  "SELECT * FROM " + TABLE_NAME + " WHERE 1";
        Cursor cursor = db.rawQuery(query, null);
        //log("made a cursor");

        cursor.moveToFirst();
//        int i = cursor.getCount();
//        if (i>0) {
//            String bebe = cursor.getString(cursor.getColumnIndex(COLUMN_TITLE));
//            log("bye" + bebe);
//        }

        while (cursor.isAfterLast() == false)
        {
            ToDoItem toDoItem = new ToDoItem();
            toDoItem.set_title(cursor.getString(cursor.getColumnIndex(COLUMN_TITLE)));
            toDoItem.set_textItself(cursor.getString(cursor.getColumnIndex(COLUMN_TEXTITSELF)));
            try {
                toDoItem.set_date(ToDoItem.
                        FORMAT.parse(cursor.getString(cursor.getColumnIndex(COLUMN_TITLE))));
            }
            catch (ParseException e){
                toDoItem.set_date(new Date());
            }

            cursor.moveToNext();
            mItems.add(toDoItem);
        }

        cursor.close();


    }


    // an extra method for making a String out of the table
//    public String databaseToString(){
//        197         String dbString = "";
//        198         SQLiteDatabase db = getWritableDatabase();
//        199         String query = "SELECT * FROM " + TABLE_PRODUCTS + " WHERE 1";// why not leave out the WHERE  clause?
//        200
//
//        201         //Cursor points to a location in your results
//        202         Cursor recordSet = db.rawQuery(query, null);
//        203         //Move to the first row in your results
//        204         recordSet.moveToFirst();
//        205
//
//        206         //Position after the last row means the end of the results
//        207         while (!recordSet.isAfterLast()) {
//            208             // null could happen if we used our empty constructor
//            209             if (recordSet.getString(recordSet.getColumnIndex("productname")) != null) {
//                210                 dbString += recordSet.getString(recordSet.getColumnIndex("productname"));
//                211                 dbString += "\n";
//                212             }
//            213             recordSet.moveToNext();
//            214         }
//        215         db.close();
//        216         return dbString;
//        217     }
    public String getTableAsString(SQLiteDatabase db, String tableName) {
        Log.d(TAG, "getTableAsString called");
        String tableString = String.format("Table %s:\n", tableName);
        Cursor allRows  = db.rawQuery("SELECT * FROM " + tableName, null);
        if (allRows.moveToFirst() ){
            String[] columnNames = allRows.getColumnNames();
            do {
                for (String name: columnNames) {
                    tableString += String.format("%s: %s\n", name,
                            allRows.getString(allRows.getColumnIndex(name)));
                }
                tableString += "\n";

            } while (allRows.moveToNext());
        }

        return tableString;
    }

    private void log(String msg) {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.i(TAG, msg);
    }

}
