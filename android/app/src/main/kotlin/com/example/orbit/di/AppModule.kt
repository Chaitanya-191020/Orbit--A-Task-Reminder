package com.example.orbit.di

import android.content.Context
import androidx.room.Room
import com.example.orbit.data.local.OrbitDatabase
import com.example.orbit.data.remote.OrbitApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        // Use 10.0.2.2 for Android Emulator connecting to localhost
        return Retrofit.Builder()
            .baseUrl("http://10.0.2.2:5000/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideOrbitApiService(retrofit: Retrofit): OrbitApiService {
        return retrofit.create(OrbitApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideOrbitDatabase(@ApplicationContext context: Context): OrbitDatabase {
        return Room.databaseBuilder(
            context,
            OrbitDatabase::class.java,
            "orbit_database"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    fun provideAlarmDao(database: OrbitDatabase) = database.alarmDao()
    
    @Provides
    fun provideTaskDao(database: OrbitDatabase) = database.taskDao()
}
