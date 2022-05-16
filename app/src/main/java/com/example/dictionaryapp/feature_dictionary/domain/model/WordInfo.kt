package com.example.dictionaryapp.feature_dictionary.domain.model

import com.example.dictionaryapp.feature_dictionary.data.remote.dto.MeaningDTO
import com.example.dictionaryapp.feature_dictionary.data.remote.dto.PhoneticDTO

data class WordInfo(
    val meanings: List<Meaning>,
    val phonetic: String,
    val word: String
)
