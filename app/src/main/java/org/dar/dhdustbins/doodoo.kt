package org.dar.dhdustbins

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Service
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.*
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.location.LocationProvider
import android.os.Build
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.airbnb.lottie.LottieAnimationView
import com.google.android.gms.maps.model.*
import com.google.android.material.bottomsheet.BottomSheetDialog
import org.json.JSONObject
import retrofit.Callback
import retrofit.RetrofitError
import retrofit.client.Response

class doodoo : Fragment() {
    companion object
    {
        var DONE : Int = 0;
    }
    val dustbinmodel : dustbinmodel by activityViewModels()
    lateinit var alertDialog:AlertDialog
    lateinit var bottom:BottomSheetDialog
    @SuppressLint("MissingPermission")
    private val callback = OnMapReadyCallback { googleMap ->
        var l = 0;
        lateinit var mark:Marker;
        googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(context,R.raw.green))
        val loc : LocationManager = activity?.getSystemService(Service.LOCATION_SERVICE) as LocationManager
            loc.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                10000,
                10f,
                object : LocationListener {
                    override fun onLocationChanged(location: Location) {
                        if(l==0) {
                            l = 1;
                            val wut = LatLng(location.latitude, location.longitude)
                            mark = googleMap.addMarker(
                                MarkerOptions().position(wut).title("You").icon(getMarker(0))
                            ) as Marker
                            mark.tag = "you"
                            dustbinmodel.data.observe(activity!!, Observer {
                                DONE = 1;
                                alertDialog.cancel()
                                alertDialog.dismiss()
                                googleMap.animateCamera(
                                    CameraUpdateFactory.newLatLngZoom(
                                        wut,
                                        14.4f
                                    )
                                )
                                var number = 0;
                                for (dustbin in it) {
                                    var l = LatLng(dustbin.lat, dustbin.longi)
                                    val templocation: Location =
                                        Location(LocationManager.GPS_PROVIDER)
                                    templocation.latitude = l.latitude
                                    templocation.longitude = l.longitude
                                    var dis = location.distanceTo(templocation) / 1000
                                    if (dis < 6) {
                                        number = number + 1
                                        googleMap.addMarker(
                                            MarkerOptions().position(l).title("Dustbin")
                                                .icon(getMarker(1))
                                        )
                                    }
                                }
                                val v = layoutInflater.inflate(R.layout.reaction, null);
                                v.findViewById<Button>(R.id.button)
                                    .setOnClickListener(object : View.OnClickListener {
                                        override fun onClick(v: View?) {
                                            bottom.dismiss()
                                        }
                                    })
                                bottom = BottomSheetDialog(context as Context)
                                bottom.setContentView(v)
                                bottom.show()
                                val anim = v.findViewById<LottieAnimationView>(R.id.reaction)
                                val text = v.findViewById<TextView>(R.id.reactiontext)
                                if (number == 0) {
                                    anim.setAnimation("oops.json")
                                    text.text = "No Dustbins Found!"
                                } else {
                                    anim.setAnimation("success.json")
                                    text.text = number.toString() + " Dustbins found!"
                                }
                            })
                            load_fucking_data()
                        }
                        else
                        {
                            var newlatlng = LatLng(location.latitude,location.longitude)
                            mark.position = newlatlng
                        }
                    }

                    override fun onProviderDisabled(provider: String) {
                        var al = androidx.appcompat.app.AlertDialog.Builder(context!!)
                        al.setCancelable(false)
                        al.setMessage("Turn on GPS")
                        al.setPositiveButton("OK",object: DialogInterface.OnClickListener{
                            override fun onClick(dialog: DialogInterface?, which: Int) {
                                var i = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                                startActivity(i)
                            }
                        })
                        al.show()
                    }

                    override fun onProviderEnabled(provider: String) {

                    }
                })
    }
    fun load_fucking_data()
    {
            var ob = get_dustbins()
            ob.get().get_dustbins(object : Callback<List<dustbin>> {
                override fun success(t: List<dustbin>?, response: Response?) {
                   dustbinmodel.on_done_load(t as List<dustbin>)
                }

                override fun failure(error: RetrofitError?) {
                    Toast.makeText(
                        context,
                        "Network Error" + error!!.localizedMessage.toString(),
                        Toast.LENGTH_LONG
                    ).show();
                    var ha = @SuppressLint("HandlerLeak")
                    object : Handler() {
                        override fun handleMessage(msg: Message) {
                            super.handleMessage(msg)
                            load_fucking_data();
                        }
                    }
                    Thread(Runnable {
                        kotlin.run {
                            Thread.sleep(4000)
                            ha.sendEmptyMessage(0)
                        }
                    }).start()
                }
            })
    }
    fun getMarker(a:Int):BitmapDescriptor
    {
        var icon = 0;
        icon = if (a==0) R.drawable.ic_baseline_emoji_people_24 else R.drawable.ic_baseline_delete_24
        var custom = ContextCompat.getDrawable(context as Context,icon)
        custom!!.setBounds(0,0,custom.intrinsicWidth,custom.intrinsicHeight)
        var returnbit = Bitmap.createBitmap(custom.intrinsicWidth,custom.intrinsicHeight,Bitmap.Config.ARGB_8888);
        var can = Canvas(returnbit)
        custom.draw(can)
        return BitmapDescriptorFactory.fromBitmap(returnbit)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ):View {
        val alertb = AlertDialog.Builder(context)
        this.alertDialog = alertb.create()
        this.alertDialog.setView(layoutInflater.inflate(R.layout.loading_shit,null))
        this.alertDialog.setCancelable(false)
        if(Build.VERSION.SDK_INT<= Build.VERSION_CODES.M) {
            var al = androidx.appcompat.app.AlertDialog.Builder(context as Context)
            al.setCancelable(false)
            al.setMessage("Your android version might experience a bit reduced visuals and animations.")
            al.setPositiveButton("OK",object:DialogInterface.OnClickListener{
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    dialog!!.cancel()
                    dialog.dismiss()
                    if(DONE==0)
                        alertDialog.show()
                }
            })
            al.show()
        }
        else {
            if(DONE==0)
                alertDialog.show()
        }
        return inflater.inflate(R.layout.fragment_doodoo, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }
}
class dustbinmodel : ViewModel()
{
    var data = MutableLiveData<List<dustbin>>()
    fun on_done_load(s:List<dustbin>)
    {
        data.value = s
    }
}