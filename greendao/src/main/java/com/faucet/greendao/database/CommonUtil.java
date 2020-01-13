package com.faucet.greendao.database;

import android.text.TextUtils;

/**
 * Created by chinaxin on 2017/1/20.
 */

public class CommonUtil
{
    public static String COMMON_MODULE = "common_11";
    public static String getFileName(String filename, String extension)
    {
        long companyId = 123;
        long userId = 456;

        String file = "";
        if (!TextUtils.isEmpty(extension))
            file = String.format("sz1_%s_%d_%d_%d.%s", filename, 1, companyId, userId, extension);
        else
            file = String.format("sz1_%s_%d_%d_%d", filename, 1, companyId, userId);

        return file;
    }

    public static String getFileName(String filename)
    {
        return getFileName(filename, "");
    }

    public static String defaultHeaderUrl() {
        return "http://sz-asset-new.oss-cn-hangzhou.aliyuncs.com/card/join.jpg";
    }
}
