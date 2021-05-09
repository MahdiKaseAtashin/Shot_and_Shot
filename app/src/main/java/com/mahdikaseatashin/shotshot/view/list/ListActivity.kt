package com.mahdikaseatashin.shotshot.view.list

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.mahdikaseatashin.shotshot.App
import com.mahdikaseatashin.shotshot.R
import com.mahdikaseatashin.shotshot.adapter.UserRecyclerViewAdapter
import com.mahdikaseatashin.shotshot.dagger.factory.ViewModelFactory
import com.mahdikaseatashin.shotshot.database.model.UserEntity
import com.mahdikaseatashin.shotshot.databinding.ActivityListBinding
import com.mahdikaseatashin.shotshot.viewmodel.UserViewModel
import kotlinx.android.synthetic.main.activity_list.*
import javax.inject.Inject

class ListActivity : AppCompatActivity() {

    private var selectedUser: UserEntity? = null
    private var selectedRate: Int? = null
    private var selectedFollower: Long? = null
    private var selectedGender: String? = null

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var binding: ActivityListBinding

    private var userAdapter: UserRecyclerViewAdapter? = null

    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        injectDagger()

        createViewModel()

        getExtras()

        setBinding()

        setUserAdapter()

    }

    private fun setUserAdapter() {
        rv_list_users.layoutManager = LinearLayoutManager(this)
        val minRate = selectedUser!!.interaction - selectedRate!!
        val minFollower = selectedUser!!.follower - selectedFollower!!
        val maxRate = selectedUser!!.interaction + selectedRate!!
        val maxFollower = selectedUser!!.follower + selectedFollower!!

        if (selectedGender != "Both")
            userViewModel.getUserByIFG(minRate, maxRate, minFollower, maxFollower, selectedGender!!)
                .observe(this, {
                    userAdapter = UserRecyclerViewAdapter(it,selectedUser)
                    rv_list_users.adapter = userAdapter
                })
        else
            userViewModel.getUserByIF(minRate, maxRate, minFollower, maxFollower)
                .observe(this, {
                    userAdapter = UserRecyclerViewAdapter(it,selectedUser)

                    rv_list_users.adapter = userAdapter
                })

        fast_scroll_list.setRecyclerView(rv_list_users)
    }

    private fun getExtras() {
        selectedUser = intent?.getParcelableExtra("selected_user") as? UserEntity
        selectedRate = intent?.getIntExtra("interaction_rate", 0)
        selectedFollower = intent?.getLongExtra("follower", 0)
        selectedGender = intent?.getStringExtra("gender")
    }

    private fun setBinding() {
        binding = ActivityListBinding.inflate(layoutInflater)
        binding.viewModel = userViewModel
        binding.user = selectedUser
        binding.lifecycleOwner = this
        setContentView(binding.root)
    }

    private fun createViewModel() {
        userViewModel = ViewModelProvider(this, viewModelFactory)[UserViewModel::class.java]

    }

    private fun injectDagger() {
        App.instance.appComponent.inject(this)
        userViewModel = ViewModelProvider(this, viewModelFactory)[UserViewModel::class.java]
    }
}
