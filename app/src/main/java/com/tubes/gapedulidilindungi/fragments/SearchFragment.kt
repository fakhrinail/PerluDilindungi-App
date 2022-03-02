package com.tubes.gapedulidilindungi.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.tubes.gapedulidilindungi.ProvinceCityModel
import com.tubes.gapedulidilindungi.ProvinceCityResults
import com.tubes.gapedulidilindungi.R
import com.tubes.gapedulidilindungi.databinding.FragmentSearchBinding
import com.tubes.gapedulidilindungi.retrofit.ApiService
import kotlinx.android.synthetic.main.province_dropdown_item.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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

        binding.dropdownProvince.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
                val provinceId = parent?.getItemAtPosition(pos).toString()
                provinceId?.let {
                    getCitiesDataFromApi(it)
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
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