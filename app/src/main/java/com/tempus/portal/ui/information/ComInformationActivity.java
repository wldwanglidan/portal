package com.tempus.portal.ui.information;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.tempus.portal.R;
import com.tempus.portal.base.BaseActivity;
import com.tempus.portal.base.BaseFragment;
import com.tempus.portal.model.event.LoginEvent;
import java.util.ArrayList;
import java.util.List;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class ComInformationActivity extends BaseActivity {

    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.tlComInf) TabLayout mTlTab;
    @BindView(R.id.vpComInf) ViewPager mVp;
    //public @BindView(R.id.btnAddConInf) Button mAddConInfBtn;

    private String[] tabTitles;
    private List<BaseFragment> listFragment; //定义要装fragment的列表
    private BaseFragment mPassengerFragment, mContactFragment, mAddressFragment;
    private int mPosition;

    public static final int REQUEST_CODE_PASSENGER = 0x10;//用于回调
    public static final int REQUEST_CODE_CONTACT = 0x11;
    public static final int REQUEST_CODE_ADDRESS = 0x12;


    public static Bundle buildBundle(int position) {
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        return bundle;
    }


    @Override protected void getBundleExtras(Bundle extras) {
        mPosition = extras.getInt("position");
    }


    @Override protected int getContentViewLayoutID() {
        return R.layout.activity_common_information;
    }


    @Override protected View getLoadingTargetView() {

        return ButterKnife.findById(this, R.id.vpComInf);
    }


    @Override protected void initView(Bundle savedInstanceState) {
        this.tabTitles = getResources().getStringArray(
                R.array.com_information_tabs);
        getData();
    }


    private void getData() {
        // mTlTab.setTabMode(TabLayout.MODE_SCROLLABLE);
        //设置MODE_SCROLLABLE可以滚动，当标签比较多时使用
        mPassengerFragment = PassengerInfFragment.newInstance("passenger");
        mContactFragment = ContactInfFragment.newInstance("contact");
        mAddressFragment = AddressInfFragment.newInstance("address");

        listFragment = new ArrayList<>();
        listFragment.add(mPassengerFragment);
        listFragment.add(mContactFragment);
        listFragment.add(mAddressFragment);

        mVp.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override public Fragment getItem(int position) {
                return listFragment.get(position);
            }


            @Override public int getCount() {
                return listFragment.size();
            }


            @Override public CharSequence getPageTitle(int position) {
                return tabTitles[position];
            }
        });

        mTlTab.removeAllTabs();
        mTlTab.setupWithViewPager(mVp);
        mVp.setCurrentItem(mPosition);
    }


    @Override protected void initToolbar() {
        setSupportActionBar(mToolbar);
        // getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("常用信息");
    }


    @Override protected boolean toggleOverridePendingTransition() {
        return false;
    }


    @Override protected TransitionMode getOverridePendingTransitionMode() {
        return null;
    }


    @Override @CallSuper protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }


    @Override @CallSuper protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }


    //这个方法EventBus会调用
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoginEvent(LoginEvent resultEvent) {
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        if (REQUEST_CODE_PASSENGER == requestCode) {
            PassengerInfFragment passengerInfFragment
                    = (PassengerInfFragment) listFragment.get(0);
            passengerInfFragment.getData();

        }
        if (REQUEST_CODE_CONTACT == requestCode) {
            ContactInfFragment contactInfFragment
                    = (ContactInfFragment) listFragment.get(1);
            contactInfFragment.getData();
        }
        if (REQUEST_CODE_ADDRESS == requestCode) {
            AddressInfFragment addressInfFragment
                    = (AddressInfFragment) listFragment.get(2);
            addressInfFragment.getData();
        }
    }
}
