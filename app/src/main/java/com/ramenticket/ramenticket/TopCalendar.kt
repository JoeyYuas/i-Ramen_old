package com.ramenticket.ramenticket

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v4.app.AppLaunchChecker
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.github.sundeepk.compactcalendarview.CompactCalendarView
import com.github.sundeepk.compactcalendarview.domain.Event
import java.text.SimpleDateFormat
import java.util.*

//選択した日付の色を変えてやりたい！！！！



class TopCalendar : AppCompatActivity() {

    lateinit private var userDB : db_Adapter
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_top_calendar)

        val before = AppLaunchChecker.hasStartedFromLauncher(applicationContext)
        AppLaunchChecker.onActivityCreate(this)

        if (!before) {
            startActivity(Intent(this, myRamenPointActivity::class.java))
        } else {

            val date = Date()
            val nD = SimpleDateFormat("dd")
            val nowDay = nD.format(date).toInt()
            val nM = SimpleDateFormat("MM")
            val nowMonth = nM.format(date).toInt()
            val nY = SimpleDateFormat("yyyy")
            var nowYear = nY.format(date).toInt()

            var nowMonthForArray = 0

            if (nowMonth != 1) {
                nowMonthForArray = nowMonth - 1
            }

            var eatDateList = arrayOf(nowYear, nowMonthForArray, nowDay)

            Log.v("ListDate", "${nowYear}")

            for (date in eatDateList) {
                Log.v("ListDate", "${date}")
            }



            //保存
            val thisPrefer: SharedPreferences = getSharedPreferences("savePoint", Context.MODE_PRIVATE)
            val getIntFromIntent = intent.getIntExtra("howManyPoint", 0)
            var ramenPoint = thisPrefer.getInt("sP", 0)
            val setText = findViewById<TextView>(R.id.textView3)
            setText.text = "ラーメンポイント：$ramenPoint"


            val editor = thisPrefer.edit()

            var setRamenPoint = thisPrefer.getInt("howManyEat", 1)
            //val getIntFromIntent = intent.getIntExtra("howManyPoint",0)
            if (getIntFromIntent != 0) {
                setRamenPoint = getIntFromIntent
            }
            editor.putInt("howManyEat", setRamenPoint).apply()
            Log.v("setRamenPoint", "$setRamenPoint")

            //来月分の処理
            var nextMonth = nowMonth + 1
            if (nowMonth == 12) {
                nextMonth = 1
            }
            Log.v("todayMonth", "$nextMonth")
            val savedNextMonth = thisPrefer.getInt("nM", nowMonth)
            Log.v("todayMonth", "$savedNextMonth")
            if (nowMonth == savedNextMonth) {
                ramenPoint += setRamenPoint
                setText.text = "ラーメンポイント：$ramenPoint"
                editor.putInt("nM", nextMonth).apply()
            }


            val optionButton = findViewById<Button>(R.id.otionButton)
            optionButton.setOnClickListener {
                startActivity(Intent(this, myRamenPointActivity::class.java))
            }


            val monthYear = findViewById<TextView>(R.id.monthYear)
            monthYear.text = "${nowYear}年 ${nowMonth}月"


            val compactcalendarview = findViewById<CompactCalendarView>(R.id.compactcalendar_view)

            //DBからsavedTimeInMillsを取得する。（ループ処理でまとめて）
            userDB = db_Adapter(this)
            val list = userDB.selectDB()
            if (list.isNotEmpty()) {
                for(savedTimeInMills in list){
                    val savedEatEvent = Event(Color.parseColor("#ff6600"), savedTimeInMills)
                    compactcalendarview.addEvent(savedEatEvent, true)
                    Log.d("list no kekka: ", "${savedTimeInMills}")
                }
            }


            compactcalendarview.setListener(object : CompactCalendarView.CompactCalendarViewListener {
                override fun onMonthScroll(firstDayOfNewMonth: Date) {
                    var monthS = firstDayOfNewMonth.month.toInt()
                    monthS += 1
                    var yearS = firstDayOfNewMonth.year.toInt()
                    yearS += 1900
                    monthYear.text = "${yearS}年 ${monthS}月"
                    Log.d("Month was scrolled to: ", "${firstDayOfNewMonth}")

                }


                override fun onDayClick(dateClicked: Date?) {
                    val eD = SimpleDateFormat("dd")
                    val eatDay = eD.format(dateClicked).toInt()
                    val eM = SimpleDateFormat("MM")
                    val eatMonth = eM.format(dateClicked).toInt()
                    val eY = SimpleDateFormat("yyyy")
                    var eatYear = eY.format(dateClicked).toInt()
                    Log.d("eatDay: ", "${eatDay}")

                    var eatMonthForArray = 0

                    if (eatMonth != 1) {
                        eatMonthForArray = eatMonth - 1
                    }
                    eatDateList = arrayOf(eatYear, eatMonthForArray, eatDay)


                }
            })


            val usePoint = findViewById<Button>(R.id.button)
            usePoint.setOnClickListener {
                Log.v("ramenPoint", "${ramenPoint}")
                if (ramenPoint <= 0) {
                    ramenPoint = 0
                    editor.putInt("sP", ramenPoint).apply()
                    setText.text = "ラーメンポイント：$ramenPoint"
                    Toast.makeText(this, "もうラーメンポイントがありません！", Toast.LENGTH_LONG).show()
                } else {

                    val cal = Calendar.getInstance()
                    cal.set(eatDateList[0], eatDateList[1], eatDateList[2], 0, 0, 0)
                    val eatEvent = Event(Color.parseColor("#ff6600"), cal.timeInMillis)
                    compactcalendarview.addEvent(eatEvent, true)

                    val TimeInMillis = cal.timeInMillis
                    Log.v("eatEvent", "${TimeInMillis}")

                    //DBにTimeInMillsを格納する。
                    userDB.insertDB(TimeInMillis)

                    for (event in eatDateList) {
                        Log.v("eatEvent", "${event}")
                    }
                    ramenPoint -= 1
                    editor.putInt("sP", ramenPoint).apply()
                    setText.text = "ラーメンポイント：$ramenPoint"
                }
            }

            editor.putInt("sP", ramenPoint).apply()


        }
    }

}


