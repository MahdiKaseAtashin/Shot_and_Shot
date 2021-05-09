package com.mahdikaseatashin.shotshot

import android.app.Application
import com.mahdikaseatashin.shotshot.dagger.component.AppComponent
import com.mahdikaseatashin.shotshot.dagger.component.DaggerAppComponent
import com.mahdikaseatashin.shotshot.dagger.module.RoomDatabaseModule

class App : Application() {

    companion object {
        lateinit var instance: App
    }

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        instance = this

        appComponent = DaggerAppComponent
            .builder()
            .roomDatabaseModule(RoomDatabaseModule(this))
            .build()

    }
}
