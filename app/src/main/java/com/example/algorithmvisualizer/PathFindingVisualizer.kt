package com.example.algorithmvisualizer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.algorithmvisualizer.databinding.ActivityPathFindingVisualizerBinding
import com.example.algorithmvisualizer.databinding.PathFinderBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialog

class PathFindingVisualizer : AppCompatActivity() {
    private lateinit var binding:ActivityPathFindingVisualizerBinding
    private val AlgorithmList = listOf<String>("DFS","BFS","DIJKSTRA")
    private var currentPathFindingAlgo:String = "DFS"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPathFindingVisualizerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.tbPathFindingVisualizer)
        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        binding.tbPathFindingVisualizer.setNavigationOnClickListener {
            onBackPressed()
        }

        binding.btnStart.setOnClickListener{
            //cancelAllJobs()
            //code to display the Bottom Sheet
            val dialog = BottomSheetDialog(this)
            val bindingBottomSheet  : PathFinderBottomSheetBinding = PathFinderBottomSheetBinding.inflate(layoutInflater)
            dialog.setContentView(bindingBottomSheet.root)
            dialog.show()

            //code to send the values to the adapter
            val adapter = ArrayAdapter<String>(this,R.layout.spinner_dropdown_item,AlgorithmList)
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
                    when(currentPathFindingAlgo){

                        "DFS" -> {
                            bindingBottomSheet.tvTimeComplexity.text = "O(V+E)"
                        }
                        "BFS" -> {
                            bindingBottomSheet.tvTimeComplexity.text = "O(V+E)"
                        }
                        "DIJKSTRA" -> {
                            bindingBottomSheet.tvTimeComplexity.text = "O(V^2)"
                        }
                    }
                    Toast.makeText(this@PathFindingVisualizer,"you selected ${parent.getItemAtPosition(position).toString()}",
                        Toast.LENGTH_LONG).show()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
            //code when the close button is pressed
            bindingBottomSheet.btnDismiss.setOnClickListener{
                dialog.dismiss()
            }
        }
        binding.tvClear.visibility = View.GONE
        binding.tvRedo.visibility = View.GONE
        binding.ivSort.visibility = View.GONE
        binding.llPathFindingDisplay.visibility = View.GONE
        binding.tvGenerateGrid.setOnClickListener {
            binding.llIntroText.visibility = View.GONE
            binding.tvGenerateGrid.visibility = View.GONE
            binding.tvClear.visibility = View.VISIBLE
            binding.tvRedo.visibility = View.VISIBLE
            binding.ivSort.visibility = View.VISIBLE
            binding.llPathFindingDisplay.visibility = View.VISIBLE
        }
        binding.tvClear.setOnClickListener {
            binding.tvClear.visibility = View.GONE
            binding.tvRedo.visibility = View.GONE
            binding.ivSort.visibility = View.GONE
            binding.llPathFindingDisplay.visibility = View.GONE
            binding.llIntroText.visibility = View.VISIBLE
            binding.tvGenerateGrid.visibility = View.VISIBLE
        }
    }
}