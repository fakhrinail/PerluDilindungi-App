package com.tubes.gapedulidilindungi

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.budiyev.android.codescanner.*
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.tubes.gapedulidilindungi.models.CheckinRequestModel
import com.tubes.gapedulidilindungi.models.CheckinResponseModel
import com.tubes.gapedulidilindungi.retrofit.ApiService
import kotlinx.android.synthetic.main.activity_checkin.*
import kotlinx.android.synthetic.main.fragment_location.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CheckinActivity : AppCompatActivity(), SensorEventListener {
    private lateinit var codeScanner: CodeScanner
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var sensorManager: SensorManager

    private var lastLatitude: Double? = null
    private var lastLongitude: Double? = null
    private var temperatureSensor: Sensor? = null

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkin)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        temperatureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE)
        if (temperatureSensor == null) {
            tv_temperature.text = "Temperature sensor not available in this device"
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this@CheckinActivity)

        setupPermissions()
        getLastKnownLocation()
        codeScanner()
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
        // Do something here if sensor accuracy changes.
    }

    override fun onSensorChanged(event: SensorEvent) {
        val currentTemp = event.values[0]
        tv_temperature.text = currentTemp.toString()
        // Do something with this sensor data.
    }

    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
        sensorManager.registerListener(this, temperatureSensor, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    private fun codeScanner() {
        codeScanner = CodeScanner(this, scn)

        codeScanner.apply {
            camera = CodeScanner.CAMERA_BACK
            formats = CodeScanner.ALL_FORMATS

            autoFocusMode = AutoFocusMode.SAFE
            scanMode = ScanMode.CONTINUOUS
            isAutoFocusEnabled = true
            isFlashEnabled = false

            decodeCallback = DecodeCallback {
                runOnUiThread {
                    getLastKnownLocation()

                    val latitude = lastLatitude ?: 0.0
                    val longitude = lastLongitude ?: 0.0

                    postCheckin(it.toString(), latitude, longitude)
                    tv_text.text = it.text
                }
            }

            errorCallback = ErrorCallback {
                runOnUiThread {
                    Toast.makeText(
                        this@CheckinActivity,
                        "Unable to post data to server",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.e("Main", "codeScanner: ${it.message}")
                }
            }

            scn.setOnClickListener {
                codeScanner.startPreview()
            }

        }
    }


    private fun getLastKnownLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Toast.makeText(this@CheckinActivity, "Permission not granted", Toast.LENGTH_SHORT)
                .show()
            return
        } else {
            Log.d("Location", "Permission granted")
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location ->
                    if (location != null) {
                        lastLatitude = location.latitude
                        lastLongitude = location.longitude
                    }
                }
        }
    }

    private fun postCheckin(qrCodeResult: String, latitude: Double, longitude: Double) {
        progressBarCheckin__checkinLoading.visibility = View.VISIBLE
        window?.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
        );
        val checkinRequestObject =
            CheckinRequestModel(qrCode = qrCodeResult, latitude = latitude, longitude = longitude)
        with(ApiService) {
            endpoint.checkin(checkinRequestObject)
                .enqueue(object : Callback<CheckinResponseModel> {
                    override fun onFailure(call: Call<CheckinResponseModel>, t: Throwable) {
                        progressBarCheckin__checkinLoading.visibility = View.GONE
                        window?.clearFlags(
                            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                        );
                        Log.d("CheckinActivity", ">>> onFailure <<< : $t")
                    }

                    override fun onResponse(
                        call: Call<CheckinResponseModel>,
                        response: Response<CheckinResponseModel>
                    ) {
                        progressBarCheckin__checkinLoading.visibility = View.GONE
                        window?.clearFlags(
                            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                        );
                        if (response.isSuccessful) {
                            val userData = response.body()?.data
                            when (userData?.userStatus) {
                                "red", "black" -> {
                                    tv_text.text = userData.reason

                                    val imgDrawable = ContextCompat.getDrawable(this@CheckinActivity, resources.getIdentifier("not_ready", "drawable", packageName))
                                    iv_status.setImageDrawable(imgDrawable)
                                }
                                "yellow", "green" -> {
                                    tv_text.text = userData.userStatus

                                    val imgDrawable = ContextCompat.getDrawable(this@CheckinActivity, resources.getIdentifier("ready", "drawable", packageName))
                                    iv_status.setImageDrawable(imgDrawable)
                                }
                            }
                        } else {
                            Toast.makeText(
                                this@CheckinActivity,
                                response.body().toString(),
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }
                    }
                })
        }
    }

    private fun setupPermissions() {
        val cameraPermission =
            ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
        val locationPermission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

        if (cameraPermission != PackageManager.PERMISSION_GRANTED) {
            makeRequest(CAMERA_REQ)
        }

        if (locationPermission != PackageManager.PERMISSION_GRANTED) {
            makeRequest(LOCATION_REQ)
        }
    }

    private fun makeRequest(requestCode: Int) {
        when (requestCode) {
            CAMERA_REQ -> {
                ActivityCompat.requestPermissions(
                    this, arrayOf(Manifest.permission.CAMERA),
                    CAMERA_REQ
                )
            }
            LOCATION_REQ -> {
                ActivityCompat.requestPermissions(
                    this, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                    LOCATION_REQ
                )
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            CAMERA_REQ -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(
                        this,
                        "You need the camera permission to use this feature",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            LOCATION_REQ -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(
                        this,
                        "You need the location permission to use this feature",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    companion object {
        private const val CAMERA_REQ = 101
        private const val LOCATION_REQ = 99
    }
}