package com.tempus.portal.base.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.tempus.portal.base.BaseFragment;

/**
 * <pre>
 *     author: Blankj
 *     blog  : http://blankj.com
 *     time  : 2016/9/23
 *     desc  : Activity相关工具类
 * </pre>
 */
public class ActivityUtils {

    public static boolean launchActivity(Context context, Class<? extends Activity> cls) {
        return launchActivity(context, cls, null);
    }

    public static boolean launchActivity(Context context, Class<? extends Activity> cls, Bundle bundle) {
        return launchActivity(context, cls, bundle, 0);
    }

    public static boolean launchActivity(Context context, Class<? extends Activity> cls, Bundle bundle, int requestCode) {
        if (NoDoubleClickUtils.isDoubleClick()) {
            return false;
        }
        Intent intent = new Intent(context, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        if (requestCode != 0) {
            ((Activity) context).startActivityForResult(intent, requestCode);
        } else {
            context.startActivity(intent);
        }
        return true;
    }

    public static boolean launchActivityFromFragment(BaseFragment context, Class<? extends Activity> cls, Bundle bundle, int requestCode) {
        if (NoDoubleClickUtils.isDoubleClick()) {
            return false;
        }
        Intent intent = new Intent(context.getContext(), cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        if (requestCode != 0) {
            context.startActivityForResult(intent, requestCode);
        } else {
            context.startActivity(intent);
        }
        return true;
    }
}
