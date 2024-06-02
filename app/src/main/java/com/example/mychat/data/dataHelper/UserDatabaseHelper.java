package com.example.mychat.data.dataHelper;



import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.mychat.models.User;
import com.google.firebase.Timestamp;

public class UserDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "userDatabase.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_USERS = "users";

    private static final String COLUMN_USER_ID = "userId";
    private static final String COLUMN_PHONE = "phone";
    private static final String COLUMN_USER_NAME = "userName";
    private static final String COLUMN_CREATED_TIMESTAMP = "createdTimestamp";
    private static final String COLUMN_FCM_TOKEN = "fcmToken";
    private static final String COLUMN_PASSWORD = "password";

    public UserDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "("
                + COLUMN_USER_ID + " TEXT PRIMARY KEY,"
                + COLUMN_PHONE + " TEXT,"
                + COLUMN_USER_NAME + " TEXT,"
                + COLUMN_CREATED_TIMESTAMP + " INTEGER,"
                + COLUMN_FCM_TOKEN + " TEXT,"
                + COLUMN_PASSWORD + " TEXT" + ")";
        db.execSQL(CREATE_USERS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    public void addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_ID, user.getUserId());
        values.put(COLUMN_PHONE, user.getPhoneNumber());
        values.put(COLUMN_USER_NAME, user.getUsername());
        values.put(COLUMN_CREATED_TIMESTAMP, user.getTimestamp().toDate().getTime());
        values.put(COLUMN_FCM_TOKEN, user.getFcmToken());
        values.put(COLUMN_PASSWORD, user.getPassword());
        db.insert(TABLE_USERS, null, values);
        db.close();
    }

    public User getUser(String userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, new String[] { COLUMN_USER_ID, COLUMN_PHONE, COLUMN_USER_NAME,
                        COLUMN_CREATED_TIMESTAMP, COLUMN_FCM_TOKEN, COLUMN_PASSWORD }, COLUMN_USER_ID + "=?",
                new String[] { userId }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        User user = new User(
                cursor.getString(1),
                cursor.getString(2),
                new Timestamp(new java.util.Date(cursor.getLong(3))),
                cursor.getString(0),
                cursor.getString(5)
        );
        cursor.close();
        return user;
    }

    public int updateUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PHONE, user.getPhoneNumber());
        values.put(COLUMN_USER_NAME, user.getUsername());
        values.put(COLUMN_PASSWORD, user.getPassword());
        values.put(COLUMN_FCM_TOKEN, user.getFcmToken());
        return db.update(TABLE_USERS, values, COLUMN_USER_ID + " = ?", new String[]{user.getUserId()});
    }
}
