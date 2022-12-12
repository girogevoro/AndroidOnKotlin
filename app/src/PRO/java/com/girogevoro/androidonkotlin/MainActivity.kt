package com.girogevoro.androidonkotlin

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.LinearLayout.VERTICAL
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.girogevoro.androidonkotlin.databinding.ActivityMainBinding
import com.girogevoro.androidonkotlin.domain.City
import com.girogevoro.androidonkotlin.domain.ConnectivityBroadcastReceiver
import com.girogevoro.androidonkotlin.domain.Weather
import com.girogevoro.androidonkotlin.view.contacts.ContactsFragment
import com.girogevoro.androidonkotlin.view.history.HistoryFragment
import com.girogevoro.androidonkotlin.view.weatherdetails.DetailsFragment
import com.girogevoro.androidonkotlin.view.weatherlist.WeatherListFragment
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import java.io.IOException

private val receiver = ConnectivityBroadcastReceiver()
private const val REFRESH_PERIOD = 60000L
private const val MINIMAL_DISTANCE = 100f


val CHANNEL_HIGH_ID = "channel_"
val NOTIFICATION_ID1 = 1

class MainActivity : AppCompatActivity() {


    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        if (savedInstanceState == null)
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, WeatherListFragment.newInstance()).commit()

        registerReceiver(receiver, IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED))


        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("@@@", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }
            val token = task.result
            pushNotification("token", " ----=$token=----")
            Log.d("@@@", token)
        })


    }


    private fun pushNotification(title: String, body: String) {

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification = NotificationCompat.Builder(this, CHANNEL_HIGH_ID).apply {
            setContentTitle(title)
            setContentText(body)
            setSmallIcon(R.drawable.ic_launcher_background)
            priority = NotificationCompat.PRIORITY_MAX
        }

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O) {
            val channelHigh = NotificationChannel(
                CHANNEL_HIGH_ID,
                CHANNEL_HIGH_ID,
                NotificationManager.IMPORTANCE_HIGH
            )
            channelHigh.description = "Канал  для сообщений"
            notificationManager.createNotificationChannel(channelHigh)
        }

        notificationManager.notify(NOTIFICATION_ID1,notification.build())
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.action_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_history -> {
                navigation(HistoryFragment.newInstance())
                true
            }
            R.id.menu_contacts -> {
                navigation(ContactsFragment.newInstance())
                true
            }
            R.id.menu_find -> {
                checkPermission()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun navigation(fragment: Fragment) {
        supportFragmentManager.apply {
            beginTransaction().add(R.id.container, fragment)
                .addToBackStack("")
                .commitAllowingStateLoss()
        }
    }

    override fun onDestroy() {
        unregisterReceiver(receiver)
        super.onDestroy()
    }


    private fun checkPermission() {

        when {
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                    PackageManager.PERMISSION_GRANTED -> {
                getLocation()
            }
            shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) -> {
                showRationaleDialog()
            }
            else -> {
                requestPermission()
            }
        }

    }

    private fun showRationaleDialog() {

        AlertDialog.Builder(this)
            .setTitle(getString(R.string.dialog_rationale_title))
            .setMessage(getString(R.string.dialog_rationale_meaasge))
            .setPositiveButton(getString(R.string.dialog_rationale_give_access)) { _, _ ->
                requestPermission()
            }
            .setNegativeButton(getString(R.string.dialog_rationale_decline)) { dialog, _ -> dialog.dismiss() }
            .create()
            .show()

    }

    private fun requestPermission() {
        requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                getLocation()
            } else {
                showDialog(
                    getString(R.string.dialog_title_no_gps),
                    getString(R.string.dialog_message_no_gps)
                )
            }
        }

    private fun getLocation() {

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // Получить мененджер геолокации
            val locationManager =
                getSystemService(Context.LOCATION_SERVICE) as LocationManager
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                val provider = locationManager.getProvider(LocationManager.GPS_PROVIDER)
                provider?.let {
                    // Получаем местоположение каждые 60 секунд
                    locationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER,
                        REFRESH_PERIOD,
                        MINIMAL_DISTANCE,
                        onLocationListener
                    )
                }
            } else {
                val location =
                    locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                if (location == null) {
                    showDialog(
                        getString(R.string.dialog_title_gps_turned_off),
                        getString(R.string.dialog_message_last_location_unknown)
                    )
                } else {
                    getAddressAsync(this, location)
                    showDialog(
                        getString(R.string.dialog_title_gps_turned_off),
                        getString(R.string.dialog_message_last_known_location)
                    )
                }
            }
        } else {
            showRationaleDialog()
        }
    }

    private val onLocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {

            getAddressAsync(this@MainActivity, location)

        }

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
        override fun onProviderDisabled(provider: String) {}
        override fun onProviderEnabled(provider: String) {}
    }

    private fun getAddressAsync(context: Context, location: Location) {
        val geocoder = Geocoder(context)
        Thread {
            try {
                val addresses =
                    geocoder.getFromLocation(location.latitude, location.longitude, 1)
                binding.root.post {
                    showAddressDialog(addresses[0].getAddressLine(0), location)
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }.start()
    }

    private fun showAddressDialog(address: String, location: Location) {

        val linerLayout = LinearLayout(this)
        linerLayout.orientation = VERTICAL
        val text = TextView(this)
        text.setText(R.string.find_city)
        val edit = EditText(this)
        linerLayout.addView(text)
        linerLayout.addView(edit)


        AlertDialog.Builder(this)
            .setTitle(getString(R.string.dialog_address_title))
            .setView(linerLayout)
            .setMessage(address)
            .setPositiveButton(getString(R.string.dialog_address_get_weather)) { _, _ ->
                val geocoder = Geocoder(this)
                Thread {
                    val addresses = geocoder.getFromLocationName(edit.text.toString(), 1)
                    if (addresses.size > 0) {
                        val weather = Weather(
                            City(
                                addresses[0].featureName,
                                addresses[0].latitude,
                                addresses[0].longitude
                            )
                        )
                        binding.root.post {
                            supportFragmentManager.beginTransaction()
                                .replace(R.id.container, DetailsFragment.newInstance(weather))
                                //.addToBackStack("")
                                .commit()
                        }
                    }
                }.start()
            }
            .setNegativeButton(getString(R.string.dialog_button_close)) { _, _ ->
                Thread {
                    val weather = Weather(
                        City(
                            address,
                            location.latitude,
                            location.longitude
                        )
                    )
                    binding.root.post {
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.container, DetailsFragment.newInstance(weather))
                            //.addToBackStack("")
                            .commit()

                    }
                }.start()
            }
            .create()
            .show()
    }


    private fun showDialog(title: String, message: String) {

        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setNegativeButton(getString(R.string.dialog_button_close)) { dialog, _ -> dialog.dismiss() }
            .create()
            .show()
    }
}

