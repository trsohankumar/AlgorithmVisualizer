package com.example.algorithmvisualizer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.algorithmvisualizer.databinding.ActivityInformationSpecificBinding

class InformationSpecific : AppCompatActivity() {
    private lateinit var binding:ActivityInformationSpecificBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding= ActivityInformationSpecificBinding.inflate(layoutInflater)
        val view=binding.root
        setContentView(view)

        val props=intent.getStringExtra("EXTRA_PROPS")
        if(props=="dijkstra")
            binding.textView.text="Djikstra's"
        if(props=="greedy")
            binding.textView.text="Greedy Best"
        if(props=="astar")
            binding.textView.text="A * Search"
        if(props=="bfs")
            binding.textView.text="BFS"
        if(props=="dfs")
            binding.textView.text="DFS"
        if(props=="bubblesort")
            binding.textView.text="Bubble Sort"
        if(props=="quicksort")
            binding.textView.text="Quick Sort"
        if(props=="selectionsort")
            binding.textView.text="Selection Sort"
        if(props=="radixsort")
            binding.textView.text="Radix Sort"
        if(props=="insertionsort")
            binding.textView.text="Insertion Sort"
        if(props=="heapsort")
            binding.textView.text="Heap Sort"
        if(props=="mergesort")
            binding.textView.text="Merge Sort"


    }
}