package com.skillbox.egais_second_application

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_detail_product.*

class DetailProductActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_product)

        setProductParamsFromIntent()
    }

    private fun setProductParamsFromIntent() {

        val fullNameProduct = intent.getStringExtra(FULL_NAME_PRODUCT)
        val alcCodeProduct = intent.getStringExtra(ALC_CODE_PRODUCT)
        textNameProduct.text = fullNameProduct ?: getString(R.string.error_full_name)
        textAlcCodeProduct.text = alcCodeProduct ?: getString(R.string.error_code_product)
    }
}
const val FULL_NAME_PRODUCT = "fullNameProduct"
const val ALC_CODE_PRODUCT = "aclCodeProduct"