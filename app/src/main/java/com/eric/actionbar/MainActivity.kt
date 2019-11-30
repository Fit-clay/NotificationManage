package com.eric.actionbar

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    lateinit var mContext:Context;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mContext=this
        findViewById<TextView>(R.id.tv_msg).setOnClickListener {
            NotificationManage().createNotification(mContext)
        }

        findViewById<TextView>(R.id.tv_custom2).setOnClickListener {
            NotificationManage().createCustomNotification(mContext)

        }


    }

}
