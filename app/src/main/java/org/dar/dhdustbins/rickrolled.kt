package org.dar.dhdustbins

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import com.google.gson.annotations.SerializedName
import retrofit.RestAdapter
import retrofit.http.*
import retrofit.*;
import retrofit.client.Response

class rickrolled : Activity() {
    lateinit var location:Location
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rickrolled)
        setTheme(R.style.Theme_AppCompat)
        if(checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION)== PackageManager.PERMISSION_DENIED||checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_DENIED)
        {
            val arr = arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION,android.Manifest.permission.ACCESS_FINE_LOCATION)
            requestPermissions(arr,2)
        }
        else
        {
            fuckoff()
        }
    }
    @SuppressLint("MissingPermission")
    fun fuckoff()
    {
        var p = ProgressDialog(this)
        p.setCancelable(false)
        p.setMessage("Please Wait")
        p.show()
        var ad = AlertDialog.Builder(this)
        var ad2 = AlertDialog.Builder(this)
        var h = @SuppressLint("HandlerLeak")
        object: Handler()
        {
            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)
                p.cancel()
                p.dismiss()
                ad.setMessage("Add Dustbin?")
                ad.setCancelable(false)
                ad.setPositiveButton("Yes",object:DialogInterface.OnClickListener
                {
                    override fun onClick(dialog: DialogInterface?, which: Int) {
                            val ob = doodoo_pre_add()
                            ob.send().give(
                                location.latitude.toString(),
                                location.longitude.toString(),
                                object :
                                    retrofit.Callback<ok> {
                                    override fun success(t: ok?, response: Response?) {
                                        if (t!!.result.isNotEmpty() && t!!.result.toString()
                                                .equals("200", true)
                                        ) {
                                            dialog!!.dismiss()
                                            dialog.cancel()
                                            ad2.setMessage("Done")
                                            ad2.setCancelable(false)
                                            ad2.setPositiveButton("OK",
                                                object : DialogInterface.OnClickListener {
                                                    override fun onClick(
                                                        dialog: DialogInterface?,
                                                        which: Int
                                                    ) {
                                                        finish()
                                                    }
                                                })
                                            ad2.show()
                                        }
                                    }

                                    override fun failure(error: RetrofitError?) {
                                        Toast.makeText(
                                            applicationContext,
                                            error!!.message.toString(),
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }
                                })
                    }
                })
                ad.setNegativeButton("No",object:DialogInterface.OnClickListener
                {
                    override fun onClick(dialog: DialogInterface?, which: Int){
                        dialog!!.dismiss()
                        finish()
                    }
                })
                ad.show()
            }
        }
        var lm = getSystemService(LOCATION_SERVICE) as LocationManager
        var k = 0
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,10000,10f, object:LocationListener {
            override fun onLocationChanged(locatio: Location) {
                if(k==0) {
                    location = locatio
                    h.sendEmptyMessage(0)
                    k=1
                }
            }

            override fun onProviderDisabled(provider: String) {
                Toast.makeText(applicationContext,"Location Error",Toast.LENGTH_LONG).show()
            }

            override fun onProviderEnabled(provider: String) {
                Toast.makeText(applicationContext,"Location Error",Toast.LENGTH_LONG).show()
            }
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
            else
            {
                fuckoff()
            }
        }
    }
}
interface doodoo_add
{
    @FormUrlEncoded
    @POST("/doodoo_give.php")
    fun give(@Field("lat")lat:String,
    @Field("lon")lon:String,c:Callback<ok>)
}
class doodoo_pre_add
{
    fun send():doodoo_add
    {
        val ad = RestAdapter.Builder().setEndpoint("https://respiktbot.000webhostapp.com/doodoo").build()
        val inter = ad.create(doodoo_add::class.java);
        return inter
    }
}data class ok(var c:Context)
{
    @SerializedName("result")
    public var result:String = ""
}