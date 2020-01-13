package com.faucet.quickutils.core.manager;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 观察者管理类
 */
public abstract class BaseManager {

    protected List<Object> mObservers = new ArrayList<>();

    protected Handler handler = new Handler(Looper.getMainLooper());

    private HashMap<String, List<Object>> callBackMap = new HashMap<>();

    protected void addCallBackListener(String key, Object value) {
        List<Object> list = callBackMap.get(key);
        if (list == null) {
            list = new ArrayList<>();
            callBackMap.put(key,list);
        }
        list.add(value);
    }

    protected void removeCallBackListener(String key) {
        callBackMap.remove(key);
    }

    protected List<Object> fetchCallBackListeners(String key) {
        return callBackMap.get(key);
    }

    /**
     * 注册
     * @param observer
     */
    public void register(Object observer){
        if (observer == null) {
            throw new IllegalArgumentException("The observer is null.");
        }
        synchronized(mObservers) {
            if (!mObservers.contains(observer)) {
                mObservers.add(observer);
            }
        }
    }

    /**
     * 移除注册
     * @param observer
     */
    public void unregister(Object observer){
        if (observer == null) {
            return;
        }
        synchronized(mObservers) {
            int index = mObservers.indexOf(observer);
            if (index != -1) {
                mObservers.remove(index);
            }
        }
    }

    /**
     * 移除所有
     */
    public void unregisterAll() {
        synchronized(mObservers) {
            mObservers.clear();
        }
    }

    /**
     * 获取注册的全部监听
     * @return
     */
    protected List<Object> getObservers(){
        return mObservers;
    }

    /**
     * 通知所有监听者
     * @param strMethod 要通知的方法
     * @param args 要通知的参数
     */
    protected void notifyAllObservers(String strMethod, Object... args) {
        List<Class> argTypeList = new ArrayList<>();
        List<Object> argValueList = new ArrayList<>();
        int argCount = 0;
        if (args != null) {
            for (Object arg : args) {
                argTypeList.add(arg.getClass());
                argValueList.add(arg);
                ++argCount;
            }
        }

        Class[] argTypes = new Class[argCount];
        Object[] argValues = new Object[argCount];

        if (!argTypeList.isEmpty())
            argTypes = argTypeList.toArray(argTypes);
        if (!argValueList.isEmpty())
            argValues = argValueList.toArray();

        List<Object> listeners = new ArrayList<>();
        listeners.addAll(this.getObservers());
        for (Object listener : listeners) {
            if (listener != null) {
                Class cls = listener.getClass();
                try{
                    Method method = cls.getMethod(strMethod, argTypes);
                    if (method != null)
                        method.invoke(listener, argValues);
                }catch (Exception e)
                {
                    Log.e("BaseManager", e.toString());
                }
            }
        }
    }

    /**
     * 主线程通知所有监听者
     * @param strMethod
     * @param args
     */
    protected void notifyAllOnMainThread(final String strMethod, final Object... args) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                notifyAllObservers(strMethod,args);
            }
        });
    }

    public static void clearInstance() {

    }

    /**
     *
     */
    public void setup()
    {

    }
}
