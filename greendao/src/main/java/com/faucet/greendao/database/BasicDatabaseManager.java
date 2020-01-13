package com.faucet.greendao.database;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Handler;
import android.os.Looper;


/**
 * Created by mac on 16/9/6.
 */
public abstract class BasicDatabaseManager<T> {

    //获取数据库文件名称
    protected abstract String getDBFile();
    //db
    protected  SQLiteDatabase db;

    protected T daoSession;

    protected SQLiteOpenHelper helper;

    protected abstract SQLiteOpenHelper getSQLiteOpenHelper();

    protected abstract void connect();

    //hanle
    protected static Handler handler = new Handler(Looper.getMainLooper());

    public void initConnection(){
        if(helper==null || !isConnection(getSQLiteOpenHelper())){
            helper = getSQLiteOpenHelper();
        }
        //是否连接
        if(!isOpen()){
            //读写
            connect();
        }
    }

    /**
     * 检测DB是否打开
     * @return
     */
    protected boolean isOpen(){
        try {
            if (db.isOpen()) {
                return true;
            }
        }catch (Exception e){

        }
        return false;
    }

    /**
     * 检验db连接
     * @param helper
     * @return
     */
    private boolean isConnection(SQLiteOpenHelper helper){
        try{
            String dbFile = getDBFile();
            if(helper!=null && helper.getDatabaseName().equals(dbFile)){
                return true;
            }
            close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }


    /**
     * 关闭DB
     */
    public void close(){
        if (db !=null ) {
            db.close();
        }
        if (helper != null) {
            helper.close();
        }
        db = null;
        helper = null;
    }

    /**
     * 获取数据库dao
     * @return
     */
    public T getDaoSession(){
        if(daoSession==null || !isConnection(getSQLiteOpenHelper())){
            initConnection();
        }
        return daoSession;
    }
}
