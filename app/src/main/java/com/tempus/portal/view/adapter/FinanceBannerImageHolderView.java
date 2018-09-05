package com.tempus.portal.view.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import com.bigkoo.convenientbanner.holder.Holder;
import com.tempus.portal.R;
import com.tempus.portal.base.utils.glide.GlideUtils;
import com.tempus.portal.model.Finance;

/**
 * Created by Administrator on 2016/11/15.
 */

public class FinanceBannerImageHolderView
        implements Holder<Finance.TpurseProductListBean> {
    private ImageView imageView;


    @Override public View createView(Context context) {
        //你可以通过layout文件来创建，也可以像我一样用代码创建，不一定是Image，任何控件都可以进行翻页
        imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        return imageView;
    }


    @Override
    public void UpdateUI(Context context, int position, Finance.TpurseProductListBean tpurseProductListBean) {
       GlideUtils.displayUrl(imageView,tpurseProductListBean.productPicture, R
               .drawable.bg_iv_default_banner);
    }
}
