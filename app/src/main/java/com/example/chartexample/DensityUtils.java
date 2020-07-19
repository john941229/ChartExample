package com.example.chartexample;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;

import java.lang.reflect.Method;

/**
 * Alibaba.com Inc.
 * Copyright (c) 1999-2019 All Rights Reserved.
 *
 * @author panes
 * @contact pt135794@alibaba-inc.com
 */
public final class DensityUtils {
    private static Context mContext;
    public static int screenW;
    public static int screenH;

    private static boolean hasInit = false;
    private static float density;//屏幕密度，dip/160
    private static Display display;
    private static int densityDpi;//dpi，像素密度
    private static DisplayMetrics displayMetrics;

    /**
     * dip转像素
     *
     * @param dipValue 指定dip值
     * @return 转换后的像素
     */
    public static int dip2px(float dipValue) {
        if (!checkInit()) {
            return 0;
        }

        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dipValue, displayMetrics);
    }

    /**
     * 像素转dip
     *
     * @param pxValue 指定像素值
     * @return 转换后的dip
     */
    public static int px2dip(float pxValue) {
        if (!checkInit()) {
            return 0;
        }
        return (int) (pxValue / density);
    }

    /**
     * 获取屏幕原始尺寸高度，包括虚拟功能键高度
     *
     * @param context
     * @return
     */
    public static int getDpi(Context context) {
        int dpi = 0;
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        @SuppressWarnings("rawtypes")
        Class c;
        try {
            c = Class.forName("android.view.Display");
            @SuppressWarnings("unchecked")
            Method method = c.getMethod("getRealMetrics", DisplayMetrics.class);
            method.invoke(display, displayMetrics);
            dpi = displayMetrics.heightPixels;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dpi;
    }

    /**
     * 获取 虚拟按键的高度
     *
     * @return
     */
    public static int getBottomStatusHeight() {
        if (!checkInit()) {
            return 0;
        }
        int totalHeight = getDpi(mContext);
        int contentHeight = getScreenHeight();
        return totalHeight - contentHeight;
    }

    /**
     * 标题栏高度
     *
     * @return
     */
    public static int getTitleHeight(Activity activity) {
        return activity.getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop();
    }

    /**
     * 获得状态栏的高度
     *
     * @param context
     * @return
     */
    public static int getStatusHeight(Context context) {
        int statusHeight = -1;
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height")
                    .get(object).toString());
            statusHeight = context.getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusHeight;
    }

    private static boolean checkInit() {
        return hasInit && mContext != null;
    }


    public static void init(Context context) {
        if (context == null) {
            return;
        }
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(dm);

        if (dm.heightPixels > dm.widthPixels) {
            screenW = dm.widthPixels;
            screenH = dm.heightPixels;
        } else {
            screenW = dm.heightPixels;
            screenH = dm.widthPixels;
        }
        mContext = context;
        density = context.getResources().getDisplayMetrics().density;
        densityDpi = dm.densityDpi;
        displayMetrics = context.getResources().getDisplayMetrics();
        hasInit = true;

    }

    public static int getMinSize() {

        return Math.min(screenW, screenH);

    }


    public static int getMaxSize() {

        return Math.max(screenW, screenH);

    }

    public static int getScreenWidth() {
        return screenW;
    }

    public static int getScreenHeight() {
        return screenH;
    }

    /**
     * 获取真实的屏幕高度 包括状态栏和虚拟按钮
     *
     * @param activity
     * @return
     */
    public static int getRealScreenHeight(Activity activity) {
        DisplayMetrics metric = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getRealMetrics(metric);
        return metric.heightPixels;
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int sp2px(float spValue) {
        if (!checkInit()) {
            return 0;
        }
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                spValue, displayMetrics);
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param pxValue （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int px2sp(float pxValue) {
        if (!checkInit()) {
            return 0;
        }
        return (int) (pxValue / mContext.getResources().getDisplayMetrics().scaledDensity);
    }

    /**
     * 获取屏幕宽度的nDesignScreenWidth分之nDesignValue
     *
     * @param nDesignValue       分子
     * @param nDesignScreenWidth 分母
     * @return 屏幕宽度× nDesignValue/nDesignScreenWidth四舍五入
     */
    public static int getWidthByDesignValue(int nDesignValue, int nDesignScreenWidth) {
        return getScreenWidth() * nDesignValue / nDesignScreenWidth;
    }

    /**
     * 获取屏幕宽度的720分之nDesignValue
     *
     * @param nDesignValue 分子
     * @return 屏幕宽度× nDesignValue/720
     */
    public static int getWidthByDesignValue720(int nDesignValue) {
        return getWidthByDesignValue(nDesignValue, 720);
    }

    /**
     * 获取屏幕宽度的750分之nDesignValue
     *
     * @param nDesignValue 分子
     * @return 屏幕宽度× nDesignValue/750
     */
    public static int getWidthByDesignValue375(int nDesignValue) {
        return getWidthByDesignValue(nDesignValue, 375);
    }
}

