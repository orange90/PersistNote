package com.dv.persistnote.business;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import com.dv.persistnote.FakeDataHelper;
import com.dv.persistnote.FakeDataHelper.HabitInfo;
import com.dv.persistnote.R;
import com.dv.persistnote.base.ResTools;
import com.dv.persistnote.framework.ActionId;
import com.dv.persistnote.framework.DefaultScreen;
import com.dv.persistnote.framework.ui.UICallBacks;

import java.util.List;

/**
 * Created by Hang on 2016/3/13.
*/
public class RootScreen extends DefaultScreen {

    private LinearLayout mContainer;

    public RootScreen(Context context, UICallBacks callBacks) {
        super(context, callBacks);
        init();
        setTitle(ResTools.getString(R.string.app_name));
    }

    protected void init() {
        super.init();
        mContainer = new LinearLayout(getContext());
        mContainer.setOrientation(LinearLayout.VERTICAL);
        setContent(mContainer);

        List<HabitInfo> habitInfos = FakeDataHelper.getMyHabitInfos();
        updateData(habitInfos);
    }

    private void updateData(List<HabitInfo> habitInfos) {
        mContainer.removeAllViews();


        for (HabitInfo info : habitInfos) {
            final HabitItemView itemView = new HabitItemView(getContext());
            LinearLayout.LayoutParams lp =
                    new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, ResTools.getDimenInt(R.dimen.habit_item_height));
            lp.topMargin = ResTools.getDimenInt(R.dimen.habit_item_margin_top);
            itemView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    mCallBacks.handleAction(ActionId.OnHabitItemClick, null, null);
                }
            });

            itemView.bindData(info);
            mContainer.addView(itemView, lp);
        }

    }

    public void setCheckInText(String checkInText) {
        //网络接口的测试返回结果
        setTitle(checkInText);
    }


}
