package com.heycode.freshnewzz.repository

import com.heycode.freshnewzz.api.RetrofitInstance
import com.heycode.freshnewzz.database.ArticleDatabase
import com.heycode.freshnewzz.models.Article

class NewsRepository(
    val db: ArticleDatabase
) {
    suspend fun getBreakingNews(countryCode: String, pageNumber: Int) =
        RetrofitInstance.api.getBreakingNews(countryCode, pageNumber)

    suspend fun getSearchNews(searchQuery: String, pageNumber: Int) =
        RetrofitInstance.api.searchForNews(searchQuery, pageNumber)

    suspend fun upsert(article: Article) = db.getArticleDAO().upsert(article)

    fun getSavedNews() = db.getArticleDAO().getAllArticles()

    suspend fun deleteArticle(article: Article) = db.getArticleDAO().deleteArticle(article)

}