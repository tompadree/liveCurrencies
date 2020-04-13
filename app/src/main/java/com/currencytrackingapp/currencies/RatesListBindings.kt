package com.currencytrackingapp.currencies

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.currencytrackingapp.data.models.RatesListItem
import androidx.databinding.adapters.TextViewBindingAdapter.setText
import android.widget.TextView
import com.currencytrackingapp.utils.helpers.CountryHelper
import de.hdodenhof.circleimageview.CircleImageView


/**
 * @author Tomislav Curis
 */
// custom binding of items
@BindingAdapter("app:items")
fun setItems(listView: RecyclerView, items: List<RatesListItem>?) {
    items?.let { (listView.adapter as RatesListAdapter).submitList(items) }
}

@BindingAdapter("subTitleName")
fun setFormattedText(textView: TextView, name: String) {
    if (name == "") return
    textView.text = textView.resources.getString(CountryHelper.getNameForISO(name))
}

@BindingAdapter("imageName")
fun setIcon(imageView: CircleImageView, name: String) {
    if (name == "") return

    imageView.setImageResource(CountryHelper.getFlagForISO(name))
    imageView.clearFocus()
}