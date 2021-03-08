package com.sad_ballala_projects.ObmenKnigami_Kotlin.act

import android.R.attr
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.fxn.pix.Pix
import com.fxn.utility.PermUtil
import com.sad_ballala_projects.ObmenKnigami_Kotlin.Frag.FragmentCloseInterface
import com.sad_ballala_projects.ObmenKnigami_Kotlin.Frag.ImageListFrag
import com.sad_ballala_projects.ObmenKnigami_Kotlin.Frag.SelectImageItem
import com.sad_ballala_projects.ObmenKnigami_Kotlin.R
import com.sad_ballala_projects.ObmenKnigami_Kotlin.adapters.ImageAdapter
import com.sad_ballala_projects.ObmenKnigami_Kotlin.databinding.ActivityEditAdsBinding
import com.sad_ballala_projects.ObmenKnigami_Kotlin.dialogs.DialogSpinnerHelper
import com.sad_ballala_projects.ObmenKnigami_Kotlin.utils.CityHelper
import com.sad_ballala_projects.ObmenKnigami_Kotlin.utils.ImagePicker


class EditAdsAct : AppCompatActivity(), FragmentCloseInterface  {
    lateinit var rootElement:ActivityEditAdsBinding
    private val dialog = DialogSpinnerHelper()
    private var isImagesPermissionGranted = false
    private  lateinit var imageAdapter : ImageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        rootElement = ActivityEditAdsBinding.inflate(layoutInflater)
        setContentView(rootElement.root)
        init()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == ImagePicker.REQUEST_CODE_GET_IMAGES) {
            if(data != null) {
                val returnValues = data.getStringArrayListExtra(Pix.IMAGE_RESULTS)
                    if(returnValues?.size!! > 1)
                    rootElement.scroolViewMain.visibility = View.GONE
                    val fm = supportFragmentManager.beginTransaction()
                    fm.replace(R.id.place_holder, ImageListFrag(this, returnValues))
                    fm.commit()


            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PermUtil.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS -> {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    ImagePicker.getImages(this, 3)
                } else {
                    isImagesPermissionGranted = false
                    Toast.makeText(
                        this,
                        "Approve permissions to open Pix ImagePicker",
                        Toast.LENGTH_LONG
                    ).show()
                }
                return
            }
        }
    }

    private  fun init(){
        imageAdapter = ImageAdapter()
        rootElement.vpimages.adapter = imageAdapter
    }

    //Onclicks
    fun onClickSelectCountry(view: View){
        val listCountry = CityHelper.getAllCountries(this)
        dialog.showSpinnerDialog(this, listCountry, rootElement.tvCountry)
        if(rootElement.tvCity.text.toString() != getString(R.string.select_city)){
            rootElement.tvCity.text = getString(R.string.select_city)
        }

    }

    fun onClickSelectCity(view: View){
        val selectedCountry = rootElement.tvCountry.text.toString()
        if(selectedCountry != getString(R.string.select_country)) {
            val listCity = CityHelper.getAllCities(selectedCountry, this)
            dialog.showSpinnerDialog(this, listCity, rootElement.tvCity)
        } else{
            Toast.makeText(
                this,
                "No country was selected ${ImagePicker.REQUEST_CODE_GET_IMAGES}",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    fun onClickGetImages(view: View){
        ImagePicker.getImages(this,3)
    }

    override fun onFragClose(list : ArrayList<SelectImageItem>) {
        rootElement.scroolViewMain.visibility = View.VISIBLE
        imageAdapter.update(list)
    }
}