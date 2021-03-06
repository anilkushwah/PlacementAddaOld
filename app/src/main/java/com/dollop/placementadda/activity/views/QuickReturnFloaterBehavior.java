package com.dollop.placementadda.activity.views;

import android.content.Context;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;

public class QuickReturnFloaterBehavior extends CoordinatorLayout.Behavior<View> {

    private int distance;

    public QuickReturnFloaterBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, View child, View directTargetChild, View target, int nestedScrollAxes) {
        return (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }

    @Override public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, View child, View target, int dx, int dy, int[] consumed) {
        if (dy > 0 && distance < 0 || dy < 0 && distance > 0) {
            child.animate().cancel();
            distance = 0;
        }
        distance += dy;
        final int height = child.getHeight() > 0 ? (child.getHeight()) : 600/*update this accordingly*/;
        if (distance > height && child.isShown()) {
            hide(child);
        } else if (distance < 0 && !child.isShown()) {
            show(child);
        }
    }

    private void hide(View view) {
        view.setVisibility(View.GONE);// use animate.translateY(height); instead
    }

    private void show(View view) {
        view.setVisibility(View.VISIBLE);// use animate.translateY(-height); instead
    }

}