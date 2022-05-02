package com.linbug.emotioncalendar

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import com.linbug.emotioncalendar.databinding.ActivityMainBinding
import com.linbug.room.Emotion
import com.linbug.room.EmotionApplication
import com.linbug.room.EmotionViewModel
import com.linbug.room.EmotionViewModelFactory
import com.prolificinteractive.materialcalendarview.CalendarDay
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val emotionViewModel: EmotionViewModel by viewModels {
        EmotionViewModelFactory((application as EmotionApplication).repository)
    }

    private var date = CalendarDay.today()
    private var emotion = 0
    private var description: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    private fun init() {
        binding.calendarView.addDecorator(TodayDecorator(Collections.singleton(CalendarDay.today())))


        val time = LocalDateTime.of(date.year, date.month, date.day, 0, 0)
        val milliTime = time.atZone(ZoneId.systemDefault()).toInstant()?.toEpochMilli() ?: 0
        val time2 = Instant.ofEpochMilli(milliTime).atZone(ZoneId.systemDefault()).toLocalDateTime()
        val date2 = CalendarDay.from(time2.year, time2.monthValue, time2.dayOfMonth)


        binding.spinner.adapter = ArrayAdapter.createFromResource(this, R.array.itemList, android.R.layout.simple_spinner_item)
        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                setEmotion(position)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                //do nothing
            }
        }

        decorateEmotions()

        binding.calendarView.setOnDateChangedListener { _, date, _ ->
            this.date = date
            updateView()
        }

        binding.saveBtn.setOnClickListener {
            description = binding.contextEditText.text.toString()
            saveEmotion(date, binding.spinner.selectedItemPosition, description)
            binding.spinner.setSelection(emotion)

            showDescription()
        }

        binding.updateBtn.setOnClickListener {
            binding.contextEditText.setText(description)
            binding.diaryContent.text = binding.contextEditText.text

            showEdit()
        }

        binding.deleteBtn.setOnClickListener {
            description = null
            binding.contextEditText.text = description
            removeEmotion(this.date)

            showEdit()
        }
    }

    private fun calendarDay2milli(date: CalendarDay): Long {
        val time = LocalDateTime.of(date.year, date.month, date.day, 0, 0)
        return time.atZone(ZoneId.systemDefault()).toInstant()?.toEpochMilli() ?: 0
    }

    private fun milli2calendarDay(milli: Long): CalendarDay {
        val time2 = Instant.ofEpochMilli(milli).atZone(ZoneId.systemDefault()).toLocalDateTime()
        return CalendarDay.from(time2.year, time2.monthValue, time2.dayOfMonth)
    }

    private fun showDescription() {
        binding.emotion.visibility = View.VISIBLE
        binding.spinner.visibility = View.GONE
        binding.contextEditText.visibility = View.INVISIBLE
        binding.diaryContent.visibility = View.VISIBLE
        binding.saveBtn.visibility = View.GONE
        binding.configButtonLayout.visibility = View.VISIBLE
    }

    private fun showEdit() {
        binding.emotion.visibility = View.VISIBLE
        binding.spinner.visibility = View.VISIBLE
        binding.contextEditText.visibility = View.VISIBLE
        binding.diaryContent.visibility = View.INVISIBLE
        binding.saveBtn.visibility = View.VISIBLE
        binding.configButtonLayout.visibility = View.GONE
    }

    private fun updateView() {
        emotionViewModel.getDateEmotion(calendarDay2milli(date)).observe(this) { emotion ->
            Log.d("linburg", "view2")
            checkDate(emotion)
        }
    }

    private fun decorateEmotions() {
        emotionViewModel.allEmotions.observe(this) { emotions ->
            val calList = ArrayList<CalendarDay>()
            for (emotion in emotions) {
                val day = milli2calendarDay(emotion.date)
                calList.add(day)
                binding.calendarView.addDecorators(EmotionDecorator(applicationContext, day, emotion.emotion))
            }
        }
    }

    private fun setEmotion(id: Int) {
        val resId = when(id) {
            0 -> R.drawable.happy
            1 -> R.drawable.sad
            2 -> R.drawable.angry
            3 -> R.drawable.love
            else -> R.drawable.happy
        }

        binding.spinner.setSelection(id)
        binding.emotion.setImageResource(resId)
    }

    private fun saveEmotion(date: CalendarDay, emotion: Int, description: String?) {
        val emotionSet = Emotion(calendarDay2milli(date), emotion, description)
        emotionViewModel.insertEmotion(emotionSet)
    }

    private fun removeEmotion(date: CalendarDay) {
        val emotion = Emotion(calendarDay2milli(date), 0, null)
        emotionViewModel.deleteEmotion(emotion)
    }

    private fun checkDate(emotion: Emotion?) {
        emotion?.let {
            description = it.description
            binding.diaryContent.text = description
            this.emotion = it.emotion
            setEmotion(it.emotion)

            showDescription()

        } ?: run {
            description = null
            binding.contextEditText.text = description
            this.emotion = 0
            setEmotion(this.emotion)

            showEdit()
        }
    }
}