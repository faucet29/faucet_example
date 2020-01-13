package com.faucet.quickutils.utils;

import android.app.Activity;

import java.util.Iterator;
import java.util.Stack;

/**
 * Created by Amethy on 15/6/16.
 */
public class WActivityManager {

    private Stack<Activity> activityStack;
    private static WActivityManager sInstance;

    public static WActivityManager getInstance() {
        if (sInstance == null) {
            sInstance = new WActivityManager();
        }
        return sInstance;
    }

    public static void clearInstance() {
        sInstance.activityStack.clear();
        sInstance = null;
    }

    private WActivityManager() {
        this.activityStack = new Stack<>();
    }

    /**
     * 添加Activity到堆栈
     */
    public void addActivity(Activity act) {
        if (act != null) {
            activityStack.push(act);
        }
    }

    /**
     * 移除Activity
     * @param act
     */
    public void removeActivity(Activity act) {
        if (act != null) {
            activityStack.remove(act);
        }
    }
    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public Activity currentActivity(){
        if (activityStack.isEmpty())
            return null;
        else{
            return activityStack.lastElement();
        }
    }


    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    public void finishActivity(){
        Activity activity=activityStack.lastElement();
        finishActivity(activity);
    }
    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity){
        if(activity!=null){
            activity.finish();
        }
    }

    public boolean inActivityStack(Class<?> cls){
        for (Activity activity : activityStack) {
            if(activity.getClass().equals(cls) ){
                return true;
            }
        }
        return false;
    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls){
        Stack<Activity> activityStackCopy = (Stack<Activity>) activityStack.clone();
        Iterator<Activity> listActivity = activityStackCopy.iterator();
        while (listActivity.hasNext()){
            Activity activity = listActivity.next();
            if(activity!=null&&activity.getClass().equals(cls)){
                activity.finish();
            }
        }
    }
    /**
     * 结束所有Activity
     */
    public void finishAllActivity(){
        Stack<Activity> activityStackCopy = (Stack<Activity>) activityStack.clone();
        Iterator<Activity> it = activityStackCopy.iterator();
        while (it.hasNext()){
            Activity activity = it.next();
            if(activity!=null){
                activity.finish();
            }
        }
    }

    public void toMainActivity(){
        Stack<Activity> activityStackCopy = (Stack<Activity>) activityStack.clone();
        Iterator<Activity> it = activityStackCopy.iterator();
        while (it.hasNext()){
            Activity activity = it.next();
//            if(activity!=null&&!activity.getClass().equals(MainActivity.class)){
//                activity.finish();
//            }
        }
    }
}
