package com.mahdikaseatashin.shotshot.view.add

import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.mahdikaseatashin.shotshot.App
import com.mahdikaseatashin.shotshot.R
import com.mahdikaseatashin.shotshot.dagger.factory.ViewModelFactory
import com.mahdikaseatashin.shotshot.database.model.UserEntity
import com.mahdikaseatashin.shotshot.databinding.ActivityAddUserBinding
import com.mahdikaseatashin.shotshot.utils.GetImage.captureImage
import com.mahdikaseatashin.shotshot.utils.GetImage.chooseImageFromGallery
import com.mahdikaseatashin.shotshot.utils.GetImage.getImageUri
import com.mahdikaseatashin.shotshot.viewmodel.UserViewModel
import kotlinx.android.synthetic.main.activity_add_user.*
import kotlinx.android.synthetic.main.content_add.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*
import javax.inject.Inject


class AddEditUserActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "AddUserActivity"
        const val PICK_PHOTO_CODE = 1
        const val CAPTURE_PHOTO_CODE = 0
    }

    private var isUpdateUser: Boolean = false
    private var selectedUser: UserEntity? = null

    private lateinit var addViewModel: UserViewModel

    private lateinit var activityAddUserBinding: ActivityAddUserBinding

    @Inject
    lateinit var addViewModelFactory: ViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_user)

        injectDagger()

        getIntentExtras()

        createViewModel()

        initializeToolbar()

        setBinding()

        observeViewModel()

        val items = listOf("Male", "Female", "Both")
        val adapter = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, items)
        (dd_gender_add_field.editText as? AutoCompleteTextView)?.setAdapter(adapter)

        dd_gender_add.setText(selectedUser?.gender, false)

        val items2 = listOf("None", "K", "M")
        val adapter2 = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, items2)
        (follower_number_extension_add_field.editText as? AutoCompleteTextView)?.setAdapter(adapter2)
        follower_number_extension_add.setText(selectedUser?.followerEx, false)

        if (!addViewModel.image.isNullOrEmpty() && addViewModel.image.toString() != "No Image") {
            Log.e(TAG, "createViewModel: ${addViewModel.image}")
            iv_user_add_photo.setImageURI(Uri.parse(addViewModel.image))
        }

    }

    private fun initializeToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.collapsing_toolbar_add)
        if (addViewModel.isUpdateUser)
            toolbar.title = "Update User"
        else
            toolbar.title = "Add User"
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun injectDagger() {
        App.instance.appComponent.inject(this)
    }

    private fun getIntentExtras() {
        if (intent?.extras != null && intent?.extras!!.containsKey("is_update_user"))
            isUpdateUser = intent.getBooleanExtra("is_update_user", false)
        selectedUser = intent?.getParcelableExtra("selected_user") as? UserEntity
    }

    private fun createViewModel() {
        addViewModel = ViewModelProvider(this, addViewModelFactory)[UserViewModel::class.java]
        addViewModel.selectedUser = selectedUser
        addViewModel.isUpdateUser = isUpdateUser
        addViewModel.instaId = selectedUser?.instaId
        selectedUser?.followerNum?.let {
            addViewModel.followerNumber = it.toString()
        }
        addViewModel.followerEx = selectedUser?.followerEx
        addViewModel.gender = selectedUser?.gender
        selectedUser?.interaction?.let {
            addViewModel.interaction = it.toString()
        }
        addViewModel.image = selectedUser?.image
    }

    private fun setBinding() {
        activityAddUserBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_user)
        activityAddUserBinding.viewModel = addViewModel
        activityAddUserBinding.user = selectedUser
        activityAddUserBinding.lifecycleOwner = this
        setContentView(activityAddUserBinding.root)
    }

    private fun observeViewModel() {
        addViewModel.isUserIdEmpty.observe(this, {
            if (!isDestroyed) {
                if (it) edt_insta_id.error = getString(R.string.warning_user_insta_id)
            }
        })

        addViewModel.isUserFollowerEmpty.observe(this, {
            if (!isDestroyed) {
                if (it) edt_follower_number_add.error = getString(R.string.warning_user_follower)
            }
        })
        addViewModel.isUserFollowerExEmpty.observe(this, {
            if (!isDestroyed) {
                if (it) follower_number_extension_add.error =
                    getString(R.string.warning_user_follower_ex)
            }
        })
        addViewModel.isUserInteractionEmpty.observe(this, {
            if (!isDestroyed) {
                if (it) edt_interaction_rate.error = getString(R.string.warning_user_interaction)
            }
        })
        addViewModel.isUserGenderEmpty.observe(this, {
            if (!isDestroyed) {
                if (it) dd_gender_add.error = getString(R.string.warning_user_gender)
            }
        })
        addViewModel.shouldFinishActivity.observe(this, {
            if (!isDestroyed) {
                if (it) finish()
            }
        })
    }

    override fun onBackPressed() {
        MaterialAlertDialogBuilder(this)
            .setTitle("Are you sure?")
            .setPositiveButton("Save") { _, _ ->
                addViewModel.saveUser()
            }
            .setNegativeButton("Discard") { _, _ ->
                super.onBackPressed()
            }.show()
    }

    fun addPhoto(view: View) {
        if (view.id == R.id.fab_add_photo) {
            val items = arrayOf("Capture Image", "Choose From Gallery")
            MaterialAlertDialogBuilder(this)
                .setTitle("Add Image")
                .setItems(items) { _, which ->
                    if (which == 0) {
                        captureImage(this)
                    } else if (which == 1) {
                        chooseImageFromGallery(this)
                    }
                }
                .show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != RESULT_CANCELED) {
            when (requestCode) {
                CAPTURE_PHOTO_CODE -> {
                    if (resultCode == RESULT_OK && data != null) {
                        val capturedImage: Bitmap = data.extras!!.get("data") as Bitmap
                        iv_user_add_photo.setImageBitmap(capturedImage)
                        val cw = ContextWrapper(applicationContext)
                        val directory: File = cw.getDir("imageDir", Context.MODE_PRIVATE)
                        Log.e(TAG, "onActivityResult Path -> ${directory.absolutePath} ")
                        val myPath = File(directory, "${UUID.randomUUID()}.jpg")
                        var fos: FileOutputStream? = null
                        try {
                            fos = FileOutputStream(myPath)
                            // Use the compress method on the BitMap object to write image to the OutputStream
                            capturedImage.compress(Bitmap.CompressFormat.PNG, 100, fos)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        } finally {
                            try {
                                fos?.close()
                            } catch (e: IOException) {
                                e.printStackTrace()
                            }
                        }
                        addViewModel.saveImageInInternalStorage = Uri.parse(filesDir.absolutePath)
                    }
                }

                PICK_PHOTO_CODE -> {
                    if (resultCode == RESULT_OK && data != null) {
                        var bm: Bitmap? = null
                        try {
                            bm = MediaStore.Images.Media.getBitmap(
                                applicationContext.contentResolver,
                                data.data
                            )
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                        addViewModel.saveImageInInternalStorage = bm?.let { getImageUri(this, it) }
                        iv_user_add_photo.setImageBitmap(bm)
                    }
                }
            }
        }

    }

}
