package com.example.android.politicalpreparedness.election.adapter

import com.example.android.politicalpreparedness.network.models.Election

class ElectionListener(val listener: (election: Election) -> Unit) {
    fun onClick(election: Election)  = listener(election)
}