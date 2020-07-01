package com.potato.lrcview

import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.InputStreamReader
import java.util.regex.Matcher
import java.util.regex.Pattern
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private var time : Long = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val list = getLrcList()
        lrc_view.post {
            lrc_view.setLrcData(list)
        }

        Thread(Runnable {
            while(true){
                Thread.sleep(1000)
                time += 1000
                runOnUiThread(Runnable {
                    lrc_view.updateTime(time)
                })
            }
        }).start()
    }

    fun getLrcList() : List<Lrc>{
        var list : ArrayList<Lrc> = ArrayList()
        var file : File? = File(Environment.getExternalStorageDirectory(), "/jhym.lrc")
        if(file != null){
            var inputStreamReader : InputStreamReader? = InputStreamReader(FileInputStream(file))
            var bufferedReader : BufferedReader? = BufferedReader(inputStreamReader)
            var line : String? = bufferedReader?.readLine()
            while (line != null){
                list.addAll(parseLrc(line))
                line = bufferedReader?.readLine()
            }
        }
        return list
    }

    fun parseLrc(line : String) : ArrayList<Lrc>{
        val lrcs : ArrayList<Lrc> = ArrayList<Lrc>()
        val matcher: Matcher = Pattern.compile("((\\[\\d{2}:\\d{2}\\.\\d{2}])+)(.*)").matcher(line)
        if(!matcher.matches()){
            return ArrayList()
        }
        val time = matcher.group(1)
        val content  = matcher.group(3)
        val timeMatcher = Pattern.compile("\\[(\\d{2}):(\\d{2})\\.(\\d{2})]").matcher(time)

        while (timeMatcher.find()) {
            val min = timeMatcher.group(1)
            val sec = timeMatcher.group(2)
            val mil = timeMatcher.group(3)

            if (content != null && content.isNotEmpty()) {
                val lrc = Lrc(min.toLong() * 60 * 1000 + sec.toLong() * 1000 + mil.toLong() * 10, content)
                lrcs.add(lrc)
            }
        }
        return lrcs
    }
}
