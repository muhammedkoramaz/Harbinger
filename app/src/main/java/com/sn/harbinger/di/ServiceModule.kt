package com.sn.harbinger.di

import com.sn.harbinger.data.remote.NoteService
import com.sn.harbinger.data.remote.ProjectService
import com.sn.harbinger.util.Constants
import com.sn.harbinger.data.remote.UserService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ServiceModule {

    @Singleton
    @Provides
    fun provideInterceptor() = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    @Singleton
    @Provides
    fun provideHttpClient() = OkHttpClient.Builder().apply {
        addInterceptor(provideInterceptor())
    }.build()

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit =
        Retrofit.Builder()
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(provideHttpClient())
            .baseUrl(Constants.TEST_URL)
            .build()

    @Singleton
    @Provides
    fun provideUserApi(retrofit: Retrofit): UserService = retrofit.create(UserService::class.java)

    @Singleton
    @Provides
    fun provideProjectApi(retrofit: Retrofit): ProjectService = retrofit.create(ProjectService::class.java)

    @Singleton
    @Provides
    fun provideNoteApi(retrofit: Retrofit): NoteService = retrofit.create(NoteService::class.java)

}