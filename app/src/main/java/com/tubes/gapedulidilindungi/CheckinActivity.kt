package com.tubes.gapedulidilindungi

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode
import com.tubes.gapedulidilindungi.models.ProvinceCityModel
import com.tubes.gapedulidilindungi.retrofit.ApiService
import kotlinx.android.synthetic.main.activity_checkin.*
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CheckinActivity : AppCompatActivity() {
    private lateinit var codeScanner: CodeScanner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkin)

        setupPermissions()
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
                    tv_text.text = it.text
                }
            }

            errorCallback = ErrorCallback {
                runOnUiThread {
                    Toast.makeText(this@CheckinActivity, "Unable to post data to server", Toast.LENGTH_SHORT).show()
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

    private fun postCheckin(requestBody: RequestBody) {
        progressBarCheckin__checkinLoading.visibility = View.VISIBLE
        with(ApiService) {
            endpoint.checkin(requestBody)
                .enqueue(object : Callback<ResponseBody> {
                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        progressBarCheckin__checkinLoading.visibility = View.GONE
                        Log.d("CheckinActivity", ">>> onFailure <<< : $t")
                    }

                    override fun onResponse(
                        call: Call<ResponseBody>,
                        response: Response<ResponseBody>
                    ) {
                        progressBarCheckin__checkinLoading.visibility = View.GONE
                        if (response.isSuccessful) {
                            Log.d("CheckinActivity", response.body().toString())

                        } else {
                            Log.d("CheckinActivity", response.body().toString())
                            Log.d("CheckinActivity", response.message())
                            Toast.makeText(this@CheckinActivity, response.message(), Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                })
        }
    }

    private fun setupPermissions() {
        val permission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)

        if (permission != PackageManager.PERMISSION_GRANTED) {
            makeRequest()
        }
    }

    private fun makeRequest() {
        ActivityCompat.requestPermissions(
            this, arrayOf(android.Manifest.permission.CAMERA),
            CAMERA_REQ
        )
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
                        "You need the camera permission to use this app",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    companion object {
        private const val CAMERA_REQ = 101
    }
}