package com.skillbox.egais_second_application

import android.content.Intent
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
        val intentExtra = Intent.EXTRA_TEXT
        val fullNameProduct = intent.getStringExtra(intentExtra)
        val alcCodeProduct = intent.getStringExtra(intentExtra)
        textNameProduct.text = fullNameProduct ?: "full name product is not set"
        textAlcCodeProduct.text = alcCodeProduct ?: "code product is not set"
    }
}
