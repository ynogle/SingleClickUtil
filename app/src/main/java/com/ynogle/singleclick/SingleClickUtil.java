package com.ynogle.singleclick;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;

/**
 * 点击仿抖动
 *
 * @author ynogle
 * @since 2020/2/11.
 */
public class SingleClickUtil {

    //最近一次发生事件的target
    private static String mLastTargetName;

    //最近一次点击的时间
    private static long mLastClickTime;

    //最近一次点击的控件ID
    private static int mLastClickViewId;

    // 默认 500 ms
    private static final int INTERVAL_MILLIS = 500;

    /**
     * 是否是快速点击
     *
     * @param v 点击控件
     * @return true:是，false:不是
     */
    public static boolean isFastDoubleClick(View v) {
        return isFastDoubleClick(v.getClass(), v, INTERVAL_MILLIS);
    }

    /**
     * 是否是快速点击
     *
     * @param v              点击控件
     * @param intervalMillis 时间间隔（ms）
     * @return true:是，false:不是
     */
    public static boolean isFastDoubleClick(View v, long intervalMillis) {
        return isFastDoubleClick(v.getClass(), v, intervalMillis);
    }

    /**
     * 是否是快速点击
     *
     * @param target         目标类
     * @param v              点击控件
     * @param intervalMillis 时间间隔（ms）
     * @return true:是，false:不是
     */
    public static boolean isFastDoubleClick(Object target, View v, long intervalMillis) {
        int viewId = v.getId();
        long time = System.currentTimeMillis();
        mLastClickTime = mLastClickTime == 0 ? time : mLastClickTime;
        long timeInterval = Math.abs(time - mLastClickTime);
        //大于10ms，小于限制时间，并且是同一个view点击
        if (timeInterval > 10 && timeInterval < intervalMillis && viewId == mLastClickViewId && TextUtils.equals(getTargetHash(target), mLastTargetName)) {
            Log.d("SingleClickUtil", String.format("SingleClickUtil, 重复点击 target = [%s], v = [%s], timeInterval = [%s]", getTargetHash(target), viewId, timeInterval));
            return true;
        } else {
            Log.d("SingleClickUtil", String.format("SingleClickUtil, 单次点击 target = [%s], v = [%s], timeInterval = [%s]", getTargetHash(target), viewId, timeInterval));
            mLastTargetName = getTargetHash(target);
            mLastClickTime = time;
            mLastClickViewId = viewId;
            return false;
        }
    }

    /**
     * 列表item是否是快速点击
     *
     * @param v        点击控件
     * @param position 控件位置
     * @return true:是，false:不是
     */
    public static boolean isFastDoubleClick(View v, int position) {
        return isFastDoubleClick(v, position, INTERVAL_MILLIS);
    }

    /**
     * 列表item是否是快速点击
     *
     * @param v              点击控件
     * @param position       控件位置
     * @param intervalMillis 时间间隔（ms）
     * @return true:是，false:不是
     */
    public static boolean isFastDoubleClick(View v, int position, long intervalMillis) {
        int viewId = v.getId();
        Object target = v.getClass();
        long time = System.currentTimeMillis();
        mLastClickTime = mLastClickTime == 0 ? time : mLastClickTime;
        long timeInterval = Math.abs(time - mLastClickTime);
        //大于10ms，小于时间间隔，并且是同一个view点击
        if (timeInterval > 10 && timeInterval < intervalMillis && viewId == mLastClickViewId && TextUtils.equals(getListTargetHash(target, position), mLastTargetName)) {
            Log.d("SingleClickUtil", String.format("SingleClickUtil, 重复点击 target = [%s], v = [%s$%s], timeInterval = [%s]", getListTargetHash(target, position), viewId, position, timeInterval));
            return true;
        } else {
            Log.d("SingleClickUtil", String.format("SingleClickUtil, 单次点击 target = [%s], v = [%s$%s], timeInterval = [%s]", getListTargetHash(target, position), viewId, position, timeInterval));
            mLastTargetName = getListTargetHash(target, position);
            mLastClickTime = time;
            mLastClickViewId = viewId;
            return false;
        }
    }

    private static String getListTargetHash(Object object, int position) {
        return String.format("%s@%s$%s", object.getClass().getName(), object.hashCode(), position);
    }

    private static String getTargetHash(Object object) {
        return String.format("%s@%s", object.getClass().getName(), object.hashCode());
    }
}