package com.tempus.portal.manager;

import android.text.TextUtils;
import android.util.Log;
import com.tempus.portal.app.App;
import com.tempus.portal.app.Constant;
import com.tempus.portal.base.utils.ACache;
import com.tempus.portal.base.utils.ActivityUtils;
import com.tempus.portal.base.utils.ToastUtils;
import com.tempus.portal.model.AccessTokenInfo;
import com.tempus.portal.model.QqUserInfo;
import com.tempus.portal.model.User;
import com.tempus.portal.model.WxUserInfo;
import com.tempus.portal.model.event.LoginEvent;
import com.tempus.portal.ui.user.LoginActivity;
import io.reactivex.Observable;
import org.greenrobot.eventbus.EventBus;

/**
 * <用户管理类>
 *
 * @author chenml@cncn.com
 * @data: 2015/11/18 14:23
 * @version: V1.0
 */
public class UserManager {
    private static final String TAG = "UserManager";
    private static volatile UserManager mInstance;
    private User mUser;
    // private MyInfo mMyInfo;
    private ACache mACache;


    public static UserManager getInstance() {
        if (mInstance == null) {
            synchronized (UserManager.class) {
                if (mInstance == null) {
                    mInstance = new UserManager();
                }
            }
        }
        return mInstance;
    }


    private UserManager() {
        mACache = App.getACache();
    }

    //public int getUserId() {
    //    if (mUser != null) {
    //        return mUser;
    //    }
    //    else {
    //        return 0;
    //    }
    //}


    //判断常登陆状态是否为微信登陆
    public boolean isWxLogin() {
        return isLogin() && mUser.isWxLogin();
    }


    public void fillUser() {
        // String userJson = SpUtil.getUserJson();
        User user = (User) mACache.getAsObject(Constant.KEY_USER);
        if (user != null) {
            this.mUser = user;
        }
    }


    public void setUserInfo(User user) {
        try {
            //this.mMyInfo = null;
            this.mUser = user;
            if (user != null) {
                mACache.put(Constant.KEY_USER, user);
            }
            else {
                mACache.remove(Constant.KEY_USER);
            }
        } catch (Exception e) {
            Log.d(TAG, "setUserInfo=" + e);
        }
    }


    public boolean isLogin() {
        return mUser != null;
    }


    public boolean isJumpLoginPage() {
        if (mUser == null) {
            ActivityUtils.launchActivity(App.getInstance(),
                    LoginActivity.class);
        }
        else {
            return false;
        }
        return false;
    }


    //登录
    public Observable<User> login(String mobile, String smsCode) {
        return DataManager.login(mobile, smsCode).flatMap(user -> {
            setUserInfo(user);
            return Observable.just(user);
        });
    }


    //第三方用户绑定手机号
    public Observable<User> bindPhone(String unionId, String mobile, String smsCode) {
        return DataManager.bindPhone(unionId, mobile, smsCode).flatMap(user -> {
            setUserInfo(user);
            return Observable.just(user);
        });
    }


    //微信登录
    public Observable<User> wxLogin(WxUserInfo wxUserInfo) {
        return DataManager.otherLogin(wxUserInfo).flatMap(userResponse -> {
            User user = userResponse;
            if (!TextUtils.isEmpty(userResponse.userId)) {
                user.isWxLogin = true;
                setUserInfo(user);
            }
            return Observable.just(user);
        });
    }


    //qq登录
    public Observable<User> qqLogin(QqUserInfo qqUserInfo) {
        return DataManager.otherLogin(qqUserInfo).flatMap(userResponse -> {
            User user = userResponse;
            if (!TextUtils.isEmpty(userResponse.userId)) {
                user.isQq = true;
                setUserInfo(user);
            }
            return Observable.just(user);
        });
    }


    public interface SignOutListener {
        void signOutSuccess();

        void signOutFailure();
    }

    //退出登录
    //public void loginOut(SignOutListener signOutListener) {
    //    SimpleManager.loginOut()
    //                 .subscribe(new CSubscriber<BaseTransactionStatus>() {
    //                     @Override public void onPrepare() {
    //
    //                     }
    //
    //
    //                     @Override
    //                     public void onError(ErrorThrowable throwable) {
    //                         if (signOutListener != null) {
    //                             signOutListener.signOutFailure();
    //                         }
    //                     }
    //
    //
    //                     @Override
    //                     public void onSuccess(BaseTransactionStatus baseTransactionStatus) {
    //                         if (baseTransactionStatus.transactionStatus.errorCode ==
    //                                 0) {
    //                             // 获取所有会话，包括陌生人
    //                             Map<String, EMConversation>
    //                                     conversations
    //                                     = EMClient
    //                                     .getInstance()
    //                                     .chatManager()
    //                                     .getAllConversations();
    //                             for (EMConversation conversation : conversations
    //                                     .values()) {
    //                                 EMClient.getInstance()
    //                                         .chatManager()
    //                                         .deleteConversation(
    //                                                 conversation
    //                                                         .getUserName(),
    //                                                 false);
    //                             }
    //                             HxHelper.getInstance().logout(false, null);
    //                             setUserInfo(null);
    //                             //TokenManager.getInstance()
    //                             //            .resetToken();
    //                             RxBus.$().post(Constant.EVENT_LOGIN, null);
    //                             if (signOutListener != null) {
    //                                 signOutListener.signOutSuccess();
    //                             }
    //                         }
    //                         else {
    //                             if (signOutListener != null) {
    //                                 signOutListener.signOutFailure();
    //                             }
    //                         }
    //                     }
    //                 });
    //}

    //获取我的信息
    //public MyInfo getMyInfoLocal() {
    //    return mMyInfo;
    //}

    public void loginOut() {
        setUserInfo(null);
        TokenManager.getInstance().resetToken();
        EventBus.getDefault().post(new LoginEvent());
        ToastUtils.showLongToast("退出登录成功!");
    }


    //退出登录
    //public void loginOut() {
    //
    //}


    public User getUserInfo() {
        return mUser;
    }

    //获取我的信息
    //public Observable<MyInfo> getMyInfo(boolean reset) {
    //    if (!reset && mMyInfo != null) {
    //        return Observable.just(mMyInfo);
    //    }
    //    mMyInfo = null;
    //    return DataManager.getMyMessage()
    //                      .doOnNext(myInfo -> this.mMyInfo = myInfo);
    //}


    public static Observable<User> getRefreshLoginParams(AccessTokenInfo tokenInfo) {
        //return Observable.create(subscriber -> {
        //    User User = UserManager.getInstance().getUserInfo();
        //    String timestamp = String.valueOf(
        //            System.currentTimeMillis() / 1000);
        //    String signature = Md5Tool.MD5(User.authenUserId + timestamp +
        //            tokenInfo.sessionSecret);
        //    subscriber.onNext(User.authenUserId);
        //    subscriber.onNext(timestamp);
        //    subscriber.onNext(User.authenTicket);
        //    subscriber.onNext(signature);
        //    subscriber.onCompleted();
        //})
        //                 .map(o -> (String) o)
        //                 .buffer(4)
        //                 .flatMap(strings -> DataManager.refreshLogin(strings))
        //                 .flatMap(user -> {
        //                     String displayName = user.displayName;
        //                     //UserManager.getInstance().setUserInfo(user);
        //                     user.displayName = displayName;
        //                     return Observable.just(user);
        //                 });
        return null;
    }
}