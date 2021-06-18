package com.example.algorithmvisualizer

import android.content.Context
import android.widget.Toast
//just a helpers file for not having too much code for toast messages
fun Context.toast(message:String)= Toast.makeText(this,message,Toast.LENGTH_LONG).show()