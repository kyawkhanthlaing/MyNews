package com.jojoe.mynews.repository

import com.jojoe.mynews.api.NewsAPI
import com.jojoe.mynews.db.ArticleDatabase
import com.jojoe.mynews.model.Article


class NewsRepository(
    private val db: ArticleDatabase,
    private val api: NewsAPI
) {
    suspend fun getNews(country:String,category:String)=
        api.getNews(country,category)

    suspend fun searchNews(searchQuery:String)=
        api.searchForNews(searchQuery)

    suspend fun upsert(article: Article)=db.getArticleDao().upsert(article)

    fun getSavedNews()=db.getArticleDao().getAllArticles()

    suspend fun deleteArticle(article: Article)=db.getArticleDao().deleteArticle(article)

}