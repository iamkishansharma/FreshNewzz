package com.heycode.freshnewzz.ui

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.heycode.freshnewzz.R
import com.heycode.freshnewzz.database.ArticleDatabase
import com.heycode.freshnewzz.repository.NewsRepository
import com.heycode.freshnewzz.util.Constants
import kotlinx.android.synthetic.main.activity_news.*

class NewsActivity : AppCompatActivity() {

    lateinit var viewModel: NewsViewModel
    lateinit var context: Context

    //saving context for shared pref in Constants
    init {
        Constants.MyClass.setContext(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setSupportActionBar(toolbar)

        setContentView(R.layout.activity_news)
//        supportActionBar?.title

        val newsRepository = NewsRepository(ArticleDatabase(this))
        val viewModelProviderFactory = NewsViewModelProviderFactory(application, newsRepository)

        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(NewsViewModel::class.java)

        bottomNavigationView.setupWithNavController(newsNavHostFragment.findNavController())
    }

}
