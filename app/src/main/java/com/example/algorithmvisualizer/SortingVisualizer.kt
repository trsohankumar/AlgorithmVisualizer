package com.example.algorithmvisualizer

import android.content.SharedPreferences
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import com.example.algorithmvisualizer.databinding.ActivitySortingVisualizerBinding
import kotlinx.coroutines.*

class SortingVisualizer : AppCompatActivity() {
    private lateinit var binding:ActivitySortingVisualizerBinding
    lateinit var jobBubbleSort: Job
    private var size:Int = 10
    private val buttons: MutableList<MutableList<Button>> = ArrayList()
    var arrayToBeSorted: MutableList<Int> = ArrayList()
    private var IndigoColor:String = "#FFB500"
    private var OrangeColor:String = "#F93800"
    private var PurpleColor:String = "#283350"
    private var backGroundColor:String = "#121212"
    private var textColor:String= "#FFFFFFFF"

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivitySortingVisualizerBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setSupportActionBar(binding.tbSortingVisualizer)
        val actionBar = supportActionBar
        if(actionBar!= null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            //actionBar.setDisplayShowTitleEnabled(false)
        }
        binding.tbSortingVisualizer.setNavigationOnClickListener {
            onBackPressed()
            cancelAllJobs()
        }

        val appSettingPrefs: SharedPreferences =getSharedPreferences("AppSettingPrefs",0)
     //   val sharedPrefsEdit: SharedPreferences.Editor=appSettingPrefs.edit()
        val isNightModeOn:Boolean=appSettingPrefs.getBoolean("NightMode",false)
       // Log.d("Night Mode one", isNightModeOn.toString())
        if(!isNightModeOn){
            backGroundColor = "#E6E6E6"
            textColor="#FF000000"
            OrangeColor="#FFD55A"
            IndigoColor="#293250"
            PurpleColor="#6DD47E"

        }

        falseJobInit()
        binding.tvClear.visibility = View.GONE
        binding.tvRedo.visibility = View.GONE
        binding.tvSort.visibility = View.GONE
        binding.tvGenerateGrid.setOnClickListener {
            binding.llIntroText.visibility = View.GONE
            createButtonGrid(size)
            paintAllButtonsWhiteAgain(size)
            randamize(size)
            binding.tvGenerateGrid.visibility = View.GONE
            binding.tvClear.visibility = View.VISIBLE
            binding.tvRedo.visibility = View.VISIBLE
            binding.tvSort.visibility = View.VISIBLE
        }


        binding.tvRedo.setOnClickListener {
            cancelAllJobs()
            deleteMainScreen()
            createButtonGrid(size)
            paintAllButtonsWhiteAgain(size)
            randamize(size)
        }
        binding.tvSort.setOnClickListener {
            cancelAllJobs()
            bubbleSort()
        }
        binding.tvClear.setOnClickListener {
            cancelAllJobs()
            deleteMainScreen()
            binding.tvClear.visibility = View.GONE
            binding.tvRedo.visibility = View.GONE
            binding.tvSort.visibility = View.GONE
            binding.llIntroText.visibility = View.VISIBLE
            binding.tvGenerateGrid.visibility = View.VISIBLE
        }
    }

    private fun falseJobInit() {
        jobBubbleSort = GlobalScope.launch { }
    }

    private fun cancelAllJobs() {
        jobBubbleSort.cancel()
    }

    private fun bubbleSort() {
        jobBubbleSort = GlobalScope.launch(Dispatchers.Main)
        {
            var swap = true
            while (swap) {
                swap = false
                for (i in 0 until arrayToBeSorted.size - 1) {
                    paintSingleColWhite(i)
                    colorButton(i, arrayToBeSorted[i], IndigoColor)
                    colorButton(i+1, arrayToBeSorted[i+1], IndigoColor)
                    buttons[i][arrayToBeSorted[i]+1].text = arrayToBeSorted[i].toString()
                    buttons[i+1][arrayToBeSorted[i+1]+1].text = arrayToBeSorted[i+1].toString()
                    delay(200)
                    colorButton(i, arrayToBeSorted[i], OrangeColor)
                    colorButton(i+1, arrayToBeSorted[i+1], OrangeColor)
                    delay(200)
                    if (arrayToBeSorted[i] > arrayToBeSorted[i + 1]) {
                        replaceTwoColInGrid(i, i + 1)
                        delay(200)
                        val temp = arrayToBeSorted[i]
                        arrayToBeSorted[i] = arrayToBeSorted[i + 1]
                        arrayToBeSorted[i + 1] = temp
                        swap = true
                        buttons[i][arrayToBeSorted[i]+1].text = arrayToBeSorted[i].toString()
                        buttons[i][arrayToBeSorted[i]+1].setTextColor(Color.parseColor(textColor))
                        buttons[i+1][arrayToBeSorted[i+1]+1].text = arrayToBeSorted[i+1].toString()
                        buttons[i+1][arrayToBeSorted[i+1]+1].setTextColor(Color.parseColor(textColor))
                    }
                }
            }
        }
    }


    private suspend fun replaceTwoColInGrid(a: Int, b: Int) {
        val job = GlobalScope.launch(Dispatchers.Main) {
            delay(200)
            paintSingleColWhite(a)
            paintSingleColWhite(b)
            colorButton(a, arrayToBeSorted[b], PurpleColor)
            colorButton(b, arrayToBeSorted[a], PurpleColor)
            delay(200)
            colorButton(a, arrayToBeSorted[b], OrangeColor)
            colorButton(b, arrayToBeSorted[a], OrangeColor)
        }
        job.join()
    }
    private fun paintAllButtonsWhiteAgain(size: Int) {
        for (i in 0..size) {
            for (j in 0..size) {
                buttons[i][j].setBackgroundColor(Color.parseColor(backGroundColor))
            }
        }
    }
    private fun randamize(size: Int) {
        arrayToBeSorted.removeAll(arrayToBeSorted)
        for (col in 0..size) {
            val rowSize = size -1
            val row = (0..rowSize).random()
            arrayToBeSorted.add(row)
            colorButton(col, row, OrangeColor)
            buttons[col][row+1].text = row.toString()
            buttons[col][row+1].setTextColor(Color.parseColor(textColor))
        }
    }

    private fun colorButton(col: Int, row: Int, color: String) {
        for (i in 0..row) {
            buttons[col][i].setBackgroundColor(Color.parseColor(color))
        }
    }

    private fun paintSingleColWhite(col: Int) {
        for (i in 0..size) {
            buttons[col][i].setBackgroundColor(Color.parseColor(backGroundColor))
            buttons[col][i].text = null
        }
    }


    private fun createButtonGrid(size: Int) {
        val mainSortingScreen = LinearLayout(this)
        mainSortingScreen.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        mainSortingScreen.orientation = LinearLayout.HORIZONTAL
        var mainSortingScreenId = resources.getIdentifier("mainscreen", "id", packageName)
        mainSortingScreen.id = mainSortingScreenId
        binding.SortingDisplay.addView(mainSortingScreen)
        for (i in 0..size) {
            val arrayLinearLayout = LinearLayout(this)
            arrayLinearLayout.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.MATCH_PARENT, 1.0f
            )
            arrayLinearLayout.orientation = LinearLayout.VERTICAL
            arrayLinearLayout.setPadding(10, 2, 10, 2)
            val buttoncol: MutableList<Button> = ArrayList()
            for (j in 0..size) {
                val button = Button(this)
                button.layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    1.0f
                )
                buttoncol.add(button)
                arrayLinearLayout.addView(button)
            }
            buttons.add(buttoncol)
            mainSortingScreen.addView(arrayLinearLayout)
        }
        paintAllButtonsWhiteAgain(size)
    }

    private fun deleteMainScreen() {
        var mainscreenid = resources.getIdentifier("mainscreen", "id", packageName)
        val mainscreen=findViewById<LinearLayout>(mainscreenid)
        (mainscreen.parent as ViewGroup).removeView(mainscreen)
        buttons.removeAll(buttons)
    }
}


