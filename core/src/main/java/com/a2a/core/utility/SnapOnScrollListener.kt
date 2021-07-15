package com.a2a.core.utility

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.a2a.core.extensions.getSnapPosition

class SnapOnScrollListener(
    private val snapHelper: SnapHelper,
    var recyclerBehavior: Behavior = Behavior.NOTIFY_ON_SCROLL,
    var onSnapPositionChangeListener: OnSnapPositionChangeListener? = null
) : RecyclerView.OnScrollListener() {

    enum class Behavior {
        NOTIFY_ON_SCROLL,
        NOTIFY_ON_SCROLL_STATE_IDLE
    }

    private var snapPosition = RecyclerView.NO_POSITION

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        if (recyclerBehavior == Behavior.NOTIFY_ON_SCROLL) {
            snapNotifyPositionChange(recyclerView)
        }
    }

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        if (recyclerBehavior == Behavior.NOTIFY_ON_SCROLL_STATE_IDLE
            && newState == RecyclerView.SCROLL_STATE_IDLE) {
            snapNotifyPositionChange(recyclerView)
        }
    }

    private fun snapNotifyPositionChange(recyclerView: RecyclerView) {
        val snapPosition = snapHelper.getSnapPosition(recyclerView)
        val snapPositionChanged = this.snapPosition != snapPosition
        if (snapPositionChanged) {
            onSnapPositionChangeListener?.onSnapPositionChange(snapPosition)
            this.snapPosition = snapPosition
        }
    }
}