package com.sad_ballala_projects.ObmenKnigami_Kotlin.Frag

import android.app.Activity
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.sad_ballala_projects.ObmenKnigami_Kotlin.R
import com.sad_ballala_projects.ObmenKnigami_Kotlin.databinding.ListImageFragBinding
import com.sad_ballala_projects.ObmenKnigami_Kotlin.gialogshelper.ProgressDialog
import com.sad_ballala_projects.ObmenKnigami_Kotlin.utils.ImageManager
import com.sad_ballala_projects.ObmenKnigami_Kotlin.utils.ImagePicker
import com.sad_ballala_projects.ObmenKnigami_Kotlin.utils.ItemTouchMoveCallback
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ImageListFrag(private val fragCloseInterface : FragmentCloseInterface, private val newList : ArrayList<String>?) : Fragment(){
    lateinit var rootElement : ListImageFragBinding
    val adapter = SelectImageRvAdapter()
    val dragCallback = ItemTouchMoveCallback(adapter)
    val touchHelper = ItemTouchHelper(dragCallback)

    private var job: Job? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootElement = ListImageFragBinding.inflate(inflater)
        return rootElement.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpToolBar()

        touchHelper.attachToRecyclerView(rootElement.rcViewSelectImage)
        rootElement.rcViewSelectImage.layoutManager = LinearLayoutManager(activity)
        rootElement.rcViewSelectImage.adapter = adapter
        // корутина создается на основном потоке, а сама функция на второстепенном потоке
        if(newList != null ) resizedSelectedImages(newList, true)
        //adapter.updateAdapter(newList, true)

    }

    fun updateAdapterFromEdit(bitmapList : List<Bitmap>){
        adapter.updateAdapter(bitmapList, true)
    }


    override fun onDetach() {
        super.onDetach()
        fragCloseInterface.onFragClose(adapter.mainArray)
        job?.cancel()

    }

    private fun resizedSelectedImages(newList: ArrayList<String>, needClear: Boolean ){
        job = CoroutineScope(Dispatchers.Main).launch {
            // так как функции suspend задачи запускаются последовально. То есть вторая
            // строчка кода не закончится пока жива первая строчка кода
            val dialog = ProgressDialog.createProgressDialog(activity as Activity)
            val bitmapList = ImageManager.imageResize(newList)
            dialog.dismiss()
            adapter.updateAdapter(bitmapList, needClear)

            //Log.d("MyLog", "Result : $text")
        }
    }

    private fun setUpToolBar(){
        rootElement.tb.inflateMenu(R.menu.menu_choose_image)
        val deleteItem = rootElement.tb.menu.findItem(R.id.id_delete_image)
        val addImageItem = rootElement.tb.menu.findItem(R.id.id_add_image)

        rootElement.tb.setNavigationOnClickListener(){
            activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit()
        }


        deleteItem.setOnMenuItemClickListener {
            adapter.updateAdapter(ArrayList(), true)
            true
        }
        addImageItem.setOnMenuItemClickListener {
            val imageCount = ImagePicker.MAX_IMAGE_COUNT - adapter.mainArray.size
            ImagePicker.getImages(activity as AppCompatActivity, imageCount, ImagePicker.REQUEST_CODE_GET_IMAGES)
            true
        }
    }

    fun updateAdapter(newList : ArrayList<String>){ resizedSelectedImages(newList, false) }

    fun setSingleImage(uri : String, pos : Int){
        val pBar = rootElement.rcViewSelectImage[pos].findViewById<ProgressBar>(R.id.pBar)

        job = CoroutineScope(Dispatchers.Main).launch{
            // так как функции suspend задачи запускаются последовально. То есть вторая
            // строчка кода не закончится пока жива первая строчка кода
            pBar.visibility = View.VISIBLE
            val bitmapList = ImageManager.imageResize(listOf(uri))
            pBar.visibility = View.GONE
            adapter.mainArray[pos] = bitmapList[0]
            adapter.notifyItemChanged(pos)

            //Log.d("MyLog", "Result : $text")
        }


    }


}
