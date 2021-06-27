package com.sample.myapplauncher

import com.sample.appsmodule.AppsRepository
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { AppsListViewModel(appsRepository = get()) }
}

val repositoryModule = module {
    single { AppsRepository(context = androidApplication()) }
}