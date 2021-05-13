package com.mahdikaseatashin.shotshot.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.doyaaaaaken.kotlincsv.dsl.csvWriter
import com.mahdikaseatashin.shotshot.R
import com.mahdikaseatashin.shotshot.database.model.UserEntity
import com.mahdikaseatashin.shotshot.utils.Constants
import kotlinx.android.synthetic.main.activity_save.*
import java.io.File


class SaveActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_save)
        btn_export_csv.setOnClickListener {

        }
    }


}
