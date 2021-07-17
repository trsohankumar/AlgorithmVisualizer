package com.example.algorithmvisualizer

import android.content.Context
import android.widget.Toast
//just a helpers file for not having too much code for toast messages
fun Context.toast(message:String)= Toast.makeText(this,message,Toast.LENGTH_LONG).show()

class Tuple2(var d:Int,var x:Int,var y:Int){
    fun getx():Int{
        return x
    }
    fun gety():Int{
        return y
    }
    fun getd():Int{
        return d
    }
}
class ComparatorTuple {

    companion object : Comparator<Tuple2> {

        override fun compare(a: Tuple2, b: Tuple2): Int = when {
            a.d != b.d -> a.d - b.d
            else -> 1
        }
    }
}