package com.sad_ballala_projects.ObmenKnigami_Kotlin.utils

import android.graphics.BitmapFactory
import android.util.Log
import androidx.exifinterface.media.ExifInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext


import java.io.File

object ImageManager {
    const val MAX_IMAGE_SIZE = 1000
    const val WIDTH = 0
    const val HEIGHT = 1




    fun  getImageSize(uri : String) : List<Int>{

        val options = BitmapFactory.Options().apply {
            inJustDecodeBounds = true
        }

        BitmapFactory.decodeFile(uri, options)

        return if(imageRotation(uri) == 90)
             listOf(options.outHeight, options.outWidth)

        else listOf(options.outWidth, options.outHeight)
    }

    private  fun imageRotation(uri : String) : Int{
        // подготовили массивы с размерами на которые мы будем уменьшать  далее с помощью библиотеке пикасо бутем уменьшать
        val rotation : Int
        val imageFile = File(uri)
        val exif = ExifInterface(imageFile.absolutePath)
        val orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
        rotation = if(orientation == ExifInterface.ORIENTATION_ROTATE_90 || orientation == ExifInterface.ORIENTATION_ROTATE_270){
            90
        } else{
            0
        }
        return rotation
    }

   suspend fun imageResize(uris: List<String>): String = withContext(Dispatchers.IO){
        val tempList = ArrayList<List<Int>>()
        for(n in uris.indices){

            val size = getImageSize(uris[n])
            Log.d("MyLog", "Width : ${size[WIDTH]} Height ${size[HEIGHT]} ")
            val imageRatio = size[WIDTH].toFloat() / size[HEIGHT].toFloat()
            // если картинка горизонтальная
            if(imageRatio > 1){

                // проверка самая большая сторона больше максимального  разрешонного размера картинки
                if(size[WIDTH] > MAX_IMAGE_SIZE) {
                    //если больше то уменьшаем размер
                    // делаем ширину как мах размер
                        // высоту вычесляем согласно пропорциям
                    tempList.add(listOf(MAX_IMAGE_SIZE, (MAX_IMAGE_SIZE / imageRatio).toInt()))

                } else{
                    // если нет записываем размеры которые  уже были
                    tempList.add(listOf(size[WIDTH], size[HEIGHT]))
                }

            } else {

                    // проверка самая большая сторона больше максимального  разрешонного размера картинки
                    if (size[HEIGHT] > MAX_IMAGE_SIZE) {
                        //если больше то уменьшаем размер
                        // делаем высоту как мах размер
                        // ширину вычесляем согласно пропорциям
                        // умножаем потому что ширина меньше чем высота
                        tempList.add(listOf((MAX_IMAGE_SIZE * imageRatio).toInt(), MAX_IMAGE_SIZE))

                    } else {
                        // если нет записываем размеры которые  уже были
                        tempList.add(listOf(size[WIDTH], size[HEIGHT]))
                    }
            }




             Log.d("MyLog", "Width : ${tempList[n][WIDTH]} Height ${tempList[n][HEIGHT]}")
        }

        delay(10000)
       return@withContext "Done"
    }

}