package com.dv.persistnote.business;

import android.content.Context;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.NumberKeyListener;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dv.persistnote.R;
import com.dv.persistnote.base.ResTools;
import com.dv.persistnote.framework.ActionId;
import com.dv.persistnote.framework.DefaultScreen;
import com.dv.persistnote.framework.ui.CircleView;
import com.dv.persistnote.framework.ui.UICallBacks;

import java.lang.reflect.Field;

import static com.dv.persistnote.base.ContextManager.getSystemService;

/**
 * Created by QinZheng on 2016/3/29.
 */
public class LoginScreen extends DefaultScreen implements View.OnClickListener {

    private RelativeLayout mContainer;

    private RelativeLayout mContainerPhoneNumber;

    private RelativeLayout mContainerPassword;

    private RelativeLayout mContainerOKButton;

    private RelativeLayout mContainerPhoneNumberRemoveEZTouch;

    private RelativeLayout mContainerPasswordRemoveEZTouch;

    private RelativeLayout mContainerPasswordHideEZTouch;

    private EditText mEtPhoneNumber;

    private EditText mEtPassword;

    private View mLinePhoneNumber;

    private View mLinePassword;

    private View mDividerPassword;

    private CircleView mOkButton;

    private CircleView mOkButtonClick;

    private ImageView mOkButtonArrow;

    private ImageView mRemovePhoneNumber;

    private ImageView mRemovePassword;

    private ImageView mHidePassword;

    private Boolean mIsHidden = true;

    private Boolean mIsOkButtonAvailable = false;

    private Boolean mIsRemovePhoneNumber = false;

    private Boolean mIsRemovePassword = false;

    private TextView mForgetPassword;

    private float mStartX, mNowX;

    private float mStartY, mNowY;

    private InputMethodManager mImm;

    public LoginScreen(Context context, UICallBacks callBacks) {
        super(context, callBacks);
        init();
        setTitle(ResTools.getString(R.string.login));

    }

    private void init() {
        mContainer = new RelativeLayout(getContext());
        setContent(mContainer);

        mContainer.removeAllViews();
        mContainer.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                // 失去焦点，隐藏键盘
                if(mEtPhoneNumber.hasFocus()) {
                    mEtPhoneNumber.clearFocus();
                    mImm.hideSoftInputFromWindow(mEtPhoneNumber.getWindowToken(), 0);
                }
                if(mEtPassword.hasFocus()) {
                    mEtPassword.clearFocus();
                    mImm.hideSoftInputFromWindow(mEtPassword.getWindowToken(), 0);
                }
                return false;
            }
        });

        // 获取InputMethodManager
        mImm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        /************** mContainerPhoneNumber ******************/

        mContainerPhoneNumber = new RelativeLayout(getContext());
        mContainerPhoneNumber.setId(R.id.login_rl_phone_number);
        mContainerPhoneNumber.setFocusable(true);
        mContainerPhoneNumber.setFocusableInTouchMode(true);

        RelativeLayout.LayoutParams lpC1 = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, ResTools.getDimenInt(R.dimen.common_rl_height));
        lpC1.topMargin = ResTools.getDimenInt(R.dimen.login_rl_phone_number_margin_top);
        lpC1.leftMargin = ResTools.getDimenInt(R.dimen.common_rl_margin_left);
        lpC1.rightMargin = ResTools.getDimenInt(R.dimen.common_rl_margin_right);

        mEtPhoneNumber = new EditText(getContext());
        mEtPhoneNumber.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.h1));
        mEtPhoneNumber.setPadding(0, ResTools.getDimenInt(R.dimen.common_et_padding_top),
                0, ResTools.getDimenInt(R.dimen.common_et_padding_bottom));
        mEtPhoneNumber.setId(R.id.login_et_phone_number);
        mEtPhoneNumber.setBackgroundColor(ResTools.getColor(R.color.c4));
        mEtPhoneNumber.setHint(R.string.common_et_hint_phone_number);
        mEtPhoneNumber.setSingleLine(true);
        mEtPhoneNumber.setInputType(InputType.TYPE_CLASS_PHONE);
        mEtPhoneNumber.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    mEtPhoneNumber.setHint(R.string.common_et_hint_phone_number);
                    mRemovePhoneNumber.setVisibility(View.GONE);
                    mIsRemovePhoneNumber = false;
                } else {
                    mEtPhoneNumber.setHint(null);
                    if (mEtPhoneNumber.getText().toString().length() > 0)
                        mRemovePhoneNumber.setVisibility(View.VISIBLE);
                        mIsRemovePhoneNumber = true;
                }
            }
        });
        try {
            // https://github.com/android/platform_frameworks_base/blob/kitkat-release/core/java/android/widget/TextView.java#L562-564
            Field f = TextView.class.getDeclaredField("mCursorDrawableRes");
            f.setAccessible(true);
            f.set(mEtPhoneNumber, R.drawable.color_cursor);
        } catch (Exception ignored) {
            Log.e(TAG, "Login Init : color_cursor Error");
        }

        mEtPhoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() == 11 && mEtPassword.getText().toString().length() > 0) {
                    mIsOkButtonAvailable = true;
                    mOkButton.setAlpha(1.0f);
                } else {
                    mIsOkButtonAvailable = false;
                    mOkButton.setAlpha(0.3f);
                }
                if (s.toString().length() > 0) {
                    mRemovePhoneNumber.setVisibility(View.VISIBLE);
                    mIsRemovePhoneNumber = true;
                } else {
                    mRemovePhoneNumber.setVisibility(View.GONE);
                    mIsRemovePhoneNumber = false;
                }
            }
        });

        mEtPhoneNumber.setFilters(new InputFilter[]{new InputFilter.LengthFilter(11)});

        RelativeLayout.LayoutParams lpC1V1 =
                new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        lpC1V1.rightMargin = ResTools.getDimenInt(R.dimen.common_rl_remove_width);

        mContainerPhoneNumber.addView(mEtPhoneNumber, lpC1V1);

        mLinePhoneNumber = new View(getContext());
        mLinePhoneNumber.setId(R.id.login_line_phone_number);
        mLinePhoneNumber.setBackgroundColor(ResTools.getColor(R.color.c1));

        RelativeLayout.LayoutParams lpC1V2 =
                new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, ResTools.getDimenInt(R.dimen.common_line_height));
        lpC1V2.addRule(RelativeLayout.BELOW, R.id.login_et_phone_number);

        mContainerPhoneNumber.addView(mLinePhoneNumber, lpC1V2);

        /** EZ Touch start **/

        mContainerPhoneNumberRemoveEZTouch = new RelativeLayout(getContext());
        mContainerPhoneNumberRemoveEZTouch.setId(R.id.login_rl_phone_number_remove);
        mContainerPhoneNumberRemoveEZTouch.setOnClickListener(this);

        RelativeLayout.LayoutParams lpC1C1 = new RelativeLayout.LayoutParams(ResTools.getDimenInt(R.dimen.common_rl_remove_width),
                LayoutParams.MATCH_PARENT);
        lpC1C1.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

        mRemovePhoneNumber = new ImageView(getContext());
        mRemovePhoneNumber.setId(R.id.login_iv_password_remove);
        mRemovePhoneNumber.setImageDrawable(ResTools.getDrawable(R.drawable.delete));
        mRemovePhoneNumber.setVisibility(View.GONE);

        RelativeLayout.LayoutParams lpC1C1V1 = new RelativeLayout.LayoutParams(ResTools.getDimenInt(R.dimen.h2), ResTools.getDimenInt(R.dimen.h2));
        lpC1C1V1.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        lpC1C1V1.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        lpC1C1V1.bottomMargin = ResTools.getDimenInt(R.dimen.common_margin_bottom_12);

        mContainerPhoneNumberRemoveEZTouch.addView(mRemovePhoneNumber, lpC1C1V1);

        mContainerPhoneNumber.addView(mContainerPhoneNumberRemoveEZTouch, lpC1C1);

        /** EZ Touch end **/

        mContainer.addView(mContainerPhoneNumber, lpC1);

        /************** mContainerPassword ******************/

        mContainerPassword = new RelativeLayout(getContext());
        mContainerPassword.setId(R.id.login_rl_password);
        mContainerPassword.setFocusable(true);
        mContainerPassword.setFocusableInTouchMode(true);

        RelativeLayout.LayoutParams lpC2 = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,
                ResTools.getDimenInt(R.dimen.common_rl_height));
        lpC2.topMargin = ResTools.getDimenInt(R.dimen.login_rl_password_margin_top);
        lpC2.leftMargin = ResTools.getDimenInt(R.dimen.common_rl_margin_left);
        lpC2.rightMargin = ResTools.getDimenInt(R.dimen.common_rl_margin_right);
        lpC2.addRule(RelativeLayout.BELOW, R.id.login_rl_phone_number);

        mEtPassword = new EditText(getContext());
        mEtPassword.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.h1));
        mEtPassword.setPadding(0, ResTools.getDimenInt(R.dimen.common_et_padding_top),
                0, ResTools.getDimenInt(R.dimen.common_et_padding_bottom));
        mEtPassword.setId(R.id.login_et_password);
        mEtPassword.setBackgroundColor(ResTools.getColor(R.color.c4));
        mEtPassword.setKeyListener(new NumberKeyListener() {
            @Override
            protected char[] getAcceptedChars() {
                char[] chars = new char[62];
                int j = 0;
                for(char i = 'a'; i <= 'z';j++,i++) {
                    chars[j] = i;
                }
                for(char i = 'A'; i <= 'Z'; j++,i++) {
                    chars[j] = i;
                }
                for(char i = '0'; i <= '9'; j++,i++) {
                    chars[j] = i;
                }
                return chars;
            }

            @Override
            public int getInputType() {
                return InputType.TYPE_TEXT_VARIATION_PASSWORD;
            }
        });
        mEtPassword.setHint(R.string.common_et_hint_password);
        mEtPassword.setSingleLine(true);
        mEtPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
        mEtPassword.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    mEtPassword.setHint(R.string.common_et_hint_password);
                    mDividerPassword.setVisibility(View.INVISIBLE);
                    mRemovePassword.setVisibility(View.GONE);
                    mIsRemovePassword = false;
                } else {
                    mEtPassword.setHint(null);
                    if (mEtPassword.getText().toString().length() > 0) {
                        mDividerPassword.setVisibility(View.VISIBLE);
                        mRemovePassword.setVisibility(View.VISIBLE);
                        mIsRemovePassword = true;
                    }
                }
            }
        });
        try {
            // https://github.com/android/platform_frameworks_base/blob/kitkat-release/core/java/android/widget/TextView.java#L562-564
            Field f = TextView.class.getDeclaredField("mCursorDrawableRes");
            f.setAccessible(true);
            f.set(mEtPassword, R.drawable.color_cursor);
        } catch (Exception ignored) {
            Log.e(TAG, "Login Init : color_cursor Error");
        }

        mEtPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() > 0 && mEtPhoneNumber.getText().toString().length() == 11) {
                    mIsOkButtonAvailable = true;
                    mOkButton.setAlpha(1.0f);
                } else {
                    mIsOkButtonAvailable = false;
                    mOkButton.setAlpha(0.3f);
                }
                if (s.toString().length() > 0) {
                    mDividerPassword.setVisibility(View.VISIBLE);
                    mRemovePassword.setVisibility(View.VISIBLE);
                    mIsRemovePassword = true;
                } else {
                    mDividerPassword.setVisibility(View.INVISIBLE);
                    mRemovePassword.setVisibility(View.GONE);
                    mIsRemovePassword = false;
                }
            }
        });

        RelativeLayout.LayoutParams lpC2V1 =
                new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        lpC2V1.rightMargin = ResTools.getDimenInt(R.dimen.common_et_remove_hide_margin_right);

        mContainerPassword.addView(mEtPassword, lpC2V1);

        mLinePassword = new View(getContext());
        mLinePassword.setId(R.id.login_line_password);
        mLinePassword.setBackgroundColor(ResTools.getColor(R.color.c1));

        RelativeLayout.LayoutParams lpC2V2 = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,
                ResTools.getDimenInt(R.dimen.common_line_height));
        lpC2V2.addRule(RelativeLayout.BELOW, R.id.login_et_password);

        mContainerPassword.addView(mLinePassword, lpC2V2);

        /** EZ Touch start **/

        mContainerPasswordHideEZTouch = new RelativeLayout(getContext());
        mContainerPasswordHideEZTouch.setId(R.id.login_rl_password_hide);
        mContainerPasswordHideEZTouch.setOnClickListener(this);

        RelativeLayout.LayoutParams lpC2C1 = new RelativeLayout.LayoutParams(ResTools.getDimenInt(R.dimen.common_rl_remove_width),
                LayoutParams.MATCH_PARENT);
        lpC2C1.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

        mContainerPassword.addView(mContainerPasswordHideEZTouch, lpC2C1);

        mHidePassword = new ImageView(getContext());
        mHidePassword.setId(R.id.login_iv_password_hide);
        mHidePassword.setImageDrawable(ResTools.getDrawable(R.drawable.eyes_close));

        RelativeLayout.LayoutParams lpC2C1V1 =
                new RelativeLayout.LayoutParams(ResTools.getDimenInt(R.dimen.h1), ResTools.getDimenInt(R.dimen.h1));
        lpC2C1V1.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        lpC2C1V1.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        lpC2C1V1.bottomMargin = ResTools.getDimenInt(R.dimen.common_margin_bottom_11);

        mContainerPasswordHideEZTouch.addView(mHidePassword, lpC2C1V1);

        mDividerPassword = new View(getContext());
        mDividerPassword.setId(R.id.login_divider_password);
        mDividerPassword.setBackgroundColor(ResTools.getColor(R.color.c7));
        mDividerPassword.setVisibility(View.INVISIBLE);

        RelativeLayout.LayoutParams lpC2V3 = new RelativeLayout.LayoutParams(ResTools.getDimenInt(R.dimen.common_line_height),
                ResTools.getDimenInt(R.dimen.common_divider_height));
        lpC2V3.addRule(RelativeLayout.LEFT_OF, R.id.login_rl_password_hide);
        lpC2V3.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        lpC2V3.bottomMargin = ResTools.getDimenInt(R.dimen.common_margin_bottom_11);

        mContainerPassword.addView(mDividerPassword, lpC2V3);

        mContainerPasswordRemoveEZTouch = new RelativeLayout(getContext());
        mContainerPasswordRemoveEZTouch.setId(R.id.login_rl_password_remove);
        mContainerPasswordRemoveEZTouch.setOnClickListener(this);

        RelativeLayout.LayoutParams lpC2C2 = new RelativeLayout.LayoutParams(ResTools.getDimenInt(R.dimen.common_rl_remove_hide_width),
                LayoutParams.MATCH_PARENT);
        lpC2C2.addRule(RelativeLayout.LEFT_OF, R.id.login_divider_password);

        mContainerPassword.addView(mContainerPasswordRemoveEZTouch, lpC2C2);

        mRemovePassword = new ImageView(getContext());
        mRemovePassword.setId(R.id.login_iv_password_remove);
        mRemovePassword.setImageDrawable(ResTools.getDrawable(R.drawable.delete));
        mRemovePassword.setVisibility(View.GONE);

        RelativeLayout.LayoutParams lpC2C2V1 = new RelativeLayout.LayoutParams(ResTools.getDimenInt(R.dimen.h2), ResTools.getDimenInt(R.dimen.h2));
        lpC2C2V1.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        lpC2C2V1.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        lpC2C2V1.bottomMargin = ResTools.getDimenInt(R.dimen.common_margin_bottom_12);
        lpC2C2V1.rightMargin = ResTools.getDimenInt(R.dimen.h2);

        mContainerPasswordRemoveEZTouch.addView(mRemovePassword, lpC2C2V1);

        /** EZ Touch end **/

        mContainer.addView(mContainerPassword, lpC2);

        /************** mContainerOKButton ******************/

        mOkButtonClick = new CircleView(getContext(), ResTools.getDimenInt(R.dimen.common_cv_radius),
                ResTools.getDimenInt(R.dimen.common_cv_radius), ResTools.getDimenInt(R.dimen.common_cv_radius));
        mOkButtonClick.setColor(ResTools.getColor(R.color.c9));
        mOkButtonClick.setVisibility(View.GONE);

        mContainerOKButton = new RelativeLayout(getContext());
        mContainerOKButton.setId(R.id.login_rl_ok);
        mContainerOKButton.setOnClickListener(this);
        mContainerOKButton.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                // 失去焦点，隐藏键盘
                if(mEtPhoneNumber.hasFocus()) {
                    mEtPhoneNumber.clearFocus();
                    mImm.hideSoftInputFromWindow(mEtPhoneNumber.getWindowToken(), 0);
                }
                if(mEtPassword.hasFocus()) {
                    mEtPassword.clearFocus();
                    mImm.hideSoftInputFromWindow(mEtPassword.getWindowToken(), 0);
                }
                if(mIsOkButtonAvailable) {
                    mNowX = motionEvent.getX();
                    mNowY = motionEvent.getY();
                    switch (motionEvent.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            mOkButtonClick.setVisibility(View.VISIBLE);
                            mStartX = motionEvent.getX();
                            mStartY = motionEvent.getY();
                            break;
                        case MotionEvent.ACTION_MOVE:
                            break;
                        case MotionEvent.ACTION_UP:
                            mOkButtonClick.setVisibility(View.GONE);
                            if (Math.abs(mNowX - mStartX) < 3.0 && Math.abs(mNowY - mStartY) < 3.0) {
                                mCallBacks.handleAction(ActionId.CommitLoginClick, null, null);
                            }
                            break;
                    }
                }
                return false;
            }
        });

        RelativeLayout.LayoutParams lpC3 = new RelativeLayout.LayoutParams(ResTools.getDimenInt(R.dimen.common_rl_ok_width_height),
                ResTools.getDimenInt(R.dimen.common_rl_ok_width_height));
        lpC3.topMargin = ResTools.getDimenInt(R.dimen.common_rl_ok_margin_top);
        lpC3.addRule(RelativeLayout.BELOW, R.id.login_rl_password);
        lpC3.addRule(RelativeLayout.CENTER_HORIZONTAL);

        mOkButton = new CircleView(getContext(), ResTools.getDimenInt(R.dimen.common_cv_radius),
                ResTools.getDimenInt(R.dimen.common_cv_radius), ResTools.getDimenInt(R.dimen.common_cv_radius));
        mOkButton.setId(R.id.login_v_ok);
        mOkButton.setColor(ResTools.getColor(R.color.c1));
        mOkButton.setAlpha(0.3f);

        mContainerOKButton.addView(mOkButton);
        mContainerOKButton.addView(mOkButtonClick);

        mOkButtonArrow = new ImageView(getContext());
        mOkButtonArrow.setId(R.id.login_iv_ok);
        mOkButtonArrow.setImageDrawable(ResTools.getDrawable(R.drawable.arrow_right));

        RelativeLayout.LayoutParams lpC3V1 = new RelativeLayout.LayoutParams(ResTools.getDimenInt(R.dimen.h1), ResTools.getDimenInt(R.dimen.h1));
        lpC3V1.addRule(RelativeLayout.CENTER_IN_PARENT);

        mContainerOKButton.addView(mOkButtonArrow, lpC3V1);

        mContainer.addView(mContainerOKButton, lpC3);


        /************** mForgetPassword ******************/

        mForgetPassword = new TextView(getContext());
        mForgetPassword.setId(R.id.login_tv_forget_password);
        mForgetPassword.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.h1));
        mForgetPassword.setText(ResTools.getString(R.string.login_tv_forget_password));
        mForgetPassword.setTextColor(ResTools.getColor(R.color.c3));
        mForgetPassword.setOnClickListener(this);

        RelativeLayout.LayoutParams lpV1 = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        lpV1.addRule(RelativeLayout.BELOW, R.id.login_rl_password);
        lpV1.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        lpV1.topMargin = ResTools.getDimenInt(R.dimen.common_tv_margin_top);
        lpV1.rightMargin = ResTools.getDimenInt(R.dimen.common_rl_margin_right);

        mContainer.addView(mForgetPassword, lpV1);

    }

    @Override
    public void onClick(View v) {
        if(v == mContainerPasswordHideEZTouch) {
            if (mIsHidden) {
                mEtPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                mHidePassword.setImageDrawable(ResTools.getDrawable(R.drawable.eyes_open));
                mIsHidden = false;
            } else {
                mEtPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                mHidePassword.setImageDrawable(ResTools.getDrawable(R.drawable.eyes_close));
                mIsHidden = true;
            }
            mEtPassword.postInvalidate();
            CharSequence charSequence = mEtPassword.getText();
            if (charSequence instanceof Spannable) {
                Spannable spanText = (Spannable) charSequence;
                Selection.setSelection(spanText, charSequence.length());
            }
        }
        if (v == mContainerPasswordRemoveEZTouch) {
            if (mIsRemovePassword)
            mEtPassword.setText(null);
        }
        if (v == mContainerPhoneNumberRemoveEZTouch) {
            if (mIsRemovePhoneNumber)
            mEtPhoneNumber.setText(null);
        }
        if (v == mForgetPassword) {
            // 失去焦点，隐藏键盘
            if(mEtPhoneNumber.hasFocus()) {
                mEtPhoneNumber.clearFocus();
                mImm.hideSoftInputFromWindow(mEtPhoneNumber.getWindowToken(), 0);
            }
            if(mEtPassword.hasFocus()) {
                mEtPassword.clearFocus();
                mImm.hideSoftInputFromWindow(mEtPassword.getWindowToken(), 0);
            }
            mCallBacks.handleAction(ActionId.OnForgetPasswordClick, null, null);
        }
    }
}
