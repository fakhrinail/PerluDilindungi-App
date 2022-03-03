package com.tubes.gapedulidilindungi

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode
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

class CheckinActivity : AppCompatActivity() {
    private lateinit var codeScanner: CodeScanner
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private var lastLatitude: Double? = null
    private var lastLongitude: Double? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkin)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this@CheckinActivity)

        setupPermissions()
        getLastKnownLocation()
        codeScanner()
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

    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
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
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Log.d("Location", "No permission granted")
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
        val checkinRequestObject =
            CheckinRequestModel(qrCode = qrCodeResult, latitude = latitude, longitude = longitude)
        with(ApiService) {
            endpoint.checkin(checkinRequestObject)
                .enqueue(object : Callback<CheckinResponseModel> {
                    override fun onFailure(call: Call<CheckinResponseModel>, t: Throwable) {
                        progressBarCheckin__checkinLoading.visibility = View.GONE
                        Log.d("CheckinActivity", ">>> onFailure <<< : $t")
                    }

                    override fun onResponse(
                        call: Call<CheckinResponseModel>,
                        response: Response<CheckinResponseModel>
                    ) {
                        progressBarCheckin__checkinLoading.visibility = View.GONE
                        if (response.isSuccessful) {
                            val userData = response.body()?.data
                            when (userData?.userStatus) {
                                "red", "black" -> tv_text.text = userData.reason
                                "yellow", "green" -> tv_text.text = userData.userStatus
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