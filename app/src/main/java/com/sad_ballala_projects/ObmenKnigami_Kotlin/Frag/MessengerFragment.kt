package com.sad_ballala_projects.ObmenKnigami_Kotlin.Frag

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sad_ballala_projects.ObmenKnigami_Kotlin.R

class MessengerFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_messenger, container, false)

    }
}

