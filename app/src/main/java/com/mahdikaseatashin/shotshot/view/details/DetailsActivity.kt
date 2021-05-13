package com.mahdikaseatashin.shotshot.view.details

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.mahdikaseatashin.shotshot.App
import com.mahdikaseatashin.shotshot.R
import com.mahdikaseatashin.shotshot.dagger.factory.ViewModelFactory
import com.mahdikaseatashin.shotshot.database.model.UserEntity
import com.mahdikaseatashin.shotshot.databinding.ActivityDetailsBinding
import com.mahdikaseatashin.shotshot.viewmodel.UserViewModel
import javax.inject.Inject

class DetailsActivity : AppCompatActivity() {

    private var selectedUser: UserEntity? = null

    private lateinit var detailsViewModel: UserViewModel

    private lateinit var activityDetailsUserBinding: ActivityDetailsBinding

    @Inject
    lateinit var detailsViewModelFactory: ViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        injectDagger()

        getIntentExtras()

        initializeToolbar()

        createViewModel()

        setBinding()

        detailsViewModel.shouldFinishActivity.observe(this, {
            if (!isDestroyed) {
                if (it) finish()
            }
        })
    }

    private fun getIntentExtras() {
        selectedUser = intent?.getParcelableExtra("selected_user") as? UserEntity
    }

    private fun injectDagger() {
        App.instance.appComponent.inject(this)
    }

    private fun initializeToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.collapsing_toolbar_details)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun createViewModel() {
        detailsViewModel =
            ViewModelProvider(this, detailsViewModelFactory)[UserViewModel::class.java]
        detailsViewModel.selectedUser = selectedUser
        detailsViewModel.instaId = selectedUser?.instaId
        selectedUser?.followerNum?.let {
            detailsViewModel.followerNumber = it.toString()
        }
        detailsViewModel.followerEx = selectedUser?.followerEx
        detailsViewModel.gender = selectedUser?.gender
        selectedUser?.interaction?.let {
            detailsViewModel.interaction = it.toString()
        }
        detailsViewModel.image = selectedUser?.image
    }

    private fun setBinding() {
        activityDetailsUserBinding = DataBindingUtil.setContentView(this, R.layout.activity_details)
        activityDetailsUserBinding.viewModel = detailsViewModel
        activityDetailsUserBinding.user = selectedUser
        activityDetailsUserBinding.lifecycleOwner = this
        setContentView(activityDetailsUserBinding.root)
    }

}
