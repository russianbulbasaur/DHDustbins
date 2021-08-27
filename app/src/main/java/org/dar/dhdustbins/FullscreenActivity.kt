package org.dar.dhdustbins

import androidx.appcompat.app.AppCompatActivity
import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.google.gson.annotations.SerializedName
import retrofit.Callback
import retrofit.http.Field
import retrofit.http.FormUrlEncoded
import retrofit.http.GET
import retrofit.http.POST
import java.security.Permission
import java.security.Permissions
import java.util.jar.Manifest

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class FullscreenActivity : AppCompatActivity() {


    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fullscreen)
        if(checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION)==PackageManager.PERMISSION_DENIED||checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_DENIED)
        {
            val arr = arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION,android.Manifest.permission.ACCESS_FINE_LOCATION)
            requestPermissions(arr,2)
        }
        fuckingoff()
    }
fun fuckingoff()
{
    window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
    supportActionBar?.hide()
    val fragmanager:FragmentManager = supportFragmentManager
    val model_class : intro_done by viewModels()
    model_class.d.observe(this, Observer {
        if(it==200)
            fragmanager.beginTransaction().remove(not_doodoo()).replace(R.id.nav_frag,doodoo()).commit()
    })
}
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==2) {
            if (checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_DENIED || checkSelfPermission(
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_DENIED
            ) {
                val arr = arrayOf(
                    android.Manifest.permission.ACCESS_COARSE_LOCATION,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                )
                requestPermissions(arr, 2)
            }
        }
    }
    override fun onBackPressed() {
        finish()
    }
}
data class dustbin(var c: Context)
{
    @SerializedName("lat")
    val lat : Double = 0.0

    @SerializedName("lon")
    val longi:Double = 0.0
}
interface retrointerface
{
    @GET("/doodoo_get.php")
    fun get_dustbins(c:Callback<List<dustbin>>)
}
class get_dustbins
{
    fun get():retrointerface
    {
        val a  = retrofit.RestAdapter.Builder().setEndpoint("https://respiktbot.000webhostapp.com/doodoo").build()
        val o = a.create(retrointerface::class.java)
        return o
    }
}
class intro_done : ViewModel()
{
    var d = MutableLiveData<Int>()
    fun set_data(a:Int)
    {
        d.value = a
    }
}