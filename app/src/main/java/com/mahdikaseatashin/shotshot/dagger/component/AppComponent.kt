package com.mahdikaseatashin.shotshot.dagger.component

import com.mahdikaseatashin.shotshot.dagger.module.AppModule
import com.mahdikaseatashin.shotshot.dagger.module.RoomDatabaseModule
import com.mahdikaseatashin.shotshot.view.add.AddEditUserActivity
import com.mahdikaseatashin.shotshot.view.details.DetailsActivity
import com.mahdikaseatashin.shotshot.view.interaction.InteractionActivity
import com.mahdikaseatashin.shotshot.view.list.ListActivity
import com.mahdikaseatashin.shotshot.view.main.MainActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [RoomDatabaseModule::class, AppModule::class])
interface AppComponent {
    fun inject(mainActivity: MainActivity)
    fun inject(addEditUserActivity: AddEditUserActivity)
    fun inject(interactionActivity: InteractionActivity)
    fun inject(listActivity: ListActivity)
    fun inject(detailsActivity: DetailsActivity)
}
