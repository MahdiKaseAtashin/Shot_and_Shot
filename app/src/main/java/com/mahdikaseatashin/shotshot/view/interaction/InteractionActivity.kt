package com.mahdikaseatashin.shotshot.view.interaction

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.mahdikaseatashin.shotshot.App
import com.mahdikaseatashin.shotshot.R
import com.mahdikaseatashin.shotshot.database.model.UserEntity
import com.mahdikaseatashin.shotshot.databinding.ActivityInteractionBinding
import com.mahdikaseatashin.shotshot.view.list.ListActivity
import kotlinx.android.synthetic.main.activity_interaction.*

class InteractionActivity : AppCompatActivity() {

    private var selectedUser: UserEntity? = null

    lateinit var activityInteractionBinding: ActivityInteractionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_interaction)

        injectDagger()

        getIntentExtras()

        setBinding()

        val items = listOf("Male", "Female", "Both")
        val adapter = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, items)
        (dd_gender_field.editText as? AutoCompleteTextView)?.setAdapter(adapter)

        dd_gender.setText(selectedUser?.gender, false)

        btn_calculate_interaction.setOnClickListener {
            if (edt_interaction.text.toString().isNotEmpty() && edt_follower.text.toString()
                    .isNotEmpty()
            )
                listActivity()
            else if (edt_interaction.text.toString().isEmpty())
                edt_interaction.error = "یه عدد یزن که با اختلافش آیدی هارو بیارم!"
            else if (edt_follower.text.toString().isEmpty())
                edt_interaction.error = "یه عدد یزن که با اختلافش آیدی هارو بیارم!"
            else
                Toast.makeText(this, "something went wrong!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun listActivity() {
        val intent = Intent(this, ListActivity::class.java)
        intent.putExtra("selected_user", selectedUser)
        intent.putExtra("interaction_rate", edt_interaction.text.toString().toInt())
        intent.putExtra("follower", edt_follower.text.toString().toLong())
        intent.putExtra("gender", dd_gender.text.toString())
        startActivity(intent)
        finish()
    }


    private fun injectDagger() {
        App.instance.appComponent.inject(this)
    }

    private fun getIntentExtras() {
        selectedUser = intent?.getParcelableExtra("selected_user") as? UserEntity
    }

    private fun setBinding() {
        activityInteractionBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_interaction)
        activityInteractionBinding.user = selectedUser
        activityInteractionBinding.lifecycleOwner = this
        setContentView(activityInteractionBinding.root)
    }


}
