package com.example.android.politicalpreparedness.representative

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.android.politicalpreparedness.databinding.FragmentRepresentativeBinding
import com.example.android.politicalpreparedness.extensions.setDisplayHomeAsUpEnabled
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.representative.adapter.RepresentativeListAdapter
import com.google.android.gms.location.LocationServices
import java.util.Locale

class DetailFragment : Fragment() {

    companion object {
        const val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 100
    }

    private lateinit var viewModel: RepresentativeViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        setDisplayHomeAsUpEnabled(true)

        viewModel = ViewModelProvider(this).get(RepresentativeViewModel::class.java)

        val binding = FragmentRepresentativeBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        binding.recycler.adapter = RepresentativeListAdapter()

        viewModel.representatives.observe(viewLifecycleOwner) { representatives ->
            if (representatives == null || representatives.isEmpty()) {
                binding.listPlaceholder.visibility = View.VISIBLE
            } else {
                binding.listPlaceholder.visibility = View.GONE
            }
        }

        binding.buttonSearch.setOnClickListener {
            viewModel.searchWithAddress()
        }

        binding.buttonLocation.setOnClickListener {
            getLocation()
        }

        return binding.root
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {
            if (isLocationPermissionGranted()) {
                getLocation()
            } else {
                
            }
        }
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION)
    }

    private fun isLocationPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    @SuppressLint("MissingPermission")
    private fun getLocation() {
        if (isLocationPermissionGranted()) {
            LocationServices.getFusedLocationProviderClient(requireActivity()).lastLocation.addOnSuccessListener { location ->
                val address = geoCodeLocation(location)
                viewModel.searchWithLocation(address)
            }
        } else {
            requestLocationPermission()
        }
    }

    private fun geoCodeLocation(location: Location): Address {
        val geocoder = Geocoder(context, Locale.getDefault())
        return geocoder.getFromLocation(location.latitude, location.longitude, 1)
                .map { address ->
                    Address(address.thoroughfare, address.subThoroughfare, address.locality, address.adminArea, address.postalCode)
                }
                .first()
    }

    private fun hideKeyboard() {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(requireView().windowToken, 0)
    }

}