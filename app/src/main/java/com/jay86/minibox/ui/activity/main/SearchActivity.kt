package com.jay86.minibox.ui.activity.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.jay86.minibox.R
import com.jay86.minibox.bean.BoxGroup
import com.jay86.minibox.network.RequestManager
import com.jay86.minibox.network.observer.BaseObserver
import com.jay86.minibox.ui.activity.BaseActivity
import com.jay86.minibox.ui.adapter.SearchRvAdapter
import com.jay86.minibox.utils.extension.hideKeyBoard
import com.jay86.minibox.utils.extension.showKeyBoard
import kotlinx.android.synthetic.main.activity_search.*
import org.jetbrains.anko.toast

class SearchActivity : BaseActivity(), TextWatcher {

    private lateinit var adapter: SearchRvAdapter
    private var list: ArrayList<BoxGroup> = arrayListOf()

    private var isLoading: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        overridePendingTransition(0, 0)

        searchView.showKeyBoard()
        searchView.addTextChangedListener(this)
        /*searchView.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                if (list.isNotEmpty() && !isLoading) {
                    setResult(Activity.RESULT_OK, Intent().putParcelableArrayListExtra("data", list))
                    finish()
                }
                true
            } else {
                false
            }
        }*/

        clearButton.setOnClickListener {
            searchView.setText("")
            searchView.showKeyBoard()
        }
        backButton.setOnClickListener {
            setResult(Activity.RESULT_CANCELED)
            finish()
        }
        initRv()
    }

    private fun initRv() {
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        rv.layoutManager = layoutManager
        adapter = SearchRvAdapter {
            setResult(Activity.RESULT_OK, Intent().putParcelableArrayListExtra("data", arrayListOf(it)))
            finish()
        }
        rv.adapter = adapter
    }

    private fun search(str: String) {
        rv.visibility = View.INVISIBLE
        loadingText.visibility = View.VISIBLE
        isLoading = true

        RequestManager.searchByDestination(str, object : BaseObserver<List<BoxGroup>>() {
            override fun onNext(_object: List<BoxGroup>) {
                super.onNext(_object)
                list.clear()
                list.addAll(_object)
                rv.visibility = View.VISIBLE
                loadingText.visibility = View.INVISIBLE
                adapter.resetData(_object)
                isLoading = false
            }

            override fun onError(e: Throwable) {
                super.onError(e)
                toast(e.message ?: "连接超时")
                rv.visibility = View.VISIBLE
                loadingText.visibility = View.INVISIBLE
                isLoading = false
            }
        })
    }

    override fun afterTextChanged(s: Editable) = search(s.toString())

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit

    override fun onDestroy() {
        super.onDestroy()
        searchView.hideKeyBoard()
    }
}
