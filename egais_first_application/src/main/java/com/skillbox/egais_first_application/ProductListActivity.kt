package com.skillbox.egais_first_application

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import kotlinx.android.synthetic.main.list_productions.*
import ru.evotor.egais.api.provider.dictionary.ProductInfoContract
import ru.evotor.egais.api.provider.dictionary.ProductInfoContract.URI

class ProductListActivity : AppCompatActivity() {

    private val fullNameProduct = ProductInfoContract.COLUMN_FULL_NAME
    private val aclCodeProduct = ProductInfoContract.COLUMN_ALC_CODE

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContentView(R.layout.list_productions)

        fetchContacts()
        setProductText()
    }

    private fun fetchContacts() {
        val productList = mutableListOf<String>()
        val uri = URI
        val projection = arrayOf(fullNameProduct, aclCodeProduct)
        val selection: String? = null
        val selectionArgs: Array<String>? = null
        val sortOrder: String? = null
        val resolver = contentResolver
        val cursor = resolver.query(uri, projection, selection, selectionArgs, sortOrder)
        while (cursor?.moveToNext() != null) {
            val fullNameProductCursor = cursor.getString(cursor.getColumnIndex(fullNameProduct))
            val aclCodeProductCursor = cursor.getString(cursor.getColumnIndex(aclCodeProduct))
            productList.add(fullNameProductCursor + "\n" + aclCodeProductCursor)
        }
        (findViewById<View>(R.id.listProductions) as ListView).adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            productList
        )
    }

    private fun setProductText() {
        listProductions.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, _, _ ->
                val uri = Uri.parse("com.skillbox.egais_second_application")
                val productIntent = Intent(Intent.ACTION_SEND, uri)
                val intentExtra = Intent.EXTRA_TEXT
                productIntent.putExtra(intentExtra, "FULL_NAME")
                productIntent.putExtra(intentExtra, "ALC_CODE")

                if (productIntent.resolveActivity(packageManager) != null) {
                    startActivity(productIntent)
                } else {
                    toast("No component to handle intent")
                }
            }
    }

    private fun toast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }
}