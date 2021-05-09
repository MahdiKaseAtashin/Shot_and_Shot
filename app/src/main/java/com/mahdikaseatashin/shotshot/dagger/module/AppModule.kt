package com.mahdikaseatashin.shotshot.dagger.module

import com.mahdikaseatashin.shotshot.database.AppDatabase
import com.mahdikaseatashin.shotshot.repository.CategoryRepository
import com.mahdikaseatashin.shotshot.repository.UserRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {
    @Singleton
    @Provides
    fun providesBookRepository(appDatabase: AppDatabase): UserRepository {
        return UserRepository(appDatabase)
    }

    @Singleton
    @Provides
    fun providesCategoryRepository(appDatabase: AppDatabase): CategoryRepository {
        return CategoryRepository(appDatabase)
    }

}
