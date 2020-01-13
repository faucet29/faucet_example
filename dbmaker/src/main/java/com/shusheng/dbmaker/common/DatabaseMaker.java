package com.shusheng.dbmaker.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by mac on 16/9/6.
 */
public abstract class DatabaseMaker {

    private static String path = "./dbmaker/versions/";

    protected static int getVersion(String name){
        File dir = new File(path);
        if(!dir.exists()){
            dir.mkdir();
        }
        File file = new File(path+name);
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        int str = 0;
        try {
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            str = Integer.parseInt(bufferedReader.readLine());
            fileReader.close();
        } catch (Exception e) {
            writeVersion(name,str);
        }
        return str;
    }

    /**
     * 写入新版本号
     * @param version
     */
    protected static void writeVersion(String name,int version){
        try {
            String str = String.valueOf(version);
            FileWriter fileWriter = new FileWriter(path+name);
            fileWriter.write(str);
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.getStackTrace();
        }
    }
}
