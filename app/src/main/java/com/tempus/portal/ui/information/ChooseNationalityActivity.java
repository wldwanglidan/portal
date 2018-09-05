package com.tempus.portal.ui.information;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import com.camnter.easyrecyclerview.widget.EasyRecyclerView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tempus.portal.R;
import com.tempus.portal.base.BaseActivity;
import com.tempus.portal.base.utils.glide.GlideUtils;
import com.tempus.portal.model.Nationality;
import com.tempus.portal.view.adapter.NationalityAdapter;
import com.tempus.portal.view.widget.easyrecyclerviewsidebar.EasyFloatingImageView;
import com.tempus.portal.view.widget.easyrecyclerviewsidebar.EasyRecyclerViewSidebar;
import com.tempus.portal.view.widget.easyrecyclerviewsidebar.sections.EasyImageSection;
import com.tempus.portal.view.widget.easyrecyclerviewsidebar.sections.EasySection;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Administrator on 2017/9/8.
 */

public class ChooseNationalityActivity extends BaseActivity
        implements EasyRecyclerViewSidebar.OnTouchSectionListener {
    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.section_rv) EasyRecyclerView mSectionRv;
    @BindView(R.id.section_sidebar) EasyRecyclerViewSidebar mSectionSidebar;
    @BindView(R.id.section_floating_iv) EasyFloatingImageView
            mSectionFloatingIv;
    @BindView(R.id.section_floating_tv) TextView mSectionFloatingTv;
    @BindView(R.id.section_floating_rl) RelativeLayout mSectionFloatingRl;
    private NationalityAdapter mNationalityAdapter;


    @Override protected void getBundleExtras(Bundle extras) {

    }


    @Override protected int getContentViewLayoutID() {
        return R.layout.activity_choose_nationality;
    }


    @Override protected View getLoadingTargetView() {
        return null;
    }


    @Override protected void initView(Bundle savedInstanceState) {
        this.initAdapter();
        if (this.mSectionRv != null) {
            this.mSectionRv.setAdapter(this.mNationalityAdapter);
        }

        this.mSectionSidebar.setFloatView(mSectionFloatingRl);
        this.mSectionSidebar.setOnTouchSectionListener(this);
        initData();
        initListeners();
    }


    @Override protected void initToolbar() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("选择国籍");
    }


    @Override protected boolean toggleOverridePendingTransition() {
        return false;
    }


    @Override protected TransitionMode getOverridePendingTransitionMode() {
        return null;
    }


    private void initData() {
        this.mNationalityAdapter.setList(this.getData());
        this.mNationalityAdapter.notifyDataSetChanged();
        this.mSectionSidebar.setSections(
                this.mNationalityAdapter.getSections());
    }


    public void initAdapter() {
        this.mNationalityAdapter = new NationalityAdapter();
    }


    private void initListeners() {
        this.mNationalityAdapter.setOnItemClickListener((view, i) -> {
            setResult(RESULT_OK, new Intent().putExtra("nationality",
                    (Serializable) mNationalityAdapter.getItemByPosition(i)));
            finish();
        });
    }


    public List<Nationality> getData() {
        String json =
                "[{\"countryCode\":\"CN\",\"countryName\":\"中国大陆\",\"initial\":\"Z\",\"hotFlag\":\"Y\"},\n" +
                        "{\"countryCode\":\"HK\",\"countryName\":\"中国香港\",\"initial\":\"Z\",\"hotFlag\":\"Y\"},\n" +
                        "{\"countryCode\":\"MO\",\"countryName\":\"中国澳门\",\"initial\":\"Z\",\"hotFlag\":\"Y\"},\n" +
                        "{\"countryCode\":\"TW\",\"countryName\":\"中国台湾\",\"initial\":\"Z\",\"hotFlag\":\"Y\"},\n" +
                        "{\"countryCode\":\"US\",\"countryName\":\"美国\",\"initial\":\"M\",\"hotFlag\":\"Y\"},\n" +
                        "{\"countryCode\":\"FR\",\"countryName\":\"法国\",\"initial\":\"F\",\"hotFlag\":\"Y\"},\n" +
                        "{\"countryCode\":\"GB\",\"countryName\":\"英国\",\"initial\":\"Y\",\"hotFlag\":\"Y\"},\n" +
                        "{\"countryCode\":\"JP\",\"countryName\":\"日本\",\"initial\":\"R\",\"hotFlag\":\"Y\"},\n" +
                        "{\"countryCode\":\"KR\",\"countryName\":\"韩国\",\"initial\":\"H\",\"hotFlag\":\"Y\"},\n" +
                        "{\"countryCode\":\"MY\",\"countryName\":\"马来西亚\",\"initial\":\"M\",\"hotFlag\":\"Y\"},\n" +
                        "{\"countryCode\":\"CA\",\"countryName\":\"加拿大\",\"initial\":\"J\",\"hotFlag\":\"Y\"},\n" +
                        "{\"countryCode\":\"AD\",\"countryName\":\"安道尔共和国\",\"initial\":\"A\",\"hotFlag\":\"N\"},\n" +
                        "{\"countryCode\":\"EE\",\"countryName\":\"爱沙尼亚\",\"initial\":\"A\",\"hotFlag\":\"N\"},\n" +
                        "{\"countryCode\":\"DZ\",\"countryName\":\"阿尔及利亚\",\"initial\":\"A\",\"hotFlag\":\"N\"},\n" +
                        "{\"countryCode\":\"EG\",\"countryName\":\"埃及\",\"initial\":\"A\",\"hotFlag\":\"N\"},\n" +
                        "{\"countryCode\":\"ET\",\"countryName\":\"埃塞俄比亚\",\"initial\":\"A\",\"hotFlag\":\"N\"},\n" +
                        "{\"countryCode\":\"OM\",\"countryName\":\"阿曼\",\"initial\":\"A\",\"hotFlag\":\"N\"},\n" +
                        "{\"countryCode\":\"AZ\",\"countryName\":\"阿塞拜疆\",\"initial\":\"A\",\"hotFlag\":\"N\"},\n" +
                        "{\"countryCode\":\"AU\",\"countryName\":\"澳大利亚\",\"initial\":\"A\",\"hotFlag\":\"N\"},\n" +
                        "{\"countryCode\":\"AT\",\"countryName\":\"奥地利\",\"initial\":\"A\",\"hotFlag\":\"N\"},\n" +
                        "{\"countryCode\":\"AR\",\"countryName\":\"阿根廷\",\"initial\":\"A\",\"hotFlag\":\"N\"},\n" +
                        "{\"countryCode\":\"AF\",\"countryName\":\"阿富汗\",\"initial\":\"A\",\"hotFlag\":\"N\"},\n" +
                        "{\"countryCode\":\"AG\",\"countryName\":\"安提瓜和巴布达\",\"initial\":\"A\",\"hotFlag\":\"N\"},\n" +
                        "{\"countryCode\":\"AE\",\"countryName\":\"阿拉伯联合酋长国\",\"initial\":\"A\",\"hotFlag\":\"N\"},\n" +
                        "{\"countryCode\":\"AI\",\"countryName\":\"安圭拉岛\",\"initial\":\"A\",\"hotFlag\":\"N\"},\n" +
                        "{\"countryCode\":\"AL\",\"countryName\":\"阿尔巴尼亚\",\"initial\":\"A\",\"hotFlag\":\"N\"},\n" +
                        "{\"countryCode\":\"IE\",\"countryName\":\"爱尔兰\",\"initial\":\"A\",\"hotFlag\":\"N\"},\n" +
                        "{\"countryCode\":\"AO\",\"countryName\":\"安哥拉\",\"initial\":\"A\",\"hotFlag\":\"N\"},\n" +
                        "{\"countryCode\":\"BZ\",\"countryName\":\"伯利兹\",\"initial\":\"B\",\"hotFlag\":\"N\"},\n" +
                        "{\"countryCode\":\"PR\",\"countryName\":\"波多黎各\",\"initial\":\"B\",\"hotFlag\":\"N\"},\n" +
                        "{\"countryCode\":\"PL\",\"countryName\":\"波兰\",\"initial\":\"B\",\"hotFlag\":\"N\"},\n" +
                        "{\"countryCode\":\"PK\",\"countryName\":\"巴基斯坦\",\"initial\":\"B\",\"hotFlag\":\"N\"},\n" +
                        "{\"countryCode\":\"PG\",\"countryName\":\"巴布亚新几内亚\",\"initial\":\"B\",\"hotFlag\":\"N\"},\n" +
                        "{\"countryCode\":\"PY\",\"countryName\":\"巴拉圭\",\"initial\":\"B\",\"hotFlag\":\"N\"},\n" +
                        "{\"countryCode\":\"BO\",\"countryName\":\"玻利维亚\",\"initial\":\"B\",\"hotFlag\":\"N\"},\n" +
                        "{\"countryCode\":\"PA\",\"countryName\":\"巴拿马\",\"initial\":\"B\",\"hotFlag\":\"N\"},\n" +
                        "{\"countryCode\":\"IS\",\"countryName\":\"冰岛\",\"initial\":\"B\",\"hotFlag\":\"N\"},\n" +
                        "{\"countryCode\":\"BY\",\"countryName\":\"白俄罗斯\",\"initial\":\"B\",\"hotFlag\":\"N\"},\n" +
                        "{\"countryCode\":\"BW\",\"countryName\":\"博茨瓦纳\",\"initial\":\"B\",\"hotFlag\":\"N\"},\n" +
                        "{\"countryCode\":\"BF\",\"countryName\":\"布基纳法索\",\"initial\":\"B\",\"hotFlag\":\"N\"},\n" +
                        "{\"countryCode\":\"BH\",\"countryName\":\"巴林\",\"initial\":\"B\",\"hotFlag\":\"N\"},\n" +
                        "{\"countryCode\":\"BE\",\"countryName\":\"比利时\",\"initial\":\"B\",\"hotFlag\":\"N\"},\n" +
                        "{\"countryCode\":\"BI\",\"countryName\":\"布隆迪\",\"initial\":\"B\",\"hotFlag\":\"N\"},\n" +
                        "{\"countryCode\":\"BG\",\"countryName\":\"保加利亚\",\"initial\":\"B\",\"hotFlag\":\"N\"},\n" +
                        "{\"countryCode\":\"BJ\",\"countryName\":\"贝宁\",\"initial\":\"B\",\"hotFlag\":\"N\"},\n" +
                        "{\"countryCode\":\"BL\",\"countryName\":\"巴勒斯坦\",\"initial\":\"B\",\"hotFlag\":\"N\"},\n" +
                        "{\"countryCode\":\"BS\",\"countryName\":\"巴哈马\",\"initial\":\"B\",\"hotFlag\":\"N\"},\n" +
                        "{\"countryCode\":\"BM\",\"countryName\":\"百慕大群岛\",\"initial\":\"B\",\"hotFlag\":\"N\"},\n" +
                        "{\"countryCode\":\"BB\",\"countryName\":\"巴巴多斯\",\"initial\":\"B\",\"hotFlag\":\"N\"},\n" +
                        "{\"countryCode\":\"BR\",\"countryName\":\"巴西\",\"initial\":\"B\",\"hotFlag\":\"N\"},\n" +
                        "{\"countryCode\":\"KP\",\"countryName\":\"朝鲜\",\"initial\":\"C\",\"hotFlag\":\"N\"},\n" +
                        "{\"countryCode\":\"DK\",\"countryName\":\"丹麦\",\"initial\":\"D\",\"hotFlag\":\"N\"},\n" +
                        "{\"countryCode\":\"DE\",\"countryName\":\"德国\",\"initial\":\"D\",\"hotFlag\":\"N\"},\n" +
                        "{\"countryCode\":\"TG\",\"countryName\":\"多哥\",\"initial\":\"D\",\"hotFlag\":\"N\"},\n" +
                        "{\"countryCode\":\"DO\",\"countryName\":\"多米尼加共和国\",\"initial\":\"D\",\"hotFlag\":\"N\"},\n" +
                        "{\"countryCode\":\"EC\",\"countryName\":\"厄瓜多尔\",\"initial\":\"E\",\"hotFlag\":\"N\"},\n" +
                        "{\"countryCode\":\"RU\",\"countryName\":\"俄罗斯\",\"initial\":\"E\",\"hotFlag\":\"N\"},\n" +
                        "{\"countryCode\":\"FR\",\"countryName\":\"法国\",\"initial\":\"F\",\"hotFlag\":\"Y\"},\n" +
                        "{\"countryCode\":\"FI\",\"countryName\":\"芬兰\",\"initial\":\"F\",\"hotFlag\":\"N\"},\n" +
                        "{\"countryCode\":\"FJ\",\"countryName\":\"斐济\",\"initial\":\"F\",\"hotFlag\":\"N\"},\n" +
                        "{\"countryCode\":\"GF\",\"countryName\":\"法属圭亚那\",\"initial\":\"F\",\"hotFlag\":\"N\"},\n" +
                        "{\"countryCode\":\"PH\",\"countryName\":\"菲律宾\",\"initial\":\"F\",\"hotFlag\":\"N\"},\n" +
                        "{\"countryCode\":\"PF\",\"countryName\":\"法属玻利尼西亚\",\"initial\":\"F\",\"hotFlag\":\"N\"},\n" +
                        "{\"countryCode\":\"GY\",\"countryName\":\"圭亚那\",\"initial\":\"G\",\"hotFlag\":\"N\"},\n" +
                        "{\"countryCode\":\"GU\",\"countryName\":\"关岛\",\"initial\":\"G\",\"hotFlag\":\"N\"},\n" +
                        "{\"countryCode\":\"GM\",\"countryName\":\"冈比亚\",\"initial\":\"G\",\"hotFlag\":\"N\"},\n" +
                        "{\"countryCode\":\"CG\",\"countryName\":\"刚果\",\"initial\":\"G\",\"hotFlag\":\"N\"},\n" +
                        "{\"countryCode\":\"CU\",\"countryName\":\"古巴\",\"initial\":\"G\",\"hotFlag\":\"N\"},\n" +
                        "{\"countryCode\":\"GE\",\"countryName\":\"格鲁吉亚\",\"initial\":\"G\",\"hotFlag\":\"N\"},\n" +
                        "{\"countryCode\":\"GD\",\"countryName\":\"格林纳达\",\"initial\":\"G\",\"hotFlag\":\"N\"},\n" +
                        "{\"countryCode\":\"CO\",\"countryName\":\"哥伦比亚\",\"initial\":\"G\",\"hotFlag\":\"N\"},\n" +
                        "{\"countryCode\":\"CR\",\"countryName\":\"哥斯达黎加\",\"initial\":\"G\",\"hotFlag\":\"N\"},\n" +
                        "{\"countryCode\":\"KR\",\"countryName\":\"韩国\",\"initial\":\"H\",\"hotFlag\":\"Y\"},\n" +
                        "{\"countryCode\":\"NL\",\"countryName\":\"荷兰\",\"initial\":\"H\",\"hotFlag\":\"N\"},\n" +
                        "{\"countryCode\":\"HT\",\"countryName\":\"海地\",\"initial\":\"H\",\"hotFlag\":\"N\"},\n" +
                        "{\"countryCode\":\"KZ\",\"countryName\":\"哈萨克斯坦\",\"initial\":\"H\",\"hotFlag\":\"N\"},\n" +
                        "{\"countryCode\":\"HN\",\"countryName\":\"洪都拉斯\",\"initial\":\"H\",\"hotFlag\":\"N\"},\n" +
                        "{\"countryCode\":\"CA\",\"countryName\":\"加拿大\",\"initial\":\"J\",\"hotFlag\":\"Y\"},\n" +
                        "{\"countryCode\":\"KH\",\"countryName\":\"柬埔寨\",\"initial\":\"J\",\"hotFlag\":\"N\"},\n" +
                        "{\"countryCode\":\"KG\",\"countryName\":\"吉尔吉斯坦\",\"initial\":\"J\",\"hotFlag\":\"N\"},\n" +
                        "{\"countryCode\":\"GA\",\"countryName\":\"加蓬\",\"initial\":\"J\",\"hotFlag\":\"N\"},\n" +
                        "{\"countryCode\":\"GH\",\"countryName\":\"加纳\",\"initial\":\"J\",\"hotFlag\":\"N\"},\n" +
                        "{\"countryCode\":\"GN\",\"countryName\":\"几内亚\",\"initial\":\"J\",\"hotFlag\":\"N\"},\n" +
                        "{\"countryCode\":\"ZW\",\"countryName\":\"津巴布韦\",\"initial\":\"J\",\"hotFlag\":\"N\"},\n" +
                        "{\"countryCode\":\"DJ\",\"countryName\":\"吉布提\",\"initial\":\"J\",\"hotFlag\":\"N\"},\n" +
                        "{\"countryCode\":\"CZ\",\"countryName\":\"捷克\",\"initial\":\"J\",\"hotFlag\":\"N\"},\n" +
                        "{\"countryCode\":\"KE\",\"countryName\":\"肯尼亚\",\"initial\":\"K\",\"hotFlag\":\"N\"},\n" +
                        "{\"countryCode\":\"KW\",\"countryName\":\"科威特\",\"initial\":\"K\",\"hotFlag\":\"N\"},\n" +
                        "{\"countryCode\":\"KT\",\"countryName\":\"科特迪瓦共和国\",\"initial\":\"K\",\"hotFlag\":\"N\"},\n" +
                        "{\"countryCode\":\"QA\",\"countryName\":\"卡塔尔\",\"initial\":\"K\",\"hotFlag\":\"N\"},\n" +
                        "{\"countryCode\":\"CK\",\"countryName\":\"库克群岛\",\"initial\":\"K\",\"hotFlag\":\"N\"},\n" +
                        "{\"countryCode\":\"CM\",\"countryName\":\"喀麦隆\",\"initial\":\"K\",\"hotFlag\":\"N\"},\n" +
                        "{\"countryCode\":\"LA\",\"countryName\":\"老挝\",\"initial\":\"L\",\"hotFlag\":\"N\"},\n" +
                        "{\"countryCode\":\"LI\",\"countryName\":\"列支敦士登\",\"initial\":\"L\",\"hotFlag\":\"N\"},\n" +
                        "{\"countryCode\":\"LB\",\"countryName\":\"黎巴嫩\",\"initial\":\"L\",\"hotFlag\":\"N\"},\n" +
                        "{\"countryCode\":\"LR\",\"countryName\":\"利比里亚\",\"initial\":\"L\",\"hotFlag\":\"N\"},\n" +
                        "{\"countryCode\":\"LS\",\"countryName\":\"莱索托\",\"initial\":\"L\",\"hotFlag\":\"N\"},\n" +
                        "{\"countryCode\":\"LT\",\"countryName\":\"立陶宛\",\"initial\":\"L\",\"hotFlag\":\"N\"},\n" +
                        "{\"countryCode\":\"LU\",\"countryName\":\"卢森堡\",\"initial\":\"L\",\"hotFlag\":\"N\"},\n" +
                        "{\"countryCode\":\"LV\",\"countryName\":\"拉脱维亚\",\"initial\":\"L\",\"hotFlag\":\"N\"},\n" +
                        "{\"countryCode\":\"LY\",\"countryName\":\"利比亚\",\"initial\":\"L\",\"hotFlag\":\"N\"},\n" +
                        "{\"countryCode\":\"RO\",\"countryName\":\"罗马尼亚\",\"initial\":\"L\",\"hotFlag\":\"N\"},\n" +
                        "{\"countryCode\":\"US\",\"countryName\":\"美国\",\"initial\":\"M\",\"hotFlag\":\"Y\"},\n" +
                        "{\"countryCode\":\"MY\",\"countryName\":\"马来西亚\",\"initial\":\"M\",\"hotFlag\":\"Y\"},\n" +
                        "{\"countryCode\":\"MT\",\"countryName\":\"马耳他\",\"initial\":\"M\",\"hotFlag\":\"N\"},\n" +
                        "{\"countryCode\":\"PE\",\"countryName\":\"秘鲁\",\"initial\":\"M\",\"hotFlag\":\"N\"},\n" +
                        "{\"countryCode\":\"ML\",\"countryName\":\"马里\",\"initial\":\"M\",\"hotFlag\":\"N\"},\n" +
                        "{\"countryCode\":\"MZ\",\"countryName\":\"莫桑比克\",\"initial\":\"M\",\"hotFlag\":\"N\"},\n" +
                        "{\"countryCode\":\"MS\",\"countryName\":\"蒙特塞拉特岛\",\"initial\":\"M\",\"hotFlag\":\"N\"},\n" +
                        "{\"countryCode\":\"MX\",\"countryName\":\"墨西哥\",\"initial\":\"M\",\"hotFlag\":\"N\"},\n" +
                        "{\"countryCode\":\"MV\",\"countryName\":\"马尔代夫\",\"initial\":\"M\",\"hotFlag\":\"N\"},\n" +
                        "{\"countryCode\":\"MU\",\"countryName\":\"毛里求斯\",\"initial\":\"M\",\"hotFlag\":\"N\"},\n" +
                        "{\"countryCode\":\"MM\",\"countryName\":\"缅甸\",\"initial\":\"M\",\"hotFlag\":\"N\"},\n" +
                        "{\"countryCode\":\"MN\",\"countryName\":\"蒙古\",\"initial\":\"M\",\"hotFlag\":\"N\"},\n" +
                        "{\"countryCode\":\"MD\",\"countryName\":\"摩尔多瓦\",\"initial\":\"M\",\"hotFlag\":\"N\"},\n" +
                        "{\"countryCode\":\"MC\",\"countryName\":\"摩纳哥\",\"initial\":\"M\",\"hotFlag\":\"N\"},\n" +
                        "{\"countryCode\":\"MA\",\"countryName\":\"摩洛哥\",\"initial\":\"M\",\"hotFlag\":\"N\"},\n" +
                        "{\"countryCode\":\"BD\",\"countryName\":\"孟加拉国\",\"initial\":\"M\",\"hotFlag\":\"N\"},\n" +
                        "{\"countryCode\":\"MW\",\"countryName\":\"马拉维\",\"initial\":\"M\",\"hotFlag\":\"N\"},\n" +
                        "{\"countryCode\":\"MG\",\"countryName\":\"马达加斯加\",\"initial\":\"M\",\"hotFlag\":\"N\"},\n" +
                        "{\"countryCode\":\"NG\",\"countryName\":\"尼日利亚\",\"initial\":\"N\",\"hotFlag\":\"N\"},\n" +
                        "{\"countryCode\":\"NI\",\"countryName\":\"尼加拉瓜\",\"initial\":\"N\",\"hotFlag\":\"N\"},\n" +
                        "{\"countryCode\":\"NO\",\"countryName\":\"挪威\",\"initial\":\"N\",\"hotFlag\":\"N\"},\n" +
                        "{\"countryCode\":\"NP\",\"countryName\":\"尼泊尔\",\"initial\":\"N\",\"hotFlag\":\"N\"},\n" +
                        "{\"countryCode\":\"NR\",\"countryName\":\"瑙鲁\",\"initial\":\"N\",\"hotFlag\":\"N\"},\n" +
                        "{\"countryCode\":\"YU\",\"countryName\":\"南斯拉夫\",\"initial\":\"N\",\"hotFlag\":\"N\"},\n" +
                        "{\"countryCode\":\"ZA\",\"countryName\":\"南非\",\"initial\":\"N\",\"hotFlag\":\"N\"},\n" +
                        "{\"countryCode\":\"NA\",\"countryName\":\"纳米比亚\",\"initial\":\"N\",\"hotFlag\":\"N\"},\n" +
                        "{\"countryCode\":\"NE\",\"countryName\":\"尼日尔\",\"initial\":\"N\",\"hotFlag\":\"N\"},\n" +
                        "{\"countryCode\":\"PT\",\"countryName\":\"葡萄牙\",\"initial\":\"P\",\"hotFlag\":\"N\"},\n" +
                        "{\"countryCode\":\"JP\",\"countryName\":\"日本\",\"initial\":\"R\",\"hotFlag\":\"Y\"},\n" +
                        "{\"countryCode\":\"SE\",\"countryName\":\"瑞典\",\"initial\":\"R\",\"hotFlag\":\"N\"},\n" +
                        "{\"countryCode\":\"CH\",\"countryName\":\"瑞士\",\"initial\":\"R\",\"hotFlag\":\"N\"},\n" +
                        "{\"countryCode\":\"SK\",\"countryName\":\"斯洛伐克\",\"initial\":\"S\",\"hotFlag\":\"N\"},\n" +
                        "{\"countryCode\":\"SA\",\"countryName\":\"沙特阿拉伯\",\"initial\":\"S\",\"hotFlag\":\"N\"},\n" +
                        "{\"countryCode\":\"SM\",\"countryName\":\"圣马力诺\",\"initial\":\"S\",\"hotFlag\":\"N\"},\n" +
                        "{\"countryCode\":\"SB\",\"countryName\":\"所罗门群岛\",\"initial\":\"S\",\"hotFlag\":\"N\"},\n" +
                        "{\"countryCode\":\"RS\",\"countryName\":\"塞尔维亚\",\"initial\":\"S\",\"hotFlag\":\"N\"},\n" +
                        "{\"countryCode\":\"SC\",\"countryName\":\"塞舌尔\",\"initial\":\"S\",\"hotFlag\":\"N\"},\n" +
                        "{\"countryCode\":\"SD\",\"countryName\":\"苏丹\",\"initial\":\"S\",\"hotFlag\":\"N\"},\n" +
                        "{\"countryCode\":\"SL\",\"countryName\":\"塞拉利昂\",\"initial\":\"S\",\"hotFlag\":\"N\"},\n" +
                        "{\"countryCode\":\"SI\",\"countryName\":\"斯洛文尼亚\",\"initial\":\"S\",\"hotFlag\":\"N\"},\n" +
                        "{\"countryCode\":\"SN\",\"countryName\":\"塞内加尔\",\"initial\":\"S\",\"hotFlag\":\"N\"},\n" +
                        "{\"countryCode\":\"SO\",\"countryName\":\"索马里\",\"initial\":\"S\",\"hotFlag\":\"N\"},\n" +
                        "{\"countryCode\":\"SR\",\"countryName\":\"苏里南\",\"initial\":\"S\",\"hotFlag\":\"N\"},\n" +
                        "{\"countryCode\":\"VC\",\"countryName\":\"圣文森特岛\",\"initial\":\"S\",\"hotFlag\":\"N\"},\n" +
                        "{\"countryCode\":\"LC\",\"countryName\":\"圣卢西亚\",\"initial\":\"S\",\"hotFlag\":\"N\"},\n" +
                        "{\"countryCode\":\"WS\",\"countryName\":\"萨摩亚群岛\",\"initial\":\"S\",\"hotFlag\":\"N\"},\n" +
                        "{\"countryCode\":\"LK\",\"countryName\":\"斯里兰卡\",\"initial\":\"S\",\"hotFlag\":\"N\"},\n" +
                        "{\"countryCode\":\"SZ\",\"countryName\":\"斯威士兰\",\"initial\":\"S\",\"hotFlag\":\"N\"},\n" +
                        "{\"countryCode\":\"CY\",\"countryName\":\"塞浦路斯\",\"initial\":\"S\",\"hotFlag\":\"N\"},\n" +
                        "{\"countryCode\":\"SV\",\"countryName\":\"萨尔瓦多\",\"initial\":\"S\",\"hotFlag\":\"N\"},\n" +
                        "{\"countryCode\":\"ST\",\"countryName\":\"圣多美和普林西比\",\"initial\":\"S\",\"hotFlag\":\"N\"},\n" +
                        "{\"countryCode\":\"TZ\",\"countryName\":\"坦桑尼亚\",\"initial\":\"T\",\"hotFlag\":\"N\"},\n" +
                        "{\"countryCode\":\"TR\",\"countryName\":\"土耳其\",\"initial\":\"T\",\"hotFlag\":\"N\"},\n" +
                        "{\"countryCode\":\"TO\",\"countryName\":\"汤加\",\"initial\":\"T\",\"hotFlag\":\"N\"},\n" +
                        "{\"countryCode\":\"TN\",\"countryName\":\"突尼斯\",\"initial\":\"T\",\"hotFlag\":\"N\"},\n" +
                        "{\"countryCode\":\"TM\",\"countryName\":\"土库曼斯坦\",\"initial\":\"T\",\"hotFlag\":\"N\"},\n" +
                        "{\"countryCode\":\"TJ\",\"countryName\":\"塔吉克斯坦\",\"initial\":\"T\",\"hotFlag\":\"N\"},\n" +
                        "{\"countryCode\":\"TT\",\"countryName\":\"特立尼达和多巴哥\",\"initial\":\"T\",\"hotFlag\":\"N\"},\n" +
                        "{\"countryCode\":\"TH\",\"countryName\":\"泰国\",\"initial\":\"T\",\"hotFlag\":\"N\"},\n" +
                        "{\"countryCode\":\"UG\",\"countryName\":\"乌干达\",\"initial\":\"W\",\"hotFlag\":\"N\"},\n" +
                        "{\"countryCode\":\"UA\",\"countryName\":\"乌克兰\",\"initial\":\"W\",\"hotFlag\":\"N\"},\n" +
                        "{\"countryCode\":\"UY\",\"countryName\":\"乌拉圭\",\"initial\":\"W\",\"hotFlag\":\"N\"},\n" +
                        "{\"countryCode\":\"UZ\",\"countryName\":\"乌兹别克斯坦\",\"initial\":\"W\",\"hotFlag\":\"N\"},\n" +
                        "{\"countryCode\":\"VE\",\"countryName\":\"委内瑞拉\",\"initial\":\"W\",\"hotFlag\":\"N\"},\n" +
                        "{\"countryCode\":\"BN\",\"countryName\":\"文莱\",\"initial\":\"W\",\"hotFlag\":\"N\"},\n" +
                        "{\"countryCode\":\"GT\",\"countryName\":\"危地马拉\",\"initial\":\"W\",\"hotFlag\":\"N\"},\n" +
                        "{\"countryCode\":\"GR\",\"countryName\":\"希腊\",\"initial\":\"X\",\"hotFlag\":\"N\"},\n" +
                        "{\"countryCode\":\"SG\",\"countryName\":\"新加坡\",\"initial\":\"X\",\"hotFlag\":\"N\"},\n" +
                        "{\"countryCode\":\"HU\",\"countryName\":\"匈牙利\",\"initial\":\"X\",\"hotFlag\":\"N\"},\n" +
                        "{\"countryCode\":\"ES\",\"countryName\":\"西班牙\",\"initial\":\"X\",\"hotFlag\":\"N\"},\n" +
                        "{\"countryCode\":\"SY\",\"countryName\":\"叙利亚\",\"initial\":\"X\",\"hotFlag\":\"N\"},\n" +
                        "{\"countryCode\":\"NZ\",\"countryName\":\"新西兰\",\"initial\":\"X\",\"hotFlag\":\"N\"},\n" +
                        "{\"countryCode\":\"GB\",\"countryName\":\"英国\",\"initial\":\"Y\",\"hotFlag\":\"Y\"},\n" +
                        "{\"countryCode\":\"YE\",\"countryName\":\"也门\",\"initial\":\"Y\",\"hotFlag\":\"N\"},\n" +
                        "{\"countryCode\":\"IR\",\"countryName\":\"伊朗\",\"initial\":\"Y\",\"hotFlag\":\"N\"},\n" +
                        "{\"countryCode\":\"VN\",\"countryName\":\"越南\",\"initial\":\"Y\",\"hotFlag\":\"N\"},\n" +
                        "{\"countryCode\":\"JO\",\"countryName\":\"约旦\",\"initial\":\"Y\",\"hotFlag\":\"N\"},\n" +
                        "{\"countryCode\":\"JM\",\"countryName\":\"牙买加\",\"initial\":\"Y\",\"hotFlag\":\"N\"},\n" +
                        "{\"countryCode\":\"IQ\",\"countryName\":\"伊拉克\",\"initial\":\"Y\",\"hotFlag\":\"N\"},\n" +
                        "{\"countryCode\":\"IN\",\"countryName\":\"印度\",\"initial\":\"Y\",\"hotFlag\":\"N\"},\n" +
                        "{\"countryCode\":\"AM\",\"countryName\":\"亚美尼亚\",\"initial\":\"Y\",\"hotFlag\":\"N\"},\n" +
                        "{\"countryCode\":\"IL\",\"countryName\":\"以色列\",\"initial\":\"Y\",\"hotFlag\":\"N\"},\n" +
                        "{\"countryCode\":\"IT\",\"countryName\":\"意大利\",\"initial\":\"Y\",\"hotFlag\":\"N\"},\n" +
                        "{\"countryCode\":\"ID\",\"countryName\":\"印度尼西亚\",\"initial\":\"Y\",\"hotFlag\":\"N\"},\n" +
                        "{\"countryCode\":\"CN\",\"countryName\":\"中国大陆\",\"initial\":\"Z\",\"hotFlag\":\"Y\"},\n" +
                        "{\"countryCode\":\"HK\",\"countryName\":\"中国香港\",\"initial\":\"Z\",\"hotFlag\":\"Y\"},\n" +
                        "{\"countryCode\":\"MO\",\"countryName\":\"中国澳门\",\"initial\":\"Z\",\"hotFlag\":\"Y\"},\n" +
                        "{\"countryCode\":\"TW\",\"countryName\":\"中国台湾\",\"initial\":\"Z\",\"hotFlag\":\"Y\"},\n" +
                        "{\"countryCode\":\"CL\",\"countryName\":\"智利\",\"initial\":\"Z\",\"hotFlag\":\"N\"},\n" +
                        "{\"countryCode\":\"CF\",\"countryName\":\"中非共和国\",\"initial\":\"Z\",\"hotFlag\":\"N\"},\n" +
                        "{\"countryCode\":\"GI\",\"countryName\":\"直布罗陀\",\"initial\":\"Z\",\"hotFlag\":\"N\"},\n" +
                        "{\"countryCode\":\"ZM\",\"countryName\":\"赞比亚\",\"initial\":\"Z\",\"hotFlag\":\"N\"},\n" +
                        "{\"countryCode\":\"ZR\",\"countryName\":\"扎伊尔\",\"initial\":\"Z\",\"hotFlag\":\"N\"},\n" +
                        "{\"countryCode\":\"TD\",\"countryName\":\"乍得\",\"initial\":\"Z\",\"hotFlag\":\"N\"}]";

        return new Gson().fromJson(json,
                new TypeToken<List<Nationality>>() {}.getType());
    }


    /**
     * @author I321533
     */
    public static <T> List<T> jsonToList(String json, Class<T[]> clazz) {
        Gson gson = new Gson();
        T[] array = gson.fromJson(json, clazz);
        return Arrays.asList(array);
    }


    /**
     * On touch image section
     *
     * @param sectionIndex sectionIndex
     * @param imageSection imageSection
     */
    @Override
    public void onTouchImageSection(int sectionIndex, EasyImageSection imageSection) {
        this.mSectionFloatingTv.setVisibility(View.INVISIBLE);
        this.mSectionFloatingTv.setVisibility(View.VISIBLE);
        switch (imageSection.imageType) {
            case EasyImageSection.CIRCLE:
                this.mSectionFloatingIv.setImageType(
                        EasyFloatingImageView.CIRCLE);
                break;
            case EasyImageSection.ROUND:
                this.mSectionFloatingIv.setImageType(
                        EasyFloatingImageView.ROUND);
                break;
        }
        GlideUtils.displayNative(this.mSectionFloatingIv, imageSection.resId);
        this.scrollToPosition(
                this.mNationalityAdapter.getPositionForSection(sectionIndex));
    }


    /**
     * On touch letter section
     *
     * @param sectionIndex sectionIndex
     * @param letterSection letterSection
     */
    @Override
    public void onTouchLetterSection(int sectionIndex, EasySection letterSection) {
        this.mSectionFloatingTv.setVisibility(View.VISIBLE);
        this.mSectionFloatingIv.setVisibility(View.INVISIBLE);
        this.mSectionFloatingTv.setText(letterSection.letter);
        this.scrollToPosition(
                this.mNationalityAdapter.getPositionForSection(sectionIndex));
    }


    private void scrollToPosition(int position) {
        this.mSectionRv.getLinearLayoutManager()
                       .scrollToPositionWithOffset(position, 0);
    }
}

