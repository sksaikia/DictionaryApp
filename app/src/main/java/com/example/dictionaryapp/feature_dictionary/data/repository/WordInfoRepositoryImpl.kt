package com.example.dictionaryapp.feature_dictionary.data.repository

import com.example.dictionaryapp.core.util.Resource
import com.example.dictionaryapp.feature_dictionary.data.local.WordInfoDao
import com.example.dictionaryapp.feature_dictionary.data.remote.DictionaryAPI
import com.example.dictionaryapp.feature_dictionary.domain.model.WordInfo
import com.example.dictionaryapp.feature_dictionary.domain.repository.WordInfoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

class WordInfoRepositoryImpl(
    private val api : DictionaryAPI,
    private val dao: WordInfoDao
) : WordInfoRepository {

    override fun getWordInfo(word: String):
            Flow<Resource<List<WordInfo>>> = flow{
        emit(Resource.Loading())

        //These are cached word infos
        val wordInfos = dao.getWordInfos(word).map { it.toWordInfo() }
        emit(Resource.Loading(data = wordInfos))

        try {
            val remoteWordInfos = api.getWordInfo(word)
            dao.deleteWordInfos(remoteWordInfos.map { it.word })
            dao.insertWordInfo(remoteWordInfos.map { it.toWordInfoEntity() })
        } catch (e: HttpException){
            emit(Resource.Error(
                message = "Oops. SOmething went wrong",
                data = wordInfos))
        } catch (e: IOException){
            emit(Resource.Error(
                message = "Could not reach server, check your internet connection",
                data = wordInfos))
        }

        val newWordInfos = dao.getWordInfos(word).map { it.toWordInfo() }
        emit(Resource.Success(newWordInfos))
    }
}