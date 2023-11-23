package com.example.newsarticlesdemo.base

import androidx.fragment.app.Fragment

abstract class BaseFragment:Fragment() {
    abstract fun setUpViewModel()
    abstract fun setUpObserver()
    abstract fun setUpClickListener()
}