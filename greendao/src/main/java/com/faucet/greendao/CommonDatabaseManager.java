package com.faucet.greendao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.faucet.greendao.dao.DBCompanyUserEmailInfoDao;
import com.faucet.greendao.dao.DBCompanyUserInfoDao;
import com.faucet.greendao.dao.DBCompanyUserTelInfoDao;
import com.faucet.greendao.dao.DBUserInfoDao;
import com.faucet.greendao.dao.DBValidationMessageDao;
import com.faucet.greendao.dao.DaoMaster;
import com.faucet.greendao.dao.DaoSession;
import com.faucet.greendao.database.BasicDatabaseManager;
import com.faucet.greendao.database.CommonUtil;
import com.faucet.greendao.database.MigrationHelper;

import org.greenrobot.greendao.database.Database;

/*
 * Created by apple on 17/1/19.
 */

public class CommonDatabaseManager extends BasicDatabaseManager<DaoSession> {

    public static String COMMON_MODULE = "common_11";
    private static CommonDatabaseManager commonDatabaseManager;

    public static CommonDatabaseManager getInstance(){
        if(commonDatabaseManager == null){
            commonDatabaseManager = new CommonDatabaseManager();
        }
        return commonDatabaseManager;
    }

    public static CommonDatabaseManager newInstance(){
        return  new CommonDatabaseManager();
    }

    @Override
    protected String getDBFile() {
        return CommonUtil.getFileName(COMMON_MODULE,"db");
    }

    @Override
    protected SQLiteOpenHelper getSQLiteOpenHelper() {
        return new QytSQLiteOpenHelper(MyApplication.getInstance(), getDBFile(), null);
    }

    @Override
    protected void connect() {
        helper = getSQLiteOpenHelper();
        db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
    }

    private class QytSQLiteOpenHelper extends DaoMaster.OpenHelper {

        public QytSQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
            super(context, name, factory);
        }

        @Override
        public void onUpgrade(Database db, int oldVersion, int newVersion) {
            if (oldVersion < newVersion) {
                MigrationHelper.migrate(db, new MigrationHelper.ReCreateAllTableListener() {
                    @Override
                    public void onCreateAllTables(Database db, boolean ifNotExists) {
                        DaoMaster.createAllTables(db, ifNotExists);
                    }
                    @Override
                    public void onDropAllTables(Database db, boolean ifExists) {
                        DaoMaster.dropAllTables(db, ifExists);
                    }
                }, DBUserInfoDao.class, DBValidationMessageDao.class, DBCompanyUserInfoDao.class, DBCompanyUserTelInfoDao.class, DBCompanyUserEmailInfoDao.class);
            }
        }
    }
}
