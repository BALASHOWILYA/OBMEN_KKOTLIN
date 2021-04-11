package com.sad_ballala_projects.ObmenKnigami_Kotlin.act

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.fxn.pix.Pix
import com.fxn.utility.PermUtil
import com.sad_ballala_projects.ObmenKnigami_Kotlin.Frag.FragmentCloseInterface
import com.sad_ballala_projects.ObmenKnigami_Kotlin.Frag.ImageListFrag
import com.sad_ballala_projects.ObmenKnigami_Kotlin.R
import com.sad_ballala_projects.ObmenKnigami_Kotlin.adapters.ImageAdapter
import com.sad_ballala_projects.ObmenKnigami_Kotlin.databinding.ActivityEditAdsBinding
import com.sad_ballala_projects.ObmenKnigami_Kotlin.dialogs.DialogSpinnerHelper
import com.sad_ballala_projects.ObmenKnigami_Kotlin.utils.CityHelper
import com.sad_ballala_projects.ObmenKnigami_Kotlin.utils.ImageManager
import com.sad_ballala_projects.ObmenKnigami_Kotlin.utils.ImagePicker


class EditAdsAct : AppCompatActivity(), FragmentCloseInterface  {
    private var chooseImageFrag : ImageListFrag? = null
    lateinit var rootElement:ActivityEditAdsBinding
    private val dialog = DialogSpinnerHelper()
    private var isImagesPermissionGranted = false
    private  lateinit var imageAdapter : ImageAdapter
    var editImagePos = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        rootElement = ActivityEditAdsBinding.inflate(layoutInflater)
        setContentView(rootElement.root)
        init()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == ImagePicker.REQUEST_CODE_GET_IMAGES) {

            if (data != null) {

                val returnValues = data.getStringArrayListExtra(Pix.IMAGE_RESULTS)

                if (returnValues?.size!! > 1 && chooseImageFrag == null) {

                    openChooseImageFrag(returnValues)

                }else if (returnValues.size == 1 && chooseImageFrag == null) {

                   // imageAdapter.update(returnValues)
                    val tempList = ImageManager.getImageSize(returnValues[0])
                    Log.d("MyLog", "Imaga width : ${tempList[0]}")
                    Log.d("MyLog", "Imaga height : ${tempList[1]}")


                } else if (chooseImageFrag != null) {

                    chooseImageFrag?.updateAdapter(returnValues)

                }

            }
        } else if (resultCode == RESULT_OK && requestCode == ImagePicker.REQUEST_CODE_GET_SINGLE_IMAGES){
            if (data != null) {

                val uris = data.getStringArrayListExtra(Pix.IMAGE_RESULTS)
                chooseImageFrag?.setSingleImage(uris?.get(0)!!,editImagePos)
            }
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PermUtil.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS -> {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    ImagePicker.getImages(this, 3, ImagePicker.REQUEST_CODE_GET_IMAGES)
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

        if(imageAdapter.mainArray.size == 0){

            ImagePicker.getImages(this,3, ImagePicker.REQUEST_CODE_GET_IMAGES)

        } else{
            openChooseImageFrag(null)
            chooseImageFrag?.updateAdapterFromEdit(imageAdapter.mainArray)
        }


    }

    override fun onFragClose(list : ArrayList<Bitmap>) {
        rootElement.scroolViewMain.visibility = View.VISIBLE
        imageAdapter.update(list)
        chooseImageFrag = null
    }

    private fun openChooseImageFrag(newList : ArrayList<String>?){

        chooseImageFrag = ImageListFrag(this, newList)
        rootElement.scroolViewMain.visibility = View.GONE
        val fm = supportFragmentManager.beginTransaction()
        fm.replace(R.id.place_holder, chooseImageFrag!!)
        fm.commit()

    }
}