package com.heycode.freshnewzz.ui

import androidx.lifecycle.ViewModel
import com.heycode.freshnewzz.repository.NewsRepository

class NewsViewModel(
    val newsRepository: NewsRepository
) : ViewModel() {
}