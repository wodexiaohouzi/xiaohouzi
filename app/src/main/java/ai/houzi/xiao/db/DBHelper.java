package ai.houzi.xiao.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_USER = "user.db";
    public static final String DATABASE_REGISTER = "register.db";
    private static final int DATABASE_VERSION = 1;

    public DBHelper(Context context, String database_name) {
        //CursorFactory设置为null,使用默认值
        super(context, database_name + ".db", null, DATABASE_VERSION);
    }

    //数据库第一次被创建时onCreate会被调用
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS user" +
                "(_id INTEGER PRIMARY KEY AUTOINCREMENT, user_id VARCHAR,  user_name VARCHAR, user_autograph TEXT, user_head_url TEXT, user_phone VARCHAR, user_lv INTEGER, user_qrcode TEXT,city VARCHAR,temperatrue VARCHAR)");
    }

    //如果DATABASE_VERSION值被改为2,系统发现现有数据库版本不同,即会调用onUpgrade
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("ALTER TABLE user ADD COLUMN other STRING");
    }
}

