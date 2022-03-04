package com.tubes.gapedulidilindungi.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.tubes.gapedulidilindungi.models.FaskesModel
import com.tubes.gapedulidilindungi.models.ProvinceCityModel
import com.tubes.gapedulidilindungi.R
import com.tubes.gapedulidilindungi.data.BookmarkData
import com.tubes.gapedulidilindungi.data.ListBookmarkData
import com.tubes.gapedulidilindungi.databinding.FragmentSearchBinding
import com.tubes.gapedulidilindungi.models.FaskesResults
import com.tubes.gapedulidilindungi.retrofit.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.pow

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SearchFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SearchFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private var selectedProvince: String? = null
    private var selectedCity: String? = null
    private var top5Faskes: List<BookmarkData>? = null
    private var nearestFaskes: ListBookmarkData? = null

    private var lastLatitude: Double? = null
    private var lastLongitude: Double? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        getProvincesDataFromApi()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSearchBinding.inflate(inflater, container, false)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        getLastKnownLocation()

        binding.dropdownProvince.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    pos: Int,
                    id: Long
                ) {
                    val provinceId = parent?.getItemAtPosition(pos).toString()
                    getCitiesDataFromApi(provinceId)
                    selectedProvince = provinceId
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }
            }

        binding.dropdownCity.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
                val cityId = parent?.getItemAtPosition(pos).toString()
                selectedCity = cityId
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        binding.btnSearch.setOnClickListener {
            if (selectedProvince != null && selectedCity != null) {
                getFacilitiesDataFromApi(selectedProvince!!, selectedCity!!)
            } else {
                Toast.makeText(requireContext(), "Select province and city", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getProvincesDataFromApi() {
        binding.progressBarSearchProvincesLoading.visibility = View.VISIBLE
        with(ApiService) {
            endpoint.getProvinces()

                .enqueue(object : Callback<ProvinceCityModel> {
                    override fun onFailure(call: Call<ProvinceCityModel>, t: Throwable) {
                        binding.progressBarSearchProvincesLoading.visibility = View.GONE
                        Log.d("SearchFragment", ">>> onFailure <<< : $t")
                    }

                    override fun onResponse(
                        call: Call<ProvinceCityModel>,
                        response: Response<ProvinceCityModel>
                    ) {
                        binding.progressBarSearchProvincesLoading.visibility = View.GONE
                        if (response.isSuccessful) {
                            Log.d("SearchFragment", response.body().toString())
                            val provinces = response.body()!!.results.map { it.key }
                            val provincesAdapter = ArrayAdapter(
                                requireContext(),
                                R.layout.province_dropdown_item,
                                provinces
                            )
                            binding.dropdownProvince.adapter = provincesAdapter
                        } else {
                            Toast.makeText(
                                requireContext(),
                                "Unable to get provinces",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                })
        }
    }

    private fun getCitiesDataFromApi(provinceId: String) {
        binding.progressBarSearchCitiesLoading.visibility = View.VISIBLE
        with(ApiService) {
            endpoint.getCities(provinceId)
                .enqueue(object : Callback<ProvinceCityModel> {
                    override fun onFailure(call: Call<ProvinceCityModel>, t: Throwable) {
                        binding.progressBarSearchCitiesLoading.visibility = View.GONE
                        Log.d("SearchFragment", ">>> onFailure <<< : $t")
                    }

                    override fun onResponse(
                        call: Call<ProvinceCityModel>,
                        response: Response<ProvinceCityModel>
                    ) {
                        binding.progressBarSearchCitiesLoading.visibility = View.GONE
                        if (response.isSuccessful) {
                            Log.d("SearchFragment", response.body().toString())
                            val cities = response.body()!!.results.map { it.key }
                            val citiesAdapter = ArrayAdapter(
                                requireContext(),
                                R.layout.city_dropdown_item,
                                cities
                            )
                            binding.dropdownCity.adapter = citiesAdapter
                        } else {
                            Log.d("CityFail", response.body().toString())
                            Log.d("CityFail", response.message())
                            Toast.makeText(requireContext(), response.message(), Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                })
        }
    }

    private fun getFacilitiesDataFromApi(provinceId: String, cityId: String) {
        binding.progressBarSearchSearchLoading.visibility = View.VISIBLE
        with(ApiService) {
            endpoint.getFaskes(provinceId, cityId)
                .enqueue(object : Callback<FaskesModel> {
                    override fun onFailure(call: Call<FaskesModel>, t: Throwable) {
                        binding.progressBarSearchSearchLoading.visibility = View.GONE
                        Log.d("SearchFragment", ">>> onFailure <<< : $t")
                    }

                    override fun onResponse(
                        call: Call<FaskesModel>,
                        response: Response<FaskesModel>
                    ) {
                        binding.progressBarSearchSearchLoading.visibility = View.GONE
                        if (response.isSuccessful) {
                            Log.d("FaskesResults", response.body().toString())
                            val sortedListFaskes =
                                sortFaskes(response.body()?.data, lastLongitude, lastLatitude)

                            Log.d("ListFaskes", sortedListFaskes.toString())

                            val listFaskes = sortedListFaskes?.map {
                                BookmarkData(
                                    kodeFaskes = it.kode.toString(),
                                    namaFaskes = it.nama,
                                    alamatFaskes = it.alamat,
                                    noTelpFaskes = it.telp,
                                    jenisFaskes = it.jenisFaskes,
                                    statusFaskes = it.status
                                )
                            }

                            Log.d("ListFaskes", listFaskes?.size.toString())

                            if (listFaskes != null) {
                                top5Faskes = listFaskes.slice(0..4)

                                val bundle = Bundle()
                                val listFaskesFragment = BookmarkFragment()
                                val listFaskes =
                                    top5Faskes?.let { it1 -> ListBookmarkData(listFaskes = it1) }
                                Log.d("LIST_FASKES", listFaskes.toString())
                                bundle.putParcelable(
                                    "LIST_FASKES",
                                    listFaskes
                                )
                                listFaskesFragment.arguments = bundle
                                val fragment = childFragmentManager.findFragmentById(R.id.fragment_container_2)
                                if (fragment != null) {
                                    childFragmentManager.beginTransaction().remove(fragment).commit()
                                }
                                childFragmentManager.beginTransaction().apply {
                                    add(R.id.fragment_container_2, listFaskesFragment)
                                    commit()
                                }
                            }

                            Log.d("ListFaskes", nearestFaskes?.listFaskes.toString())
                            Log.d("ListFaskes", nearestFaskes.toString())
                        } else {
                            Log.d("FaskesResults", response.body().toString())
                            Toast.makeText(
                                requireContext(),
                                "Unable to get facilities",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                })
        }
    }

    private fun getLastKnownLocation() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Toast.makeText(requireContext(), "Permission not granted", Toast.LENGTH_SHORT)
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

    private fun sortFaskes(
        listFaskes: List<FaskesResults>?,
        longitude: Double?,
        latitude: Double?
    ): List<FaskesResults>? {
        val mutableListFaskes = listFaskes?.toMutableList()
        val currLongitude = longitude ?: 0.0
        val currLatitude = latitude ?: 0.0

        mutableListFaskes?.sortBy { it ->
            ((it.longitude?.toDouble()?.minus(currLongitude))?.pow(2)
                ?.plus((it.latitude?.toDouble()?.minus(currLatitude))?.pow(2)!!))?.pow(0.5)
        }

        return mutableListFaskes?.toList()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SearchFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SearchFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}