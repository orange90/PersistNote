package com.dv.persistnote.business;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dv.persistnote.R;
import com.dv.persistnote.framework.ActionId;
import com.dv.persistnote.framework.ui.AbstractScreen;
import com.dv.persistnote.framework.FontManager;
import com.dv.persistnote.framework.ui.UICallBacks;


/**
 * Created by Hang on 2016/3/14.
 */
public class WelcomeScreen extends AbstractScreen implements View.OnClickListener{

    private TextView mCenterText;

    private TextView mRegisterButton;

    private TextView mLoginButton;

    public WelcomeScreen(Context context, UICallBacks callBacks) {
        super(context, callBacks);
        init();
    }

    private void init() {

        mCenterText = new TextView(getContext());
        Drawable drawable = getResources().getDrawable(R.drawable.main_logo);
        int edge = (int)getResources().getDimension(R.dimen.welcome_icon_width);
        drawable.setBounds(0, 0, edge, edge);
        mCenterText.setCompoundDrawables(null, drawable, null, null);
        mCenterText.setCompoundDrawablePadding(edge / 3);
        mCenterText.setText(getResources().getString(R.string.slogen));
        mCenterText.setTextColor(getResources().getColor(R.color.c2));
        mCenterText.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.h4));
        mCenterText.setTypeface(FontManager.getInstance().getDefaultTypeface());

        LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        lp.addRule(CENTER_HORIZONTAL);
        lp.topMargin = (int) getResources().getDimension(R.dimen.welcome_icon_top_margin);
        addView(mCenterText, lp);


        LinearLayout container = new LinearLayout(getContext());

        mRegisterButton = createActionButton("注册");
        mLoginButton = createActionButton("登录");

        container.addView(mRegisterButton);
        container.addView(mLoginButton);


        lp = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
        lp.addRule(CENTER_HORIZONTAL);
        lp.addRule(ALIGN_PARENT_BOTTOM);
        lp.bottomMargin = (int) getResources().getDimension(R.dimen.welcome_icon_top_margin);
        lp.leftMargin = lp.rightMargin = (int) getResources().getDimension(R.dimen.action_button_margin);
        addView(container, lp);
    }


    private TextView createActionButton(String text) {
        TextView textView = new TextView(getContext());
        textView.setText(text);
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.common_margin_top_22));
        textView.setTextColor(getResources().getColor(R.color.c1));
        textView.setTypeface(FontManager.getInstance().getDefaultTypeface());

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        lp.weight = 1;
        textView.setLayoutParams(lp);
        textView.setOnClickListener(this);

        return textView;
    }

    @Override
    public void onClick(View v) {
        if(v == mRegisterButton) {
            mCallBacks.handleAction(ActionId.OnRegisterClick, null, null);
        } if (v == mLoginButton) {
            mCallBacks.handleAction(ActionId.OnLoginClick, null, null);
        }
    }
}
