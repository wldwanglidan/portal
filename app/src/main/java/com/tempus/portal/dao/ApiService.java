package com.tempus.portal.dao;

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
import com.tempus.portal.model.SendSmsRequest;
import com.tempus.portal.model.Sign;
import com.tempus.portal.model.SubmitFeedbackRequest;
import com.tempus.portal.model.TokenApplyRequest;
import com.tempus.portal.model.User;
import com.tempus.portal.model.VersionInfo;
import com.tempus.portal.model.VersionUpdateRequest;
import com.tempus.portal.model.information.AddressInformation;
import com.tempus.portal.model.information.AddressRequest;
import com.tempus.portal.model.information.ContactInformation;
import com.tempus.portal.model.information.ContactRequest;
import com.tempus.portal.model.information.PassengerInformation;
import com.tempus.portal.model.information.PassengerRequest;
import com.tempus.portal.model.usercenter.UserInfRequest;
import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Url;

/**
 * @author jianhao025@gmail.com
 * @data: 2016/07/27 08:59
 * @version: V1.0
 */
public interface ApiService {
    @GET Observable<ResponseBody> downLoadImage(@Url String url);

    //申请appId和appSerect
    @POST("app/appIdApply") Observable<AppInfo> getAppInfo(
            @Body AppIdApplyRequest appIdApplyRequest);

    //获取访问令牌
    @POST("app/tokenApply") Observable<AccessTokenInfo> getTokenInfo(
            @Body TokenApplyRequest tokenApplyRequest);

    //人脸识别生成签名
    @POST("getSign") Observable<Sign> getSign(
            @Body GetSignRequest getSignRequest);

    //3.4.71.	人脸识别验证签名
    @POST("checkSign") Observable<CheckSign> checkSign(
            @Body CheckSignRequest checkSignRequest);

    //app版本更新
    @POST("app/appUpdate") Observable<VersionInfo> checkForUpdates(
            @Body VersionUpdateRequest versionUpdateRequest);

    //我的信息
    @POST("user/myInfo") Observable<User> myInfo(@Body BaseHead baseHead);

    //首页
    @POST("portal/queryMerchList") Observable<ListData<HomeMenu>> getHome(
            @Body BaseHead baseHead);

    //获取全局参数
    @POST("app/getGlobalParams") Observable<GlobalParams> getGlobalParameters(
            @Body BaseHead baseHead);

    //获取金融首页
    @POST("portal/queryFinaProducts") Observable<Finance> getFinance(
            @Body BaseHead baseHead);

    //意见反馈
    @POST("portal/app/submitFeedback") Observable<Base> submitFeedback(
            @Body SubmitFeedbackRequest submitFeedbackRequest);

    //////////////////////// 用户 /////////////////////////////////////
    //发送短信验证码
    @POST("system/sendVerifyCode") Observable<Base> sendSms(
            @Body SendSmsRequest sendSmsRequest);

    //使用手机短信登录
    @POST("user/login") Observable<User> login(@Body LoginRequest loginRequest);

    //第三方用户绑定手机号
    @POST("user/thirdUserBind") Observable<User> bindPhone(
            @Body BindPhoneRequest bindPhoneRequest);

    //第三方登录
    @POST("portal/user/thirdUserLogin") Observable<User> otherLogin(
            @Body OtherLoginRequest otherLoginRequest);

    //我的消息列表
    @POST("portal/user/myNews") Observable<ListData<Message>> myMessage(
            @Body PageRequest pageRequest);

    //我的收藏
    @POST("portal/queryFavorites")
    Observable<ListData<Collection>> myCollection(
            @Body PageRequest pageRequest);

    //添加收藏
    @POST("portal/addFavorite") Observable<Base> addCollection(
            @Body AddCollectionRequest addCollectionRequest);

    //取消收藏
    @POST("portal/cancelFavorite") Observable<Base> cancelCollection(
            @Body CancelCollectionRequest cancelCollectionRequest);

    //////////////////////// 联系人 /////////////////////////////////////
    //查询联系人
    @POST("portal/commonInfo/queryContactInfos")
    Observable<ListData<ContactInformation>> getContactList(
            @Body PageRequest pageRequest);

    //新增联系人
    @POST("portal/commonInfo/addContactInfo") Observable<Base> addContact(
            @Body ContactRequest contactRequest);

    //更新联系人
    @POST("portal/commonInfo/editContactInfo") Observable<Base> editContact(
            @Body ContactRequest contactRequest);

    //删除联系人   contactId  当前联系人ID
    @POST("portal/commonInfo/deleteContactInfo") Observable<Base> deleteContact(
            @Body ContactRequest contactRequest);

    //查询支付订单信息
    @POST("portal/order/queryOrderInfo") Observable<OrderInfo> getOrderInfo(
            @Body OrderInfoRequest orderInfoRequest);

    //////////////////////// 地址 /////////////////////////////////////
    //查询地址
    @POST("portal/commonInfo/queryAddressInfos")
    Observable<ListData<AddressInformation>> getAddressList(
            @Body PageRequest pageRequest);

    //新增地址
    @POST("portal/commonInfo/addAddressInfo") Observable<Base> addAddress(
            @Body AddressRequest addressRequest);

    //更新地址
    @POST("portal/commonInfo/editAddressInfo") Observable<Base> editAddress(
            @Body AddressRequest addressRequest);

    //删除地址
    @POST("portal/commonInfo/deleteAddressInfo") Observable<Base> deleteAddress(
            @Body AddressRequest addressRequest);

    //////////////////////// 旅客 /////////////////////////////////////

    //查询旅客
    @POST("portal/commonInfo/queryPassengerInfos")
    Observable<ListData<PassengerInformation>> getPassengerList(
            @Body PageRequest pageRequest);

    //新增旅客
    @POST("portal/commonInfo/addPassengerInfo") Observable<Base> addPassenger(
            @Body PassengerRequest passengerRequest);

    //更新旅客
    @POST("portal/commonInfo/editPassengerInfo") Observable<Base> editPassenger(
            @Body PassengerRequest passengerRequest);

    //删除旅客
    @POST("portal/commonInfo/deletePassengerInfo")
    Observable<Base> deletePassenger(@Body PassengerRequest passengerRequest);

    //////////////////////// 个人中心 /////////////////////////////////////
    //查询个人信息
    @POST("user/myInfo") Observable<User> getUserInf(
            @Body UserInfRequest userInfRequest);

    //头像上传(以表单形式) 注意这里的sessionContext
    @Multipart @POST("user/headUpload") Observable<User> editAvatar(
            @Part("sessionContext") RequestBody appRequest,
            @Part MultipartBody.Part file);

    //更新旅客
    @POST("user/modifyNickname") Observable<User> editNickname(
            @Body UserInfRequest userInfRequest);


    //退出登录
    @POST("user/logout") Observable<Base> loginOut(
            @Body BaseHead baseHead);
}
