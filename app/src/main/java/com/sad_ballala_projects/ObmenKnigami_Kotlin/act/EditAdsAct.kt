package com.sad_ballala_projects.ObmenKnigami_Kotlin.act

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import com.sad_ballala_projects.ObmenKnigami_Kotlin.R
import com.sad_ballala_projects.ObmenKnigami_Kotlin.databinding.ActivityEditAdsBinding
import com.sad_ballala_projects.ObmenKnigami_Kotlin.utils.CityHelper

class EditAdsAct : AppCompatActivity() {
    private lateinit var rootElement:ActivityEditAdsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        rootElement = ActivityEditAdsBinding.inflate(layoutInflater)
        setContentView(rootElement.root)

        val listCountry = CityHelper.getAllCountries(this)


    }
}