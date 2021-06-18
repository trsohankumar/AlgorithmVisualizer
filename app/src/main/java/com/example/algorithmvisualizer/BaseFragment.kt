package com.example.algorithmvisualizer

import android.os.Bundle
import androidx.fragment.app.Fragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext
//using a basefragment inheriting fragment for implementing coroutines asynchronously //implemented this bcoz asynctask was deprecated
abstract class BaseFragment:Fragment(),CoroutineScope {

    private lateinit var job:Job

    override val coroutineContext: CoroutineContext
        get() = job+Dispatchers.Main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        job=Job()

    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}