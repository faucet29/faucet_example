package com.faucet.greendao;

import android.database.sqlite.SQLiteOpenHelper;

import com.faucet.greendao.dao.DaoMaster;
import com.faucet.greendao.dao.DaoSession;
import com.faucet.greendao.database.BasicDatabaseManager;
import com.faucet.greendao.database.CommonUtil;

/*
 * Created by apple on 17/1/19.
 */

public class CommonDatabaseManager extends BasicDatabaseManager<DaoSession> {

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
        return CommonUtil.getFileName(CommonUtil.COMMON_MODULE,"db");
    }

    @Override
    protected SQLiteOpenHelper getSQLiteOpenHelper() {
        return new DaoMaster.DevOpenHelper(MyApplication.getInstance(), getDBFile(), null);
    }

    @Override
    protected void connect() {
        helper = getSQLiteOpenHelper();
        db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
    }
}
