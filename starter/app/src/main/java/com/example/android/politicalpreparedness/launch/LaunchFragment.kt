package com.example.android.politicalpreparedness.launch

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.android.politicalpreparedness.databinding.FragmentLaunchBinding
import com.example.android.politicalpreparedness.extensions.setDisplayHomeAsUpEnabled
import com.example.android.politicalpreparedness.extensions.setTitle

class LaunchFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding = FragmentLaunchBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this

        binding.upcomingButton.setOnClickListener { navToElections() }
        binding.representativeButton.setOnClickListener { navToRepresentatives() }

        setDisplayHomeAsUpEnabled(false)

        return binding.root
    }

    private fun navToElections() {
        this.findNavController().navigate(LaunchFragmentDirections.actionLaunchFragmentToElectionsFragment())
    }

    private fun navToRepresentatives() {
        this.findNavController().navigate(LaunchFragmentDirections.actionLaunchFragmentToRepresentativeFragment())
    }

}
