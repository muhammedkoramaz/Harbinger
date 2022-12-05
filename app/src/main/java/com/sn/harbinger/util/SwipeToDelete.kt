package com.sn.harbinger.util

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.GradientDrawable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.sn.harbinger.R
import com.sn.harbinger.ui.dashboard.home.project.NotesAdapter
import kotlin.math.roundToInt

class SwipeToDelete(private val adapter: NotesAdapter, val context: Context) :
    ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
    private val gradientDrawable = GradientDrawable()
    private val icon = ContextCompat.getDrawable(context, R.drawable.ic_delete_filled)
    private val intrinsicWidth = icon?.intrinsicWidth ?: 80
    private val intrinsicHeight = icon?.intrinsicHeight ?: 80

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.adapterPosition
        adapter.deleteNote(position)
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        if (dX != 0f && isCurrentlyActive) {
            val itemView = viewHolder.itemView

            gradientDrawable.colors = intArrayOf(
                context.getColor(R.color.error_color),
                context.getColor(R.color.error_color_cc)
            )

            val radius = (5.0F * context.resources.displayMetrics.density).roundToInt().toFloat()
            gradientDrawable.gradientType = GradientDrawable.LINEAR_GRADIENT
            gradientDrawable.cornerRadius = radius

            val top = itemView.top + (itemView.height - intrinsicHeight) / 2
            val left = itemView.width - intrinsicWidth - (itemView.height - intrinsicHeight) / 2
            val right = left + intrinsicHeight
            val bottom = top + intrinsicHeight

            gradientDrawable.setBounds(
                itemView.right + dX.toInt(),
                itemView.top,
                itemView.right,
                itemView.bottom
            )
            icon?.setBounds(left, top, right, bottom)
            gradientDrawable.draw(c)
            icon?.draw(c)
        }
    }
}