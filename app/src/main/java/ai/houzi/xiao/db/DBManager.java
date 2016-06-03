package ai.houzi.xiao.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import ai.houzi.xiao.entity.User;

public class DBManager {
    private final DBHelper helper;
    private final SQLiteDatabase db;

    public DBManager(Context context) {
        helper = new DBHelper(context, DBHelper.DATABASE_USER);
        //因为getWritableDatabase内部调用了mContext.openOrCreateDatabase(mName, 0, mFactory);
        //所以要确保context已初始化,我们可以把实例化DBManager的步骤放在Activity的onCreate里
        db = helper.getWritableDatabase();
    }

    /**
     * add users
     *
     * @param users
     */
    public void add(List<User> users) {
        db.beginTransaction();    //开始事务
        try {
            for (User user : users) {
                db.execSQL("INSERT INTO user VALUES(null, user_id, user_name, user_phone)", new Object[]{user.userId, user.userName, user.userPhone});
            }
            db.setTransactionSuccessful();    //设置事务成功完成
        } finally {
            db.endTransaction();    //结束事务
        }
    }

    /**
     * update user
     *
     * @param user
     */
//    public void updateUser(User user) {
//        ContentValues cv = new ContentValues();
//        cv.put("age", person.age);
//        db.update("user", cv, "name = ?", new String[]{person.name});
//    }

    /**
     * delete user
     *
     * @param userId
     */
    public void deleteUser(String userId) {
        db.delete("person", "userId == ?", new String[]{userId});
    }

    /**
     * query all users, return list
     *
     * @return List<User>
     */
    public List<User> query() {
        ArrayList<User> users = new ArrayList<>();
        Cursor c = queryTheCursor();
        while (c.moveToNext()) {
            User user = new User();
            user.userId = c.getString(c.getColumnIndex("user_id"));
            user.userName = c.getString(c.getColumnIndex("user_name"));
            user.userHeadUrl = c.getString(c.getColumnIndex("user_headUrl"));
            user.userAutograph = c.getString(c.getColumnIndex("user_autograph"));
            user.userQRcode = c.getString(c.getColumnIndex("user_qrcode"));
            user.userPhone = c.getString(c.getColumnIndex("user_phone"));
            user.userLv = c.getInt(c.getColumnIndex("user_lv"));
            user.city = c.getString(c.getColumnIndex("city"));
            user.temperatrue = c.getString(c.getColumnIndex("temperatrue"));
            users.add(user);
        }
        c.close();
        return users;
    }

    /**
     * query all users, return cursor
     *
     * @return Cursor
     */
    public Cursor queryTheCursor() {
        return db.rawQuery("SELECT * FROM user", null);
    }

    /**
     * 整个应用关闭时执行
     * close database
     */
    public void closeDB() {
        db.close();
    }
}

