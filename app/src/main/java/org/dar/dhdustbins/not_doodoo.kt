package org.dar.dhdustbins

import android.Manifest
import android.animation.Animator
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.os.*
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.airbnb.lottie.LottieAnimationView
import com.google.gson.annotations.SerializedName
import retrofit.http.*;

class not_doodoo : Fragment() {
    lateinit var lottie : LottieAnimationView;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val model : intro_done by activityViewModels()
        val v = inflater.inflate(R.layout.fragment_not_doodoo, container, false)
        v.findViewById<LottieAnimationView>(R.id.lottie).setOnLongClickListener(object:View.OnLongClickListener {
            override fun onLongClick(v: View?): Boolean{
                var i = Intent(context,rickrolled::class.java)
               // startActivity(i)
                return true
            }
        })
        val textview = v.findViewById<TextView>(R.id.title)
        val h2 = @SuppressLint("HandlerLeak")
        object : Handler()
        {
            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)
                if(Build.VERSION.SDK_INT>Build.VERSION_CODES.M) {
                    lottie.pauseAnimation();
                }
                model.set_data(200)
            }
        }
        var th1 = object :CountDownTimer(400,1)
        {
            override fun onTick(millisUntilFinished: Long) {

            }
            override fun onFinish() {
                h2.sendEmptyMessage(0)
            }
        }
        val h = @SuppressLint("HandlerLeak")
        object : Handler()
        {
            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)
                var char = msg.data.getString("next")
                if(char.equals("@",true)) {
                    if(Build.VERSION.SDK_INT>Build.VERSION_CODES.M) {
                        lottie.visibility = View.VISIBLE
                        lottie.playAnimation()
                    }
                    th1.start();
                }
                else
                    textview.text = textview.text.toString() + char
            }
        }
        val t : Thread = Thread(Runnable {
            kotlin.run {
                var arr = arrayOf("D","H"," ","D","u","s","t","b","i","n","s")
                for(i in arr) {
                    var m = Message();
                    var b = Bundle()
                    b.putString("next",i)
                    m.data = b
                    h.sendMessage(m)
                    Thread.sleep(200)
                }
                var m = Message();
                var b = Bundle()
                b.putString("next","@")
                m.data = b
                h.sendMessage(m)
            }
        })
        t.start()
        lottie = v.findViewById<LottieAnimationView>(R.id.pencil);
        lottie.playAnimation()
        return v
    }
}