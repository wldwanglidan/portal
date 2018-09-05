package com.tempus.portal.view.adapter;

import android.widget.ImageView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tempus.portal.R;
import com.tempus.portal.base.utils.glide.GlideUtils;
import com.tempus.portal.model.Collection;
import com.tempus.portal.view.widget.edit.EditLayout;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/9/5.
 */

public class MyCollectionAdapter
        extends BaseQuickAdapter<Collection, BaseViewHolder> {
    private boolean isEdit;  //是否处于编辑状态
    private List<EditLayout> allItems = new ArrayList<>();
    private EditLayout mRightOpenItem;  //向右展开的删除项，只会存在一项


    public MyCollectionAdapter() {
        super(R.layout.item_my_collection, null);
    }


    @Override
    protected void convert(BaseViewHolder helper, Collection collection) {
        helper.setText(R.id.tvTitle, collection.title)
              .setText(R.id.tvUrl, collection.url)
              .setText(R.id.tvDate, collection.date).addOnClickListener(R.id
                .llCollection);
        final EditLayout editLayout = helper.getView(R.id.editLayout);
        // collection.logo图片地址。
        GlideUtils.displayUrl(helper.getView(R.id.ivLogo), collection.logo,R
                .drawable.bg_iv_default_tour);
        if (!allItems.contains(editLayout)) {
            allItems.add(editLayout);
        }
        editLayout.setEdit(isEdit);
        helper.getView(R.id.ibtSelect).setOnClickListener(view -> {
            if (collection.isSelect) {
                ((ImageView) helper.getView(R.id.ibtSelect)).setImageResource
                        (R.drawable.cb_cancel_collection_f);
                collection.isSelect = false;
                getData().set(helper.getAdapterPosition(), collection);
                notifyDataSetChanged();
            }
            else {
                ((ImageView) helper.getView(R.id.ibtSelect)).setImageResource
                        (R.drawable.cb_cancel_collection_t);
                collection.isSelect = true;
                getData().set(helper.getAdapterPosition(), collection);
                notifyDataSetChanged();
            }
        });
        if (collection.isSelect) {
            ((ImageView) helper.getView(R.id.ibtSelect)).setImageResource
                    (R.drawable.cb_cancel_collection_t);
        }
        else {
            ((ImageView) helper.getView(R.id.ibtSelect)).setImageResource
                    (R.drawable.cb_cancel_collection_f);
        }
    }


    /**
     * 关闭所有 item
     */
    private void closeAll() {
        for (EditLayout editLayout : allItems) {
            editLayout.close();
        }
    }


    /**
     * 设置编辑状态
     *
     * @param isEdit 是否为编辑状态
     */
    public void setEdit(boolean isEdit) {
        this.isEdit = isEdit;
        if (isEdit) {
            openLeftAll();
        }
        else {
            closeAll();
        }
        for (EditLayout editLayout : allItems) {
            editLayout.setEdit(isEdit);
        }
    }


    /**
     * 将所有 item 向左展开
     */
    private void openLeftAll() {
        for (EditLayout editLayout : allItems) {
            editLayout.openLeft();
        }
    }


    /**
     * 获取编辑状态
     *
     * @return 是否为编辑状态
     */
    public boolean isEdit() {
        return isEdit;
    }


    /**
     * 获取向右展开的 item
     *
     * @return 向右展开的 item
     */
    public EditLayout getRightOpenItem() {
        return mRightOpenItem;
    }
}

