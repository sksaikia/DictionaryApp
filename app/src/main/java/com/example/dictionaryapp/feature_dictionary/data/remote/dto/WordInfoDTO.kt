package com.example.dictionaryapp.feature_dictionary.data.remote.dto

import com.example.dictionaryapp.feature_dictionary.data.local.entity.WordInfoEntity
import com.example.dictionaryapp.feature_dictionary.domain.model.WordInfo

data class WordInfoDTO(
    val meanings: List<MeaningDTO>,
    val phonetic: String,
    val phonetics: List<PhoneticDTO>,
    val word: String
){
    fun toWordInfoEntity(): WordInfoEntity{
        return WordInfoEntity(
            meanings = meanings.map {
               it.toMeaning()
            },
            phonetic = phonetic,
            word = word
        )
    }
}