package com.mahdikaseatashin.shotshot.view.main

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.mahdikaseatashin.shotshot.App
import com.mahdikaseatashin.shotshot.R
import com.mahdikaseatashin.shotshot.adapter.UserRecyclerViewAdapter
import com.mahdikaseatashin.shotshot.dagger.factory.ViewModelFactory
import com.mahdikaseatashin.shotshot.database.model.UserEntity
import com.mahdikaseatashin.shotshot.databinding.ActivityMainBinding
import com.mahdikaseatashin.shotshot.view.add.AddEditUserActivity
import com.mahdikaseatashin.shotshot.viewmodel.UserViewModel
import com.opencsv.CSVReader
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.*
import java.util.*
import javax.inject.Inject


class MainActivity : AppCompatActivity() {


    companion object {
        private val PERMISSIONS_STORAGE = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        private const val REQUEST_EXTERNAL_STORAGE = 1
        private const val ACTIVITY_CHOOSE_FILE = 2
        private const val TAG = "MainActivity"
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private var sortSelect = 0

    private lateinit var userListClickHandler: UserListClickHandlers

    private lateinit var binding: ActivityMainBinding

    private lateinit var sortAdapter: ArrayAdapter<String>

    private var userAdapter: UserRecyclerViewAdapter? = null

    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        injectDagger()

        createViewModel()

        setBinding()

        observeViewModel()

        setUserAdapter()

        iv_export_csv.setOnClickListener {

            val items = arrayOf("Export CSV", "Import CSV")
            MaterialAlertDialogBuilder(this)
                .setTitle("What do you want to do?")
                .setItems(items) { _, which ->
                    if (which == 0) {
                        exportData()
                    } else if (which == 1) {
                        importData()
                    }
                }
                .show()
        }
    }

    private fun importData() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "*/*"
        startActivityForResult(Intent.createChooser(intent, "Open CSV"), ACTIVITY_CHOOSE_FILE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ACTIVITY_CHOOSE_FILE && resultCode == RESULT_OK && data != null) {
            try {
                var path = data.data?.path.toString()
                path = "/storage/emulated/0/" + path.substring(path.indexOf(':') + 1)
                Log.e(TAG, "onActivityResult: $path")
                val file = File(path)
                val reader = CSVReader(FileReader(file))

                val myEntries: List<*> = reader.readAll()
                var i = 0



                while (i < myEntries.size) {
                    val nextLine = myEntries[i] as Array<*>
//                    Log.e(TAG, "onActivityResult:ID ${nextLine[0]}")
//                    Log.e(TAG, "onActivityResult:Insta ID ${nextLine[1]}")
//                    Log.e(TAG, "onActivityResult:Follower ${nextLine[2]}")
//                    Log.e(TAG, "onActivityResult:Interaction ${nextLine[3]}")
//                    Log.e(TAG, "onActivityResult:Gender ${nextLine[4]}")
//                    Log.e(TAG, "onActivityResult:Phone Number ${nextLine[5]}")
//                    Log.e(TAG, "onActivityResult:Follower Extension ${nextLine[6]}")
//                    Log.e(TAG, "onActivityResult:Follower Number ${nextLine[7]}")
//                    Log.e(TAG, "onActivityResult:Interaction Number ${nextLine[8]}")
                    if (i > 0) {
                        val user = UserEntity(
                            0,
                            nextLine[1].toString(),
                            nextLine[7].toString().toLong(),
                            nextLine[2].toString().toLong(),
                            nextLine[6].toString(),
                            nextLine[4].toString(),
                            nextLine[3].toString().toInt(),
                            "No Image",
                            nextLine[5].toString(),
                            nextLine[8].toString().toInt()
                        )
                        userViewModel.addUser(user)
                        updateUserList(sortSelect)
                    }
                    i++
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this, "something went wrong", Toast.LENGTH_SHORT).show()
                Log.e(TAG, "onActivityResult: ${e.printStackTrace()}")
            }

        }
    }

    private fun exportData() {
        MaterialAlertDialogBuilder(this)
            .setTitle("Export Data?")
            .setPositiveButton("Yes") { _, _ ->
                verifyStoragePermissions(this)
            }
            .setNegativeButton("Cancel") { _, _ ->

            }.show()
    }

    private fun createViewModel() {
        userViewModel = ViewModelProvider(this, viewModelFactory)[UserViewModel::class.java]
        userListClickHandler = UserListClickHandlers()
    }

    private fun setBinding() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        binding.viewModel = userViewModel
        binding.lifecycleOwner = this
        binding.clickHandlers = userListClickHandler
        setContentView(binding.root)
    }

    private fun observeViewModel() {
        userViewModel.isLoading.value = true
        setDataToSpinner()
    }

    private fun setDataToSpinner() {
        val sortList: List<String> = listOf("Insta ID", "Follower", "Interaction")
        sortAdapter = ArrayAdapter(this, R.layout.list_user_categories, sortList)
        sortAdapter.setDropDownViewResource(R.layout.list_user_categories)
        binding.spinnerAdapter = sortAdapter
    }


    private fun setUserAdapter() {
        rv_list_all_users.layoutManager = LinearLayoutManager(this)

        rv_list_all_users.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!(dy <= 0 || fab_add_user.visibility !== View.VISIBLE)) {
                    fab_add_user.hide()
                } else if (dy < 0 && fab_add_user.visibility !== View.VISIBLE) {
                    fab_add_user.show()
                }
            }
        })
        val itemTouchHelper = ItemTouchHelper(simpleItemTouchCallback)
        itemTouchHelper.attachToRecyclerView(rv_list_all_users)
        when (sortSelect) {
            0 -> {
                userViewModel.getAllUsers().observe(this, {
                    userAdapter = UserRecyclerViewAdapter(it, null)
                    rv_list_all_users.adapter = userAdapter
                })
            }
            1 -> {
                userViewModel.getAllUsersSortByFollower().observe(this, {
                    userAdapter = UserRecyclerViewAdapter(it, null)
                    rv_list_all_users.adapter = userAdapter
                })
            }
            2 -> {
                userViewModel.getAllUsersSortByInteraction().observe(this, {
                    userAdapter = UserRecyclerViewAdapter(it, null)
                    rv_list_all_users.adapter = userAdapter
                })
            }
        }
        fast_scroll_all.setRecyclerView(rv_list_all_users)
    }

    private fun injectDagger() {
        App.instance.appComponent.inject(this)
        userViewModel = ViewModelProvider(this, viewModelFactory)[UserViewModel::class.java]
    }

    private fun updateUserList(position: Int) {
        if (userAdapter != null) {
            sortSelect = position
            when (sortSelect) {
                0 -> {
                    userViewModel.getAllUsers().observe(this, {
                        if (!isDestroyed) {
                            showUserList(it)
                        }
                    })
                }
                1 -> {
                    userViewModel.getAllUsersSortByFollower().observe(this, {
                        if (!isDestroyed) {
                            showUserList(it)
                        }
                    })
                }
                2 -> {
                    userViewModel.getAllUsersSortByInteraction().observe(this, {
                        if (!isDestroyed) {
                            showUserList(it)
                        }
                    })
                }
            }
        }
    }

    private fun showUserList(userList: List<UserEntity>) {
        if (userAdapter == null) {
            rv_list_all_users.layoutManager = LinearLayoutManager(
                this
            )
            userAdapter = UserRecyclerViewAdapter(userList, null)
            rv_list_all_users.adapter = userAdapter
            val itemTouchHelper = ItemTouchHelper(simpleItemTouchCallback)
            itemTouchHelper.attachToRecyclerView(rv_list_all_users)
        } else
            userAdapter?.updateUserList(userList)
    }

    override fun onResume() {
        super.onResume()
        setUpdatedUserList()
    }

    private var simpleItemTouchCallback: ItemTouchHelper.SimpleCallback =
        object : ItemTouchHelper.SimpleCallback(
            0 or 2,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                Toast.makeText(this@MainActivity, "hit", Toast.LENGTH_SHORT).show()
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {
                if (swipeDir == ItemTouchHelper.LEFT) {
                    if (viewHolder is UserRecyclerViewAdapter.UserViewHolder) {
                        userViewModel.deleteUser(viewHolder.dataBinding.user as UserEntity)
                        setUpdatedUserList()
                    }
                }
                if (swipeDir == ItemTouchHelper.RIGHT) {
                    if (viewHolder is UserRecyclerViewAdapter.UserViewHolder) {
                        val intent = Intent(this@MainActivity, AddEditUserActivity::class.java)
                        intent.putExtra(
                            "selected_user",
                            viewHolder.dataBinding.user as UserEntity
                        )
                        intent.putExtra("is_update_user", true)
                        startActivity(intent)
                        setUpdatedUserList(viewHolder.position)
                    }
                }
            }
        }

    inner class UserListClickHandlers {
        fun onFABClicked(view: View) {
            val intent = Intent(view.context, AddEditUserActivity::class.java)
            startActivity(intent)
        }

        fun onCategorySelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            Log.e(TAG, position.toString())
            updateUserList(position)
        }
    }

    private fun setUpdatedUserList() {
        Handler(Looper.myLooper()!!).postDelayed({
            if (!isDestroyed) {
                updateUserList(sortSelect)
            }
        }, 100)
    }

    private fun setUpdatedUserList(position: Int) {
        Handler(Looper.myLooper()!!).postDelayed({
            if (!isDestroyed) {
                updateUserList(sortSelect)
                userAdapter?.notifyItemChanged(position)
            }
        }, 500)
    }

    fun verifyStoragePermissions(activity: Activity?) {
        // Check if we have write permission
        val permission = ActivityCompat.checkSelfPermission(
            activity!!,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                activity,
                PERMISSIONS_STORAGE,
                REQUEST_EXTERNAL_STORAGE
            )
        } else {
            CoroutineScope(Dispatchers.IO).launch {
                userViewModel.writeToCsv()
            }
        }
    }
}
