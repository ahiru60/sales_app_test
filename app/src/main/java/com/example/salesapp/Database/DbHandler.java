package com.example.salesapp.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.salesapp.Models.DBCartItem;

import java.util.ArrayList;

public class DbHandler extends SQLiteOpenHelper {
    public DbHandler(Context context) {
        super(context, "sales", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String itemsTableCreateQuery = "CREATE TABLE items ("
                +" 'itemId' INTEGER PRIMARY KEY AUTOINCREMENT, "
                +"'itemName' TEXT,"
                + "'stock' TEXT,"
                + "'price' TEXT)";
        db.execSQL(itemsTableCreateQuery);
        String usersTableCreateQuery = "CREATE TABLE users ("
                +" 'userId' INTEGER PRIMARY KEY AUTOINCREMENT, "
                +"'userName' TEXT,"
                + "'gender' TEXT,"
                + "'location' TEXT)";
        db.execSQL(usersTableCreateQuery);
        String cartTableCreateQuery = "CREATE TABLE cart ("
                +" 'cartId' INTEGER PRIMARY KEY AUTOINCREMENT, "
                +"'userId' TEXT,"
                + "'itemId' TEXT,"
                + "'quantity' TEXT,"
                + "'price' TEXT)";
        db.execSQL(cartTableCreateQuery);
//        addItemtoItems("Apple", "1500", "3");
//        addItemtoItems("Orange", "1000", "2");
//        addItemtoItems("Mango", "1000", "3");
//        addItemtoItems("Coconut", "300", "5");
//        addItemtoItems("Chocolate", "100", "3");
//        addItemtoItems("Candles", "500", "2");
    }

    public void addItemtoItems(String itemName,String stock,String price){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("itemName",itemName);
        values.put("stock",stock);
        values.put("price",price);
        db.insert("items",null,values);
    }
    public void addItemtoCart(String userID,String itemId,String quantity,String price){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("userId",userID);
        values.put("itemId",itemId);
        values.put("quantity",quantity);
        values.put("price",price);
        db.insert("cart",null,values);
    }
    public void addItemtoCart(String userId,String itemId,String quantity){
        ArrayList<DBCartItem> items = getItemById(itemId);
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        for(DBCartItem item : items){
            values.put("userId",userId);
            values.put("itemId",item.getItemId());
            values.put("quantity",quantity);
            db.insert("cart",null,values);
        }

    }

    public void addUser(String userName,String gender,String location){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("userName",userName);
        values.put("gender",gender);
        values.put("location",location);
        db.insert("users",null,values);
    }
    public ArrayList<DBCartItem> getItems(){
        ArrayList<DBCartItem> items = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM items;", null);
        if(cursor.moveToFirst()){
            do{
                items.add(new DBCartItem(cursor.getString(0),cursor.getString(1),cursor.getString(2),cursor.getString(3)));
            }while(cursor.moveToNext());
        }
        return items;
    }
    public ArrayList<DBCartItem> getItemById(String itemId){
        ArrayList<DBCartItem> items = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM items WHERE itemId = ?;",new String[]{itemId});
        if(cursor.moveToFirst()){
            do{
                items.add(new DBCartItem(cursor.getString(0),cursor.getString(1),cursor.getString(2),cursor.getString(3)));
            }while(cursor.moveToNext());
        }
        return items;
    }

    public ArrayList<DBCartItem> searchItemByName(String keyword){
        ArrayList<DBCartItem> items = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM items WHERE itemName LIKE ?", new String[]{"%" + keyword + "%"});
        if(cursor.moveToFirst()){
            do{
                items.add(new DBCartItem(cursor.getString(0),cursor.getString(1),cursor.getString(2),cursor.getString(3)));
            }while(cursor.moveToNext());
        }
        return items;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS items");
        db.execSQL("DROP TABLE IF EXISTS users");
        db.execSQL("DROP TABLE IF EXISTS cart");
        //onCreate(db);
    }
}
