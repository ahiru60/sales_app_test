package com.example.salesapp.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import com.example.salesapp.Models.DBItem;
import com.example.salesapp.Models.Discount;
import com.example.salesapp.Models.Order;
import com.example.salesapp.Models.OrderItem;
import com.example.salesapp.Models.SessionItem;
import com.example.salesapp.Models.User;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class DbHandler extends SQLiteOpenHelper {
    public DbHandler(Context context) {
        super(context, "sales", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String itemsTableCreateQuery = "CREATE TABLE items ("
                +" 'itemId' INTEGER PRIMARY KEY AUTOINCREMENT, "
                +"'image' TEXT,"
                +"'itemName' TEXT,"
                + "'stock' TEXT,"
                + "'cost' TEXT,"
                + "'selling_price' TEXT)";
        db.execSQL(itemsTableCreateQuery);
        String ordersTableCreateQuery = "CREATE TABLE orders ("
                +" 'orderId' INTEGER PRIMARY KEY AUTOINCREMENT, "
                +" 'userId' INTEGER,"
                +" 'discount' TEXT, "
                +" 'is_value' INTEGER, "
                + "'timeStamp' DATETIME DEFAULT CURRENT_TIMESTAMP,"
                + "FOREIGN KEY (userId) REFERENCES users(userId))";
        db.execSQL(ordersTableCreateQuery);
        String orderItemsTableCreateQuery = "CREATE TABLE orderItems ("
                + "orderItemId INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "orderId INTEGER, "
                + "itemId INTEGER, "
                + "quantity INTEGER, "
                + "FOREIGN KEY (orderId) REFERENCES orders(orderId)  ON DELETE CASCADE,"
                + "FOREIGN KEY (itemId) REFERENCES items(itemId))";
        try {
            db.execSQL(orderItemsTableCreateQuery);
        }catch (SQLException e){
            Log.d("DbHandler", "onCreate: "+e);
        }
        String usersTableCreateQuery = "CREATE TABLE users ("
                +" 'userId' INTEGER PRIMARY KEY AUTOINCREMENT, "
                +"'image_url' TEXT,"
                +"'user_name' TEXT,"
                + "'gender' TEXT,"
                + "'location' TEXT)";
        db.execSQL(usersTableCreateQuery);
//        addItemtoItems("Apple", "1500", "3");
//        addItemtoItems("Orange", "1000", "2");
//        addItemtoItems("Mango", "1000", "3");
//        addItemtoItems("Coconut", "300", "5");
//        addItemtoItems("Chocolate", "100", "3");
//        addItemtoItems("Candles", "500", "2");
    }

    public void addItemtoItems(Bitmap imageBtmp,String itemName, String stock, String cost, String selling_price){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        if(imageBtmp != null){
            String imageURL = saveImage(imageBtmp,itemName);
            values.put("image",imageURL);
        }else{
            values.put("image","");
        }
        values.put("itemName",itemName);
        values.put("stock",stock);
        values.put("cost",cost);
        values.put("selling_price",selling_price);
        db.insert("items",null,values);
    }
    public boolean updateItem(String itemId,Bitmap imageBtmp,String itemName, String stock, String cost, String selling_price){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        String[] whereArgs = new String[] { String.valueOf(itemId) };
        if(imageBtmp != null){
            String imageURL = saveImage(imageBtmp,itemName);
            values.put("image",imageURL);
        }else{
            values.put("image","");
        }
        values.put("itemName",itemName);
        values.put("stock",stock);
        values.put("cost",cost);
        values.put("selling_price",selling_price);
        int rowsAffected = db.update("items",values,"itemId=?",whereArgs);
        if(rowsAffected > 0){
            return true;
        }
        return false;
    }

    public ArrayList<DBItem> getItems(){
        ArrayList<DBItem> items = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM items;", null);
        if(cursor.moveToFirst()){
            do{
                Bitmap bitmap = readImage(cursor.getString(1));
                items.add(new DBItem(bitmap,cursor.getString(0),cursor.getString(2),cursor.getString(3),cursor.getString(4),cursor.getString(5)));
            }while(cursor.moveToNext());
        }
        return items;
    }
    public ArrayList<DBItem> getItemById(String itemId){
        ArrayList<DBItem> items = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM items WHERE itemId = ?;",new String[]{itemId});
        if(cursor.moveToFirst()){
            do{
                Bitmap bitmap = readImage(cursor.getString(1));
                items.add(new DBItem(bitmap,cursor.getString(0),cursor.getString(2),cursor.getString(3),cursor.getString(4),cursor.getString(5)));
            }while(cursor.moveToNext());
        }
        return items;
    }

    public ArrayList<DBItem> searchItemByName(String keyword){
        ArrayList<DBItem> items = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM items WHERE itemName LIKE ?", new String[]{"%" + keyword + "%"});
        if(cursor.moveToFirst()){
            do{
                Bitmap bitmap = readImage(cursor.getString(1));
                items.add(new DBItem(bitmap,cursor.getString(0),cursor.getString(2),cursor.getString(3),cursor.getString(4),cursor.getString(5)));
            }while(cursor.moveToNext());
        }
        return items;
    }

    public void saveOrder(ArrayList<SessionItem> order,String discount,boolean isValue,String userId){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        if(discount != null){
            values.put("discount",discount);
            if(isValue){
                values.put("is_value",1);
            }else {
                values.put("is_value",0);
            }
        }else{
            values.put("discount","0");
        }
        if(userId != null){
            values.put("userId",userId);
        }else {
            values.put("userId","");
        }
        long orderId = db.insert("orders", null, values);

        if(orderId != -1){
            for(SessionItem item : order){
                values.clear();
                values.put("orderId",orderId);
                values.put("itemId",item.getItemId());
                values.put("quantity",item.getQuantity());
                db.insert("orderItems",null,values);
                values.clear();
            }
        }
    }
    public ArrayList<Order> getOrders(){
        ArrayList<Order> orders = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM orders;", null);
        if(cursor.moveToFirst()){
            do{
                orders.add(new Order(cursor.getString(0),cursor.getString(1),cursor.getString(4)));
            }while(cursor.moveToNext());
        }
        return orders;
    }
    public ArrayList<SessionItem> getOrder(User user, String orderId){
        ArrayList<SessionItem> items = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String[] arg = {orderId};
        String query = "SELECT " +
                "    o.orderId," +
                "    o.discount," +
                "    o.is_value," +
                "    oi.orderItemId," +
                "    o.timeStamp," +
                "    oi.quantity," +
                "    i.itemId," +
                "    i.itemName," +
                "    i.image," +
                "    i.stock," +
                "    i.cost," +
                "    i.selling_price," +
                "    u.user_name," +
                "    u.gender," +
                "    u.location\n" +
                "FROM" +
                "    orders o\n" +
                "INNER JOIN" +
                "    orderItems oi ON o.orderId = oi.orderId\n" +
                "INNER JOIN" +
                "    items i ON oi.itemId = i.itemId\n" +
                "LEFT JOIN" +
                "    users u ON o.userId = u.userId\n" +
                "WHERE" +
                "    o.orderId = ?;";

        Cursor cursor = db.rawQuery(query,arg);
        if(cursor.moveToFirst()){
            user.setUserName(cursor.getString(12));
            user.setGender(cursor.getString(13));
            user.setLocation(cursor.getString(14));
            Discount discount = new Discount();
            String isValue = cursor.getString(2);
            if(isValue.equals("1")){
                discount.setValue(Double.valueOf(cursor.getString(1)));
            }
            if(isValue.equals("0")){
                discount.setPercentage(Double.valueOf(cursor.getString(1)));
            }
            user.setDiscount(discount);
            do{
                items.add(new SessionItem(cursor.getString(6),null,cursor.getString(7),cursor.getString(5),cursor.getString(11)));
            }while(cursor.moveToNext());

        }

        ;
        return items;
    }

    public ArrayList<Order> searchOrders(String keyword){
        ArrayList<Order> orders = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM orders WHERE orderId LIKE ? OR user LIKE ? OR timeStamp LIKE ?", new String[]{"%" + keyword + "%","%" + keyword + "%","%" + keyword + "%"});
        if(cursor.moveToFirst()){
            do{
                orders.add(new Order(cursor.getString(0),cursor.getString(1),cursor.getString(4)));
            }while(cursor.moveToNext());
        }
        return orders;
    }
    public void updateOrder(String orderId,String userId, ArrayList<SessionItem> userCartItems){
        SQLiteDatabase db = this.getWritableDatabase();
        for(SessionItem item :userCartItems){
            ContentValues values = new ContentValues();
            values.put("quantity",item.getQuantity());
           int rowsAffected = db.update("orderItems",values,"ItemId = ? AND orderId = ?",new String []{item.getItemId(),orderId});
           if(rowsAffected<1){
               values.clear();
               values.put("orderId",orderId);
               values.put("itemId",item.getItemId());
               values.put("quantity",item.getQuantity());
               db.insert("orderItems",null,values);
           }
        }
        if(userId != null){
            ContentValues values = new ContentValues();
            values.put("userId",userId);
            db.update("orders",values,"orderId = ?",new String[]{orderId});
        }
    }
public void deleteOrder(String orderId){
    SQLiteDatabase db = this.getWritableDatabase();
    db.execSQL("PRAGMA foreign_keys = ON;");
    db.delete("orders","orderId = ?",new String[]{ orderId });
}

    public long addUser(Bitmap image,String userName,String gender,String location){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        long userId;

        values.put("image_url",saveImage(image,userName));
        values.put("user_name",userName);
        values.put("gender",gender);
        values.put("location",location);
        userId = db.insert("users",null,values);
        return userId;

    }
    public ArrayList<User> getUsers(){
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<User> users = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM users",null);
        if(cursor.moveToNext()){
            do{
               Bitmap image = readImage(cursor.getString(1));
                users.add(new User(cursor.getString(0),image,cursor.getString(2),cursor.getString(3),cursor.getString(4),null,null));
            }while(cursor.moveToNext());
        }
        return users;
    }
    public void deleteTable(String tableName) {
        SQLiteDatabase db = this.getWritableDatabase();

        // SQL statement to drop the table if it exists
        String dropTableQuery = "DROP TABLE IF EXISTS " + tableName;

        db.execSQL(dropTableQuery);
    }
    public void deleteDatabase(Context context) {
        context.deleteDatabase("sales.db");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS items");
        db.execSQL("DROP TABLE IF EXISTS users");
        db.execSQL("DROP TABLE IF EXISTS cart");
        onCreate(db);
    }
    private String saveImage(Bitmap finalBitmap,String productName) {
        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/SalesApp/images/";
        path = path + productName.toLowerCase() + ".jpg";
        File file = new File (path);
        if (!file.exists()) {
            file.mkdirs();  // This will create the folder and any necessary parent directories
        }
        if (file.exists ()) file.delete ();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 50, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return path;
    }
    private Bitmap readImage(String ImageURL){
        Bitmap bitmap = null;
        if(!ImageURL.equals(null)){
            File imgFile = new  File(ImageURL);

            if(imgFile.exists()){
                bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            }
        }
        return bitmap;
    }
}
