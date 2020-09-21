package com.skillbox.egais_first_application

import android.content.Intent
import android.database.Cursor
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
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class ProductListActivity : AppCompatActivity() {

    private val fullNameProduct = ProductInfoContract.COLUMN_FULL_NAME
    private val aclCodeProduct = ProductInfoContract.COLUMN_ALC_CODE

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContentView(R.layout.list_productions)

        fetchContacts()
    }

    private fun fetchContacts(): Observable<Cursor?>? {
        val productList = mutableListOf<String>()
        val uri = URI
        val projection = arrayOf(fullNameProduct, aclCodeProduct)
        val selection: String? = null
        val selectionArgs: Array<String>? = null
        val sortOrder: String? = null
        val resolver = contentResolver
        val cursor = resolver.query(uri, projection, selection, selectionArgs, sortOrder)
        val observable: Observable<Cursor?>? =
            Observable.just(cursor)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    while (cursor?.moveToNext() != null) {
        val fullNameProductCursor =
            cursor.use { getString(cursor.getColumnIndex(fullNameProduct)) }
        val aclCodeProductCursor =
            cursor.use { getString(cursor.getColumnIndex(aclCodeProduct)) }
        productList.add(fullNameProductCursor + "\n" + aclCodeProductCursor)
        listProductions.onItemClickListener = AdapterView.OnItemClickListener { l, _, position, _ ->
            l.getItemAtPosition(position)?.let {
                val uri = Uri.parse(URI_APPLICATION_SECOND)
                val productIntent = Intent(Intent.ACTION_SEND, uri)
                productIntent.putExtra(FULL_NAME_PRODUCT, fullNameProductCursor)
                productIntent.putExtra(ALC_CODE_PRODUCT, aclCodeProductCursor)

                if (productIntent.resolveActivity(packageManager) != null) {
                    startActivity(productIntent)
                } else {
                    toast(getString(R.string.no_component_intent))
                }
            }
        }
    }
        (findViewById<View>(R.id.listProductions) as ListView).adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            productList
        )

        return observable
    }

    private fun toast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }
}

const val FULL_NAME_PRODUCT = "fullNameProduct"
const val ALC_CODE_PRODUCT = "aclCodeProduct"
const val URI_APPLICATION_SECOND = "com.skillbox.egais_second_application"