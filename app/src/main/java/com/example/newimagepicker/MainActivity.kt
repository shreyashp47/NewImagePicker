package com.example.newimagepicker

import android.os.Bundle
import android.provider.MediaStore.PickerMediaColumns
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia.VisualMediaType
import androidx.activity.result.registerForActivityResult
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.io.File

class MainActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var image: ImageView
    lateinit var singleButton: Button
    lateinit var multipleButton: Button
    lateinit var upload: Button
    lateinit var videoCheckBox: CheckBox
    lateinit var imageCheckBox: CheckBox
    lateinit var filePath: TextView
    var isVideo = false
    var isImage = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        image = findViewById(R.id.image)
        filePath = findViewById(R.id.filePath)
        singleButton = findViewById(R.id.single_button)
        multipleButton = findViewById(R.id.multiple_button)
        videoCheckBox = findViewById(R.id.video_checkbox)
        imageCheckBox = findViewById(R.id.image_checkbox)
        upload = findViewById(R.id.upload)
        imageCheckBox.setOnCheckedChangeListener { buttonView, isChecked -> isImage = isChecked }
        videoCheckBox.setOnCheckedChangeListener { buttonView, isChecked -> isVideo = isChecked }
        singleButton.setOnClickListener(this)
        multipleButton.setOnClickListener(this)
        upload.setOnClickListener(this)
    }

    val picker = registerForActivityResult(PickVisualMedia()) { uri ->
        if (uri != null) {
            Log.d("PhotoPicker", "Selected URI: $uri")
            filePath.text = uri.toString()
        } else {
            Log.d("PhotoPicker", "No media selected")
        }
        image.setImageURI(uri)
    }
    val pickMultipleMedia =
        registerForActivityResult(ActivityResultContracts.PickMultipleVisualMedia(10)) { uris ->
            // Callback is invoked after the user selects media items or closes the
            // photo picker.
            try {
                if (uris.isNotEmpty()) {
                    Log.d("PhotoPicker", "Number of items selected: ${uris.size}")
                    image.setImageURI(uris[0])
                } else {
                    Log.d("PhotoPicker", "No media selected")
                }

            } catch (e: Exception) {
                Log.e("PhotoPicker", "Error loading image", e)
            }
        }

    override fun onClick(v: View?) {
        var type: VisualMediaType = PickVisualMedia.ImageAndVideo
        if (isImage || isVideo) {
            if (isImage && isVideo) type = PickVisualMedia.ImageAndVideo
            else if (isImage) type = PickVisualMedia.ImageOnly
            else if (isVideo) type = PickVisualMedia.VideoOnly
        }else {
            Toast.makeText(this, "Please select image or video", Toast.LENGTH_SHORT).show()
            return
        }
        when (v?.id) {
            R.id.single_button -> {
                picker.launch(PickVisualMediaRequest(type))
            }
            R.id.multiple_button -> {
                pickMultipleMedia.launch(PickVisualMediaRequest(type))
            }
            R.id.upload -> {
                val path = filePath.text.toString()
                uploadFile(path)
            }
            else -> {

            }
        }
    }

    private fun uploadFile(path :String) {
        val imageFile = File(path)
        uploadImageToImgBB(imageFile)
    }
}