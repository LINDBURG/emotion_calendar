package com.linbug.emotioncalendar

import android.content.Context
import android.graphics.drawable.Drawable
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import androidx.appcompat.content.res.AppCompatResources

class EmotionDecorator(context: Context, targetDay: CalendarDay, emotion: Int) : DayViewDecorator {
    private val resId = when(emotion) {
        0 -> R.drawable.happy
        1 -> R.drawable.sad
        2 -> R.drawable.angry
        3 -> R.drawable.love
        else -> R.drawable.happy
    }
    private val drawable: Drawable = AppCompatResources.getDrawable(context, resId)!!
    private var myDay = targetDay

    override fun shouldDecorate(day: CalendarDay): Boolean {
        return day == myDay
    }

    override fun decorate(view: DayViewFacade) {
        view.setBackgroundDrawable(drawable)
    }

}