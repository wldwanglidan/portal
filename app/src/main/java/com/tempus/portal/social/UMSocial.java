package com.tempus.portal.social;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import com.tempus.portal.base.utils.ToastUtils;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import java.io.File;
import java.util.Map;

/**
 * Created by Administrator on 2017/1/12.
 */

public class UMSocial {
    private static UMShareAPI mShareAPI = null;

    public static final int SHARE_QQ = 0;
    public static final int SHARE_QZONE = 1;
    public static final int SHARE_WECHAT = 2;
    public static final int SHARE_CIRCLE = 3;
    public static final int SHARE_SINA = 4;
    public static final int SHARE_COPY = 5;

    public static final int LOGIN_QQ = 0;
    public static final int LOGIN_WECHAT = 2;
    public static final int LOGIN_SINA = 4;
    private SHARE_MEDIA[] clients = new SHARE_MEDIA[] { SHARE_MEDIA.QQ,
            SHARE_MEDIA.QZONE, SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN,
            SHARE_MEDIA.SINA };


    public UMSocial(Context context) {
        if (context != null) {
            init(context);
        }
    }


    public void init(Context context) {
        if (mShareAPI == null) {
            mShareAPI = UMShareAPI.get(context);
        }
    }


    public void login(final Context context, final int type, final OnAutoLoginCallback callback) {
        if (callback != null) {
            if (!isClientInstalled(context, type)) {
                if (type == LOGIN_QQ) {
                    ToastUtils.showLongToast("QQ未安装");
                }
                else if (type == LOGIN_WECHAT) {
                    ToastUtils.showLongToast("微信未安装");
                }
                else if (type == LOGIN_SINA) {
                    ToastUtils.showLongToast("新浪微博未安装");
                }
                return;
            }
            mShareAPI.doOauthVerify((Activity) context, clients[type],
                    new UMAuthListener() {
                        @Override public void onStart(SHARE_MEDIA platform) {

                        }


                        @Override
                        public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
                            Log.e("TAG", "doOauthVerify:" + map.toString());
                            getUserInfo(context, type, share_media, i, map,
                                    callback);
                        }


                        @Override
                        public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
                            callback.onFailed(throwable);
                        }


                        @Override
                        public void onCancel(SHARE_MEDIA share_media, int i) {
                            callback.onCanceled();
                        }
                    });
        }
    }


    private void getUserInfo(Context context, final int type, SHARE_MEDIA share_media, int i, final Map<String, String> map, final OnAutoLoginCallback callback) {
        mShareAPI.getPlatformInfo((Activity) context, share_media,
                new UMAuthListener() {
                    @Override public void onStart(SHARE_MEDIA platform) {

                    }


                    @Override
                    public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> user) {
                        map.putAll(user);
                        Log.e("TAG", "getPlatformInfo:" + map.toString());

                        if (callback != null) {
                            callback.onSuccess(type, map);
                        }
                    }


                    @Override
                    public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
                        callback.onFailed(throwable);
                    }


                    @Override
                    public void onCancel(SHARE_MEDIA share_media, int i) {
                        callback.onCanceled();
                    }
                });
    }


    private boolean isClientInstalled(Context context, int type) {
        if (type > clients.length - 1) {
            return true;
        }
        else {
            return mShareAPI.isInstall((Activity) context, clients[type]);
        }
    }


    public void share(Context context, int mediatp, String title, String url, final UMShareListener listener) {
        if (context == null || !(context instanceof Activity)) return;
        if (!isClientInstalled(context, mediatp)) {
            if (mediatp == SHARE_QQ) {
                ToastUtils.showLongToast("QQ未安装");
            }
            else if (mediatp == SHARE_CIRCLE && mediatp == SHARE_WECHAT) {
                ToastUtils.showLongToast("微信未安装");
            }
            else if (mediatp == SHARE_SINA) {
                ToastUtils.showLongToast("新浪微博未安装");
            }
            else if (mediatp == SHARE_QZONE) {
                ToastUtils.showLongToast("qq空间未安装");
                Log.d("result", "qq空间未安装");
            }
            return;
        }
        if (!check(mediatp, title, url)) return;
        ShareAction shareAction = null;
        // UMImage imgs = new UMImage(context, img);
        UMWeb web = new UMWeb(url);
        web.setTitle(title);//标题
        //  web.setThumb(imgs);  //缩略图
        // web.setDescription(content);//描述
        if (mediatp == SHARE_QQ) {
            Log.d("result", "进来没啊");
            shareAction = new ShareAction((Activity) context).withMedia(web)
                                                             .setPlatform(
                                                                     SHARE_MEDIA.QQ)
                                                             .setCallback(
                                                                     new UMShareListener() {
                                                                         @Override
                                                                         public void onStart(SHARE_MEDIA platform) {

                                                                         }


                                                                         @Override
                                                                         public void onResult(SHARE_MEDIA share_media) {
                                                                             if (listener !=
                                                                                     null) {
                                                                                 listener.onResult(
                                                                                         share_media);
                                                                             }
                                                                         }


                                                                         @Override
                                                                         public void onError(SHARE_MEDIA share_media, Throwable throwable) {
                                                                             if (listener !=
                                                                                     null) {
                                                                                 listener.onError(
                                                                                         share_media,
                                                                                         throwable);
                                                                             }
                                                                         }


                                                                         @Override
                                                                         public void onCancel(SHARE_MEDIA share_media) {
                                                                             if (listener !=
                                                                                     null) {
                                                                                 listener.onCancel(
                                                                                         share_media);
                                                                             }
                                                                         }
                                                                     });
            shareAction.share();
        }
        else if (mediatp == SHARE_QZONE) {
            Log.d("result", "进来没");
            shareAction = new ShareAction((Activity) context).withMedia(web)
                                                             .setPlatform(
                                                                     SHARE_MEDIA.QZONE)
                                                             .setCallback(
                                                                     new UMShareListener() {
                                                                         @Override
                                                                         public void onStart(SHARE_MEDIA platform) {

                                                                         }


                                                                         @Override
                                                                         public void onResult(SHARE_MEDIA share_media) {
                                                                             if (listener !=
                                                                                     null) {
                                                                                 listener.onResult(
                                                                                         share_media);
                                                                             }
                                                                             Log.d("result",
                                                                                     share_media
                                                                                             .toString() +
                                                                                             "哟西");
                                                                         }


                                                                         @Override
                                                                         public void onError(SHARE_MEDIA share_media, Throwable throwable) {
                                                                             if (listener !=
                                                                                     null) {
                                                                                 listener.onError(
                                                                                         share_media,
                                                                                         throwable);
                                                                             }
                                                                         }


                                                                         @Override
                                                                         public void onCancel(SHARE_MEDIA share_media) {
                                                                             if (listener !=
                                                                                     null) {
                                                                                 listener.onCancel(
                                                                                         share_media);
                                                                             }
                                                                         }
                                                                     });
            shareAction.share();
        }
        else if (mediatp == SHARE_SINA) {
            shareAction = new ShareAction((Activity) context).withText(title)
                                                             .withMedia(web)
                                                             // .withMedia(imgs)
                                                             .setPlatform(
                                                                     SHARE_MEDIA.SINA)
                                                             .setCallback(
                                                                     new UMShareListener() {
                                                                         @Override
                                                                         public void onStart(SHARE_MEDIA platform) {

                                                                         }


                                                                         @Override
                                                                         public void onResult(SHARE_MEDIA share_media) {
                                                                             if (listener !=
                                                                                     null) {
                                                                                 listener.onResult(
                                                                                         share_media);
                                                                             }
                                                                         }


                                                                         @Override
                                                                         public void onError(SHARE_MEDIA share_media, Throwable throwable) {
                                                                             if (listener !=
                                                                                     null) {
                                                                                 listener.onError(
                                                                                         share_media,
                                                                                         throwable);
                                                                             }
                                                                         }


                                                                         @Override
                                                                         public void onCancel(SHARE_MEDIA share_media) {
                                                                             if (listener !=
                                                                                     null) {
                                                                                 listener.onCancel(
                                                                                         share_media);
                                                                             }
                                                                         }
                                                                     });
            shareAction.share();
        }

        else if (mediatp == SHARE_WECHAT) {
            shareAction = new ShareAction((Activity) context).withMedia(web)
                                                             .setPlatform(
                                                                     SHARE_MEDIA.WEIXIN)
                                                             .setCallback(
                                                                     new UMShareListener() {
                                                                         @Override
                                                                         public void onStart(SHARE_MEDIA platform) {

                                                                         }


                                                                         @Override
                                                                         public void onResult(SHARE_MEDIA share_media) {
                                                                             if (listener !=
                                                                                     null) {
                                                                                 listener.onResult(
                                                                                         share_media);
                                                                             }
                                                                         }


                                                                         @Override
                                                                         public void onError(SHARE_MEDIA share_media, Throwable throwable) {
                                                                             if (listener !=
                                                                                     null) {
                                                                                 listener.onError(
                                                                                         share_media,
                                                                                         throwable);
                                                                             }
                                                                         }


                                                                         @Override
                                                                         public void onCancel(SHARE_MEDIA share_media) {
                                                                             if (listener !=
                                                                                     null) {
                                                                                 listener.onCancel(
                                                                                         share_media);
                                                                             }
                                                                         }
                                                                     });
            shareAction.share();
        }
        else if (mediatp == SHARE_CIRCLE) {
            shareAction = new ShareAction((Activity) context).withMedia(web)
                                                             .setPlatform(
                                                                     SHARE_MEDIA.WEIXIN_CIRCLE)
                                                             .setCallback(
                                                                     new UMShareListener() {
                                                                         @Override
                                                                         public void onStart(SHARE_MEDIA platform) {

                                                                         }


                                                                         @Override
                                                                         public void onResult(SHARE_MEDIA share_media) {
                                                                             if (listener !=
                                                                                     null) {
                                                                                 listener.onResult(
                                                                                         share_media);
                                                                             }
                                                                         }


                                                                         @Override
                                                                         public void onError(SHARE_MEDIA share_media, Throwable throwable) {
                                                                             if (listener !=
                                                                                     null) {
                                                                                 listener.onError(
                                                                                         share_media,
                                                                                         throwable);
                                                                             }
                                                                         }


                                                                         @Override
                                                                         public void onCancel(SHARE_MEDIA share_media) {
                                                                             if (listener !=
                                                                                     null) {
                                                                                 listener.onCancel(
                                                                                         share_media);
                                                                             }
                                                                         }
                                                                     });
            shareAction.share();
        }
        else if (mediatp == SHARE_COPY) {
            if (context != null) {
                ClipboardManager cmb
                        = (ClipboardManager) context.getSystemService(
                        Context.CLIPBOARD_SERVICE);
                cmb.setText(url.trim());
            }
        }
    }


    public void share(Context context, int mediatp, File file, final UMShareListener listener) {
        if (context == null || !(context instanceof Activity)) return;
        if (!isClientInstalled(context, mediatp)) {
            if (mediatp == SHARE_QQ && mediatp == SHARE_QZONE) {
                ToastUtils.showLongToast("QQ未安装");
            }
            else if (mediatp == SHARE_CIRCLE && mediatp == SHARE_WECHAT) {
                ToastUtils.showLongToast("微信未安装");
            }
            else if (mediatp == SHARE_SINA) {
                ToastUtils.showLongToast("新浪微博未安装");
            }
            return;
        }
        //if (!check(mediatp, title, url, img, content)) return;
        ShareAction shareAction = null;
        UMImage imgs = new UMImage(context, file);
        //UMWeb web = new UMWeb(url);
        //web.setTitle(title);//标题
        //web.setThumb(imgs);  //缩略图
        //web.setDescription(content);//描述
        if (mediatp == SHARE_QQ) {
            shareAction = new ShareAction((Activity) context).withMedia(imgs)
                                                             .setPlatform(
                                                                     SHARE_MEDIA.QQ)
                                                             .setCallback(
                                                                     new UMShareListener() {
                                                                         @Override
                                                                         public void onStart(SHARE_MEDIA platform) {

                                                                         }


                                                                         @Override
                                                                         public void onResult(SHARE_MEDIA share_media) {
                                                                             if (listener !=
                                                                                     null) {
                                                                                 listener.onResult(
                                                                                         share_media);
                                                                             }
                                                                         }


                                                                         @Override
                                                                         public void onError(SHARE_MEDIA share_media, Throwable throwable) {
                                                                             if (listener !=
                                                                                     null) {
                                                                                 listener.onError(
                                                                                         share_media,
                                                                                         throwable);
                                                                             }
                                                                         }


                                                                         @Override
                                                                         public void onCancel(SHARE_MEDIA share_media) {
                                                                             if (listener !=
                                                                                     null) {
                                                                                 listener.onCancel(
                                                                                         share_media);
                                                                             }
                                                                         }
                                                                     });
            shareAction.share();
        }
        else if (mediatp == SHARE_SINA) {
            shareAction = new ShareAction((Activity) context).withMedia(imgs)
                                                             .setPlatform(
                                                                     SHARE_MEDIA.SINA)
                                                             .setCallback(
                                                                     new UMShareListener() {
                                                                         @Override
                                                                         public void onStart(SHARE_MEDIA platform) {

                                                                         }


                                                                         @Override
                                                                         public void onResult(SHARE_MEDIA share_media) {
                                                                             if (listener !=
                                                                                     null) {
                                                                                 listener.onResult(
                                                                                         share_media);
                                                                             }
                                                                         }


                                                                         @Override
                                                                         public void onError(SHARE_MEDIA share_media, Throwable throwable) {
                                                                             if (listener !=
                                                                                     null) {
                                                                                 listener.onError(
                                                                                         share_media,
                                                                                         throwable);
                                                                             }
                                                                         }


                                                                         @Override
                                                                         public void onCancel(SHARE_MEDIA share_media) {
                                                                             if (listener !=
                                                                                     null) {
                                                                                 listener.onCancel(
                                                                                         share_media);
                                                                             }
                                                                         }
                                                                     });
            shareAction.share();
        }
        else if (mediatp == SHARE_QZONE) {
            shareAction = new ShareAction((Activity) context).withMedia(imgs)
                                                             .setPlatform(
                                                                     SHARE_MEDIA.QZONE)
                                                             .setCallback(
                                                                     new UMShareListener() {
                                                                         @Override
                                                                         public void onStart(SHARE_MEDIA platform) {

                                                                         }


                                                                         @Override
                                                                         public void onResult(SHARE_MEDIA share_media) {
                                                                             if (listener !=
                                                                                     null) {
                                                                                 listener.onResult(
                                                                                         share_media);
                                                                             }
                                                                         }


                                                                         @Override
                                                                         public void onError(SHARE_MEDIA share_media, Throwable throwable) {
                                                                             if (listener !=
                                                                                     null) {
                                                                                 listener.onError(
                                                                                         share_media,
                                                                                         throwable);
                                                                             }
                                                                         }


                                                                         @Override
                                                                         public void onCancel(SHARE_MEDIA share_media) {
                                                                             if (listener !=
                                                                                     null) {
                                                                                 listener.onCancel(
                                                                                         share_media);
                                                                             }
                                                                         }
                                                                     });
            shareAction.share();
        }
        else if (mediatp == SHARE_WECHAT) {
            shareAction = new ShareAction((Activity) context).withMedia(imgs)
                                                             .setPlatform(
                                                                     SHARE_MEDIA.WEIXIN)
                                                             .setCallback(
                                                                     new UMShareListener() {
                                                                         @Override
                                                                         public void onStart(SHARE_MEDIA platform) {

                                                                         }


                                                                         @Override
                                                                         public void onResult(SHARE_MEDIA share_media) {
                                                                             if (listener !=
                                                                                     null) {
                                                                                 listener.onResult(
                                                                                         share_media);
                                                                             }
                                                                         }


                                                                         @Override
                                                                         public void onError(SHARE_MEDIA share_media, Throwable throwable) {
                                                                             if (listener !=
                                                                                     null) {
                                                                                 listener.onError(
                                                                                         share_media,
                                                                                         throwable);
                                                                             }
                                                                         }


                                                                         @Override
                                                                         public void onCancel(SHARE_MEDIA share_media) {
                                                                             if (listener !=
                                                                                     null) {
                                                                                 listener.onCancel(
                                                                                         share_media);
                                                                             }
                                                                         }
                                                                     });
            shareAction.share();
        }
        else if (mediatp == SHARE_CIRCLE) {
            shareAction = new ShareAction((Activity) context).withMedia(imgs)
                                                             .setPlatform(
                                                                     SHARE_MEDIA.WEIXIN_CIRCLE)
                                                             .setCallback(
                                                                     new UMShareListener() {
                                                                         @Override
                                                                         public void onStart(SHARE_MEDIA platform) {

                                                                         }


                                                                         @Override
                                                                         public void onResult(SHARE_MEDIA share_media) {
                                                                             if (listener !=
                                                                                     null) {
                                                                                 listener.onResult(
                                                                                         share_media);
                                                                             }
                                                                         }


                                                                         @Override
                                                                         public void onError(SHARE_MEDIA share_media, Throwable throwable) {
                                                                             if (listener !=
                                                                                     null) {
                                                                                 listener.onError(
                                                                                         share_media,
                                                                                         throwable);
                                                                             }
                                                                         }


                                                                         @Override
                                                                         public void onCancel(SHARE_MEDIA share_media) {
                                                                             if (listener !=
                                                                                     null) {
                                                                                 listener.onCancel(
                                                                                         share_media);
                                                                             }
                                                                         }
                                                                     });
            shareAction.share();
        }
    }


    private boolean check(int mediatp, String title, String url, String img, String context) {
        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(url) ||
                TextUtils.isEmpty(img) || TextUtils.isEmpty(context)) {
            return false;
        }
        return true;
    }


    private boolean check(int mediatp, String title, String url) {
        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(url)) {
            return false;
        }
        return true;
    }


    public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
        UMShareAPI.get(activity)
                  .onActivityResult(requestCode, resultCode, data);
    }


    public interface OnAutoLoginCallback {
        void onSuccess(int type, Map<String, String> map);

        void onFailed(Throwable t);

        void onCanceled();
    }
}
