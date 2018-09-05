package com.tempus.portal.ui.information;

import android.app.Service;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import com.bigkoo.pickerview.OptionsPickerView;
import com.google.gson.Gson;
import com.tempus.portal.R;
import com.tempus.portal.base.BaseActivity;
import com.tempus.portal.base.utils.AppUtils;
import com.tempus.portal.base.utils.TextCheckUtils;
import com.tempus.portal.base.utils.ToastUtils;
import com.tempus.portal.manager.DataManager;
import com.tempus.portal.model.ProvinceCityDistrictJsonBean;
import com.tempus.portal.model.information.AddressInformation;
import com.tempus.portal.view.widget.ClearEditText;
import java.util.ArrayList;
import org.json.JSONArray;

/**
 * Created by Administrator on 2016/12/14.
 */

public class AddAddressInfActivity extends BaseActivity {
    private static final String TAG = "AddAddressInfActivity";
    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.tvRight) TextView mTvRight;

    @BindView(R.id.cetName) ClearEditText mCetName;
    @BindView(R.id.cetMobile) ClearEditText mCetMobile;
    @BindView(R.id.tvProvince) TextView mTvProvince;
    @BindView(R.id.cetDetailAddress) ClearEditText mCetDetailAddress;
    @BindView(R.id.cetZipCode) ClearEditText mCetZipCode;
    @BindView(R.id.cbDefault) CheckBox mCBAddressDefault;

    private String mProvince;
    private String mCity;
    private String mArea;

    private AddressInformation mAddress;

    private ArrayList<ProvinceCityDistrictJsonBean> options1Items
            = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<String>>> options3Items
            = new ArrayList<>();
    private Thread thread;
    private static final int MSG_LOAD_DATA = 0x0001;
    private static final int MSG_LOAD_SUCCESS = 0x0002;
    private static final int MSG_LOAD_FAILED = 0x0003;
    private boolean isLoaded = false;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_LOAD_DATA:
                    if (thread == null) {//如果已创建就不再重新创建子线程了

                        thread = new Thread(new Runnable() {
                            @Override public void run() {
                                // 写子线程中的操作,解析省市区数据
                                initJsonData();
                            }
                        });
                        thread.start();
                    }
                    break;

                case MSG_LOAD_SUCCESS:
                    isLoaded = true;
                    break;

                case MSG_LOAD_FAILED:
                    break;
            }
        }
    };


    public static Bundle buildBundle(AddressInformation address) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("address", address);
        return bundle;
    }


    @Override protected void getBundleExtras(Bundle extras) {
        mAddress = (AddressInformation) extras.getSerializable("address");
    }


    @Override protected int getContentViewLayoutID() {
        return R.layout.activity_add_address;
    }


    @Override protected View getLoadingTargetView() {
        return null;
    }


    @Override protected void initView(Bundle savedInstanceState) {
        mHandler.sendEmptyMessage(MSG_LOAD_DATA);

        if (mAddress != null) {
            mCetName.setText(mAddress.receiverName);
            mCetMobile.setText(mAddress.receiverMobile);
            mCetDetailAddress.setText(mAddress.detailAddress);
            mCetZipCode.setText(mAddress.postCode);
            if (mAddress.selfFlag.equals("1")) {
                mCBAddressDefault.setChecked(true);
            }
            mProvince = mAddress.province;
            mCity = mAddress.city;
            mArea = mAddress.area;
            mTvProvince.setText(mProvince + mCity + mArea);
        }
    }


    @Override protected void initToolbar() {
        setSupportActionBar(mToolbar);
        // getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(mAddress == null ? "新增地址信息" : "编辑地址信息");

        mTvRight.setText("保存");
    }


    @Override protected boolean toggleOverridePendingTransition() {
        return false;
    }


    @Override protected TransitionMode getOverridePendingTransitionMode() {
        return null;
    }


    @OnClick({ R.id.tvRight, R.id.llSelectProvince })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvRight:
                saveOrChange();
                break;

            case R.id.llSelectProvince:
                if (isLoaded) {
                    InputMethodManager imm
                            = (InputMethodManager) getSystemService(
                            Service.INPUT_METHOD_SERVICE);
                    //隐藏键盘
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    ShowPickerView();
                }
                else {
                    ToastUtils.showLongToast(
                            "Please waiting until the data is parsed");
                }
                break;
        }
    }


    private void saveOrChange() {

        String name = mCetName.getText().toString().trim();
        String mobile = mCetMobile.getText().toString().trim();
        String address = mTvProvince.getText().toString().trim();
        String detailAddress = mCetDetailAddress.getText().toString().trim();
        String zipCode = mCetZipCode.getText().toString().trim();
        String selfFlag = "0";
        if (mCBAddressDefault.isChecked()) {
            selfFlag = "1";
        }
        if (TextUtils.isEmpty(name)) {
            ToastUtils.showLongToast(R.string.notAddressName);
            return;
        }
        if (!TextUtils.isEmpty(name) && !TextCheckUtils.checkNameChese(name)) {
            ToastUtils.showLongToast(R.string.notRightChinaName);
            return;
        }
        if (TextUtils.isEmpty(mobile)) {
            ToastUtils.showLongToast(R.string.notMobile);
            return;
        }
        if (!TextUtils.isEmpty(mobile) && !TextCheckUtils.isMobileNO(mobile)) {
            ToastUtils.showLongToast(R.string.notRightMobile);
            return;
        }
        if (TextUtils.isEmpty(address)) {
            ToastUtils.showLongToast(R.string.notAddress);
            return;
        }
        if (TextUtils.isEmpty(detailAddress)) {
            ToastUtils.showLongToast(R.string.notDetailAddress);
            return;
        }

        if (!TextUtils.isEmpty(detailAddress) &&
                !((detailAddress.replaceAll(" ", "")).length() > 4)) {
            ToastUtils.showLongToast(R.string.notRightAddress);
            return;
        }

        if (TextCheckUtils.isInteger(detailAddress.replaceAll(" ", ""))) {
            ToastUtils.showLongToast(R.string.notRightAddress1);
            return;
        }
        if (TextCheckUtils.isAlphabet(detailAddress.replaceAll(" ", ""))) {
            ToastUtils.showLongToast(R.string.notRightAddress1);
            return;
        }

        if (!(TextCheckUtils.checkDetailAddress(
                detailAddress.replaceAll(" ", "")))) {
            ToastUtils.showLongToast(R.string.notRightAddress1);
            return;
        }
        if (!TextUtils.isEmpty(zipCode) && !TextCheckUtils.isZipNO(zipCode)) {
            ToastUtils.showLongToast(R.string.notRightZip);
            return;
        }

        if (mAddress == null) {
            DataManager.addAddress(name, mobile, mProvince, mCity, mArea,
                    detailAddress.replaceAll(" ", ""), zipCode, selfFlag)
                       .compose(bindToLifecycle())
                       .
                               subscribe(getSubscribe(R.string.saveIng, s -> {
                                   setResult(RESULT_OK);
                                   ToastUtils.showLongToast(
                                           R.string.addSuccess);
                                   AddAddressInfActivity.this.finish();
                               }));
        }

        //编辑联系人
        else {
            DataManager.editAddress(name, mobile, mProvince, mCity, mArea,
                    detailAddress.replaceAll(" ", ""), zipCode, selfFlag,
                    mAddress.addressId).compose(bindToLifecycle()).
                               subscribe(getSubscribe(R.string.saveIng, s -> {
                                   setResult(RESULT_OK);
                                   ToastUtils.showLongToast(
                                           R.string.addSuccess);
                                   AddAddressInfActivity.this.finish();
                               }));
        }
    }


    private void ShowPickerView() {// 弹出选择器

        OptionsPickerView pvOptions = new OptionsPickerView.Builder(this,
                new OptionsPickerView.OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int options2, int options3, View v) {
                        //返回的分别是三个级别的选中位置
                        mProvince = options1Items.get(options1)
                                                 .getPickerViewText();
                        mCity = options2Items.get(options1).get(options2);
                        mArea = options3Items.get(options1)
                                             .get(options2)
                                             .get(options3);

                        String tx = mProvince + mCity + mArea;

                        mTvProvince.setText(tx);
                    }
                })

                .setTitleText("地区选择")
                .setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                .setContentTextSize(20)
                .build();

        pvOptions.setPicker(options1Items);//一级选择器
        pvOptions.setPicker(options1Items, options2Items);//二级选择器
        pvOptions.setPicker(options1Items, options2Items, options3Items);//三级选择器
        pvOptions.show();
    }


    private void initJsonData() {//解析数据

        /**
         * 注意：assets 目录下的Json文件仅供参考，实际使用可自行替换文件
         * 关键逻辑在于循环体
         *
         * */

        String JsonData = AppUtils.getJson(mContext, "province.json");
        //获取assets目录下的json文件数据

        ArrayList<ProvinceCityDistrictJsonBean> jsonBean = parseData(
                JsonData);//用Gson 转成实体

        /**
         * 添加省份数据
         *
         * 注意：如果是添加的JavaBean实体，则实体类需要实现 IPickerViewData 接口，
         * PickerView会通过getPickerViewText方法获取字符串显示出来。
         */
        options1Items = jsonBean;

        for (int i = 0; i < jsonBean.size(); i++) {//遍历省份
            ArrayList<String> CityList = new ArrayList<>();//该省的城市列表（第二级）
            ArrayList<ArrayList<String>> Province_AreaList
                    = new ArrayList<>();//该省的所有地区列表（第三极）

            for (int c = 0;
                 c < jsonBean.get(i).getCityList().size();
                 c++) {//遍历该省份的所有城市
                String CityName = jsonBean.get(i)
                                          .getCityList()
                                          .get(c)
                                          .getName();
                CityList.add(CityName);//添加城市

                ArrayList<String> City_AreaList = new ArrayList<>();//该城市的所有地区列表

                //如果无地区数据，建议添加空字符串，防止数据为null 导致三个选项长度不匹配造成崩溃
                if (jsonBean.get(i).getCityList().get(c).getArea() == null ||
                        jsonBean.get(i).getCityList().get(c).getArea().size() ==
                                0) {
                    City_AreaList.add("");
                }
                else {

                    for (int d = 0;
                         d < jsonBean.get(i)
                                     .getCityList()
                                     .get(c)
                                     .getArea()
                                     .size();
                         d++) {//该城市对应地区所有数据
                        String AreaName = jsonBean.get(i)
                                                  .getCityList()
                                                  .get(c)
                                                  .getArea()
                                                  .get(d);

                        City_AreaList.add(AreaName);//添加该城市所有地区数据
                    }
                }
                Province_AreaList.add(City_AreaList);//添加该省所有地区数据
            }

            /**
             * 添加城市数据
             */
            options2Items.add(CityList);

            /**
             * 添加地区数据
             */
            options3Items.add(Province_AreaList);
        }

        mHandler.sendEmptyMessage(MSG_LOAD_SUCCESS);
    }


    public ArrayList<ProvinceCityDistrictJsonBean> parseData(String result) {//Gson 解析
        ArrayList<ProvinceCityDistrictJsonBean> detail = new ArrayList<>();
        try {
            JSONArray data = new JSONArray(result);
            Gson gson = new Gson();
            for (int i = 0; i < data.length(); i++) {
                ProvinceCityDistrictJsonBean entity = gson.fromJson(
                        data.optJSONObject(i).toString(),
                        ProvinceCityDistrictJsonBean.class);
                detail.add(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
            mHandler.sendEmptyMessage(MSG_LOAD_FAILED);
        }
        return detail;
    }
}