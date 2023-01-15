package com.hafiztaruligani.movieday.util

import android.content.res.ColorStateList
import android.graphics.Color
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.hafiztaruligani.movieday.R
import java.text.SimpleDateFormat
import java.util.*

fun ImageView.glide(
    resource: String,
    topLeft: Int = 0,
    topRight: Int = 0,
    bottomRight: Int = 0,
    bottomLeft: Int = 0,
    circleCrop: Boolean = false,
    placeholder: Int? =null
){
    val glide = Glide.with(context)
        .load(resource)
        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
        .override(this@glide.width, this@glide.height)

    placeholder?.let { glide.placeholder(placeholder) }

    if(
        topLeft > 0 ||
        topRight > 0 ||
        bottomRight > 0 ||
        bottomLeft > 0
    ) {
        val rounded = RequestOptions().transform(
            CenterInside(), GranularRoundedCorners(
                topLeft.toFloat(),
                topRight.toFloat(),
                bottomRight.toFloat(),
                bottomLeft.toFloat()
            )
        )
        glide.apply(rounded).into(this)
    }else if(circleCrop) glide.circleCrop().into(this)
    else glide.into(this)

}

fun ChipGroup.addChip(id: Int, text: String, color: Int?=null){

    val chip = Chip(context)
    chip.text = text
    chip.id = id
    chip.setTextAppearanceResource(R.style.font_small)

    val backgroundColor = ColorStateList.valueOf(
        if (color!=null) ContextCompat.getColor(context, color)
        else Color.TRANSPARENT
    )
    val strokeColor = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.font_yellow))

    chip.chipStrokeColor = strokeColor
    chip.chipStrokeWidth = (4).toFloat()
    chip.chipBackgroundColor = backgroundColor
    chip.isClickable = false
    addView(chip)
}

fun String.convertIntoList(): List<String>{
    return if (this.contains("["))
        this.replace(" ","").replace("[","").replace("]","").split(',')
    else listOf()
}

fun String.toStringDate(): String {
    return try {
        val reader = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val writer = SimpleDateFormat("dd MMMM yyyy", Locale.US)
        val date = reader.parse(this)
        writer.format(date ?: this)
    }catch (e:Exception){
        this
    }
}

fun List<Any>.removeBracket(): String{
    if (isEmpty()) return ""
    return toString().drop(1).dropLast(1)
}