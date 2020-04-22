package com.currencytrackingapp.currencies

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.currencytrackingapp.data.models.RatesListItem
import android.widget.TextView
import com.currencytrackingapp.R
import com.currencytrackingapp.utils.helpers.CountryHelper
import de.hdodenhof.circleimageview.CircleImageView


/**
 * @author Tomislav Curis
 */

// custom binding of items
@BindingAdapter("app:items")
fun setItems(listView: RecyclerView, items: List<RatesListItem>?) {
    if(items.isNullOrEmpty()) return

    (listView.adapter as RatesListAdapter).submitList(items)
}

@BindingAdapter("subTitleName")
fun setFormattedText(textView: TextView, subtitleName: String) {
    try {
        textView.text = textView.resources.getString(CountryHelper.getNameForISO(subtitleName))
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

@BindingAdapter("imageName")
fun setIcon(imageView: CircleImageView, iconName: String) {
    try {
        imageView.setImageResource(CountryHelper.getFlagForISO(iconName))
        imageView.clearFocus()
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

@BindingAdapter("app:onBaseChanged")
fun onBaseChanged(itemCurrenciesEtAmount: EditText, string: EditTextListener) {

    itemCurrenciesEtAmount.addTextChangedListener(object : TextWatcher {

        var beforeCount = 0

        override fun afterTextChanged(p0: Editable) {
            itemCurrenciesEtAmount.requestFocus()
        }

        override fun beforeTextChanged(p0: CharSequence, p1: Int, p2: Int, p3: Int) {
            beforeCount = p0.length
        }

        override fun onTextChanged(p0: CharSequence?, start: Int, before: Int, count: Int) {
                if(beforeCount!= count && itemCurrenciesEtAmount.isEnabled)
                    string.onTextChanged(p0.toString())
        }
    })
}

interface EditTextListener {
    fun onTextChanged(string: String)
}