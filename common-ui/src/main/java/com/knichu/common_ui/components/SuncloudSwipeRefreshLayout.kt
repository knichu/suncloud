package com.knichu.common_ui.components

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

class SuncloudSwipeRefreshLayout(context: Context, attrs: AttributeSet) : SwipeRefreshLayout(context, attrs) {

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        // SwipeRefreshLayout의 기본 동작을 유지하면서 RecyclerView가 스크롤 중인지 여부를 확인
        return !isAnyRecyclerViewScrolling() && super.onInterceptTouchEvent(ev)
    }

    private fun isAnyRecyclerViewScrolling(): Boolean {
        // 현재 화면에 있는 모든 RecyclerView를 찾기
        val recyclerViews = mutableListOf<RecyclerView>()
        findRecyclerViews(this, recyclerViews)

        // 하나라도 스크롤 중인 RecyclerView가 있는지 확인
        for (recyclerView in recyclerViews) {
            if (recyclerView.scrollState != RecyclerView.SCROLL_STATE_IDLE) {
                return true
            }
        }
        return false
    }

    // 레이아웃 내부에서 RecyclerView를 찾아 리스트에 추가
    private fun findRecyclerViews(view: View, recyclerViews: MutableList<RecyclerView>) {
        if (view is RecyclerView) {
            recyclerViews.add(view)
        } else if (view is ViewGroup) {
            for (i in 0 until view.childCount) {
                findRecyclerViews(view.getChildAt(i), recyclerViews)
            }
        }
    }
}