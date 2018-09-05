package com.tempus.portal.view.adapter;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tempus.portal.R;
import com.tempus.portal.base.utils.TextCheckUtils;
import com.tempus.portal.model.information.PassengerInformation;

import static com.tempus.portal.R.id.tvName;

/**
 * Created by Administrator on 2017/1/3.
 */

public class PassengerInfAdapter
        extends BaseQuickAdapter<PassengerInformation, BaseViewHolder> {

    public PassengerInfAdapter() {
        super(R.layout.item_com_passenger, null);
    }


    @Override
    protected void convert(BaseViewHolder helper, PassengerInformation item) {
        //setText(R.id.tvEnglishName, TextCheckUtils.exChangeUpper(item
        //        .surname) + " " + TextCheckUtils.exChangeUpper(item.enName))
        helper.setText(tvName, item.name)
              .setText(R.id.tvEnglishName,
                      item.surname.toUpperCase() + " " +
                              item.enName.toUpperCase())
              .addOnClickListener(R.id.ibDelete)
              .addOnClickListener(R.id.ibEdit);
        //根据数据生成几个item
        if (item.certInfos != null) {
            //这里要判断是否为空，再removeAllViews，不然会消耗性能
            ((LinearLayout) helper.getView(R.id.llData)).removeAllViews();
            for (int i = 0; i < item.certInfos.size(); i++) {
                PassengerInformation.CertInfosBean certInfosBean
                        = item.certInfos.get(i);
                View view = LayoutInflater.from(mContext)
                                          .inflate(R.layout.item_certinfo_tab,
                                                  (ViewGroup) helper.itemView,
                                                  false);

                TextView tvType = view.findViewById(R.id.tvCertificateType);
                TextView tvNum = view.findViewById(R.id.tvCertificateNum);
                tvType.setText(getCertType(certInfosBean) + "");
                tvNum.setText(certInfosBean.certNo);
                ((LinearLayout) helper.getView(R.id.llData)).addView(view);
            }
        }
    }


    /**
     * 得到护照类型
     */
    @NonNull
    private String getCertType(PassengerInformation.CertInfosBean certInfosBean) {
        if (certInfosBean.certType.equals("NI")) {
            return "身份证";
        }
        else if (certInfosBean.certType.equals("P")) {
            return "护照";
        }
        else {
            return "其他";
        }
    }
}
