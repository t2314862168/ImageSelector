package cn.tangxb.imageselector.utils;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Created by tangxb on 2015/10/19.
 */
public class ScreenUtils {
    /**
     * 获取屏幕高度
     */
    public static int getScreenH(Activity activity) {
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return metrics.heightPixels;
    }

    /**
     * 获取dimen的px值
     */
    public static int getDimen(Context context, int dimenId) {
        return (int) context.getResources().getDimension(dimenId);
    }
}
