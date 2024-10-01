package com.example.salesapp.Database;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.salesapp.Models.DBItem;
import com.example.salesapp.Models.Discount;
import com.example.salesapp.Models.Order;
import com.example.salesapp.Models.Receipt;
import com.example.salesapp.Models.SessionItem;
import com.example.salesapp.Models.User;

import java.util.ArrayList;

public class DbHandler extends SQLiteOpenHelper {

    private int quality = 90;
    private int maxWidth = 250;
    private int maxHeight = 250;

    public DbHandler(Context context) {
        super(context, "sales", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String itemsTableCreateQuery = "CREATE TABLE items ("
                + " 'itemId' INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "'image' TEXT,"
                + "'itemName' TEXT,"
                + "'stock' TEXT,"
                + "'cost' TEXT,"
                + "'selling_price' TEXT)";
        try {
            db.execSQL(itemsTableCreateQuery);
        } catch (SQLException e) {
            Log.d("DbHandler", "onCreate: " + e);
        }
        String ordersTableCreateQuery = "CREATE TABLE orders ("
                + " 'orderId' INTEGER PRIMARY KEY AUTOINCREMENT, "
                + " 'userId' INTEGER,"
                + " 'discount' TEXT, "
                + " 'is_value' INTEGER, "
                + "'timeStamp' DATETIME DEFAULT CURRENT_TIMESTAMP,"
                + "FOREIGN KEY (userId) REFERENCES users(userId))";
        try {
            db.execSQL(ordersTableCreateQuery);
        } catch (SQLException e) {
            Log.d("DbHandler", "onCreate: " + e);
        }
        String orderItemsTableCreateQuery = "CREATE TABLE orderItems ("
                + "orderItemId INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "orderId INTEGER, "
                + "itemId INTEGER, "
                + "quantity INTEGER, "
                + "FOREIGN KEY (orderId) REFERENCES orders(orderId)  ON DELETE CASCADE,"
                + "FOREIGN KEY (itemId) REFERENCES items(itemId))";
        try {
            db.execSQL(orderItemsTableCreateQuery);
        } catch (SQLException e) {
            Log.d("DbHandler", "onCreate: " + e);
        }
        String usersTableCreateQuery = "CREATE TABLE users ("
                + " 'userId' INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "'image_url' TEXT,"
                + "'user_name' TEXT,"
                + "'gender' TEXT,"
                + "'location' TEXT)";
        try {
            db.execSQL(usersTableCreateQuery);
        } catch (SQLException e) {
            Log.d("DbHandler", "onCreate: " + e);
        }
        String receiptTableCreateQuery = "CREATE TABLE receipts ("
                + "'receiptId' INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "'total_amount' TEXT,"
                + "'userId' TEXT,"
                + "'timeStamp' DATETIME DEFAULT CURRENT_TIMESTAMP,"
                + "FOREIGN KEY (userId) REFERENCES users(userId))";
        try {
            db.execSQL(receiptTableCreateQuery);
        } catch (SQLException e) {
            Log.d("DbHandler", "onCreate: " + e);
        }
    }

    public void saveReceipt(Receipt receipt){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("total_amount",receipt.getTotal_amount());
        values.put("userId",receipt.getTotal_amount());
        db.insert("receipts", null, values);
    }
    public ArrayList<Receipt> getReceipts() {
        ArrayList<Receipt> receipts = new ArrayList<>();
        ArrayList<Receipt> formatedReceipts = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM receipts;", null);
        if (cursor.moveToFirst()) {
            do {
                receipts.add(new Receipt(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3)));
            } while (cursor.moveToNext());
        }
        String date = "";
        for(Receipt receipt : receipts){
            if(!date.equals(receipt.getTimestamp().split(" ")[0])){
                date = receipt.getTimestamp().split(" ")[0];
                Receipt receipt1 = new Receipt(null,null,null,receipt.getTimestamp());
                receipt1.setOnlyDate(true);
                formatedReceipts.add(receipt1);
                formatedReceipts.add(receipt);

            }else{
                formatedReceipts.add(receipt);
            }
        }

        return formatedReceipts;
    }

    public void addItemtoItems(String imageURL, String itemName, String stock, String cost, String selling_price) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("image", imageURL);
        values.put("itemName", itemName);
        values.put("stock", stock);
        values.put("cost", cost);
        values.put("selling_price", selling_price);
        db.insert("items", null, values);
    }

    public boolean updateItem(String itemId, String imageURL, String itemName, String stock, String cost, String selling_price) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        String[] whereArgs = new String[]{String.valueOf(itemId)};
        values.put("image", imageURL);
        values.put("itemName", itemName);
        values.put("stock", stock);
        values.put("cost", cost);
        values.put("selling_price", selling_price);
        int rowsAffected = db.update("items", values, "itemId=?", whereArgs);
        if (rowsAffected > 0) {
            return true;
        }
        return false;
    }

    public ArrayList<DBItem> getItems() {
        ArrayList<DBItem> items = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM items;", null);
        if (cursor.moveToFirst()) {
            do {
                items.add(new DBItem(cursor.getString(1), cursor.getString(0), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5)));
            } while (cursor.moveToNext());
        }
        return items;
    }

    public ArrayList<DBItem> getItemById(String itemId) {
        ArrayList<DBItem> items = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM items WHERE itemId = ?;", new String[]{itemId});
        if (cursor.moveToFirst()) {
            do {
                items.add(new DBItem(cursor.getString(1), cursor.getString(0), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5)));
            } while (cursor.moveToNext());
        }
        return items;
    }

    public ArrayList<DBItem> searchItemByName(String keyword) {
        ArrayList<DBItem> items = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM items WHERE itemName LIKE ?", new String[]{"%" + keyword + "%"});
        if (cursor.moveToFirst()) {
            do {
                items.add(new DBItem(cursor.getString(1), cursor.getString(0), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5)));
            } while (cursor.moveToNext());
        }
        return items;
    }
    public ArrayList<User> searchUserByName(String keyword) {
        ArrayList<User> items = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM users WHERE user_name LIKE ? OR userId LIKE ? OR location LIKE ? OR gender LIKE ?", new String[]{"%" + keyword + "%","%" + keyword + "%","%" + keyword + "%","%" + keyword + "%"});
        if (cursor.moveToFirst()) {
            do {
                items.add(new User(cursor.getString(0), cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4),null,null));
            } while (cursor.moveToNext());
        }
        return items;
    }
    public ArrayList<Receipt> searchReceiptByName(String keyword,String timestamp) {
        ArrayList<Receipt> items = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        if(timestamp.equals("")){
            Cursor cursor = db.rawQuery("SELECT * FROM receipts WHERE receiptId LIKE ? OR total_amount LIKE ?", new String[]{"%" + keyword + "%","%" + keyword + "%"});
            if (cursor.moveToFirst()) {
                do {
                    items.add(new Receipt(cursor.getString(0),cursor.getString(1),cursor.getString(2),cursor.getString(3)));
                } while (cursor.moveToNext());
            }
            return items;
        }
        Cursor cursor = db.rawQuery("SELECT * FROM receipts WHERE timeStamp LIKE ? AND (receiptId LIKE ? OR total_amount LIKE ?)", new String[]{timestamp + "%","%" + keyword + "%","%" + keyword + "%"});
        if (cursor.moveToFirst()) {
            do {
                items.add(new Receipt(cursor.getString(0),cursor.getString(1),cursor.getString(2),cursor.getString(3)));
            } while (cursor.moveToNext());
        }
        return items;
    }

    public void saveOrder(ArrayList<SessionItem> order, String discount, boolean isValue, String userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        if (discount != null) {
            values.put("discount", discount);
            if (isValue) {
                values.put("is_value", 1);
            } else {
                values.put("is_value", 0);
            }
        } else {
            values.put("discount", "0");
        }
        if (userId != null) {
            values.put("userId", userId);
        } else {
            values.put("userId", "");
        }
        long orderId = db.insert("orders", null, values);

        if (orderId != -1) {
            for (SessionItem item : order) {
                values.clear();
                values.put("orderId", orderId);
                values.put("itemId", item.getItemId());
                values.put("quantity", item.getQuantity());
                db.insert("orderItems", null, values);
                values.clear();
            }
        }

    }

    public ArrayList<Order> getOrders() {
        ArrayList<Order> orders = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM orders;", null);
        if (cursor.moveToFirst()) {
            do {
                orders.add(new Order(cursor.getString(0), cursor.getString(1), cursor.getString(4)));
            } while (cursor.moveToNext());
        }
        return orders;
    }

    public ArrayList<SessionItem> getOrder(User user, String orderId) {
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
                "    u.userId," +
                "    u.user_name," +
                "    u.gender," +
                "    u.location," +
                "    u.image_url\n" +
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

        Cursor cursor = db.rawQuery(query, arg);
        if (cursor.moveToFirst()) {
            user.setUserName(cursor.getString(13));
            user.setUserId(cursor.getString(12));
            user.setGender(cursor.getString(14));
            user.setLocation(cursor.getString(15));
            if (cursor.getString(16) != null) {
                user.setImage(cursor.getString(16));
            }
            Discount discount = new Discount();
            String isValue = cursor.getString(2);
            if (isValue.equals("1")) {
                discount.setValue(Double.valueOf(cursor.getString(1)));
            }
            if (isValue.equals("0")) {
                discount.setPercentage(Double.valueOf(cursor.getString(1)));
            }
            user.setDiscount(discount);
            do {
                items.add(new SessionItem(cursor.getString(6), null, cursor.getString(7), cursor.getString(5), cursor.getString(11)));
            } while (cursor.moveToNext());

        }

        ;
        return items;
    }

    public ArrayList<Order> searchOrders(String keyword) {
        ArrayList<Order> orders = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM orders WHERE orderId LIKE ? OR userId LIKE ? OR timeStamp LIKE ?", new String[]{"%" + keyword + "%", "%" + keyword + "%", "%" + keyword + "%"});
        if (cursor.moveToFirst()) {
            do {
                orders.add(new Order(cursor.getString(0), cursor.getString(1), cursor.getString(4)));
            } while (cursor.moveToNext());
        }
        return orders;
    }

    public void updateOrder(String orderId, String userId, ArrayList<SessionItem> userCartItems, String discount, boolean isValue) {
        SQLiteDatabase db = this.getWritableDatabase();
        for (SessionItem item : userCartItems) {
            ContentValues values = new ContentValues();
            values.put("quantity", item.getQuantity());
            int rowsAffected = db.update("orderItems", values, "ItemId = ? AND orderId = ?", new String[]{item.getItemId(), orderId});
            if (rowsAffected < 1) {
                values.clear();
                values.put("orderId", orderId);
                values.put("itemId", item.getItemId());
                values.put("quantity", item.getQuantity());
                db.insert("orderItems", null, values);
            }
        }
        if (userId != null) {
            ContentValues values = new ContentValues();
            values.put("userId", userId);
            db.update("orders", values, "orderId = ?", new String[]{orderId});
        } else {
            ContentValues values = new ContentValues();
            values.put("userId", "");
            db.update("orders", values, "orderId = ?", new String[]{orderId});
        }
        if (discount != null) {
            ContentValues values = new ContentValues();
            values.put("is_value", isValue);
            values.put("discount", discount);
            db.update("orders", values, "orderId = ?", new String[]{orderId});
        }
    }

    public void deleteOrder(String orderId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("PRAGMA foreign_keys = ON;");
        db.delete("orders", "orderId = ?", new String[]{orderId});
    }

    public long addUser(String imageURL, String userName, String gender, String location) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        long userId;

        values.put("image_url", imageURL);
        values.put("user_name", userName);
        values.put("gender", gender);
        values.put("location", location);
        userId = db.insert("users", null, values);
        return userId;

    }

    public ArrayList<User> getUsers() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<User> users = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM users", null);
        if (cursor.moveToNext()) {
            do {
                users.add(new User(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), null, null));
            } while (cursor.moveToNext());
        }
        return users;
    }

    public String getUserImage(String userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<User> users = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT image_url FROM users WHERE userId = ?", new String[]{userId});
        String imageURL = null;
        if (cursor.moveToNext()) {
            do {
                imageURL = cursor.getString(1);
            } while (cursor.moveToNext());
        }
        return imageURL;
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


}
