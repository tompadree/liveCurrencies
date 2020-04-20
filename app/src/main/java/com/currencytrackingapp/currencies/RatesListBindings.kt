package com.currencytrackingapp.currencies

import android.text.Editable
import android.text.TextWatcher
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
    items?.let { (listView.adapter as RatesListAdapter).submitList(items) }
}

//@BindingAdapter("app:items")
//fun setItems(listView: RecyclerView, items: List<RatesListItem>?, positionToSwap: Int) {
//    items?.let {
//        Collections.swap(items, positionToSwap, 0)
//        (listView.adapter as RatesListAdapter).submitList(items)
//    }
//}

@BindingAdapter("subTitleName")
fun setFormattedText(textView: TextView, name: String) {
    if (name == "") return
    textView.text = textView.resources.getString(CountryHelper.getNameForISO(name))
}

@BindingAdapter("imageName")
fun setIcon(imageView: CircleImageView, name: String) {
    if (name == "") return

    imageView.setImageResource(CountryHelper.getFlagForISO(name)?: R.drawable.ic_european_union)
    imageView.clearFocus()
}

@BindingAdapter("app:onBaseChanged")
fun onBaseChanged(itemCurrenciesEtAmount: EditText, string: EditTextListener) {

//    itemCurrenciesEtAmount.setOnClickListener { itemCurrenciesEtAmount.isEnabled = true }

//    if(itemCurrenciesEtAmount.text.toString() != "12321")
//        return

    itemCurrenciesEtAmount.addTextChangedListener(object : TextWatcher {

        var beforeCount = 0

        override fun afterTextChanged(p0: Editable) {
//            if (beforeCount != p0.length)
//                lastPosition = itemCurrenciesEtAmount.selectionStart

            itemCurrenciesEtAmount.requestFocus()
        }

        override fun beforeTextChanged(p0: CharSequence, p1: Int, p2: Int, p3: Int) {
            beforeCount = p0.length
        }

        override fun onTextChanged(p0: CharSequence?, start: Int, before: Int, count: Int) {
                if(beforeCount!= count && itemCurrenciesEtAmount.isEnabled)
                    string.onTextChanged(p0.toString())
//                    onCurrencyListener.onTypeListener(p0.toString())
        }
    })
}

interface EditTextListener {
    fun onTextChanged(string: String)
}