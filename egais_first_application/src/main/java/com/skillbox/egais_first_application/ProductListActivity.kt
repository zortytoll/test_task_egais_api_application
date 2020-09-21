package com.skillbox.egais_first_application

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import kotlinx.android.synthetic.main.list_productions.*
import ru.evotor.egais.api.provider.dictionary.ProductInfoContract
import ru.evotor.egais.api.provider.dictionary.ProductInfoContract.URI
import rx.Single
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class ProductListActivity : AppCompatActivity() {

    private val fullNameProduct = ProductInfoContract.COLUMN_FULL_NAME
    private val aclCodeProduct = ProductInfoContract.COLUMN_ALC_CODE

    data class ProductItem(val fullName: String, val alcoCode: String) // пусть будет кастомный класс для модели

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContentView(R.layout.list_productions)

        fetchContacts()
    }

    private fun fetchContacts() {
        Single.fromCallable {
            val productList = mutableListOf<ProductItem>()
            val uri = URI
            val projection = arrayOf(fullNameProduct, aclCodeProduct)
            val selection: String? = null
            val selectionArgs: Array<String>? = null
            val sortOrder: String? = null
            val resolver = contentResolver
            val cursor = resolver.query(uri, projection, selection, selectionArgs, sortOrder)
            cursor?.use {  // получаем данные из курсора
                while (it.moveToNext()) {
                    val fullNameProductCursor = getString(cursor.getColumnIndex(fullNameProduct))
                    val aclCodeProductCursor = getString(cursor.getColumnIndex(aclCodeProduct))

                    productList.add(ProductItem(fullNameProductCursor, aclCodeProductCursor))
                }
            } // после блока кода курсор закроется, все ок
            productList
        }
                .subscribeOn(Schedulers.io()) // получение курсора и чтение данных из него на io потоке
                .observeOn(AndroidSchedulers.mainThread()) // а дальше все на main потоке
                .subscribe({ productList ->
                    listProductions.adapter = ArrayAdapter(
                            this,
                            android.R.layout.simple_list_item_1,
                            productList.map { it.fullName + "\n" + it.alcoCode } // смапим в одну строку — это всего лишь для отображения
                    )
                    // каждому элементу из listProductions соответствует ячейка списка и ячека адаптера
                    listProductions.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
                        val item = productList[position]
                        val uri = Uri.parse(URI_APPLICATION_SECOND)
                        val productIntent = Intent(Intent.ACTION_SEND, uri)
                        productIntent.putExtra(FULL_NAME_PRODUCT, item.fullName)
                        productIntent.putExtra(ALC_CODE_PRODUCT, item.alcoCode)

                        if (productIntent.resolveActivity(packageManager) != null) {
                            startActivity(productIntent)
                        } else {
                            toast(getString(R.string.no_component_intent))
                        }
                    }
                }, { throwable ->
                    Log.e(ProductListActivity::class.java.simpleName, throwable.message, throwable)
                })
    }

    private fun toast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }
}

const val FULL_NAME_PRODUCT = "fullNameProduct"
const val ALC_CODE_PRODUCT = "aclCodeProduct"
const val URI_APPLICATION_SECOND = "com.skillbox.egais_second_application"