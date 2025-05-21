package com.company.androidtask.presentation.common.custom_views

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.company.androidtask.R
import com.company.androidtask.databinding.CustomSearchViewBinding

class CustomSearchView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val binding: CustomSearchViewBinding =
        CustomSearchViewBinding.inflate(LayoutInflater.from(context), this, true)
    private var onQueryTextChangeListener: ((String) -> Unit)? = null
    private var onQrCodeClickListener: (() -> Unit)? = null

    init {
        setupListeners()
    }

    private fun setupListeners() {
        binding.editTextSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty()) {
                    binding.iconSearchClear.setImageResource(R.drawable.round_search_24)
                    binding.iconSearchClear.setOnClickListener(null)
                } else {
                    binding.iconSearchClear.setImageResource(R.drawable.round_clear_24)
                    binding.iconSearchClear.setOnClickListener {
                        binding.editTextSearch.text.clear()
                    }
                }
                onQueryTextChangeListener?.invoke(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        binding.buttonQrCode.setOnClickListener {
            onQrCodeClickListener?.invoke()
        }
    }

    fun onSearch(listener: (String) -> Unit) {
        this.onQueryTextChangeListener = listener
    }

    fun onQrClick(listener: () -> Unit) {
        this.onQrCodeClickListener = listener
    }

    fun setQuery(query: String) {
        binding.editTextSearch.setText(query)
        binding.editTextSearch.setSelection(query.length)
    }
}
