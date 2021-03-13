package pers.jyb.K_on;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

public class AddListsAdapter extends BaseQuickAdapter<MusicList, BaseViewHolder> {   //参数一：提高数据的Bean类；参数二：BaseViewHolder
    AddListsAdapter(int layoutResId, List data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder viewHolder, MusicList item) {
        viewHolder.setText(R.id.add_name_text_view, item.getName());
        viewHolder.setText(R.id.add_number_text_view,item.getNumString());
    }

}
