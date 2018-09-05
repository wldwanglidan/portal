package com.tempus.portal.base.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import com.tempus.portal.R;
import com.tempus.portal.app.Constant;
import com.tempus.portal.base.BaseActivity;
import com.tempus.portal.manager.DataManager;
import com.tempus.portal.manager.UserManager;
import com.tempus.portal.model.OrderInfo;
import com.tempus.portal.ui.user.LoginActivity;
import com.tempus.portal.view.dialog.ShareDialogFragment;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;

import static android.content.Context.ACTIVITY_SERVICE;
import static android.content.Context.KEYGUARD_SERVICE;
import static com.tempus.portal.app.Constant.REQUEST_CODE_T_PAY;

/**
 * <pre>
 *     author: Blankj
 *     blog  : http://blankj.com
 *     time  : 2016/8/2
 *     desc  : App相关工具类
 * </pre>
 */
public class AppUtils {

    private AppUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }


    /**
     * 安装App(支持6.0)
     *
     * @param context 上下文
     * @param filePath 文件路径
     */
    public static void installApp(Context context, String filePath) {
        installApp(context, FileUtils.getFileByPath(filePath));
    }


    /**
     * 安装App(支持6.0)
     *
     * @param context 上下文
     * @param file 文件
     */
    public static void installApp(Context context, File file) {
        if (!FileUtils.isFileExists(file)) return;
        context.startActivity(IntentUtils.getInstallAppIntent(file));
    }


    /**
     * 安装App(支持6.0)
     *
     * @param activity activity
     * @param filePath 文件路径
     * @param requestCode 请求值
     */
    public static void installApp(Activity activity, String filePath, int requestCode) {
        installApp(activity, FileUtils.getFileByPath(filePath), requestCode);
    }


    /**
     * 安装App(支持6.0)
     *
     * @param activity activity
     * @param file 文件
     * @param requestCode 请求值
     */
    public static void installApp(Activity activity, File file, int requestCode) {
        if (!FileUtils.isFileExists(file)) return;
        activity.startActivityForResult(IntentUtils.getInstallAppIntent(file),
                requestCode);
    }


    /**
     * 获取App包名
     *
     * @param context 上下文
     * @return App包名
     */
    public static String getAppPackageName(Context context) {
        return context.getPackageName();
    }


    /**
     * 获取App名称
     *
     * @param context 上下文
     * @return App名称
     */
    public static String getAppName(Context context) {
        return getAppName(context, context.getPackageName());
    }


    /**
     * 获取App名称
     *
     * @param context 上下文
     * @param packageName 包名
     * @return App名称
     */
    public static String getAppName(Context context, String packageName) {
        if (StringUtils.isSpace(packageName)) return null;
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(packageName, 0);
            return pi == null
                   ? null
                   : pi.applicationInfo.loadLabel(pm).toString();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 获取App版本号
     *
     * @param context 上下文
     * @return App版本号
     */
    public static String getAppVersionName(Context context) {
        return getAppVersionName(context, context.getPackageName());
    }


    /**
     * 获取App版本号
     *
     * @param context 上下文
     * @param packageName 包名
     * @return App版本号
     */
    public static String getAppVersionName(Context context, String packageName) {
        if (StringUtils.isSpace(packageName)) return null;
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(packageName, 0);
            return pi == null ? null : pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 获取App版本码
     *
     * @param context 上下文
     * @return App版本码
     */
    public static int getAppVersionCode(Context context) {
        return getAppVersionCode(context, context.getPackageName());
    }


    /**
     * 获取App版本码
     *
     * @param context 上下文
     * @param packageName 包名
     * @return App版本码
     */
    public static int getAppVersionCode(Context context, String packageName) {
        if (StringUtils.isSpace(packageName)) return -1;
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(packageName, 0);
            return pi == null ? -1 : pi.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return -1;
        }
    }


    /**
     * 清除App所有数据
     *
     * @param context 上下文
     * @param dirPaths 目录路径
     * @return {@code true}: 成功<br>{@code false}: 失败
     */
    public static boolean cleanAppData(Context context, String... dirPaths) {
        File[] dirs = new File[dirPaths.length];
        int i = 0;
        for (String dirPath : dirPaths) {
            dirs[i++] = new File(dirPath);
        }
        return cleanAppData(context, dirs);
    }


    /**
     * 清除App所有数据
     *
     * @param context 上下文
     * @param dirs 目录
     * @return {@code true}: 成功<br>{@code false}: 失败
     */
    public static boolean cleanAppData(Context context, File... dirs) {
        boolean isSuccess = CleanUtils.cleanInternalCache(context);
        isSuccess &= CleanUtils.cleanInternalDbs(context);
        isSuccess &= CleanUtils.cleanInternalSP(context);
        isSuccess &= CleanUtils.cleanInternalFiles(context);
        isSuccess &= CleanUtils.cleanExternalCache(context);
        for (File dir : dirs) {
            isSuccess &= CleanUtils.cleanCustomCache(dir);
        }
        return isSuccess;
    }


    public static float getDensity(Context mContext) {
        DisplayMetrics displayMetrics = mContext.getResources()
                                                .getDisplayMetrics();
        return displayMetrics.density;
    }


    /**
     * 执行拨打电话的方法
     */
    public static void callPhone(Context context, String phone) {
        //判断电话号码是否为空，为空则弹出对话框，警告用户
        if (TextUtils.isEmpty(phone)) {
            return;
        }
        //2-调用手机系统里面的拨打电话的拨号盘拨出电话
        Intent intent = new Intent();//意图
        intent.setAction(Intent.ACTION_CALL);//我的意图是想拨打电话
        intent.setData(Uri.parse("tel:" + phone));//设置需要拨打的号码

        //执行意图（拨打电话）
        context.startActivity(intent);//执行一个跳转画面的动作
    }


    /**
     * 字节转换成相应大小的MB,KB
     */
    public static String bytes2Convert(long bytes) {
        BigDecimal filesize = new BigDecimal(bytes);
        BigDecimal gbyte = new BigDecimal(1024 * 1024 * 1024);
        float returnValue = filesize.divide(gbyte, 2, BigDecimal.ROUND_UP)
                                    .floatValue();
        if (returnValue >= 1) {
            return (returnValue + "GB");
        }
        BigDecimal megabyte = new BigDecimal(1024 * 1024);
        returnValue = filesize.divide(megabyte, 2, BigDecimal.ROUND_UP)
                              .floatValue();
        if (returnValue >= 1) {
            return (returnValue + "MB");
        }
        BigDecimal kilobyte = new BigDecimal(1024);
        returnValue = filesize.divide(kilobyte).intValue();
        return (returnValue + "KB");
    }


    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources()
                                .getIdentifier("status_bar_height", "dimen",
                                        "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }


    //保留2位小数
    public static String formatFloat2String(double price) {
        //NumberFormat df = DecimalFormat.getNumberInstance();
        //df.setMinimumFractionDigits(2);
        //df.setMaximumFractionDigits(2);
        //String s = df.format(price);
        //return s;
        DecimalFormat decimalFormat = new DecimalFormat(
                ".00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
        String s = decimalFormat.format(price);//format 返回的是字符串
        if (s.startsWith(".")) {
            s = "0" + s;
        }
        return s;
    }


    /**
     * 启动T钱包App
     */
    public static void launchTPayApp(Context context, OrderInfo orderInfo) {
        // 判断是否安装过App，否则去市场下载
        if (isAppInstalled(context, Constant.T_APP_PACKAGE_NAME)) {
            try {
                if (context.getPackageManager()
                           .getPackageInfo(Constant.T_APP_PACKAGE_NAME,
                                   0).versionCode >= 1) {//上线改为10

                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    //if (!isBackgroundRunning(context)) {
                    //    intent.setAction("android.intent.action.MAIN");
                    //    intent.addCategory(Intent.CATEGORY_LAUNCHER);
                    //}
                    //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                    //        Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                    //intent.setClassName(Constant.T_APP_PACKAGE_NAME,
                    //        Constant.T_APP_CLASS_NAME);
                    intent.setComponent(
                            new ComponentName(Constant.T_APP_PACKAGE_NAME,
                                    Constant.T_APP_CLASS_NAME));
                    intent.putExtra("orderId", orderInfo.orderId);
                    intent.putExtra("money", orderInfo.orderPrice);
                    intent.putExtra("orderDesc", orderInfo.orderName);
                    intent.putExtra("tcoinFlag", orderInfo.supportTcoin);
                    intent.putExtra("source", "portal");
                    ((Activity) context).startActivityForResult(intent,
                            REQUEST_CODE_T_PAY);
                }
                else {
                    ToastUtils.showLongToast("安装最新的T钱包,才可以T钱包支付哦!");
                }
            } catch (PackageManager.NameNotFoundException e) {
                ToastUtils.showLongToast("安装最新的T钱包,才可以T钱包支付哦!");
            }
        }
        else {
            jumpAppMarket(context, Constant.T_APP_PACKAGE_NAME);
        }
    }


    //在启动前进行一次判断：app是否在后台运行。一下是判断app是否运行的方法：
    public static boolean isBackgroundRunning(Context context) {
        String processName = Constant.T_APP_PACKAGE_NAME;
        ActivityManager activityManager
                = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
        KeyguardManager keyguardManager = (KeyguardManager) context.
                                                                           getSystemService(
                                                                                   KEYGUARD_SERVICE);
        if (activityManager == null) return false;
        // get running application processes
        List<ActivityManager.RunningAppProcessInfo> processList
                = activityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo process : processList) {
             /*
            BACKGROUND=400 EMPTY=500 FOREGROUND=100
            GONE=1000 PERCEPTIBLE=130 SERVICE=300 ISIBLE=200
             */
            if (process.processName.startsWith(processName)) {
                boolean isBackground = process.importance !=
                        ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND &&
                        process.importance !=
                                ActivityManager.RunningAppProcessInfo.IMPORTANCE_VISIBLE;
                boolean isLockedState
                        = keyguardManager.inKeyguardRestrictedInputMode();
                if (isBackground || isLockedState) {
                    return true;
                }
                else {
                    return false;
                }
            }
        }
        return false;
    }


    public static void launchTPayApp(Context context) {
        // 判断是否安装过App，否则去市场下载
        if (isAppInstalled(context, Constant.T_APP_PACKAGE_NAME)) {
            Intent intent = new Intent();
            intent.setData(Uri.parse("launchapp://wallet"));
            context.startActivity(intent);
        }
        else {
            jumpAppMarket(context, Constant.T_APP_PACKAGE_NAME);
        }
    }


    /**
     * 检测某个应用是否安装
     */
    public static boolean isAppInstalled(Context context, String packageName) {
        try {
            context.getPackageManager().getPackageInfo(packageName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }


    /**
     * 去市场下载页面
     */
    public static void jumpAppMarket(Context context, String packageName) {
        //这里开始执行一个应用市场跳转逻辑，默认this为Context上下文对象
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("market://details?id=" + packageName));
        //跳转到应用市场，非Google Play市场一般情况也实现了这个接口
        //intent.setData(Uri.parse(
        //        " market://comments")); //跳转到应用的评论页:
        //存在手机里没安装应用市场的情况，跳转会包异常，做一个接收判断
        if (intent.resolveActivity(context.getPackageManager()) !=
                null) { //可以接收
            context.startActivity(intent);
        }
        else {
            ToastUtils.showLongToast("您的系统中没有安装应用市场");
        }
    }


    public static String getJson(Context context, String fileName) {

        StringBuilder stringBuilder = new StringBuilder();
        try {
            AssetManager assetManager = context.getAssets();
            BufferedReader bf = new BufferedReader(
                    new InputStreamReader(assetManager.open(fileName)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }


    /**
     * 计算出来的位置，y方向就在anchorView的上面和下面对齐显示，x方向就是与屏幕右边对齐显示
     * 如果anchorView的位置有变化，就可以适当自己额外加入偏移来修正
     *
     * @param anchorView 呼出window的view
     * @param contentView window的内容布局
     * @return window显示的左上角的xOff, yOff坐标
     */
    public static int[] calculatePopWindowPos(final View anchorView, final View contentView) {
        final int windowPos[] = new int[2];
        final int anchorLoc[] = new int[2];
        // 获取锚点View在屏幕上的左上角坐标位置
        anchorView.getLocationOnScreen(anchorLoc);
        final int anchorHeight = anchorView.getHeight();
        // 获取屏幕的高宽
        final int screenHeight = ScreenUtils.getScreenHeight(
                anchorView.getContext());
        final int screenWidth = ScreenUtils.getScreenWidth(
                anchorView.getContext());
        contentView.measure(View.MeasureSpec.UNSPECIFIED,
                View.MeasureSpec.UNSPECIFIED);
        // 计算contentView的高宽
        final int windowHeight = contentView.getMeasuredHeight();
        final int windowWidth = contentView.getMeasuredWidth();
        // 判断需要向上弹出还是向下弹出显示
        final boolean isNeedShowUp = (
                screenHeight - anchorLoc[1] - anchorHeight < windowHeight);
        if (isNeedShowUp) {
            windowPos[0] = screenWidth - windowWidth;
            windowPos[1] = anchorLoc[1] - windowHeight;
        }
        else {
            windowPos[0] = screenWidth - windowWidth;
            windowPos[1] = anchorLoc[1] + anchorHeight;
        }
        return windowPos;
    }


    public static void getPopupWindow(View view, Context context, String merchId, String name, String url) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.pop_share, null);
        final PopupWindow popupWindow = new PopupWindow(contentView,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        contentView.measure(View.MeasureSpec.UNSPECIFIED,
                View.MeasureSpec.UNSPECIFIED);
        int popupWidth = contentView.getMeasuredWidth();
        int popupHeight = contentView.getMeasuredHeight();
        int windowPos[] = AppUtils.calculatePopWindowPos(view, contentView);
        int xOff = 30;// 可以自己调整偏移
        windowPos[0] -= xOff;
        int[] location = new int[2];
        // 允许点击外部消失
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        // 获得位置
        //view.getLocationOnScreen(location);
        //popupWindow.showAtLocation(view, Gravity.NO_GRAVITY,
        //        (location[0] + view.getWidth() / 2) - popupWidth / 2,
        //        location[1] - popupHeight);
        popupWindow.showAtLocation(view, Gravity.TOP | Gravity.START,
                windowPos[0], windowPos[1]);
        TextView tvCollection = contentView.findViewById(R.id.tvCollection);
        tvCollection.setOnClickListener(v -> {
            popupWindow.dismiss();
            if (!UserManager.getInstance().isLogin()) {
                context.startActivity(new Intent(context, LoginActivity.class));
                return;
            }
            DataManager.addCollection(merchId, name, url).
                    subscribe(((BaseActivity) context).getNotProSubscribe(b -> {
                        ToastUtils.showLongToast("收藏成功!");
                    }));
        });
        contentView.findViewById(R.id.tvShare).setOnClickListener(view1 -> {
            popupWindow.dismiss();
            ShareDialogFragment shareDialogFragment
                    = ShareDialogFragment.newInstance(url, name);
            shareDialogFragment.show(
                    ((BaseActivity) context).getSupportFragmentManager(),
                    "shareDialogFragment");
        });
    }
}
