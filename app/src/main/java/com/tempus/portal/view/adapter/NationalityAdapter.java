package com.tempus.portal.view.adapter;

import android.text.TextUtils;
import android.util.SparseIntArray;
import android.view.View;
import android.widget.TextView;
import com.camnter.easyrecyclerview.adapter.EasyRecyclerViewAdapter;
import com.camnter.easyrecyclerview.holder.EasyRecyclerViewHolder;
import com.tempus.portal.R;
import com.tempus.portal.model.Nationality;
import com.tempus.portal.view.widget.easyrecyclerviewsidebar.helper.EasyRecyclerSectionIndexer;
import com.tempus.portal.view.widget.easyrecyclerviewsidebar.sections.EasyImageSection;
import com.tempus.portal.view.widget.easyrecyclerviewsidebar.sections.EasySection;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/4/21.
 */

public class NationalityAdapter extends EasyRecyclerViewAdapter
        implements EasyRecyclerSectionIndexer<EasySection> {

    private SparseIntArray positionOfSection;
    private SparseIntArray sectionOfPosition;
    private List<EasySection> easySections;


    /**
     * Please return RecyclerView loading layout Id array
     * 请返回RecyclerView加载的布局Id数组
     *
     * @return 布局Id数组
     */
    @Override public int[] getItemLayouts() {
        return new int[] { R.layout.item_nationality };
    }


    /**
     * butt joint the onBindViewHolder and
     * If you want to write logic in onBindViewHolder, you can write here
     * 对接了onBindViewHolder
     * onBindViewHolder里的逻辑写在这
     *
     * @param viewHolder viewHolder
     * @param position position
     */
    @Override
    public void onBindRecycleViewHolder(EasyRecyclerViewHolder viewHolder, int position) {
        Nationality nationality = this.getItem(position);
        if (nationality == null) return;
        TextView headerTv = viewHolder.findViewById(R.id.section_header_tv);
        //ImageView sectionIv = viewHolder.findViewById(R.id.section_iv);
        TextView nameTv = viewHolder.findViewById(R.id.section_name_tv);

        if (!TextUtils.isEmpty(nationality.name)) {
            nameTv.setText(nationality.name);
        } else {
            nameTv.setText("");
        }
        //if (contacts.resId != 0) {
        //    GlideUtils.displayNative(sectionIv, contacts.resId);
        //} else {
        //    GlideUtils.displayNative(sectionIv, R.drawable.img_default_head);
        //}

        this.setHeaderLogic(nationality, headerTv, viewHolder, position);
    }


    /**
     * Set header logic
     *
     * @param nationality contacts
     * @param headerTv headerTv
     * @param viewHolder viewHolder
     * @param position position
     */
    public void setHeaderLogic(Nationality nationality , TextView headerTv, EasyRecyclerViewHolder viewHolder, int position) {
        // No implement
        if (position == 0) {
            this.setHeader(true, headerTv, nationality.getHeader());
        } else {
            Nationality pre = this.getItem(position - 1);
            if (!nationality.getHeader().equals(pre.getHeader())) {
                this.setHeader(true, headerTv, nationality.getHeader());
            } else {
                this.setHeader(false, headerTv, null);
            }
        }
    }


    /**
     * Please write judgment logic when more layout
     * and not write when single layout
     * 如果是多布局的话，请写判断逻辑
     * 单布局可以不写
     *
     * @param position Item position
     * @return 布局Id数组中的index
     */
    @Override public int getRecycleViewItemType(int position) {
        return 0;
    }


    public void setHeader(boolean visible, TextView headerTv, String header) {
        if (visible) {
            headerTv.setText(header);
            headerTv.setVisibility(View.VISIBLE);
        } else {
            headerTv.setVisibility(View.GONE);
        }
    }


    @Override public List<EasySection> getSections() {
        this.resetSectionCache();

        int itemCount = getItemCount();
        if (itemCount < 1) return this.easySections;

        String letter;

        for (int i = 0; i < itemCount; i++) {
            Nationality nationality  = this.getItem(i);
            letter = nationality.getHeader();
            int section = this.easySections.size() == 0 ? 0 : this.easySections.size() - 1;
            if (nationality.top) {
                if (i != 0) section++;
                this.positionOfSection.put(section, i);
                this.easySections.add(
                        new EasyImageSection(nationality.resId, this.getEasyImageSection(), i));
            } else {
                // A B C D E F ...
                if (section < this.easySections.size()) {
                    EasySection easySection = this.easySections.get(section);
                    if (easySection instanceof EasyImageSection) {
                        // last section = image section
                        this.easySections.add(new EasySection(letter));
                        section++;
                        this.positionOfSection.put(section, i);
                    } else {
                        // last section = letter section
                        if (!this.easySections.get(section).letter.equals(letter)) {
                            this.easySections.add(new EasySection(letter));
                            section++;
                            this.positionOfSection.put(section, i);
                        }
                    }
                } else if (section == 0) {
                    this.easySections.add(new EasySection(letter));
                    this.positionOfSection.put(section, i);
                }
            }
            this.sectionOfPosition.put(i, section);
        }
        return this.easySections;
    }


    private void resetSectionCache() {
        if (this.easySections == null) this.easySections = new ArrayList<>();
        if (this.easySections.size() > 0) this.easySections.clear();
        if (sectionOfPosition == null) this.sectionOfPosition = new SparseIntArray();
        if (this.sectionOfPosition.size() > 0) this.sectionOfPosition.clear();
        if (this.positionOfSection == null) this.positionOfSection = new SparseIntArray();
        if (this.positionOfSection.size() > 0) this.positionOfSection.clear();
    }


    public @EasyImageSection.ImageType int getEasyImageSection() {
        return EasyImageSection.CIRCLE;
    }


    @Override public int getPositionForSection(int sectionIndex) {
        return positionOfSection.get(sectionIndex);
    }


    @Override public int getSectionForPosition(int position) {
        return sectionOfPosition.get(position);
    }
}
