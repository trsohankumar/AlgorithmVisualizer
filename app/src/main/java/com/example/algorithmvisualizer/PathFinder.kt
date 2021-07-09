package com.example.algorithmvisualizer

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.example.algorithmvisualizer.databinding.ActivityPathFinderBinding
import com.example.algorithmvisualizer.databinding.PathFinderBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.varunest.sparkbutton.SparkButton
import com.varunest.sparkbutton.SparkButtonBuilder
import com.varunest.sparkbutton.SparkEventListener
import kotlinx.coroutines.*
import java.util.*
import kotlin.collections.ArrayList

@DelicateCoroutinesApi
class PathFinder : AppCompatActivity() {
    private lateinit var binding:ActivityPathFinderBinding

    private val buttonStatusKeeper: MutableList<MutableMap<SparkButton, Int>> = ArrayList()
    private val buttons: MutableList<MutableList<SparkButton>> = ArrayList()
    private val sizeColumn = 10
    private val sizeRow = 20
    var startStatusKeeper: Int = 0
    var endStatusKeeper: Int = 0
    var butsrcx: Int = -1
    var butsrcy: Int = -1
    var butdesx: Int = -1
    var butdesy: Int = -1
    var buttonWeightStatus = 0


    private val gdForRedColor: GradientDrawable = GradientDrawable()
    private val gdForGreenColor: GradientDrawable = GradientDrawable()
    private val gdForBrownColor: GradientDrawable = GradientDrawable()
    private val gdForWhiteColor: GradientDrawable = GradientDrawable()
    private val gdForBlueColor: GradientDrawable = GradientDrawable()


    var v: MutableList<MutableList<MutableList<MutableList<Int>>>> = mutableListOf()
    var dis: MutableList<MutableList<Int>> = mutableListOf()
    var path: MutableList<MutableList<MutableList<MutableList<Int>>>> = mutableListOf()

    @RequiresApi(Build.VERSION_CODES.N)
    var pq: PriorityQueue<Tuple2> = PriorityQueue<Tuple2>(ComparatorTuple)
    var weight: MutableList<MutableList<Int>> = mutableListOf()
    var sized: Int = 0
    var srcx: Int = 0
    var srcy: Int = 0
    var desx: Int = -1
    var desy: Int = -1

    var vis: MutableList<MutableList<Int>> = mutableListOf()
    var dfsPath: MutableList<MutableList<Int>> = mutableListOf()

    var algoChooseValue=0

    var ywallInvalid:MutableList<Int> = mutableListOf()
    var xwallInvalid:MutableList<Int> = mutableListOf()

    var gridButtonActiveOrNot=0

    var bfsqueue:Queue<Tuple2> = LinkedList<Tuple2>()

    private val AlgoList = listOf<String>("DFS","BFS","DIJKSTRA")
    private var currentPathFindingAlgo:String = "DFS"
    private lateinit var jobDFS: Job
    private lateinit var jobBFS: Job
    private lateinit var jobDIJKSTRA: Job
    private lateinit var jobBFSSub: Job
    private lateinit var jobDFSSub: Job
    private lateinit var jobQuickSort2: Job
    private lateinit var jobQuickSort3: Job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPathFinderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.tbPathFindingVisualizer)
        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        binding.tbPathFindingVisualizer.setNavigationOnClickListener {
            onBackPressed()
        }



        binding.btnStart.setOnClickListener{
            cancelAllJobs()
            //code to display the Bottom Sheet
            val dialog = BottomSheetDialog(this)
            val bindingBottomSheet  : PathFinderBottomSheetBinding= PathFinderBottomSheetBinding.inflate(layoutInflater)
            dialog.setContentView(bindingBottomSheet.root)
            dialog.show()

            //code to send the values to the adapter
            val adapter = ArrayAdapter<String>(this,R.layout.spinner_dropdown_item,AlgoList)
            bindingBottomSheet.sortingSelector.adapter = adapter


            //Code to get the current selected sorting algorithm
            bindingBottomSheet.sortingSelector.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    currentPathFindingAlgo = parent!!.getItemAtPosition(position).toString()
                    binding.tbPathFindingVisualizer.title = currentPathFindingAlgo
                    Toast.makeText(this@PathFinder,"you selected ${parent.getItemAtPosition(position).toString()}",Toast.LENGTH_LONG).show()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
            //code when the close button is pressed
            bindingBottomSheet.btnDismiss.setOnClickListener{
                dialog.dismiss()
            }
        }

        falseJobInit()
        binding.tvClear.visibility = View.GONE
        binding.tvRedo.visibility = View.GONE
        binding.ivSort.visibility = View.GONE
        binding.tvGenerateGrid.setOnClickListener {
            binding.llIntroText.visibility = View.GONE
            gradientDrawableValueSetter()
            createButtonGrid()
            binding.tvGenerateGrid.visibility = View.GONE
            binding.tvClear.visibility = View.VISIBLE
            binding.tvRedo.visibility = View.VISIBLE
            binding.ivSort.visibility = View.VISIBLE
        }
        binding.ivSort.setOnClickListener {
            GlobalScope.launch(Dispatchers.Main) {
                chooseStartSortingAlgorithm()
            }
        }
        binding.tvClear.setOnClickListener {
            cancelAllJobs()
            deleteMainScreen()

        }
    }




    private fun falseJobInit() {
        jobDFS = GlobalScope.launch { }
        jobBFS= GlobalScope.launch {  }
        jobDIJKSTRA = GlobalScope.launch {  }
        jobBFSSub=GlobalScope.launch {  }
        jobDFSSub=GlobalScope.launch {  }
        jobQuickSort2=GlobalScope.launch {  }
        jobQuickSort3=GlobalScope.launch {  }
    }

    private fun cancelAllJobs() {
        jobDFS.cancel()
        jobBFS.cancel()
        jobDIJKSTRA.cancel()
        jobBFSSub.cancel()
        jobDFSSub.cancel()
    }
    private fun chooseStartSortingAlgorithm(){
        when(currentPathFindingAlgo){
            "DFS" -> {
                findPathDFS()
            }
            "BFS" -> {
                findPathBFS()
            }
            "DIJKSTRA" -> {
                findPathdijikstra()
            }
        }
    }

    //All algorithms
    private  fun findPathDFS() {
        jobDFS = GlobalScope.launch(Dispatchers.Main) {
            gridButtonActiveOrNot = 1
            //Basically generating a coordinate for all the points
            for (i in 0..(sizeRow)) {
                val row: MutableList<MutableList<MutableList<Int>>> = mutableListOf()
                for (j in 0..(sizeColumn)) {
                    val point: MutableList<MutableList<Int>> = mutableListOf()
                    if (i == 0 && j == 0) {
                        val neigh1: MutableList<Int> = mutableListOf()
                        neigh1.add(i + 1)
                        neigh1.add(j)

                        Log.i("Points", (i + 1).toString() + "," + (j).toString())
                        point.add(neigh1)

                        val neigh2: MutableList<Int> = mutableListOf()
                        neigh2.add(i)
                        neigh2.add(j + 1)
                        Log.i("Points", (i).toString() + "," + (j + 1).toString())
                        point.add(neigh2)

                    } else if (i == sizeRow && j == 0) {
                        val neigh1: MutableList<Int> = mutableListOf()
                        neigh1.add(i - 1)
                        neigh1.add(j)

                        point.add(neigh1)

                        val neigh2: MutableList<Int> = mutableListOf()
                        neigh2.add(i)
                        neigh2.add(j + 1)

                        point.add(neigh2)

                    } else if (i == 0 && j == sizeColumn) {
                        val neigh1: MutableList<Int> = mutableListOf()
                        neigh1.add(i + 1)
                        neigh1.add(j)

                        point.add(neigh1)

                        val neigh2: MutableList<Int> = mutableListOf()
                        neigh2.add(i)
                        neigh2.add(j - 1)

                        point.add(neigh2)

                    } else if (i == sizeRow && j == sizeColumn) {
                        val neigh1: MutableList<Int> = mutableListOf()
                        neigh1.add(i - 1)
                        neigh1.add(j)

                        point.add(neigh1)
                        val neigh2: MutableList<Int> = mutableListOf()

                        neigh2.add(i)
                        neigh2.add(j - 1)

                        point.add(neigh2)

                    } else if (i == 0) {
                        val neigh1: MutableList<Int> = mutableListOf()
                        neigh1.add(i + 1)
                        neigh1.add(j)

                        point.add(neigh1)
                        val neigh2: MutableList<Int> = mutableListOf()

                        neigh2.add(i)
                        neigh2.add(j - 1)

                        point.add(neigh2)
                        val neigh3: MutableList<Int> = mutableListOf()

                        neigh3.add(i)
                        neigh3.add(j + 1)

                        point.add(neigh3)

                    } else if (i == sizeRow) {
                        val neigh1: MutableList<Int> = mutableListOf()
                        neigh1.add(i - 1)
                        neigh1.add(j)

                        point.add(neigh1)
                        val neigh2: MutableList<Int> = mutableListOf()

                        neigh2.add(i)
                        neigh2.add(j - 1)

                        point.add(neigh2)
                        val neigh3: MutableList<Int> = mutableListOf()

                        neigh3.add(i)
                        neigh3.add(j + 1)

                        point.add(neigh3)

                    } else if (j == 0) {
                        val neigh1: MutableList<Int> = mutableListOf()
                        neigh1.add(i - 1)
                        neigh1.add(j)

                        point.add(neigh1)

                        val neigh2: MutableList<Int> = mutableListOf()
                        neigh2.add(i + 1)
                        neigh2.add(j)

                        point.add(neigh2)
                        val neigh3: MutableList<Int> = mutableListOf()

                        neigh3.add(i)
                        neigh3.add(j + 1)

                        point.add(neigh3)

                    } else if (j == sizeColumn) {
                        val neigh1: MutableList<Int> = mutableListOf()
                        neigh1.add(i - 1)
                        neigh1.add(j)

                        point.add(neigh1)
                        val neigh2: MutableList<Int> = mutableListOf()

                        neigh2.add(i + 1)
                        neigh2.add(j)

                        point.add(neigh2)
                        val neigh3: MutableList<Int> = mutableListOf()

                        neigh3.add(i)
                        neigh3.add(j - 1)

                        point.add(neigh3)

                    } else {
                        val neigh1: MutableList<Int> = mutableListOf()
                        neigh1.add(i - 1)
                        neigh1.add(j)

                        point.add(neigh1)
                        val neigh2: MutableList<Int> = mutableListOf()
                        Log.i("Points", (i - 1).toString() + "," + (j).toString())

                        neigh2.add(i + 1)
                        neigh2.add(j)
                        Log.i("Points", (i + 1).toString() + "," + (j).toString())

                        point.add(neigh2)
                        val neigh3: MutableList<Int> = mutableListOf()

                        neigh3.add(i)
                        neigh3.add(j - 1)
                        Log.i("Points", (i).toString() + "," + (j - 1).toString())
                        point.add(neigh3)
                        val neigh4: MutableList<Int> = mutableListOf()

                        neigh4.add(i)
                        neigh4.add(j + 1)
                        Log.i("Points", (i).toString() + "," + (j + 1).toString())
                        point.add(neigh4)

                    }
                    row.add(point)
                }
                v.add(row)
            }


            for (i in 0 until v.size) {
                val visvec: MutableList<Int> = mutableListOf()
                for (j in 0 until v[i].size) {

                    visvec.add(0)
                }
                vis.add(visvec)
            }

            for (i in 0..sizeRow) {
                for (j in 0..sizeColumn) {
                    if (buttonStatusKeeper[i][buttons[i][j]] == 1) {
                        vis[i][j] = 1
                    }
                }
            }


            srcx = butsrcx
            srcy = butsrcy
            desx = butdesx
            desy = butdesy
            val job2 = GlobalScope.launch(Dispatchers.Main) {
                dfs(srcx, srcy)
            }
            job2.join()

            for (i in (dfsPath.size - 2) downTo 1) {
                buttons[dfsPath[i][0]][dfsPath[i][1]].setInactiveImage(R.drawable.ic_box_green)
                buttons[dfsPath[i][0]][dfsPath[i][1]].setActiveImage(R.drawable.ic_box_green)
                buttons[dfsPath[i][0]][dfsPath[i][1]].playAnimation()
                delay(100)

            }
            if (dfsPath.size == 0) {
                Toast.makeText(this@PathFinder, "NO PATH FOUND!!", Toast.LENGTH_LONG).show()
            }
        }
    }

    private suspend fun dfs(x: Int, y: Int): Boolean {
        if (vis[x][y] == 0) {
            buttons[x][y].setInactiveImage(R.drawable.ic_box_blue)
            buttons[x][y].playAnimation()
            delay(50)
            vis[x][y] = 1
            if (x == desx) {
                if (y == desy) {
                    val point: MutableList<Int> = mutableListOf()
                    point.add(x)
                    point.add(y)
                    dfsPath.add(point)
                    return true
                }
            }
            for (i in 0 until v[x][y].size) {
                var returner_val = false
                val job1 = GlobalScope.launch(Dispatchers.Main) {
                    returner_val = dfs(v[x][y][i][0], v[x][y][i][1])
                }
                job1.join()
                if (returner_val) {
                    val point: MutableList<Int> = mutableListOf()
                    point.add(x)
                    point.add(y)
                    dfsPath.add(point)
                    return true
                }
            }
        }
        return false
    }

    private suspend fun bfs(){
        for (i in 0..(sizeRow)) {
            val row: MutableList<MutableList<MutableList<Int>>> = mutableListOf()
            for (j in 0..(sizeColumn)) {
                val point: MutableList<MutableList<Int>> = mutableListOf()
                if (i == 0 && j == 0) {
                    val neigh1: MutableList<Int> = mutableListOf()
                    neigh1.add(i + 1)
                    neigh1.add(j)

                    point.add(neigh1)

                    val neigh2: MutableList<Int> = mutableListOf()
                    neigh2.add(i)
                    neigh2.add(j + 1)

                    point.add(neigh2)

                } else if (i == sizeRow && j == 0) {
                    val neigh1: MutableList<Int> = mutableListOf()
                    neigh1.add(i - 1)
                    neigh1.add(j)

                    point.add(neigh1)

                    val neigh2: MutableList<Int> = mutableListOf()
                    neigh2.add(i)
                    neigh2.add(j + 1)

                    point.add(neigh2)

                } else if (i == 0 && j == sizeColumn) {
                    val neigh1: MutableList<Int> = mutableListOf()
                    neigh1.add(i + 1)
                    neigh1.add(j)

                    point.add(neigh1)

                    val neigh2: MutableList<Int> = mutableListOf()
                    neigh2.add(i)
                    neigh2.add(j - 1)

                    point.add(neigh2)

                } else if (i == sizeRow && j == sizeColumn) {
                    val neigh1: MutableList<Int> = mutableListOf()
                    neigh1.add(i - 1)
                    neigh1.add(j)

                    point.add(neigh1)
                    val neigh2: MutableList<Int> = mutableListOf()

                    neigh2.add(i)
                    neigh2.add(j - 1)

                    point.add(neigh2)

                } else if (i == 0) {
                    val neigh1: MutableList<Int> = mutableListOf()
                    neigh1.add(i + 1)
                    neigh1.add(j)

                    point.add(neigh1)
                    val neigh2: MutableList<Int> = mutableListOf()

                    neigh2.add(i)
                    neigh2.add(j - 1)

                    point.add(neigh2)
                    val neigh3: MutableList<Int> = mutableListOf()

                    neigh3.add(i)
                    neigh3.add(j + 1)

                    point.add(neigh3)

                } else if (i == sizeRow) {
                    val neigh1: MutableList<Int> = mutableListOf()
                    neigh1.add(i - 1)
                    neigh1.add(j)

                    point.add(neigh1)
                    val neigh2: MutableList<Int> = mutableListOf()

                    neigh2.add(i)
                    neigh2.add(j - 1)

                    point.add(neigh2)
                    val neigh3: MutableList<Int> = mutableListOf()

                    neigh3.add(i)
                    neigh3.add(j + 1)

                    point.add(neigh3)

                } else if (j == 0) {
                    val neigh1: MutableList<Int> = mutableListOf()
                    neigh1.add(i - 1)
                    neigh1.add(j)

                    point.add(neigh1)

                    val neigh2: MutableList<Int> = mutableListOf()
                    neigh2.add(i + 1)
                    neigh2.add(j)

                    point.add(neigh2)
                    val neigh3: MutableList<Int> = mutableListOf()

                    neigh3.add(i)
                    neigh3.add(j + 1)

                    point.add(neigh3)

                } else if (j == sizeColumn) {
                    val neigh1: MutableList<Int> = mutableListOf()
                    neigh1.add(i - 1)
                    neigh1.add(j)

                    point.add(neigh1)
                    val neigh2: MutableList<Int> = mutableListOf()

                    neigh2.add(i + 1)
                    neigh2.add(j)

                    point.add(neigh2)
                    val neigh3: MutableList<Int> = mutableListOf()

                    neigh3.add(i)
                    neigh3.add(j - 1)

                    point.add(neigh3)

                } else {
                    val neigh1: MutableList<Int> = mutableListOf()
                    neigh1.add(i - 1)
                    neigh1.add(j)

                    point.add(neigh1)
                    val neigh2: MutableList<Int> = mutableListOf()

                    neigh2.add(i + 1)
                    neigh2.add(j)

                    point.add(neigh2)
                    val neigh3: MutableList<Int> = mutableListOf()

                    neigh3.add(i)
                    neigh3.add(j - 1)
                    point.add(neigh3)
                    val neigh4: MutableList<Int> = mutableListOf()

                    neigh4.add(i)
                    neigh4.add(j + 1)

                    point.add(neigh4)

                }
                row.add(point)
            }
            v.add(row)
        }


        for (i in 0 until v.size) {
            val row: MutableList<MutableList<MutableList<Int>>> = mutableListOf()
            for (j in 0 until v[i].size) {
                val p: MutableList<MutableList<Int>> = mutableListOf()
                row.add(p)
            }
            path.add(row)
        }

        for (i in 0 until v.size) {
            val disvec: MutableList<Int> = mutableListOf()
            for (j in 0 until v[i].size) {

                disvec.add(500)
            }
            dis.add(disvec)
        }


        val temp = Tuple2(0, srcx, srcy)
        bfsqueue.add(temp)
        dis[srcx][srcy] = 0
        while (!bfsqueue.isEmpty()) {
            val u = bfsqueue.peek()
            bfsqueue.remove()
            val x = u.x
            val y = u.y
            //   var d = u.d
            //tester.append(x.toString()+" "+y.toString()+"\n")
            if ((x == desx) and (y == desy)) {
                break
            }
            for (i in 0 until v[x][y].size) {
                if(vis[v[x][y][i][0]][v[x][y][i][1]]==0) {
                    vis[v[x][y][i][0]][v[x][y][i][1]]=1

                    if ((v[x][y][i][0] != desx) or (v[x][y][i][1] != desy)) {

                        buttons[v[x][y][i][0]][v[x][y][i][1]].setInactiveImage(R.drawable.ic_box_blue)
                        buttons[v[x][y][i][0]][v[x][y][i][1]].playAnimation()
                        delay(50)
                    }
                    dis[v[x][y][i][0]][v[x][y][i][1]] = ((dis[x][y]) + 1)

                    path[v[x][y][i][0]][v[x][y][i][1]].removeAll(path[v[x][y][i][0]][v[x][y][i][1]])

                    path[v[x][y][i][0]][v[x][y][i][1]] =
                        mutableListOf<MutableList<Int>>().apply { addAll(path[x][y]) }
                    val tem: MutableList<Int> = mutableListOf()
                    tem.add(x)
                    tem.add(y)
                    path[v[x][y][i][0]][v[x][y][i][1]].add(tem)
                    //   var dd: Int = dis[v[x][y][i][0]][v[x][y][i][1]]
                    val xx: Int = v[x][y][i][0]
                    val yy: Int = v[x][y][i][1]
                    val temp2 = Tuple2(0, xx, yy)
                    bfsqueue.add(temp2)
                }

            }
        }
    }

    private fun findPathBFS() {
        jobBFS = GlobalScope.launch(Dispatchers.Main) {
            gridButtonActiveOrNot = 1

            srcx = butsrcx
            srcy = butsrcy
            desx = butdesx
            desy = butdesy
            for (i in 0..sizeRow) {
                val visvec: MutableList<Int> = mutableListOf()
                for (j in 0..sizeColumn) {
                    visvec.add(0)
                }
                vis.add(visvec)
            }
            for (i in 0..sizeRow) {
                for (j in 0..sizeColumn) {
                    if (buttonStatusKeeper[i][buttons[i][j]] == 1) {
                        vis[i][j] = 1
                    }
                }
            }
            jobBFSSub = GlobalScope.launch(Dispatchers.Main) {
                bfs()
            }
            jobBFSSub.join()
            val pather = path
            for (i in 1 until pather[butdesx][butdesy].size) {
                buttons[pather[butdesx][butdesy][i][0]][pather[butdesx][butdesy][i][1]].setInactiveImage(
                    R.drawable.ic_box_green
                )
                buttons[pather[butdesx][butdesy][i][0]][pather[butdesx][butdesy][i][1]].setActiveImage(
                    R.drawable.ic_box_green
                )
                buttons[pather[butdesx][butdesy][i][0]][pather[butdesx][butdesy][i][1]].playAnimation()
                delay(200)
            }
            if (pather[butdesx][butdesy].size == 0) {
                Toast.makeText(this@PathFinder, "NO PATH FOUND", Toast.LENGTH_LONG).show()
            }
        }
    }

    private suspend fun Dijkstra() {
        for (i in 0..(sizeRow)) {
            val row: MutableList<MutableList<MutableList<Int>>> = mutableListOf()
            for (j in 0..(sizeColumn)) {
                val point: MutableList<MutableList<Int>> = mutableListOf()
                if (i == 0 && j == 0) {
                    val neigh1: MutableList<Int> = mutableListOf()
                    neigh1.add(i + 1)
                    neigh1.add(j)
                    neigh1.add(weight[i + 1][j])
                    point.add(neigh1)

                    val neigh2: MutableList<Int> = mutableListOf()
                    neigh2.add(i)
                    neigh2.add(j + 1)
                    neigh2.add(weight[i][j + 1])
                    point.add(neigh2)

                } else if (i == sizeRow && j == 0) {
                    val neigh1: MutableList<Int> = mutableListOf()
                    neigh1.add(i - 1)
                    neigh1.add(j)
                    neigh1.add(weight[i - 1][j])
                    point.add(neigh1)

                    val neigh2: MutableList<Int> = mutableListOf()
                    neigh2.add(i)
                    neigh2.add(j + 1)
                    neigh2.add(weight[i][j + 1])
                    point.add(neigh2)

                } else if (i == 0 && j == sizeColumn) {
                    val neigh1: MutableList<Int> = mutableListOf()
                    neigh1.add(i + 1)
                    neigh1.add(j)
                    neigh1.add(weight[i + 1][j])
                    point.add(neigh1)

                    val neigh2: MutableList<Int> = mutableListOf()
                    neigh2.add(i)
                    neigh2.add(j - 1)
                    neigh2.add(weight[i][j - 1])
                    point.add(neigh2)

                } else if (i == sizeRow && j == sizeColumn) {
                    val neigh1: MutableList<Int> = mutableListOf()
                    neigh1.add(i - 1)
                    neigh1.add(j)
                    neigh1.add(weight[i - 1][j])
                    point.add(neigh1)
                    val neigh2: MutableList<Int> = mutableListOf()

                    neigh2.add(i)
                    neigh2.add(j - 1)
                    neigh2.add(weight[i][j - 1])
                    point.add(neigh2)

                } else if (i == 0) {
                    val neigh1: MutableList<Int> = mutableListOf()
                    neigh1.add(i + 1)
                    neigh1.add(j)
                    neigh1.add(weight[i + 1][j])
                    point.add(neigh1)
                    val neigh2: MutableList<Int> = mutableListOf()

                    neigh2.add(i)
                    neigh2.add(j - 1)
                    neigh2.add(weight[i][j - 1])
                    point.add(neigh2)
                    val neigh3: MutableList<Int> = mutableListOf()

                    neigh3.add(i)
                    neigh3.add(j + 1)
                    neigh3.add(weight[i][j + 1])
                    point.add(neigh3)

                } else if (i == sizeRow) {
                    val neigh1: MutableList<Int> = mutableListOf()
                    neigh1.add(i - 1)
                    neigh1.add(j)
                    neigh1.add(weight[i - 1][j])
                    point.add(neigh1)
                    val neigh2: MutableList<Int> = mutableListOf()

                    neigh2.add(i)
                    neigh2.add(j - 1)
                    neigh2.add(weight[i][j - 1])
                    point.add(neigh2)
                    val neigh3: MutableList<Int> = mutableListOf()

                    neigh3.add(i)
                    neigh3.add(j + 1)
                    neigh3.add(weight[i][j + 1])
                    point.add(neigh3)

                } else if (j == 0) {
                    val neigh1: MutableList<Int> = mutableListOf()
                    neigh1.add(i - 1)
                    neigh1.add(j)
                    neigh1.add(weight[i - 1][j])
                    point.add(neigh1)
                    val neigh2: MutableList<Int> = mutableListOf()
                    neigh2.add(i + 1)
                    neigh2.add(j)
                    neigh2.add(weight[i + 1][j])
                    point.add(neigh2)
                    val neigh3: MutableList<Int> = mutableListOf()

                    neigh3.add(i)
                    neigh3.add(j + 1)
                    neigh3.add(weight[i][j + 1])
                    point.add(neigh3)

                } else if (j == sizeColumn) {
                    val neigh1: MutableList<Int> = mutableListOf()
                    neigh1.add(i - 1)
                    neigh1.add(j)
                    neigh1.add(weight[i - 1][j])
                    point.add(neigh1)
                    val neigh2: MutableList<Int> = mutableListOf()

                    neigh2.add(i + 1)
                    neigh2.add(j)
                    neigh2.add(weight[i + 1][j])
                    point.add(neigh2)
                    val neigh3: MutableList<Int> = mutableListOf()

                    neigh3.add(i)
                    neigh3.add(j - 1)
                    neigh3.add(weight[i][j - 1])
                    point.add(neigh3)

                } else {
                    val neigh1: MutableList<Int> = mutableListOf()
                    neigh1.add(i - 1)
                    neigh1.add(j)
                    neigh1.add(weight[i - 1][j])
                    point.add(neigh1)
                    val neigh2: MutableList<Int> = mutableListOf()

                    neigh2.add(i + 1)
                    neigh2.add(j)
                    neigh2.add(weight[i + 1][j])
                    point.add(neigh2)
                    val neigh3: MutableList<Int> = mutableListOf()

                    neigh3.add(i)
                    neigh3.add(j - 1)
                    neigh3.add(weight[i][j - 1])
                    point.add(neigh3)
                    val neigh4: MutableList<Int> = mutableListOf()

                    neigh4.add(i)
                    neigh4.add(j + 1)
                    neigh4.add(weight[i][j + 1])
                    point.add(neigh4)

                }
                row.add(point)
            }
            v.add(row)
        }


        for (i in 0 until v.size) {
            val row: MutableList<MutableList<MutableList<Int>>> = mutableListOf()
            for (j in 0 until v[i].size) {
                val p: MutableList<MutableList<Int>> = mutableListOf()
                row.add(p)
            }
            path.add(row)
        }

        for (i in 0 until v.size) {
            val disvec: MutableList<Int> = mutableListOf()
            for (j in 0 until v[i].size) {

                disvec.add(500)
            }
            dis.add(disvec)
        }


        val temp = Tuple2(0, srcx, srcy)
        pq.add(temp)
        dis[srcx][srcy] = 0
        while (!pq.isEmpty()) {
            val u = pq.peek()
            pq.remove()
            val x = u.x
            val y = u.y

            //tester.append(x.toString()+" "+y.toString()+"\n")
            if ((x == desx) and (y == desy)) {
                break
            }
            for (i in 0 until v[x][y].size) {

                if (dis[v[x][y][i][0]][v[x][y][i][1]] > ((dis[x][y]) + (v[x][y][i][2]))) {
                    if ((v[x][y][i][0] != desx) or (v[x][y][i][1] != desy)) {
                        if (weight[v[x][y][i][0]][v[x][y][i][1]] == 1) {
                            buttons[v[x][y][i][0]][v[x][y][i][1]].setInactiveImage(R.drawable.ic_box_blue)
                            buttons[v[x][y][i][0]][v[x][y][i][1]].playAnimation()
                            delay(50)
                        }

                    }
                    dis[v[x][y][i][0]][v[x][y][i][1]] = ((dis[x][y]) + (v[x][y][i][2]))

                    path[v[x][y][i][0]][v[x][y][i][1]].removeAll(path[v[x][y][i][0]][v[x][y][i][1]])

                    path[v[x][y][i][0]][v[x][y][i][1]] =
                        mutableListOf<MutableList<Int>>().apply { addAll(path[x][y]) }
                    val tem: MutableList<Int> = mutableListOf()
                    tem.add(x)
                    tem.add(y)
                    path[v[x][y][i][0]][v[x][y][i][1]].add(tem)
                    val dd: Int = dis[v[x][y][i][0]][v[x][y][i][1]]
                    val xx: Int = v[x][y][i][0]
                    val yy: Int = v[x][y][i][1]
                    val temp2 = Tuple2(dd, xx, yy)
                    pq.add(temp2)
                }
            }
        }
    }

    @DelicateCoroutinesApi
    private fun findPathdijikstra() {
        jobDIJKSTRA = GlobalScope.launch(Dispatchers.Main) {
            gridButtonActiveOrNot = 1
            sized = sizeColumn + 1
            srcx = butsrcx
            srcy = butsrcy
            desx = butdesx
            desy = butdesy
            val job2 = GlobalScope.launch(Dispatchers.Main) {
                weightMaker()
            }
            job2.join()
            for (i in 0..sizeRow) {
                for (j in 0..sizeColumn) {
                    if (buttonStatusKeeper[i].get(buttons[i][j]) == 1) {
                        weight[i][j] = 1000
                    } else if (buttonStatusKeeper[i].get(buttons[i][j]) == 2) {
                        weight[i][j] = 5
                    }
                }
            }
            val job1 = GlobalScope.launch(Dispatchers.Main) {
                Dijkstra()
            }
            job1.join()
            val pather = path
            for (i in 1..(pather[butdesx][butdesy].size - 1)) {
                buttons[pather[butdesx][butdesy][i][0]][pather[butdesx][butdesy][i][1]].setInactiveImage(
                    R.drawable.ic_box_green
                )
                buttons[pather[butdesx][butdesy][i][0]][pather[butdesx][butdesy][i][1]].setActiveImage(
                    R.drawable.ic_box_green
                )
                buttons[pather[butdesx][butdesy][i][0]][pather[butdesx][butdesy][i][1]].playAnimation()
                delay(200)

            }
            if (pather[butdesx][butdesy].size == 0) {
                Toast.makeText(this@PathFinder, "NO PATH FOUND", Toast.LENGTH_LONG).show()
            }
        }
    }
    private fun weightMaker() {
        for (i in 0..(sizeRow)) {
            val weightvec: MutableList<Int> = mutableListOf()
            for (j in 0..(sizeColumn)) {
                weightvec.add(1)
            }
            weight.add(weightvec)
        }
    }


    //UI code
    private fun gradientDrawableValueSetter() {
        gdForRedColor.setColor(Color.parseColor("#FF0000"))
        gdForRedColor.cornerRadius = 10.0f
        gdForRedColor.setStroke(1, Color.parseColor("#000000"))

        gdForBrownColor.setColor(Color.parseColor("#A52A2A"))
        gdForBrownColor.cornerRadius = 10.0f
        gdForBrownColor.setStroke(1, Color.parseColor("#000000"))

        gdForGreenColor.setColor(Color.parseColor("#008000"))
        gdForGreenColor.cornerRadius = 10.0f
        gdForGreenColor.setStroke(1, Color.parseColor("#000000"))

        gdForWhiteColor.setColor(Color.parseColor("#FFFFFF"))
        gdForWhiteColor.cornerRadius = 10.0f
        gdForWhiteColor.setStroke(1, Color.parseColor("#000000"))

        gdForBlueColor.setColor(Color.parseColor("#0000FF"))
        gdForBlueColor.cornerRadius = 10.0f
        gdForBlueColor.setStroke(1, Color.parseColor("#000000"))
    }

    private fun createButtonGrid() {
        val screenLinearLayout = LinearLayout(this)
        screenLinearLayout.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        screenLinearLayout.orientation = LinearLayout.VERTICAL
        val screenid = resources.getIdentifier("screen", "id", packageName)
        screenLinearLayout.id = screenid
        binding.llPathFindingDisplay.addView(screenLinearLayout)
        for (i in 0..sizeRow) {

            val arrayLinearLayout = LinearLayout(this)
            arrayLinearLayout.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f
            )
            arrayLinearLayout.orientation = LinearLayout.HORIZONTAL

            val buttonStatusRow: MutableMap<SparkButton, Int> = mutableMapOf()
            val buttonRow: MutableList<SparkButton> = mutableListOf()
            for (j in 0..(sizeColumn)) {
                val sButton: SparkButton = SparkButtonBuilder(this).setImageSizeDp(30)
                    .setActiveImage(R.drawable.ic_box)
                    .setInactiveImage(R.drawable.ic_box_empty)
                    .setPrimaryColor(
                        ContextCompat.getColor(
                            this,
                            android.R.color.holo_blue_dark
                        )
                    )
                    .setSecondaryColor(
                        ContextCompat.getColor(
                            this,
                            android.R.color.holo_purple
                        )
                    )
                    .build()
                sButton.layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    1.0f
                )
                sButton.setEventListener(object : SparkEventListener {
                    override fun onEventAnimationEnd(button: ImageView?, buttonState: Boolean) {

                    }

                    override fun onEvent(button: ImageView?, buttonState: Boolean) {
                        if (startStatusKeeper == 0) {
                            sButton.setActiveImage(R.drawable.ic_box_start)
                            sButton.isClickable = false
                            sButton.setInactiveImage(R.drawable.ic_box_start)
                            startStatusKeeper = 1
                            buttonStatusRow[sButton] = 3
                            Log.i(
                                "buttonstatus",
                                "BUTTON " + i.toString() + " " + j.toString() + "=" + buttonStatusRow.get(
                                    sButton
                                ).toString()
                            )
                            butsrcx = i
                            butsrcy = j

                        } else if (endStatusKeeper == 0) {
                            sButton.setActiveImage(R.drawable.ic_baseline_gps_fixed_24)
                            sButton.isClickable = false
                            sButton.setInactiveImage(R.drawable.ic_baseline_gps_fixed_24)
                            endStatusKeeper = 1
                            buttonStatusRow[sButton] = 3
                            Log.i(
                                "buttonstatus",
                                "BUTTON " + i.toString() + " " + j.toString() + "=" + buttonStatusRow.get(
                                    sButton
                                ).toString()
                            )
                            butdesx = i
                            butdesy = j
                        } else {
                            if (buttonStatusRow[sButton] != 3) {
                                if (buttonWeightStatus == 0) {
                                    sButton.setActiveImage(R.drawable.ic_box)
                                    when (buttonStatusRow[sButton]) {
                                        0 -> {
                                            buttonStatusRow[sButton] = 1
                                            Log.i(
                                                "buttonstatus",
                                                "BUTTON " + i.toString() + " " + j.toString() + "=" + buttonStatusRow.get(
                                                    sButton
                                                ).toString()
                                            )
                                        }
                                        1 -> {
                                            buttonStatusRow[sButton] = 0
                                            Log.i(
                                                "buttonstatus",
                                                "BUTTON " + i.toString() + " " + j.toString() + "=" + buttonStatusRow.get(
                                                    sButton
                                                ).toString()
                                            )
                                        }
                                        else -> {
                                            buttonStatusRow[sButton] = 0
                                            Log.i(
                                                "buttonstatus",
                                                "BUTTON " + i.toString() + " " + j.toString() + "=" + buttonStatusRow.get(
                                                    sButton
                                                ).toString()
                                            )
                                        }
                                    }
                                } else {
                                    sButton.setActiveImage(R.drawable.ic_gymnastic)
                                    val buttonStatus = buttonStatusRow.get(sButton)
                                    when (buttonStatus) {
                                        0 -> {
                                            buttonStatusRow[sButton] = 2
                                            Log.i(
                                                "buttonstatus",
                                                "BUTTON " + i.toString() + " " + j.toString() + "=" + buttonStatusRow.get(
                                                    sButton
                                                ).toString()
                                            )
                                        }
                                        2 -> {
                                            buttonStatusRow.put(sButton, 0)
                                            Log.i(
                                                "buttonstatus",
                                                "BUTTON " + i.toString() + " " + j.toString() + "=" + buttonStatusRow.get(
                                                    sButton
                                                ).toString()
                                            )
                                        }
                                        else -> {
                                            buttonStatusRow[sButton] = 0
                                            Log.i(
                                                "buttonstatus",
                                                "BUTTON " + i.toString() + " " + j.toString() + "=" + buttonStatusRow.get(
                                                    sButton
                                                ).toString()
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                    override fun onEventAnimationStart(
                        button: ImageView?,
                        buttonState: Boolean
                    ) {

                    }
                })
                buttonStatusRow[sButton] = 0
                buttonRow.add(sButton)
                arrayLinearLayout.addView(sButton)
            }
            buttonStatusKeeper.add(buttonStatusRow)
            buttons.add(buttonRow)
            screenLinearLayout.addView(arrayLinearLayout)
        }
    }
    private fun deleteMainScreen() {
        val screenid = resources.getIdentifier("screen", "id", packageName)
        val screen = findViewById<LinearLayout>(screenid)
        (screen.parent as ViewGroup).removeView(screen)
        buttons.removeAll(buttons)
        buttonStatusKeeper.removeAll(buttonStatusKeeper)
        startStatusKeeper = 0
        endStatusKeeper = 0
        butsrcx = -1
        butsrcy = -1
        butdesx = -1
        butdesy = -1
        buttonWeightStatus = 0
        v.removeAll(v)
        dis.removeAll(dis)
        path.removeAll(path)
        weight.removeAll(weight)
        sized = 0
        srcx = 0
        srcy = 0
        desx = -1
        desy = -1
        createButtonGrid()
        vis.removeAll(vis)
        dfsPath.removeAll(dfsPath)
        pq.removeAll(pq)
        bfsqueue.removeAll(bfsqueue)


    }

}
