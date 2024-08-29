package com.example.myapplication.customwidget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.CompoundButton;
import androidx.core.content.ContextCompat;
import com.example.myapplication.R;

public class MultiStateCheckBox extends CompoundButton {

    private int state = 0; // حالت اولیه
    private Drawable[] stateDrawables; // آرایه‌ای از Drawable ها برای حالت‌های مختلف

    public MultiStateCheckBox(Context context) {
        super(context);
        init(null);
    }

    public MultiStateCheckBox(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public MultiStateCheckBox(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        stateDrawables = new Drawable[]{
                ContextCompat.getDrawable(getContext(), R.drawable.undone_40_icon),
                ContextCompat.getDrawable(getContext(), R.drawable.perfectdone_40_icon),
                ContextCompat.getDrawable(getContext(), R.drawable.gooddone_40_icon),
                ContextCompat.getDrawable(getContext(), R.drawable.baddone_40_icon)
        };

        setButtonDrawable(stateDrawables[state]);

        setOnClickListener(v -> toggleState());
    }

    private void toggleState() {
        state = (state + 1) % stateDrawables.length;
        setButtonDrawable(stateDrawables[state]);

        refreshDrawableState();
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
        setButtonDrawable(stateDrawables[state]);
    }

}
