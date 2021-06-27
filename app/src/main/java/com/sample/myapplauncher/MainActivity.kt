package com.sample.myapplauncher

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val appsListViewModel: AppsListViewModel by viewModel()
    private lateinit var appListAdapter: AppListAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
        initObserver()
        fetchApps()
    }

    private fun initView() {

        val layoutManager = GridLayoutManager(this, 4)
        appLauncherRecyclerView.layoutManager = layoutManager

        searchAppEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                appListAdapter.filter.filter(s)
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })
    }

    private fun fetchApps() {
        appsListViewModel.showLoader()
        appsListViewModel.fetchAppsList()
    }

    private fun initObserver() {
        appsListViewModel.appsList.observe(this, { appsList ->
            appsListViewModel.hideLoader()
            appListAdapter = AppListAdapter(appsList.sortedBy { it.label })
            appLauncherRecyclerView.adapter = appListAdapter
        })

        appsListViewModel.showLoader.observe(this, { isShown ->
            handleLoader(isShown)
        })
    }

    override fun onBackPressed() {
        val i = Intent()
        i.action = Intent.ACTION_MAIN
        i.addCategory(Intent.CATEGORY_HOME)
        this.startActivity(i)
        finish()
    }

    private fun handleLoader(show: Boolean) {
        generic_loader.visibility = if (show) View.VISIBLE else View.GONE
    }
}