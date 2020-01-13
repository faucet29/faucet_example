package com.shusheng.dbmaker.module.common;

import com.shusheng.dbmaker.common.DatabaseMaker;
import com.shusheng.dbmaker.module.common.properties.DBUserInfo;

import org.greenrobot.greendao.generator.DaoGenerator;
import org.greenrobot.greendao.generator.Schema;


/**
 * Created by linxinghuang on 2017/6/22.
 */

public class FieldMaker extends DatabaseMaker {

    public static void main(String[] args) {

        int version = getVersion(FieldMaker.class.getSimpleName());
        version++;
        Schema schema = new Schema(version, "com.faucet.greendao.bean");
        schema.setDefaultJavaPackageDao("com.faucet.greendao.dao");
        schema.enableKeepSectionsByDefault();

        //添加表
        new DBUserInfo().create(schema);

        try {
            new DaoGenerator().generateAll(schema, "./greendao/src/main/java/");
            writeVersion(FieldMaker.class.getSimpleName(), version);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
