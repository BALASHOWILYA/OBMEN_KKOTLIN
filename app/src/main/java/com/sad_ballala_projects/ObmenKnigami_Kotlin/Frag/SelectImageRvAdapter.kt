package com.sad_ballala_projects.ObmenKnigami_Kotlin.Frag

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sad_ballala_projects.ObmenKnigami_Kotlin.R
import com.sad_ballala_projects.ObmenKnigami_Kotlin.act.EditAdsAct
import com.sad_ballala_projects.ObmenKnigami_Kotlin.utils.ImagePicker
import com.sad_ballala_projects.ObmenKnigami_Kotlin.utils.ItemTouchMoveCallback

class SelectImageRvAdapter : RecyclerView.Adapter<SelectImageRvAdapter.ImageHolder>(), ItemTouchMoveCallback.ItemTouchAdapter{
    val mainArray = ArrayList<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.select_image_frag_item, parent, false)
        return ImageHolder(view, parent.context, this)
    }

    override fun onBindViewHolder(holder: ImageHolder, position: Int) {
        holder.setData(mainArray[position])
    }

    override fun getItemCount(): Int {
        return mainArray.size
    }

    override fun onMove(startPos: Int, targetPos: Int) {

        val targetItem = mainArray[targetPos]
        mainArray[targetPos] = mainArray[startPos]
        mainArray[startPos] = targetItem
        notifyItemMoved(startPos, targetPos)
    }

    override fun onClear() {
        notifyDataSetChanged()
    }

    class ImageHolder(itemView: View, val context : Context, val adapter: SelectImageRvAdapter) : RecyclerView.ViewHolder(itemView) {
        lateinit var imEditImage : ImageButton
        lateinit var tvTitle : TextView
        lateinit var image : ImageView
        lateinit var imDeleteImage : ImageView

        fun setData(item : String){
            tvTitle = itemView.findViewById(R.id.tvTitle)
            image = itemView.findViewById(R.id.imageContent)
            imEditImage= itemView.findViewById(R.id.imEditImage)
            imDeleteImage= itemView.findViewById(R.id.imDelete)

            imEditImage.setOnClickListener {
                ImagePicker.getImages(context as EditAdsAct, 1, ImagePicker.REQUEST_CODE_GET_SINGLE_IMAGES)
                context.editImagePos = adapterPosition
            }

            imDeleteImage.setOnClickListener(){
                adapter.mainArray.removeAt(adapterPosition)
                adapter.notifyItemRemoved(adapterPosition)
                for(n in 0 until  adapter.mainArray.size){
                    adapter.notifyItemChanged(n)
                }
               // adapter.notifyDataSetChanged()
            }

            tvTitle.text = context.resources.getStringArray(R.array.title_array)[adapterPosition]
            image.setImageURI(Uri.parse(item))

        }
    }
    fun updateAdapter(newList : List<String>, needClear : Boolean){
        if(needClear)mainArray.clear()
        mainArray.addAll(newList)
        notifyDataSetChanged()
    }


}