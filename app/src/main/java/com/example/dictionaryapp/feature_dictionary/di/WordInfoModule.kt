package com.example.dictionaryapp.feature_dictionary.di

import android.app.Application
import androidx.room.Room
import com.example.dictionaryapp.feature_dictionary.data.local.Converters
import com.example.dictionaryapp.feature_dictionary.data.local.WordInfoDao
import com.example.dictionaryapp.feature_dictionary.data.local.WordInfoDatabase
import com.example.dictionaryapp.feature_dictionary.data.remote.DictionaryAPI
import com.example.dictionaryapp.feature_dictionary.data.repository.WordInfoRepositoryImpl
import com.example.dictionaryapp.feature_dictionary.data.util.GsonParser
import com.example.dictionaryapp.feature_dictionary.domain.model.WordInfo
import com.example.dictionaryapp.feature_dictionary.domain.repository.WordInfoRepository
import com.example.dictionaryapp.feature_dictionary.domain.usecases.GetWordInfoUseCase
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object WordInfoModule {

    @Provides
    @Singleton
    fun provideGetInfoUseCase(repository: WordInfoRepository) : GetWordInfoUseCase {
        return GetWordInfoUseCase(repository = repository)
    }

    @Provides
    @Singleton
    fun provideWordInfoRepository(
        db : WordInfoDatabase,
        api : DictionaryAPI
    ): WordInfoRepository {
        return WordInfoRepositoryImpl(api, db.dao)
    }

    @Provides
    @Singleton
    fun provideWordInfoDatabase(app : Application): WordInfoDatabase{
        return Room.databaseBuilder(
            app, WordInfoDatabase::class.java,"word_db"
        ).addTypeConverter(Converters(GsonParser(Gson())))
            .build()
    }

    @Provides
    @Singleton
    fun providesDictionaryApi() : DictionaryAPI {
        return Retrofit.Builder()
            .baseUrl(DictionaryAPI.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(DictionaryAPI::class.java)
    }

}