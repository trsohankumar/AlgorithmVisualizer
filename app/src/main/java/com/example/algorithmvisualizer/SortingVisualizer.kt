package com.example.algorithmvisualizer

import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.algorithmvisualizer.databinding.ActivitySortingVisualizerBinding
import com.example.algorithmvisualizer.databinding.SortingVisualizerBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.*


class SortingVisualizer : AppCompatActivity() {
    private lateinit var binding:ActivitySortingVisualizerBinding
    private var size:Int = 10
    private val buttons: MutableList<MutableList<Button>> = ArrayList()
    private var isArraySorted:Boolean = true
    private var arrayToBeSorted: MutableList<Int> = ArrayList()


    private var TraceColor:String = "#FFB500"
    private var BackgroundColor:String = "#F93800"
    private var SwappedColor:String = "#283350"
    private var backGroundColor:String = "#121212"
    private var textColor:String= "#FFFFFFFF"
    private val sortingList = listOf<String>("BUBBLE SORT","SELECTION SORT","INSERTION SORT","MERGE SORT","QUICK SORT")
    private var currentSortingAlgo:String = "BUBBLE SORT"

    //jobs for sorting algorithm
    private lateinit var jobBubbleSort: Job
    private lateinit var jobSelectionSort:Job
    private lateinit var jobInsertionSort:Job
    private lateinit var jobMergeSort1:Job
    private lateinit var jobMergeSort2:Job
    private lateinit var jobQuickSort1:Job
    private lateinit var jobQuickSort2:Job
    private lateinit var jobQuickSort3:Job

    override fun onCreate(savedInstanceState: Bundle?) {
        //binding code
        binding = ActivitySortingVisualizerBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setSupportActionBar(binding.tbSortingVisualizer)
        val actionBar = supportActionBar
        if(actionBar!= null){
            actionBar.setDisplayHomeAsUpEnabled(true)
        }
        binding.tbSortingVisualizer.setNavigationOnClickListener {
            onBackPressed()
            cancelAllJobs()
        }
        binding.tbSortingVisualizer.title = currentSortingAlgo

        //Dark and Light mode
        val appSettingPrefs: SharedPreferences =getSharedPreferences("AppSettingPrefs",0)
        val isNightModeOn:Boolean=appSettingPrefs.getBoolean("NightMode",false)
        if(!isNightModeOn){
            backGroundColor = "#E6E6E6"
            textColor="#FF000000"
            BackgroundColor="#FFD55A"
            TraceColor="#293250"
            SwappedColor="#6DD47E"

        }

        binding.btnStart.setOnClickListener{
            cancelAllJobs()
            //code to display the Bottom Sheet
            val dialog = BottomSheetDialog(this)
            val bindingBottomSheet  : SortingVisualizerBottomSheetBinding = SortingVisualizerBottomSheetBinding.inflate(layoutInflater)
            dialog.setContentView(bindingBottomSheet.root)
            dialog.show()

            //code to send the values to the adapter
            val adapter = ArrayAdapter<String>(this,R.layout.spinner_dropdown_item,sortingList)
            bindingBottomSheet.sortingSelector.adapter = adapter


            //Code to get the current selected sorting algorithm
            bindingBottomSheet.sortingSelector.onItemSelectedListener = object:AdapterView.OnItemSelectedListener{
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    currentSortingAlgo = parent!!.getItemAtPosition(position).toString()
                    binding.tbSortingVisualizer.title = currentSortingAlgo
                    when(currentSortingAlgo){

                        "SELECTION SORT" -> {
                            bindingBottomSheet.tvTimeComplexity.text = "O(n^2)"
                        }
                        "BUBBLE SORT" -> {
                            bindingBottomSheet.tvTimeComplexity.text = "O(n^2)"
                        }
                        "INSERTION SORT" -> {
                            bindingBottomSheet.tvTimeComplexity.text = "O(n^2)"
                        }
                        "MERGE SORT" ->{
                            bindingBottomSheet.tvTimeComplexity.text = "O(nlog(n))"
                        }
                        "QUICK SORT" ->{
                            bindingBottomSheet.tvTimeComplexity.text = "O(nlog(n))"
                        }
                    }
                    Toast.makeText(this@SortingVisualizer,"you selected ${parent.getItemAtPosition(position).toString()}",Toast.LENGTH_LONG).show()
                }


                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
            //code for the seekbar
            var startPoint = 0
            var endpoint = 0
            bindingBottomSheet.seekBarSize.progress = size
            bindingBottomSheet.sizeText.text = "Current size: ${size.toString()}"
            bindingBottomSheet.seekBarSize.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    bindingBottomSheet.sizeText.text = "Current size: ${progress.toString()}"
                    size = progress
                    //delete main screen only if visible after changing the size

                    if(!binding.tvGenerateGrid.isVisible){
                        deleteMainScreen()
                        binding.tvClear.visibility = View.GONE
                        binding.tvRedo.visibility = View.GONE
                        binding.ivSort.visibility = View.GONE
                        binding.llIntroText.visibility = View.VISIBLE
                        binding.tvGenerateGrid.visibility = View.VISIBLE
                    }

                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {
                    if(seekBar != null){
                        startPoint = seekBar.progress
                    }

                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                    if (seekBar != null) {
                        endpoint = seekBar.progress
                    }
                }

            })

            //code when the close button is pressed
            bindingBottomSheet.btnDismiss.setOnClickListener{
                dialog.dismiss()
            }
        }

        falseJobInit()

        //Disabling views that are shown after grid is generated
        binding.tvClear.visibility = View.GONE
        binding.tvRedo.visibility = View.GONE
        binding.ivSort.visibility = View.GONE

        //creating the grid
        binding.tvGenerateGrid.setOnClickListener {
            isArraySorted = false
            binding.llIntroText.visibility = View.GONE
            createButtonGrid(size)
            paintAllButtonsWhiteAgain(size)
            //creating random array
            randamize(size)
            binding.tvGenerateGrid.visibility = View.GONE
            binding.tvClear.visibility = View.VISIBLE
            binding.tvRedo.visibility = View.VISIBLE
            binding.ivSort.visibility = View.VISIBLE
        }


        binding.tvRedo.setOnClickListener {
            isArraySorted = false
            cancelAllJobs()
            deleteMainScreen()
            createButtonGrid(size)
            paintAllButtonsWhiteAgain(size)
            randamize(size)
        }
        binding.ivSort.setOnClickListener {
            if(!isArraySorted){
                cancelAllJobs()
                chooseStartSortingAlgorithm()
            }


        }
        binding.tvClear.setOnClickListener {
            isArraySorted = false
            cancelAllJobs()
            deleteMainScreen()
            binding.tvClear.visibility = View.GONE
            binding.tvRedo.visibility = View.GONE
            binding.ivSort.visibility = View.GONE
            binding.llIntroText.visibility = View.VISIBLE
            binding.tvGenerateGrid.visibility = View.VISIBLE
        }
    }

    private fun chooseStartSortingAlgorithm(){
        when(currentSortingAlgo){

            "SELECTION SORT" -> {

                selectionSort()
            }
            "BUBBLE SORT" -> {

                bubbleSort()
            }
            "INSERTION SORT" -> {

                insertionSort()
            }
            "MERGE SORT" ->{

                mergeSort(arrayToBeSorted)
            }
            "QUICK SORT" ->{
                quicksort(arrayToBeSorted,0,size)
            }
        }
    }

    private fun falseJobInit() {
        jobBubbleSort = GlobalScope.launch { }
        jobSelectionSort = GlobalScope.launch {  }
        jobInsertionSort = GlobalScope.launch {  }
        jobMergeSort1=GlobalScope.launch {  }
        jobMergeSort2=GlobalScope.launch {  }
        jobQuickSort1=GlobalScope.launch {  }
        jobQuickSort2=GlobalScope.launch {  }
        jobQuickSort3=GlobalScope.launch {  }
    }

    private fun cancelAllJobs() {
        jobBubbleSort.cancel()
        jobSelectionSort.cancel()
        jobInsertionSort.cancel()
        jobMergeSort1.cancel()
        jobMergeSort2.cancel()
        jobQuickSort1.cancel()
        jobQuickSort2.cancel()
        jobQuickSort3.cancel()
    }

    private fun selectionSort(){
        jobSelectionSort=GlobalScope.launch (Dispatchers.Main )
        {
            val n = arrayToBeSorted.size
            var temp: Int
            for (i in 0..n - 1) {
                var indexOfMin = i
                for (j in n - 1 downTo i+1) {
                    paintSingleColWhite(j)
                    colorButton(j, arrayToBeSorted[j], TraceColor)
                    colorButton(j-1, arrayToBeSorted[j-1], TraceColor)
                    buttons[j][arrayToBeSorted[j]+1].text = arrayToBeSorted[j].toString()
                    buttons[j-1][arrayToBeSorted[j-1]+1].text = arrayToBeSorted[j-1].toString()
                    delay(200)
                    colorButton(j, arrayToBeSorted[j], BackgroundColor)
                    colorButton(j-1, arrayToBeSorted[j-1], BackgroundColor)
                    delay(200)
                    if (arrayToBeSorted[j] < arrayToBeSorted[indexOfMin])
                        indexOfMin = j
                }
                if (i != indexOfMin) {
                    replaceTwoColInGrid(i, indexOfMin)
                    delay(400)
                    temp = arrayToBeSorted[i]
                    arrayToBeSorted[i] = arrayToBeSorted[indexOfMin]
                    arrayToBeSorted[indexOfMin] = temp
                    buttons[i][arrayToBeSorted[i]+1].text = arrayToBeSorted[i].toString()
                    buttons[i][arrayToBeSorted[i]+1].setTextColor(Color.parseColor(textColor))
                    buttons[indexOfMin][arrayToBeSorted[indexOfMin]+1].text = arrayToBeSorted[indexOfMin].toString()
                    buttons[indexOfMin][arrayToBeSorted[indexOfMin]+1].setTextColor(Color.parseColor(textColor))
                }
            }
        }
        isArraySorted = true
    }


    private fun bubbleSort() {
        jobBubbleSort = GlobalScope.launch(Dispatchers.Main)
        {
            var swap = true
            while (swap) {
                swap = false
                for (i in 0 until arrayToBeSorted.size - 1) {
                    paintSingleColWhite(i)
                    colorButton(i, arrayToBeSorted[i], TraceColor)
                    colorButton(i+1, arrayToBeSorted[i+1], TraceColor)
                    buttons[i][arrayToBeSorted[i]+1].text = arrayToBeSorted[i].toString()
                    buttons[i+1][arrayToBeSorted[i+1]+1].text = arrayToBeSorted[i+1].toString()
                    delay(200)
                    colorButton(i, arrayToBeSorted[i], BackgroundColor)
                    colorButton(i+1, arrayToBeSorted[i+1], BackgroundColor)
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
        isArraySorted = true
    }

    private fun insertionSort(){
        //at each iteration the lighter element is put in the front
        jobInsertionSort=GlobalScope.launch (Dispatchers.Main )
        {
            for (i in 1..size) {
                val item = arrayToBeSorted[i]
                var j = i - 1
                while (j >= 0 && arrayToBeSorted[j] > item) {
                    colorButton(j+1,arrayToBeSorted[j+1],TraceColor)
                    delay(100)
                    paintSingleColWhite(j + 1)
                    colorButton(j + 1, arrayToBeSorted[j], TraceColor)
                    delay(200)
                    arrayToBeSorted[j + 1] = arrayToBeSorted[j]
                    colorButton(j + 1, arrayToBeSorted[j], BackgroundColor)
                    buttons[j+1][arrayToBeSorted[j+1]+1].text = arrayToBeSorted[j+1].toString()
                    buttons[j+1][arrayToBeSorted[j+1]+1].setTextColor(Color.parseColor(textColor))
                    j -= 1
                }
                colorButton(j+1,arrayToBeSorted[j+1],TraceColor)
                delay(500)
                paintSingleColWhite(j + 1)
                colorButton(j + 1, item, SwappedColor)
                delay(500)
                arrayToBeSorted[j + 1] = item
                colorButton(j + 1, arrayToBeSorted[j+1], BackgroundColor)
                buttons[j+1][arrayToBeSorted[j+1]+1].text = arrayToBeSorted[j+1].toString()
                buttons[j+1][arrayToBeSorted[j+1]+1].setTextColor(Color.parseColor(textColor))

            }
        }
        isArraySorted = true
    }

    private fun mergeSort(list: MutableList<Int>){
        GlobalScope.launch (Dispatchers.Main) {
            merger(list)
        }
    }
    private suspend fun merger(list: MutableList<Int>): MutableList<Int> {

        if (list.size <= 1) {
            return list
        }
        val middle = list.size / 2
        var left = list.subList(0,middle);
        var right = list.subList(middle,list.size);
        var lleft:MutableList<Int> = mutableListOf()
        var lright:MutableList<Int> = mutableListOf()
        jobMergeSort1=GlobalScope.launch(Dispatchers.Main) {
            lleft = merger(left)
        }
        jobMergeSort1.join()
        jobMergeSort2=GlobalScope.launch(Dispatchers.Main) {
            lright = merger(right)
        }
        jobMergeSort2.join()


        var indexLeft = 0
        var indexRight = 0
        var newList : MutableList<Int> = mutableListOf()
        var i=0
        while (indexLeft < lleft.count() && indexRight < lright.count()) {
            if (lleft[indexLeft] <= lright[indexRight]) {
                paintSingleColWhite(i)
                colorButton(i,lleft[indexLeft],TraceColor)
                delay(200)
                paintSingleColWhite(i)
                colorButton(i,lleft[indexLeft],SwappedColor)
                delay(200)
                paintSingleColWhite(i)
                buttons[i][lleft[indexLeft]+1].text = lleft[indexLeft].toString()
                buttons[i][lleft[indexLeft]+1].setTextColor(Color.parseColor(textColor))
                colorButton(i,lleft[indexLeft],BackgroundColor)
                i++
                newList.add(lleft[indexLeft])
                indexLeft++
            } else {
                paintSingleColWhite(i)
                colorButton(i,lright[indexRight],TraceColor)
                delay(200)
                paintSingleColWhite(i)
                colorButton(i,lright[indexRight],SwappedColor)
                delay(200)
                paintSingleColWhite(i)
                colorButton(i,lright[indexRight],BackgroundColor)
                buttons[i][lright[indexRight]+1].text = lright[indexRight].toString()
                buttons[i][lright[indexRight]+1].setTextColor(Color.parseColor(textColor))
                i++
                newList.add(lright[indexRight])
                indexRight++
            }
        }

        while (indexLeft < lleft.size) {
            paintSingleColWhite(i)
            colorButton(i,lleft[indexLeft],TraceColor)
            delay(200)
            paintSingleColWhite(i)
            colorButton(i,lleft[indexLeft],SwappedColor)
            delay(200)
            paintSingleColWhite(i)
            colorButton(i,lleft[indexLeft],BackgroundColor)
            buttons[i][lleft[indexLeft]+1].text = lleft[indexLeft].toString()
            buttons[i][lleft[indexLeft]+1].setTextColor(Color.parseColor(textColor))
            i++
            newList.add(lleft[indexLeft])
            indexLeft++
        }

        while (indexRight < lright.size) {
            paintSingleColWhite(i)
            colorButton(i,lright[indexRight],TraceColor)
            delay(200)
            paintSingleColWhite(i)
            colorButton(i,lright[indexRight],SwappedColor)
            delay(200)
            paintSingleColWhite(i)
            colorButton(i,lright[indexRight],BackgroundColor)
            buttons[i][lright[indexRight]+1].text = lright[indexRight].toString()
            buttons[i][lright[indexRight]+1].setTextColor(Color.parseColor(textColor))
            i++
            newList.add(lright[indexRight])
            indexRight++
        }
        isArraySorted = true
        return newList;
    }

    private fun quicksort(A: MutableList<Int>, p: Int, r: Int) {
        jobQuickSort1=GlobalScope.launch(Dispatchers.Main) {
            if (p < r) {
                var q: Int = partition(A, p, r)
                quicksort(A, p, q - 1)
                quicksort(A, q + 1, r)
            }
        }
        isArraySorted = true
    }
    //quick sort component
    private suspend fun partition(A: MutableList<Int>, p: Int, r: Int): Int {
        var i=0
        jobQuickSort2=GlobalScope.launch(Dispatchers.Main) {
            var x = A[r]
            i = p - 1
            for (j in p until r) {
                if (A[j] <= x) {
                    i++
                    exchange(A, i, j)
                }
            }
            exchange(A, i + 1, r)
        }
        jobQuickSort2.join()
        return i + 1
    }
    //quick sort component
    private suspend fun exchange(A: MutableList<Int>, i: Int, j: Int) {
        jobQuickSort3=GlobalScope.launch(Dispatchers.Main) {
            replaceTwoColInGrid(i, j)
            var temp = A[i]
            A[i] = A[j]
            A[j] = temp
            buttons[i][A[i]+1].text = A[i].toString()
            buttons[i][A[i]+1].setTextColor(Color.parseColor(textColor))
            buttons[j][A[j]+1].text = A[j].toString()
            buttons[i][A[j]+1].setTextColor(Color.parseColor(textColor))

        }
        jobQuickSort3.join()
    }
    private suspend fun replaceTwoColInGrid(a: Int, b: Int) {
        val job = GlobalScope.launch(Dispatchers.Main) {
            delay(200)
            paintSingleColWhite(a)
            paintSingleColWhite(b)
            colorButton(a, arrayToBeSorted[b], SwappedColor)
            colorButton(b, arrayToBeSorted[a], SwappedColor)
            delay(200)
            colorButton(a, arrayToBeSorted[b], BackgroundColor)
            colorButton(b, arrayToBeSorted[a], BackgroundColor)
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
    //custom randomize class
    private fun randamize(size: Int) {
        arrayToBeSorted.removeAll(arrayToBeSorted)
        for (col in 0..size) {
            val rowSize = size -1
            val row = (0..rowSize).random()
            arrayToBeSorted.add(row)
            colorButton(col, row, BackgroundColor)
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
        val mainSortingScreenId = resources.getIdentifier("mainscreen", "id", packageName)
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
        val mainscreenid = resources.getIdentifier("mainscreen", "id", packageName)
        val mainscreen=findViewById<LinearLayout>(mainscreenid)
        (mainscreen.parent as ViewGroup).removeView(mainscreen)
        buttons.removeAll(buttons)
    }
}


