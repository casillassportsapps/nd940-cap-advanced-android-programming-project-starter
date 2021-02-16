package com.example.android.politicalpreparedness.representative

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.representative.model.Representative
import kotlinx.coroutines.launch

class RepresentativeViewModel : ViewModel() {

    private val _representatives = MutableLiveData<List<Representative>>()
    val representatives: LiveData<List<Representative>>
        get() = _representatives

    private val _address = MutableLiveData(Address("","","","",""))
    val address: LiveData<Address>
        get() = _address

    private fun fetchRepresentatives(address: String) {
        viewModelScope.launch {
            val response = CivicsApi.retrofitService.getRepresentatives(address)
            val list = mutableListOf<Representative>()
            response.offices.forEach { office ->
                list.addAll(office.getRepresentatives(response.officials))
                _representatives.value = list
            }
        }
    }

    fun searchWithLocation(address: Address) {
        _address.value = address
        fetchRepresentatives(address.toFormattedString())
    }

    fun searchWithAddress() {
        _address.value?.let {
            fetchRepresentatives(it.toFormattedString())
        }
    }
}
