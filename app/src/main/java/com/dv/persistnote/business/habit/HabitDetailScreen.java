package com.dv.persistnote.business.habit;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Gravity;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dv.persistnote.R;
import com.dv.persistnote.base.ResTools;
import com.dv.persistnote.business.habit.calendar.CheckInCalendar;
import com.dv.persistnote.framework.ActionId;
import com.dv.persistnote.framework.DefaultScreen;
import com.dv.persistnote.framework.ui.IUIObserver;
import com.dv.persistnote.framework.ui.UICallBacks;
import com.dv.persistnote.framework.ui.common.materialcalendarview.CalendarDay;

import habit.dao.HabitRecord;

/**
 * Created by Hang on 2016/4/3.
 */
public class HabitDetailScreen extends DefaultScreen implements IUIObserver{

    private CheckInCalendar mCalendar;
    private TextView mPersistDuration;
    private CheckInWidget mCheckInWidget;
    private TextView mFooter;
    private long mHabitId;

    private ListView mDetailListView;
    private SwipeRefreshLayout mRefreshLayout;

    private CommunityDetailAdapter mAdapter;

    public HabitDetailScreen(Context context, UICallBacks callBacks, long habitId) {
        super(context, callBacks);
        mHabitId = habitId;
        init();
        setBackgroundColor(ResTools.getColor(R.color.default_grey));
    }

    protected void init() {
        mDetailListView = new ListView(getContext());
        mDetailListView.setDivider(null);
        mDetailListView.setCacheColorHint(Color.TRANSPARENT);
        mDetailListView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        mDetailListView.setOnScrollListener(createScrollListener());
        mAdapter = new CommunityDetailAdapter(this);

        mRefreshLayout = new SwipeRefreshLayout(getContext());
        mRefreshLayout.addView(mDetailListView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        mRefreshLayout.setColorSchemeColors(ResTools.getColor(R.color.c1));
//        mRefreshLayout.setEnabled(false);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mCallBacks.handleAction(ActionId.OnCommunityRefresh, null, null);
            }
        });

        configHeader();
        configAutoLoadMore();
        mDetailListView.setAdapter(mAdapter);
        setContent(mRefreshLayout);
    }

    private void configHeader() {
        mCalendar = new CheckInCalendar(getContext(), this);
        AbsListView.LayoutParams lp = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT,
                AbsListView.LayoutParams.WRAP_CONTENT);
        mCalendar.setLayoutParams(lp);
        mCalendar.setBackgroundColor(ResTools.getColor(R.color.c4));

        mCheckInWidget = new CheckInWidget(getContext());
        lp = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT,
                AbsListView.LayoutParams.WRAP_CONTENT);
        mCheckInWidget.setLayoutParams(lp);
        mCheckInWidget.setOnUIObserver(this);

        mPersistDuration = new TextView(getContext());
        mPersistDuration.setText("第28天");
        lp = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT,
                ResTools.getDimenInt(R.dimen.calendar_height) * 2);
        mPersistDuration.setLayoutParams(lp);
        mPersistDuration.setGravity(Gravity.CENTER);
        mPersistDuration.setBackgroundColor(ResTools.getColor(R.color.c4));

        TextView communityTitle = new TextView(getContext());
        communityTitle.setText(ResTools.getString(R.string.community_title));
        communityTitle.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
        int margin = ResTools.getDimenInt(R.dimen.common_margin_16);
        communityTitle.setPadding(ResTools.getDimenInt(R.dimen.common_margin_16), margin, 0, margin);
        communityTitle.setTextColor(ResTools.getColor(R.color.c2));

        mDetailListView.addHeaderView(mCalendar);
        mDetailListView.addHeaderView(mCheckInWidget);
        mDetailListView.addHeaderView(mPersistDuration);
        mDetailListView.addHeaderView(communityTitle);
    }

    private void configAutoLoadMore() {

        mFooter = new TextView(getContext());
        mFooter.setText("正在加载..");
        mFooter.setGravity(Gravity.CENTER);
        mFooter.setTextColor(ResTools.getColor(R.color.c3));
        AbsListView.LayoutParams lp = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT,
                ResTools.getDimenInt(R.dimen.calendar_height) * 2);
        mFooter.setLayoutParams(lp);
        mDetailListView.addFooterView(mFooter);

    }

    public void setHabitDataById(long habitId) {
        HabitRecord habit = HabitModel.getInstance().getHabitById(habitId);
        if(habit != null) {
            setTitle(habit.getHabitName());
            setPersistCounts(habit.getPersistCount());
        }
        mHabitId = habitId;
        updateCheckInWidget();
    }

    private void updateCheckInWidget() {
        boolean checkedToday = CheckInModel.getInstance().isDayCheckedIn(mHabitId, CalendarDay.today().getDate());
        mCheckInWidget.setChecked(checkedToday ,false);
    }

    private void setPersistCounts(int counts) {
        mPersistDuration.setText("第" + counts + "天");
    }

    @Override
    public void onScreenStateChanged(int screenState) {
        super.onScreenStateChanged(screenState);
        switch (screenState) {
            case STATE_ON_ATTACH:
                mDetailListView.setSelection(0);
                break;
        }
    }

    public void notifyCommunityDataChange(int type) {
        mAdapter.notifyDataSetChanged();
        if(type == CommunityModel.TYPE_NEW) {
            mRefreshLayout.setRefreshing(false);
        } else if (type == CommunityModel.TYPE_ERROR) {
            mRefreshLayout.setRefreshing(false);
            Toast.makeText(getContext(),"网络不给力", Toast.LENGTH_SHORT).show();
        }
    }

    public void onNoHistoryData() {
        mFooter.setText("sb 别拉了");
    }

    @Override
    public boolean handleAction(int actionId, Object arg, Object result) {
        boolean handle = true;
        switch (actionId) {
            case ActionId.OnPageScrollStateChanged:
                int state = (Integer)arg;
                mRefreshLayout.setEnabled(state == ViewPager.SCROLL_STATE_IDLE);
                break;
            case ActionId.OnCheckIn:
                mCallBacks.handleAction(actionId, mHabitId,result);
                break;
            case ActionId.GetHabitId:
                ((Message)result).obj = mHabitId;
                break;
            case ActionId.OnPagerClick:
                mCalendar.toggleState();
                break;
            default:
                handle = false;
        }
        if(!handle) {
            mCallBacks.handleAction(actionId, arg, result);
        }
        return handle;
    }

    public void notifyCheckInDataChange() {
        mCalendar.invalidateDecorators();
    }

    public AbsListView.OnScrollListener createScrollListener() {
        AbsListView.OnScrollListener scrollListener = new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

                //向后加载更多
                int lastVisibleItem = mDetailListView.getLastVisiblePosition();
                int totalItemCount = mAdapter.getCount();
                boolean lastItemVisible = totalItemCount > 0 && (lastVisibleItem >= totalItemCount - 1);
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && lastItemVisible) {
                    mCallBacks.handleAction(ActionId.OnCommunityLoadMore, null, null);
                }

                //向下收起月面板
                if (scrollState == SCROLL_STATE_TOUCH_SCROLL && mDetailListView.getFirstVisiblePosition() == 0) {
                    mCalendar.ensureCollapse();
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        };
        return scrollListener;
    }
}
