package com.tempus.portal.ui.information;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import com.tempus.portal.R;
import com.tempus.portal.base.BaseActivity;
import com.tempus.portal.base.utils.ActivityUtils;
import com.tempus.portal.base.utils.AllCapTransformationMethod;
import com.tempus.portal.base.utils.IDCard;
import com.tempus.portal.base.utils.KeyboardUtils;
import com.tempus.portal.base.utils.TextCheckUtils;
import com.tempus.portal.base.utils.ToastUtils;
import com.tempus.portal.manager.DataManager;
import com.tempus.portal.model.Nationality;
import com.tempus.portal.model.information.PassengerInformation;
import com.tempus.portal.view.widget.ClearEditText;
import com.zhy.android.percent.support.PercentLinearLayout;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Administrator on 2016/12/14.
 */

public class AddPassengersInfActivity extends BaseActivity {
    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.tvRight) TextView mTvRight;

    @BindView(R.id.cetChinaName) ClearEditText mCetChinaName;
    @BindView(R.id.cetFirstName) ClearEditText mCetFirstName;
    @BindView(R.id.cetSecondName) ClearEditText mCetSecondName;
    @BindView(R.id.cetPassport) ClearEditText mCetPassport;
    @BindView(R.id.cetMobile) ClearEditText mCetMobile;

    @BindView(R.id.tvPassport) TextView mTvPassport;
    @BindView(R.id.tvBirthday) TextView mTvBirthday;
    @BindView(R.id.tvNationality) TextView mTvNationality;
    //@BindView(R.id.tvIssuePlace) TextView mTvIssuePlace;
    @BindView(R.id.tvValidPeriod) TextView mTvValidPeriod;

    @BindView(R.id.llBirthday) PercentLinearLayout mLlBirthday;
    @BindView(R.id.llNationality) PercentLinearLayout mLlNationality;
    //@BindView(R.id.llIssuePlace) PercentLinearLayout mLlIssuePlace;
    @BindView(R.id.llValidPeriod) PercentLinearLayout mLlValidPeriod;

    @BindView(R.id.cbWoman) CheckBox mCbWoman;
    @BindView(R.id.cbMan) CheckBox mCbMan;

    @BindView(R.id.cbDefault) CheckBox mCbDefault;

    @BindView(R.id.ll_addPassage) LinearLayout mLlAddPassage;
    @BindView(R.id.rlBottom) RelativeLayout mrlBottom;

    private PassengerInformation mPassenger;
    private boolean mClickAddBtn;

    private String[] items;
    private AlertDialog.Builder selectDialog;
    private DatePickerDialog dpd;
    private PassengerInformation.CertInfosBean currentBean;

    private boolean isBirthday = false;//区别日期选择是出生日期还是有效期

    public static final int REQUEST_CODE_NATIONALITY = 0x10;

    private Nationality mNationality;//国家信息实体
    private long mCurrentSecond;
    //mCbWoman.setOnCheckedChangeListener();
    // 在外面根本找不到setOnCheckedChangeListener这个API


    public static Bundle buildBundle(PassengerInformation passenger, boolean clickAddBtn) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("passenger", passenger);
        bundle.putSerializable("clickAddBtn", clickAddBtn);
        return bundle;
    }


    @Override protected void getBundleExtras(Bundle extras) {
        mPassenger = (PassengerInformation) extras.getSerializable("passenger");
        mClickAddBtn = (boolean) extras.getSerializable("clickAddBtn");
    }


    @Override protected int getContentViewLayoutID() {
        return R.layout.activity_add_passenger;
    }


    @Override protected View getLoadingTargetView() {
        return null;
    }


    @Override protected void initView(Bundle savedInstanceState) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
        String currentTime = df.format(new Date());
        mCurrentSecond = getSecondsFromDate(currentTime);
        //设置自动感应为大写
        mCetFirstName.setTransformationMethod(new AllCapTransformationMethod());
        mCetSecondName.setTransformationMethod(
                new AllCapTransformationMethod());

        //mCbWoman.setOnCheckedChangeListener();
        Calendar mycalendar = Calendar.getInstance();
        year = mycalendar.get(Calendar.YEAR); //获取Calendar对象中的年
        month = mycalendar.get(Calendar.MONTH);//获取Calendar对象中的月
        day = mycalendar.get(Calendar.DAY_OF_MONTH);//获取这个月的第几天
        //设置为老的样式
        //dpd = new DatePickerDialog(AddPassengersInfActivity.this,
        //        DatePickerDialog.THEME_HOLO_LIGHT, mDateSetListener, year,
        //        month, day);
        ////DatePickerDialog.THEME_HOLO_DARK
        dpd = new DatePickerDialog(AddPassengersInfActivity.this,
                mDateSetListener, year, month, day);
        items = mContext.getResources().getStringArray(R.array.idType);

        //由编辑跳转过来显示信息
        if (mPassenger != null) {

            mCetChinaName.setText(mPassenger.name);
            mCetFirstName.setText(mPassenger.surname);
            mCetSecondName.setText(mPassenger.enName);

            mTvBirthday.setText(mPassenger.birthday);
            mCetMobile.setText(mPassenger.mobile);

            mTvPassport.setText(
                    getCertTypeETC(mPassenger.certInfos.get(0).certType));//组件类型
            mCetPassport.setText(mPassenger.certInfos.get(0).certNo);//证件号码
            mTvNationality.setText(
                    mPassenger.certInfos.get(0).nationlityName);//国籍

            mTvValidPeriod.setText(mPassenger.certInfos.get(0).expireDate);//有效期
            if (mPassenger.sex.equals("F")) {
                mCbWoman.setChecked(true);
                mCbMan.setChecked(false);
            }
            else {
                mCbWoman.setChecked(false);
                mCbMan.setChecked(true);
            }

            if (mPassenger.selfFlag.equals("1")) {
                mCbDefault.setChecked(true);
            }
            currentBean = mPassenger.certInfos.get(0);
            selectDialog = new AlertDialog.Builder(
                    mContext).setSingleChoiceItems(items,
                    getChoiceItemNum(currentBean.certType), (dialog, which) -> {
                        //mAges.setText(items[which]);
                        switch (which) {
                            case 0:
                                mTvPassport.setText("护照");
                                boolean haveData0 = false;
                                for (int i = 0;
                                     i < mPassenger.certInfos.size();
                                     i++) {
                                    PassengerInformation.CertInfosBean
                                            certInfosBean = mPassenger.certInfos
                                            .get(i);

                                    if (certInfosBean.certType.equals("P")) {
                                        mCetPassport.setText(
                                                certInfosBean.certNo);//证件号码
                                        //国籍不变，直接不处理
                                        //mTvNationality.setText(
                                        //        certInfosBean.nationality);//国籍
                                        mTvValidPeriod.setText(
                                                certInfosBean.expireDate);//有效期
                                        currentBean = certInfosBean;
                                        haveData0 = true;
                                        break;
                                    }
                                }
                                if (haveData0 == false) {
                                    setDataEmpty();
                                }

                                break;
                            case 1:
                                mTvPassport.setText("身份证");
                                boolean haveData1 = false;
                                for (int i = 0;
                                     i < mPassenger.certInfos.size();
                                     i++) {
                                    PassengerInformation.CertInfosBean
                                            certInfosBean = mPassenger.certInfos
                                            .get(i);
                                    if (certInfosBean.certType.equals("NI")) {
                                        mCetPassport.setText(
                                                certInfosBean.certNo);//证件号码
                                        //mTvNationality.setText(
                                        //        certInfosBean.nationality);//国籍
                                        mTvValidPeriod.setText(
                                                certInfosBean.expireDate);//有效期
                                        currentBean = certInfosBean;
                                        haveData1 = true;
                                        break;
                                    }
                                }
                                if (haveData1 == false) {
                                    setDataEmpty();
                                }

                                break;
                            case 2:
                                mTvPassport.setText("其它");
                                boolean haveData2 = false;
                                for (int i = 0;
                                     i < mPassenger.certInfos.size();
                                     i++) {
                                    PassengerInformation.CertInfosBean
                                            certInfosBean = mPassenger.certInfos
                                            .get(i);
                                    if (certInfosBean.certType.equals("ID")) {
                                        mCetPassport.setText(
                                                certInfosBean.certNo);//证件号码
                                        //mTvNationality.setText(
                                        //        certInfosBean.nationality);//国籍
                                        mTvValidPeriod.setText(
                                                certInfosBean.expireDate);//有效期
                                        currentBean = certInfosBean;
                                        haveData2 = true;
                                        break;
                                    }
                                }
                                if (haveData2 == false) {
                                    setDataEmpty();
                                }

                                break;
                        }

                        KeyboardUtils.hideSoftInput(this);
                        dialog.dismiss();
                    }).setCancelable(true);
        }

        if (mClickAddBtn == true) {
            selectDialog = new AlertDialog.Builder(
                    mContext).setSingleChoiceItems(items, 0,
                    (dialog, which) -> {
                        switch (which) {
                            case 0:
                                mTvPassport.setText("护照");
                                break;
                            case 1:
                                mTvPassport.setText("身份证");
                                break;
                            case 2:
                                mTvPassport.setText("其它");

                                break;
                        }

                        KeyboardUtils.hideSoftInput(this);
                        dialog.dismiss();
                    }).setCancelable(true);
        }
    }


    /**
     * 设置证件信息为空
     */
    private void setDataEmpty() {
        mCetPassport.setText(null);//证件号码
        //mTvNationality.setText(null);//国籍
        mTvValidPeriod.setText(null);//有效期
    }


    /**
     * 从yyyy-MM-dd格式日期得到时间毫秒数
     */
    public static long getSecondsFromDate(String expireDate) {
        if (expireDate == null || expireDate.trim().equals("")) return 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = sdf.parse(expireDate);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }


    private int year;
    private int month;
    private int day;
    private DatePickerDialog.OnDateSetListener mDateSetListener
            = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int myyear, int monthOfYear, int dayOfMonth) {
            year = myyear;
            month = monthOfYear;
            day = dayOfMonth;
            if (isBirthday == false) {
                mTvValidPeriod.setText(year + "-" + (month + 1) + "-" + day);
            }
            else {
                mTvBirthday.setText(year + "-" + (month + 1) + "-" + day);
            }
        }
    };


    @Override protected void initToolbar() {
        setSupportActionBar(mToolbar);
        // getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(
                mPassenger == null ? "新增旅客信息" : "编辑旅客信息");
        mTvRight.setText("保存");
    }


    @Override protected boolean toggleOverridePendingTransition() {
        return false;
    }


    @Override protected TransitionMode getOverridePendingTransitionMode() {
        return null;
    }


    @OnClick({ R.id.llNationality, R.id.tvPassport, R.id.llValidPeriod,
                     R.id.llBirthday, R.id.tvRight, R.id.cbMan, R.id.cbWoman,
                     R.id.pllMobile }) public void onClick(View view) {
        switch (view.getId()) {
            case R.id.llNationality:
                ActivityUtils.launchActivity(mContext,
                        ChooseNationalityActivity.class, null,
                        REQUEST_CODE_NATIONALITY);
                break;
            case R.id.tvPassport:
                selectDialog.create().show();
                break;
            case R.id.llValidPeriod:
                isBirthday = false;
                dpd.show();
                break;
            case R.id.llBirthday:
                isBirthday = true;
                dpd.show();
                break;
            case R.id.tvRight:
                saveOrChange();
                break;
            case R.id.cbMan:
                mCbWoman.setChecked(false);
                break;
            case R.id.cbWoman:
                mCbMan.setChecked(false);
                break;
            case R.id.pllMobile:
                //mLlAddPassage.scrollTo(0, 100);
                //mrlBottom.setFocusable(true);
                break;
        }
    }


    /**
     * @param mian 跟布局
     * @param scroll 需要显示的最下方的V
     */
    private void addLayoutListener(final View mian, final View scroll) {
        mian.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            Rect rect = new Rect();
            //获取mian在窗体的可见区域高度
            mian.getWindowVisibleDisplayFrame(rect);
            //获取mian在窗体的不可见区域高度，在键盘没有弹起时
            int mainInvisibleHeight = mian.getRootView().getHeight() -
                    rect.bottom;
            //不可见区域高度对于100，说明键盘弹起
            if (mainInvisibleHeight > 100) {
                int[] location = new int[2];
                scroll.getLocationInWindow(location);
                //获取scroll的窗体位置，算出mian需要滚动的高度
                int scrollHeight = (location[1] + scroll.getHeight()) -
                        rect.bottom;
                mian.scrollTo(0, scrollHeight);
            }
            else {
                mian.scrollTo(0, 0);
            }
        });
    }


    private void saveOrChange() {
        String chinaName = mCetChinaName.getText().toString().trim();
        String firstName = mCetFirstName.getText().toString().trim();
        String secondName = mCetSecondName.getText().toString().trim();

        String passport = mCetPassport.getText().toString().trim();//证件号码
        String mobile = mCetMobile.getText().toString().trim();
        String passportStyle = mTvPassport.getText().toString().trim();//组件类型
        String birthday = mTvBirthday.getText().toString().trim();
        String validPeriod = mTvValidPeriod.getText().toString().trim();//有效期
        //mTvNationality显示的是国家的名字，要保存的是二字码
        //String nationalityCode = mTvNationality.getText().toString().trim();//有效期;
        String nationalityCode;

        String selfFlag = "0";
        if (mCbDefault.isChecked()) {
            selfFlag = "1";
        }
        if (TextUtils.isEmpty(chinaName)) {
            ToastUtils.showLongToast(R.string.notChinaName);
            return;
        }
        if (!TextUtils.isEmpty(chinaName) &&
                !TextCheckUtils.checkNameChese(chinaName)) {
            ToastUtils.showLongToast(R.string.notRightChinaName);
            return;
        }
        if (!TextUtils.isEmpty(mobile) && !TextCheckUtils.isMobileNO(mobile)) {
            ToastUtils.showLongToast(R.string.notRightMobile);
            return;
        }
        if (TextUtils.isEmpty(firstName) || TextUtils.isEmpty(secondName)) {
            ToastUtils.showLongToast(R.string.notEnglishName);
            return;
        }
        if (TextUtils.isEmpty(passport)) {
            ToastUtils.showLongToast(R.string.notIDNum);
            return;
        }
        //身份证校验
        if (passportStyle.equals("身份证")) {
            if(!IDCard.IDCardValidate(passport).equals("")){
                ToastUtils.showLongToast(IDCard.IDCardValidate(passport));
                return;
            }

        }
        //if (passportStyle.equals("护照")) {
        //    if(!TextCheckUtils.checkPassport(passport)){
        //        ToastUtils.showLongToast(R.string.notIDNum2);
        //        return;
        //    }
        //
        //}
        if (TextUtils.isEmpty(birthday) || TextUtils.isEmpty(validPeriod)) {
            ToastUtils.showLongToast(R.string.notTime);
            return;
        }
        //国籍处理
        if (mClickAddBtn == true) {
            if (mNationality == null) {
                ToastUtils.showLongToast(R.string.notNationality);
                return;
            }
            //新建
            nationalityCode = mNationality.countryCode + "";
        }
        else {
            if (mNationality != null) {
                //说明直接从编辑界面进来，国家信息改变了
                nationalityCode = mNationality.countryCode + "";
            }
            else {
                //说明直接从编辑界面进来，而且国家信息没有改变
                nationalityCode = mPassenger.certInfos.get(0).nationality;
            }
        }

        if (!(getSecondsFromDate(birthday) < mCurrentSecond)) {
            ToastUtils.showLongToast(R.string.notRightTime2);
            return;
        }
        if (!(getSecondsFromDate(validPeriod) > mCurrentSecond)) {
            ToastUtils.showLongToast(R.string.notRightTime);
            return;
        }

        //新增联系人
        if (mPassenger == null) {
            DataManager.addPassenger(chinaName, firstName, secondName, mobile,
                    mCbWoman.isChecked() ? "F" : "M", birthday, nationalityCode,
                    getCertTypeCTE(passportStyle), passport, validPeriod,
                    selfFlag).compose(bindToLifecycle()).
                               subscribe(getSubscribe(R.string.saveIng, s -> {
                                   //这里应该通知界面刷新，2个activity之间的交互
                                   ToastUtils.showLongToast(
                                           R.string.addSuccess);
                                   setResult(RESULT_OK);
                                   AddPassengersInfActivity.this.finish();
                               }));
        }
        //编辑联系人
        else {

            DataManager.editPassenger(chinaName, firstName, secondName, mobile,
                    mCbWoman.isChecked() ? "F" : "M", birthday, nationalityCode,
                    getCertTypeCTE(passportStyle), passport, validPeriod,
                    selfFlag, mPassenger.passengerId)
                       .compose(bindToLifecycle())
                       .
                               subscribe(getSubscribe(R.string.saveIng, s -> {
                                   //这里应该通知界面刷新，2个activity之间的交互
                                   ToastUtils.showLongToast(
                                           R.string.addSuccess);
                                   setResult(RESULT_OK);
                                   AddPassengersInfActivity.this.finish();
                               }));
        }
    }


    /**
     * 得到AlertDialog默认被选中的ItemNum
     */
    @NonNull private int getChoiceItemNum(String passportTyle) {
        if (passportTyle.equals("P")) {
            return 0;
        }
        else if (passportTyle.equals("NI")) {
            return 1;
        }
        else {
            return 2;
        }
    }


    /**
     * 得到证件类型，文字转换成字母
     */
    @NonNull private String getCertTypeCTE(String passportTyle) {
        if (passportTyle.equals("身份证")) {
            return "NI";
        }
        else if (passportTyle.equals("护照")) {
            return "P";
        }
        else {
            return "ID";
        }
    }


    /**
     * 得到证件类型，字母转换成文字
     */
    @NonNull private String getCertTypeETC(String passportTyle) {
        if (passportTyle.equals("NI")) {
            return "身份证";
        }
        else if (passportTyle.equals("P")) {
            return "护照";
        }
        else {
            return "其他";
        }
    }


    //获取国籍
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        if (REQUEST_CODE_NATIONALITY == requestCode) {
            mNationality = (Nationality) data.getSerializableExtra(
                    "nationality");
            mTvNationality.setText(mNationality.name + "");
        }
    }
}