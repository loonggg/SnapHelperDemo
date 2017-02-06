package com.loonggg.snaphelperdemo;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by loonggg on 2017/2/6.
 */

public class CustomSnapHelper extends LinearSnapHelper {
    private OrientationHelper mHorizontalHelper;

    @Override
    public int[] calculateDistanceToFinalSnap(RecyclerView.LayoutManager layoutManager, View targetView) {
        int[] out = new int[2];
        //判断支持水平滚动，修改水平方向的位置，是修改的out[0]的值
        if (layoutManager.canScrollHorizontally()) {
            out[0] = distanceToStart(targetView, getHorizontalHelper(layoutManager));
        } else {
            out[0] = 0;
        }
        return out;
    }

    private int distanceToStart(View targetView, OrientationHelper helper) {
        return helper.getDecoratedStart(targetView) - helper.getStartAfterPadding();
    }

    @Override
    public View findSnapView(RecyclerView.LayoutManager layoutManager) {
        return findStartView(layoutManager, getHorizontalHelper(layoutManager));
    }

    private View findStartView(RecyclerView.LayoutManager layoutManager,
                               OrientationHelper helper) {

        if (layoutManager instanceof LinearLayoutManager) {
            int firstChild = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
            int lastChild = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
            if (firstChild == RecyclerView.NO_POSITION) {
                return null;
            }
            //这行的作用是如果是最后一个，翻到最后一条，解决显示不全的问题
            if (lastChild == layoutManager.getItemCount() - 1) {
                return layoutManager.findViewByPosition(lastChild);
            }

            View child = layoutManager.findViewByPosition(firstChild);
            //获取偏左显示的Item
            if (helper.getDecoratedEnd(child) >= helper.getDecoratedMeasurement(child) / 2
                    && helper.getDecoratedEnd(child) > 0) {
                return child;
            } else {
                return layoutManager.findViewByPosition(firstChild + 1);
            }
        }

        return super.findSnapView(layoutManager);
    }


    private OrientationHelper getHorizontalHelper(
            RecyclerView.LayoutManager layoutManager) {
        if (mHorizontalHelper == null) {
            mHorizontalHelper = OrientationHelper.createHorizontalHelper(layoutManager);
        }
        return mHorizontalHelper;
    }
}
