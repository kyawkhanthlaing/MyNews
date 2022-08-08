package com.jojoe.mynews.repository

import com.jojoe.mynews.api.RetrofitInstance
import com.jojoe.mynews.db.ArticleDatabase
import com.jojoe.mynews.model.Article


class NewsRepository(
    private val db: ArticleDatabase
) {
    suspend fun getBreakingNews(countryCode: String, pageNumber: Int)=
        RetrofitInstance.api.getBreakingNews(countryCode, pageNumber)

    suspend fun searchNews(searchQuery:String,pageNumber: Int)=
        RetrofitInstance.api.searchForNews(searchQuery,pageNumber)

    suspend fun upsert(article: Article)=db.getArticleDao().upsert(article)

    fun getSavedNews()=db.getArticleDao().getAllArticles()

    suspend fun deleteArticle(article: Article)=db.getArticleDao().deleteArticle(article)

}