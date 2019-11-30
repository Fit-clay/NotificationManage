package com.eric.actionbar

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.v4.app.NotificationManagerCompat
import android.content.Context.NOTIFICATION_SERVICE
import android.graphics.BitmapFactory
import android.net.Uri
import android.support.annotation.RequiresApi
import android.support.v4.content.ContextCompat.getSystemService
import android.support.v4.app.NotificationCompat
import android.view.LayoutInflater
import android.view.View
import android.widget.RemoteViews
import java.util.*


class NotificationManage {

    fun  createNotification(mContext:Context){
        var soundPath = "android.resource://" + mContext.packageName + "/" + R.raw.ball  //声音文件地址

          var jumpIn : Intent = Intent(mContext,MainActivity::class.java)
          /**
           * 上下文对象
           * 请求码
           * Intent 对象
           * PandingIntent 使用方式
           * FLAG_ONE_SHOT  获取的PendingIntent只能使用一次
           * FLAG_NO_CREAT获取PendingIntent，如果被描述的PendingIntent不存在,那么简单地返回null，而不是创建它
           * FLAG_CANCEL_CURRENT:  如果描述的PendingIntent已经存在，则在使用新的Intent时之前会先取消掉当前的，之后再生成新的Intent进行展示
           * FLAG_UPDATE_CURRENT   如果被描述的PendingIntent已经存在,那么继续保持它，但它其中的数据会因为新Intent而更新
           */
          var intentP : PendingIntent = PendingIntent.getActivity(mContext,1102, jumpIn,PendingIntent.FLAG_UPDATE_CURRENT)

          /**
           * Intent和PendingIntent的区别： 【以备面试之需】
          Intent是立即使用的，而PendingIntent可以等到事件发生后触发，PendingIntent可以 cancel ；
          Intent在程序结束后即终止，而PendingIntent在程序结束后 依然有效 ；
          PendingIntent自带Context，而Intent需要在某个 Context 内运行；
          Intent在原task中运行，PendingIntent在新的 task 中运行。
           */


        /* 4.1之前使用  在高SDK版本中， setLatestEventInfo已被弃用，并且现在九成九Android用户的系统都在4.4以上了，所以这种情况就不需要考虑了。
          var notifi : Notification= Notification(R.mipmap.ic_launcher,"test",System.currentTimeMillis())
          notifi.LatestEventInfo(this, "hello", "world", intentP)
          val nmanger = mContext.getSystemService(NOTIFICATION_SERVICE) as NotificationManager?
          nmanger!!.notify(0, notifi)*/

         // val builder = Notification.Builder(mContext)
          /**
           * Google后来推出了NotificationCompat.Builder方式，为各种配置做兼容性处理。
           *所以Notification.Builder已经被NotificationCompat.Builder替代。
           */
          //建议使用
       /*   val builder = NotificationCompat.Builder(mContext,"id")
          builder.setContentTitle("测试数据1") // 设置标题
          builder.setContentText("啦啦啦啦啦啦啦")  //设置内容
          builder.setContentIntent(intentP)  //设置Intent对象
          builder.setSmallIcon(R.mipmap.icon) //设置小图标*/
          // （PS setSmallIcon 是必须的 如果不设置会报  IllegalArgumentException 异常）

          val builder= getChannelNotification(mContext,"ida","ces111","allallalalla",intentP)

          val mNotificationManager = mContext.getSystemService(NOTIFICATION_SERVICE) as NotificationManager

          if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {//8.0适配
              mNotificationManager.deleteNotificationChannel(mContext.packageName)

              val channel = NotificationChannel("id1", "System", NotificationManager.IMPORTANCE_HIGH)
              channel.enableLights(true)
              mNotificationManager.createNotificationChannel(channel)
              builder.setChannelId("id1")
          }
          //获取通知管理器对象
//          mNotificationManager.notify((Math.random() * 100).toInt(), builder.build())
          //notify  Id相同的notification只会存在一条，当已存在时，会先取消，刷新数据后重新展示
          mNotificationManager.notify(11, builder.build())
      }
     fun createCustomNotification(mContext: Context){
         var soundPath = "android.resource://" + mContext.packageName + "/" + R.raw.ball  //声音文件地址

         var jumpIn : Intent = Intent(mContext,MainActivity::class.java)
         var intentP : PendingIntent = PendingIntent.getActivity(mContext,1102, jumpIn,PendingIntent.FLAG_UPDATE_CURRENT)

         var mCoustomNotifi=NotificationCompat.Builder(mContext,"custom")
         var view=RemoteViews(mContext.packageName,R.layout.view_notification)

         //基本设置
         mCoustomNotifi.setContent(view)
         mCoustomNotifi.setSmallIcon(R.mipmap.icon)
         mCoustomNotifi.setContentIntent(intentP)

         //设置提示音
         //调用系统默认铃声
        //notify.defaults = Notification.DEFAULT_SOUND;
         //使用自己提供的声音  可设置资源文件和Sd卡文件
         mCoustomNotifi.setSound(Uri.parse(soundPath))
         //设置三色灯提现
         mCoustomNotifi .setLights(0xff0000ff.toInt(), 300, 0)
         //设置震动
         mCoustomNotifi.build().vibrate = longArrayOf(0, 300, 500, 700)

         //提示音和三色灯以及震动效果也可使用flags设置
        /*
          1）只有在设置了标志符Flags为Notification.FLAG_SHOW_LIGHTS的时候，才支持三色灯提醒。
          2）这边的颜色跟设备有关，不是所有的颜色都可以，要看具体设备。
        */
         //弹出提示框文案
         mCoustomNotifi.setTicker("您有一条新消息")

         //设置优先级 (PS 意义不大)
         mCoustomNotifi.priority=NotificationCompat.PRIORITY_HIGH
         //是否可以滑动删除
         mCoustomNotifi.setOngoing(false)
        //点击后自动删除   设置为FLAG_AUTO_CANCEL同理
         mCoustomNotifi.setAutoCancel(true)
         //以上设置也可设置flags值完成

        /*常用的flag值有
         public static final int FLAG_SHOW_LIGHTS        = 0x00000001;//控制闪光
         public static final int FLAG_ONGOING_EVENT      = 0x00000002;//将flag设置为这个属性那么通知就会像QQ一样一直在状态栏显示
         public static final int FLAG_INSISTENT          = 0x00000004; //重复发出声音，直到用户响应此通知
         public static final int FLAG_ONLY_ALERT_ONCE    = 0x00000008;//标记声音或者震动一次
         public static final int FLAG_AUTO_CANCEL        = 0x00000010; //在通知栏上点击此通知后自动清除此通知
         public static final int FLAG_NO_CLEAR           = 0x00000020;//将flag设置为这个属性那么通知栏的那个清楚按钮就不会出现
         public static final int FLAG_FOREGROUND_SERVICE = 0x00000040;//前台服务标记
         public static final int FLAG_HIGH_PRIORITY = 0x00000080;            //最高优先级

         使用方式    mNotification.flags=Notification.FLAG_AUTO_CANCEL| Notification.FLAG_NO_CLEAR...
         */

            //取消notification 共5种方式
               /*  1. 点击通知栏的清除按钮，会清除所有可清除的通知
                 2. 设置了 setAutoCancel() 或 FLAG_AUTO_CANCEL 的通知，点击该通知时会清除它
                 3. 通过 NotificationManager 调用 cancel(int id) 方法清除指定 ID 的通知
                 4. 通过 NotificationManager 调用 cancel(String tag, int id) 方法清除指定 TAG 和 ID 的通知
                 5. 通过 NotificationManager 调用 cancelAll() 方法清除所有该应用之前发送的通知*/
         val mNotificationManager = mContext.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {//8.0适配
             mNotificationManager.deleteNotificationChannel(mContext.packageName)
             val channel = NotificationChannel("iasd1", "System", NotificationManager.IMPORTANCE_HIGH)
             channel.enableLights(true)
             channel.setSound(Uri.parse(soundPath), Notification.AUDIO_ATTRIBUTES_DEFAULT)

             mNotificationManager.createNotificationChannel(channel)
             mCoustomNotifi.setChannelId("iasd1")
         }
         //获取通知管理器对象
         mNotificationManager.notify((Math.random() * 100).toInt(), mCoustomNotifi.build())
    }
    private fun getChannelNotification(mCon: Context, id: String, title: String, message: String, intent: PendingIntent): NotificationCompat.Builder {
        return NotificationCompat.Builder(mCon, id).setLargeIcon(BitmapFactory.decodeResource(mCon.resources, R.mipmap.icon)).setSmallIcon(R.mipmap.icon).setContentIntent(intent).setContentTitle(title).setContentText(message).setAutoCancel(true).setShowWhen(true).setVisibility(Notification.VISIBILITY_PUBLIC).setPriority(NotificationCompat.PRIORITY_HIGH)
    }

    /**
    * 判断是否获取到通知栏权限
     */
    fun isPermissionOpen(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManagerCompat.from(context).importance != NotificationManager.IMPORTANCE_NONE
        } else NotificationManagerCompat.from(context).areNotificationsEnabled()
    }

}