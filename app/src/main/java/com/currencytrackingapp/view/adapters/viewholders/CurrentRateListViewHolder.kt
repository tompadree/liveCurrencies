package com.currencytrackingapp.view.adapters.viewholders

import android.app.Activity
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethod
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.currencytrackingapp.data.models.RatesListItem
import com.currencytrackingapp.utils.helpers.AppUtils
import com.currencytrackingapp.utils.helpers.CountryHelper
import com.currencytrackingapp.view.listeners.OnCurrencyListener
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_currencies.*



