package com.heycode.freshnewzz.ui.fragments

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.heycode.freshnewzz.R
import com.heycode.freshnewzz.adapters.NewsAdapter
import com.heycode.freshnewzz.ui.NewsActivity
import com.heycode.freshnewzz.ui.NewsViewModel
import com.heycode.freshnewzz.util.Constants
import com.heycode.freshnewzz.util.Constants.Companion.COUNTRY_CODE
import com.heycode.freshnewzz.util.Constants.Companion.QUERY_PAGE_SIZE
import com.heycode.freshnewzz.util.Resource
import kotlinx.android.synthetic.main.fragment_breaking_news.*


class BreakingNewsFragment : Fragment(R.layout.fragment_breaking_news) {
    lateinit var viewModel: NewsViewModel
    private var newsAdapter: NewsAdapter = NewsAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewModel = (activity as NewsActivity).viewModel
        setupRecyclerView(newsAdapter)

        newsAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("article", it)
            }
            findNavController().navigate(
                R.id.action_breakingNewsFragment_to_newsArticleFragment,
                bundle
            )
        }

        viewModel.breakingNews.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { newsResponse ->
                        newsAdapter.differ.submitList(newsResponse.articles.toList())
                        val totalPages = newsResponse.totalResults / QUERY_PAGE_SIZE + 2
                        isLastPage = viewModel.breakingNewsPage == totalPages
                        if (isLastPage) {
                            rvBreakingNews.setPadding(0, 0, 0, 0)
                        }
                    }
                }

                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        Toast.makeText(activity, "An Error occurred!\n$message", Toast.LENGTH_LONG)
                            .show()
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }

        })


        //setting spinner
        val spinner: Spinner = requireActivity().findViewById(R.id.country_spinner)

        spinner.setSelection(
            (spinner.adapter as ArrayAdapter<String>).getPosition(
                Constants.sharedPreferences.getString(
                    COUNTRY_CODE,
                    COUNTRY_CODE
                )
            )
        )


        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                Constants.sharedPreferences.edit()
                    .putString(COUNTRY_CODE, spinner.selectedItem.toString())
                    .apply()
                (p1 as TextView).setTextColor(resources.getColor(android.R.color.holo_blue_bright))
                (p1 as TextView).textSize = 15f

                // Loading first data
                Constants.sharedPreferences.getString(COUNTRY_CODE, " ")
                    ?.let { viewModel.getBreakingNews(it) }

                if (spinner.selectedItem.toString() != Constants.sharedPreferences.getString(
                        COUNTRY_CODE, " "
                    )
                ) {
                    Toast.makeText(requireContext(), "Updated data!", Toast.LENGTH_LONG).show()
                    requireActivity().recreate()
                }

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                //Nothing to do
            }
        }
    }

    private fun hideProgressBar() {
        paginationProgressBar.visibility = View.INVISIBLE
        isLoading = false
    }

    private fun showProgressBar() {
        paginationProgressBar.visibility = View.VISIBLE
        isLoading = true
    }

    var isLoading = false
    var isLastPage = false
    var isScrolling = false

    val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= QUERY_PAGE_SIZE
            val shouldPaginate =
                isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning && isTotalMoreThanVisible && isScrolling

            if (shouldPaginate) {
                Constants.sharedPreferences.getString(COUNTRY_CODE, COUNTRY_CODE)
                    ?.let { viewModel.getBreakingNews(it) }
                isScrolling = false
            }
        }

    }


    private fun setupRecyclerView(newsAdapter: NewsAdapter) {
        rvBreakingNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
            addOnScrollListener(this@BreakingNewsFragment.scrollListener)
        }
    }

}