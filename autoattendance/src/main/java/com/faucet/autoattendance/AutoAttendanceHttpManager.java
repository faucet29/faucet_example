package com.faucet.autoattendance;

import com.faucet.quickutils.core.http.HttpManager;
import com.faucet.quickutils.utils.SPUtils;

import java.util.HashMap;

public class AutoAttendanceHttpManager extends HttpManager {

    public static AutoAttendanceHttpManager manager;

    private AutoAttendanceHttpManager(){

    }

    public synchronized static AutoAttendanceHttpManager getInstance() {
        if (manager == null) {
            manager = new AutoAttendanceHttpManager();
        }
        return manager;
    }

    @Override
    public HashMap<String, String> getHeaders() {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json");
        headers.put("Content-Type", "application/json");
        headers.put("platform", "mobile");
        headers.put("Authorization", MyApplication.getInstance().spUtils.getString("token", ""));
        headers.put("SZ-Version", "v4.1.0");
        return headers;
    }
}
