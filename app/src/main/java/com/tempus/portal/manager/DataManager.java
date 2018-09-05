package com.tempus.portal.manager;

import com.google.gson.Gson;
import com.tempus.portal.app.AppContext;
import com.tempus.portal.base.RxSchedulers;
import com.tempus.portal.dao.ResponseHandle;
import com.tempus.portal.dao.retrofit.RetrofitDao;
import com.tempus.portal.model.AccessTokenInfo;
import com.tempus.portal.model.AddCollectionRequest;
import com.tempus.portal.model.AppIdApplyRequest;
import com.tempus.portal.model.AppInfo;
import com.tempus.portal.model.Base;
import com.tempus.portal.model.BaseHead;
import com.tempus.portal.model.BindPhoneRequest;
import com.tempus.portal.model.CancelCollectionRequest;
import com.tempus.portal.model.CheckSign;
import com.tempus.portal.model.CheckSignRequest;
import com.tempus.portal.model.Collection;
import com.tempus.portal.model.Finance;
import com.tempus.portal.model.GetSignRequest;
import com.tempus.portal.model.GlobalParams;
import com.tempus.portal.model.HomeMenu;
import com.tempus.portal.model.ListData;
import com.tempus.portal.model.LoginRequest;
import com.tempus.portal.model.Message;
import com.tempus.portal.model.OrderInfo;
import com.tempus.portal.model.OrderInfoRequest;
import com.tempus.portal.model.OtherLoginRequest;
import com.tempus.portal.model.PageRequest;
import com.tempus.portal.model.QqUserInfo;
import com.tempus.portal.model.SendSmsRequest;
import com.tempus.portal.model.Sign;
import com.tempus.portal.model.SubmitFeedbackRequest;
import com.tempus.portal.model.TokenApplyRequest;
import com.tempus.portal.model.User;
import com.tempus.portal.model.VersionInfo;
import com.tempus.portal.model.VersionUpdateRequest;
import com.tempus.portal.model.WxUserInfo;
import com.tempus.portal.model.information.AddressInformation;
import com.tempus.portal.model.information.AddressRequest;
import com.tempus.portal.model.information.ContactInformation;
import com.tempus.portal.model.information.ContactRequest;
import com.tempus.portal.model.information.PassengerInformation;
import com.tempus.portal.model.information.PassengerRequest;
import com.tempus.portal.model.usercenter.UserInfRequest;
import io.reactivex.Observable;
import java.io.File;
import java.util.List;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * @author jianhao025@gmail.com
 * @data: 2016/07/27 08:59
 * @version: V1.0
 */
public class DataManager {

    private static DataManager ourInstance;


    // 单例写法---双重检查锁模式
    public static DataManager getInstance() {
        if (ourInstance == null) {
            synchronized (DataManager.class) {
                if (ourInstance == null) ourInstance = new DataManager();
            }
        }
        return ourInstance;
    }


    //2.1 申请appId和appSerect
    public static Observable<AppInfo> getAppInfo() {
        return RetrofitDao.getInstance()
                          .getApiService()
                          .getAppInfo(
                                  new AppIdApplyRequest(AppContext.DEVICE_ID));
    }


    //2.2 获取访问令牌
    public static Observable<AccessTokenInfo> getTokenInfo(TokenApplyRequest tokenApplyRequest) {
        return RetrofitDao.getInstance()
                          .getApiService()
                          .getTokenInfo(tokenApplyRequest);
    }


    //人脸识别生成签名
    public static Observable<Sign> getSign() {
        return RetrofitDao.getInstance()
                          .getApiService()
                          .getSign(new GetSignRequest(AppContext.DEVICE_ID))
                          .compose(RxSchedulers.io_main());
    }


    //3.4.71.	人脸识别验证签名
    public static Observable<CheckSign> checkSign(String order_no, String sign) {
        return RetrofitDao.getInstance()
                          .getApiService()
                          .checkSign(new CheckSignRequest(order_no, sign))
                          .compose(RxSchedulers.io_main());
    }


    //检查强制升级
    public static Observable<VersionInfo> checkForUpdates(int verCode) {
        return TokenManager.getInstance()
                           .getTokenInfo()
                           .flatMap(accessTokenInfo -> RetrofitDao.getInstance()
                                                                  .getApiService()
                                                                  .checkForUpdates(
                                                                          new VersionUpdateRequest(
                                                                                  verCode)))
                           .flatMap(ResponseHandle.newEntityData())
                           .retry(ResponseHandle.canRetry())
                           .compose(RxSchedulers.io_main());
    }

    //我的信息
    public static Observable<User> myInfo() {
        return TokenManager.getInstance()
                           .getTokenInfo()
                           .flatMap(accessTokenInfo -> RetrofitDao.getInstance()
                                                                  .getApiService()
                                                                  .myInfo(
                                                                          new BaseHead(
                                                                          )))
                           .flatMap(ResponseHandle.newEntityData())
                           .retry(ResponseHandle.canRetry())
                           .compose(RxSchedulers.io_main());
    }
    //首页
    public static Observable<ListData<HomeMenu>> getHome() {
        return TokenManager.getInstance()
                           .getTokenInfo()
                           .flatMap(accessTokenInfo -> RetrofitDao.getInstance()
                                                                  .getApiService()
                                                                  .getHome(
                                                                          new BaseHead()))
                           .flatMap(ResponseHandle.newListData())
                           .retry(ResponseHandle.canRetry())
                           .compose(RxSchedulers.io_main());
    }


    //获取金融首页
    public static Observable<Finance> getFinance() {
        return TokenManager.getInstance()
                           .getTokenInfo()
                           .flatMap(accessTokenInfo -> RetrofitDao.getInstance()
                                                                  .getApiService()
                                                                  .getFinance(
                                                                          new BaseHead()))
                           .flatMap(ResponseHandle.newEntityData())
                           .retry(ResponseHandle.canRetry())
                           .compose(RxSchedulers.io_main());
    }


    //获取全局参数
    public static Observable<GlobalParams> getGlobalParameters() {
        return TokenManager.getInstance()
                           .getTokenInfo()
                           .flatMap(accessTokenInfo -> RetrofitDao.getInstance()
                                                                  .getApiService()
                                                                  .getGlobalParameters(
                                                                          new BaseHead()))
                           .flatMap(ResponseHandle.newEntityData())


                           .retry(ResponseHandle.canRetry())
                           .compose(RxSchedulers.io_main());
    }


    //发送短信验证码
    public static Observable<Base> sendSms(String mobile, String type) {
        return TokenManager.getInstance()
                           .getTokenInfo()
                           .flatMap(accessTokenInfo -> RetrofitDao.getInstance()
                                                                  .getApiService()
                                                                  .sendSms(
                                                                          new SendSmsRequest(
                                                                                  mobile,
                                                                                  type)))
                           .flatMap(ResponseHandle.newEntityData())
                           .retry(ResponseHandle.canRetry())
                           .compose(RxSchedulers.io_main());
    }


    //使用手机短信登录
    public static Observable<User> login(String mobile, String smsCode) {
        return TokenManager.getInstance()
                           .getTokenInfo()
                           .flatMap(accessTokenInfo -> RetrofitDao.getInstance()
                                                                  .getApiService()
                                                                  .login(new LoginRequest(
                                                                          mobile,
                                                                          smsCode)))
                           .flatMap(ResponseHandle.newEntityData())
                           .retry(ResponseHandle.canRetry())
                           .compose(RxSchedulers.io_main());
    }


    //第三方用户绑定手机号
    public static Observable<User> bindPhone(String unionId, String mobile, String smsCode) {
        return TokenManager.getInstance()
                           .getTokenInfo()
                           .flatMap(accessTokenInfo -> RetrofitDao.getInstance()
                                                                  .getApiService()
                                                                  .bindPhone(
                                                                          new BindPhoneRequest(
                                                                                  unionId,
                                                                                  mobile,
                                                                                  smsCode)))
                           .flatMap(ResponseHandle.newEntityData())
                           .retry(ResponseHandle.canRetry())
                           .compose(RxSchedulers.io_main());
    }


    //第三方登录（微信）
    public static Observable<User> otherLogin(WxUserInfo wxUserInfo) {
        return TokenManager.getInstance()
                           .getTokenInfo()
                           .flatMap(accessTokenInfo -> RetrofitDao.getInstance()
                                                                  .getApiService()
                                                                  .otherLogin(
                                                                          new OtherLoginRequest(
                                                                                  wxUserInfo.iconurl,
                                                                                  wxUserInfo.city,
                                                                                  wxUserInfo.country,
                                                                                  wxUserInfo.gender,
                                                                                  wxUserInfo.name,
                                                                                  wxUserInfo.openid,
                                                                                  wxUserInfo.province,
                                                                                  "WECHAT",
                                                                                  wxUserInfo.unionid)))
                           .flatMap(ResponseHandle.newEntityData())
                           .retry(ResponseHandle.canRetry())
                           .compose(RxSchedulers.io_main());
    }


    //第三方登录（Qq）
    public static Observable<User> otherLogin(QqUserInfo qqUserInfo) {
        return TokenManager.getInstance()
                           .getTokenInfo()
                           .flatMap(accessTokenInfo -> RetrofitDao.getInstance()
                                                                  .getApiService()
                                                                  .otherLogin(
                                                                          new OtherLoginRequest(
                                                                                  qqUserInfo.iconurl,
                                                                                  qqUserInfo.city,
                                                                                  qqUserInfo.gender,
                                                                                  qqUserInfo.name,
                                                                                  qqUserInfo.openid,
                                                                                  qqUserInfo.province,
                                                                                  "QQ",
                                                                                  qqUserInfo.openid)))
                           .flatMap(ResponseHandle.newEntityData())
                           .retry(ResponseHandle.canRetry())
                           .compose(RxSchedulers.io_main());
    }


    //我的消息列表
    public static Observable<ListData<Message>> myMessage(int pageNum) {
        return TokenManager.getInstance()
                           .getTokenInfo()
                           .flatMap(accessTokenInfo -> RetrofitDao.getInstance()
                                                                  .getApiService()
                                                                  .myMessage(
                                                                          new PageRequest(
                                                                                  pageNum)))
                           .flatMap(ResponseHandle.newListData())
                           .retry(ResponseHandle.canRetry())
                           .compose(RxSchedulers.io_main());
    }


    //我的收藏列表
    public static Observable<ListData<Collection>> myCollection(int pageNum) {
        return TokenManager.getInstance()
                           .getTokenInfo()
                           .flatMap(accessTokenInfo -> RetrofitDao.getInstance()
                                                                  .getApiService()
                                                                  .myCollection(
                                                                          new PageRequest(
                                                                                  pageNum)))
                           .flatMap(ResponseHandle.newListData())
                           .retry(ResponseHandle.canRetry())
                           .compose(RxSchedulers.io_main());
    }


    //添加收藏
    public static Observable<Base> addCollection(String merchId, String title, String url) {
        return TokenManager.getInstance()
                           .getTokenInfo()
                           .flatMap(accessTokenInfo -> RetrofitDao.getInstance()
                                                                  .getApiService()
                                                                  .addCollection(
                                                                          new AddCollectionRequest(
                                                                                  merchId,
                                                                                  title,
                                                                                  url)))
                           .flatMap(ResponseHandle.newEntityData())
                           .retry(ResponseHandle.canRetry())
                           .compose(RxSchedulers.io_main());
    }


    //取消收藏
    public static Observable<Base> cancelCollection(List<Integer> ids) {
        return TokenManager.getInstance()
                           .getTokenInfo()
                           .flatMap(accessTokenInfo -> RetrofitDao.getInstance()
                                                                  .getApiService()
                                                                  .cancelCollection(
                                                                          new CancelCollectionRequest(
                                                                                  ids)))
                           .flatMap(ResponseHandle.newEntityData())
                           .retry(ResponseHandle.canRetry())
                           .compose(RxSchedulers.io_main());
    }


    //意见反馈
    public static Observable<Base> submitFeedback(String content) {
        return TokenManager.getInstance()
                           .getTokenInfo()
                           .flatMap(accessTokenInfo -> RetrofitDao.getInstance()
                                                                  .getApiService()
                                                                  .submitFeedback(
                                                                          new SubmitFeedbackRequest(
                                                                                  content)))
                           .flatMap(ResponseHandle.newEntityData())
                           .retry(ResponseHandle.canRetry())
                           .compose(RxSchedulers.io_main());
    }


    //查询支付订单信息
    public static Observable<OrderInfo> getOrderInfo(OrderInfoRequest orderInfoRequest) {
        return TokenManager.getInstance()
                           .getTokenInfo()
                           .flatMap(accessTokenInfo -> RetrofitDao.getInstance()
                                                                  .getApiService()
                                                                  .getOrderInfo(
                                                                          orderInfoRequest))
                           .flatMap(ResponseHandle.newEntityData())
                           .retry(ResponseHandle.canRetry())
                           .compose(RxSchedulers.io_main());
    }


    //////////////////////// 联系人 /////////////////////////////////////
    //查询我的联系人
    public static Observable<ListData<ContactInformation>> getContactList(int pageNum) {
        return TokenManager.getInstance()
                           .getTokenInfo()
                           .flatMap(accessTokenInfo -> RetrofitDao.getInstance()
                                                                  .getApiService()
                                                                  .getContactList(
                                                                          new PageRequest(
                                                                                  pageNum)))
                           .flatMap(ResponseHandle.newListData())
                           .retry(ResponseHandle.canRetry())
                           .compose(RxSchedulers.io_main());
    }


    //添加我的联系人
    public static Observable<Base> addContact(String name, String mobile, String email, String selfFlag) {
        return TokenManager.getInstance()
                           .getTokenInfo()
                           .flatMap(accessTokenInfo -> RetrofitDao.getInstance()
                                                                  .getApiService()
                                                                  .addContact(
                                                                          new ContactRequest(
                                                                                  name,
                                                                                  mobile,
                                                                                  email,
                                                                                  selfFlag)))
                           .flatMap(ResponseHandle.newEntityData())
                           .retry(ResponseHandle.canRetry())
                           .compose(RxSchedulers.io_main());
    }


    //编辑我的联系人
    public static Observable<Base> editContact(String name, String mobile, String email, String selfFlag, int contactId) {
        return TokenManager.getInstance()
                           .getTokenInfo()
                           .flatMap(accessTokenInfo -> RetrofitDao.getInstance()
                                                                  .getApiService()
                                                                  .editContact(
                                                                          new ContactRequest(
                                                                                  name,
                                                                                  mobile,
                                                                                  email,
                                                                                  selfFlag,
                                                                                  contactId)))
                           .flatMap(ResponseHandle.newEntityData())
                           .retry(ResponseHandle.canRetry())
                           .compose(RxSchedulers.io_main());
    }


    //删除我的联系人
    public static Observable<Base> deleteContact(int contactId) {
        return TokenManager.getInstance()
                           .getTokenInfo()
                           .flatMap(accessTokenInfo -> RetrofitDao.getInstance()
                                                                  .getApiService()
                                                                  .deleteContact(
                                                                          new ContactRequest(
                                                                                  contactId)))
                           .flatMap(ResponseHandle.newEntityData())
                           .retry(ResponseHandle.canRetry())
                           .compose(RxSchedulers.io_main());
    }


    //////////////////////// 地址 /////////////////////////////////////
    //查询地址
    public static Observable<ListData<AddressInformation>> getAddressList(int pageNum) {
        return TokenManager.getInstance()
                           .getTokenInfo()
                           .flatMap(accessTokenInfo -> RetrofitDao.getInstance()
                                                                  .getApiService()
                                                                  .getAddressList(
                                                                          new PageRequest(
                                                                                  pageNum)))
                           .flatMap(ResponseHandle.newListData())
                           .retry(ResponseHandle.canRetry())
                           .compose(RxSchedulers.io_main());
    }


    //添加地址
    public static Observable<Base> addAddress(String receiverName, String receiverMobile, String province, String city, String area, String detailAddress, String postCode, String selfFlag) {
        return TokenManager.getInstance()
                           .getTokenInfo()
                           .flatMap(accessTokenInfo -> RetrofitDao.getInstance()
                                                                  .getApiService()
                                                                  .addAddress(
                                                                          new AddressRequest(
                                                                                  receiverName,
                                                                                  receiverMobile,
                                                                                  province,
                                                                                  city,
                                                                                  area,
                                                                                  detailAddress,
                                                                                  postCode,
                                                                                  selfFlag)))
                           .flatMap(ResponseHandle.newEntityData())
                           .retry(ResponseHandle.canRetry())
                           .compose(RxSchedulers.io_main());
    }


    //编辑地址
    public static Observable<Base> editAddress(String receiverName, String receiverMobile, String province, String city, String area, String detailAddress, String postCode, String selfFlag, int addressId) {
        return TokenManager.getInstance()
                           .getTokenInfo()
                           .flatMap(accessTokenInfo -> RetrofitDao.getInstance()
                                                                  .getApiService()
                                                                  .editAddress(
                                                                          new AddressRequest(
                                                                                  receiverName,
                                                                                  receiverMobile,
                                                                                  province,
                                                                                  city,
                                                                                  area,
                                                                                  detailAddress,
                                                                                  postCode,
                                                                                  selfFlag,
                                                                                  addressId)))
                           .flatMap(ResponseHandle.newEntityData())
                           .retry(ResponseHandle.canRetry())
                           .compose(RxSchedulers.io_main());
    }


    //删除地址
    public static Observable<Base> deleteAddress(int addressId) {
        return TokenManager.getInstance()
                           .getTokenInfo()
                           .flatMap(accessTokenInfo -> RetrofitDao.getInstance()
                                                                  .getApiService()
                                                                  .deleteAddress(
                                                                          new AddressRequest(
                                                                                  addressId)))
                           .flatMap(ResponseHandle.newEntityData())
                           .retry(ResponseHandle.canRetry())
                           .compose(RxSchedulers.io_main());
    }

    //////////////////////// 旅客 /////////////////////////////////////
    //查询旅客
    public static Observable<ListData<PassengerInformation>> getPassengerList(int pageNum) {
        return TokenManager.getInstance()
                           .getTokenInfo()
                           .flatMap(accessTokenInfo -> RetrofitDao.getInstance()
                                                                  .getApiService()
                                                                  .getPassengerList(
                                                                          new PageRequest(
                                                                                  pageNum)))
                           .flatMap(ResponseHandle.newListData())
                           .retry(ResponseHandle.canRetry())
                           .compose(RxSchedulers.io_main());
    }


    //添加旅客
    public static Observable<Base> addPassenger(String name, String surname, String enName, String mobile, String sex, String birthday, String nationality, String certType, String certNo, String expireDate, String selfFlag) {
        return TokenManager.getInstance()
                           .getTokenInfo()
                           .flatMap(accessTokenInfo -> RetrofitDao.getInstance()
                                                                  .getApiService()
                                                                  .addPassenger(
                                                                          new PassengerRequest(
                                                                                  name,
                                                                                  surname,
                                                                                  enName,
                                                                                  mobile,
                                                                                  sex,
                                                                                  birthday,
                                                                                  nationality,
                                                                                  certType,
                                                                                  certNo,
                                                                                  expireDate,
                                                                                  selfFlag)))
                           .flatMap(ResponseHandle.newEntityData())
                           .retry(ResponseHandle.canRetry())
                           .compose(RxSchedulers.io_main());
    }


    //更新旅客
    public static Observable<Base> editPassenger(String name, String surname, String enName, String mobile, String sex, String birthday, String nationality, String certType, String certNo, String expireDate, String selfFlag, int passengerId) {
        return TokenManager.getInstance()
                           .getTokenInfo()
                           .flatMap(accessTokenInfo -> RetrofitDao.getInstance()
                                                                  .getApiService()
                                                                  .editPassenger(
                                                                          new PassengerRequest(
                                                                                  name,
                                                                                  surname,
                                                                                  enName,
                                                                                  mobile,
                                                                                  sex,
                                                                                  birthday,
                                                                                  nationality,
                                                                                  certType,
                                                                                  certNo,
                                                                                  expireDate,
                                                                                  selfFlag,
                                                                                  passengerId)))
                           .flatMap(ResponseHandle.newEntityData())
                           .retry(ResponseHandle.canRetry())
                           .compose(RxSchedulers.io_main());
    }


    //删除旅客
    public static Observable<Base> deletePassenger(int passengerId) {
        return TokenManager.getInstance()
                           .getTokenInfo()
                           .flatMap(accessTokenInfo -> RetrofitDao.getInstance()
                                                                  .getApiService()
                                                                  .deletePassenger(
                                                                          new PassengerRequest(
                                                                                  passengerId)))
                           .flatMap(ResponseHandle.newEntityData())
                           .retry(ResponseHandle.canRetry())
                           .compose(RxSchedulers.io_main());
    }

    //////////////////////// 个人中心 /////////////////////////////////////
    //编辑头像
    public static Observable<User> editAvatar(String path) {
        RequestBody appRequest = RequestBody.create(
                MediaType.parse("multipart/form-data"),
                new Gson().toJson(new UserInfRequest()));
        MultipartBody.Part part;
        if (path != null) {
            //根据path创建文件
            File file = new File(path);
            part = MultipartBody.Part.createFormData("file", file.getName(),
                    RequestBody.create(MediaType.parse("image/*"), file));
            return TokenManager.getInstance()
                               .getTokenInfo()
                               .flatMap(
                                       accessTokenInfo -> RetrofitDao.getInstance()
                                                                     .getApiService()
                                                                     .editAvatar(
                                                                             appRequest,
                                                                             part))
                               .flatMap(ResponseHandle.newEntityData())
                               .retry(ResponseHandle.canRetry())
                               .compose(RxSchedulers.io_main());
        }
        else {
            return TokenManager.getInstance()
                               .getTokenInfo()
                               .flatMap(
                                       accessTokenInfo -> RetrofitDao.getInstance()
                                                                     .getApiService()
                                                                     .editAvatar(
                                                                             appRequest,
                                                                             null))
                               .flatMap(ResponseHandle.newEntityData())
                               .retry(ResponseHandle.canRetry())
                               .compose(RxSchedulers.io_main());
        }
    }


    //更新昵称
    public static Observable<User> editNickname(String nickname) {
        return TokenManager.getInstance()
                           .getTokenInfo()
                           .flatMap(accessTokenInfo -> RetrofitDao.getInstance()
                                                                  .getApiService()
                                                                  .editNickname(
                                                                          new UserInfRequest(
                                                                                  nickname)))
                           .flatMap(ResponseHandle.newEntityData())
                           .retry(ResponseHandle.canRetry())
                           .compose(RxSchedulers.io_main());
    }

    //查询个人信息
    public static Observable<User> getUserInf() {
        return TokenManager.getInstance()
                           .getTokenInfo()
                           .flatMap(accessTokenInfo -> RetrofitDao.getInstance()
                                                                  .getApiService()
                                                                  .getUserInf(
                                                                          new UserInfRequest()))
                           .flatMap(ResponseHandle.newEntityData())
                           .retry(ResponseHandle.canRetry())
                           .compose(RxSchedulers.io_main());
    }


    //退出登录
    public static Observable<Base> loginOut() {
        return TokenManager.getInstance()
                           .getTokenInfo()
                           .flatMap(accessTokenInfo -> RetrofitDao.getInstance()
                                                                  .getApiService()
                                                                  .loginOut(
                                                                          new BaseHead()))
                           .flatMap(ResponseHandle.newEntityData())
                           .retry(ResponseHandle.canRetry())
                           .compose(RxSchedulers.io_main());
    }




}
