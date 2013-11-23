package com.summarecon.qcapp.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * Created by arnold on 23/11/13.
 */
public class CustomScrollView extends ScrollView{
    private boolean scrollAble = true;

    public boolean isScrollAble() {
        return scrollAble;
    }

    public void setScrollAble(boolean scrollAble) {
        this.scrollAble = scrollAble;
    }

    public CustomScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public CustomScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomScrollView(Context context) {
        super(context);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        if (isScrollAble()) {
            return super.onInterceptTouchEvent(ev);
        } else {
            return false;
        }
    }
}
