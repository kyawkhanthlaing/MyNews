package com.jojoe.mynews.model
import com.jojoe.mynews.model.Article

data class NewsResponse(
    val articles: List<Article>,
    val status: String,
    val totalResults: Int
)