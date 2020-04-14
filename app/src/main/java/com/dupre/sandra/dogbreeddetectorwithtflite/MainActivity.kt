package com.dupre.sandra.dogbreeddetectorwithtflite

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import android.widget.ImageView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.io.IOException
import java.util.*


class MainActivity : AppCompatActivity(), DogView {

    private lateinit var dogDetector: DogDetector
    private val SELECT_PICTURE = 1
    companion object {
        private const val REQUEST_IMAGE_CAPTURE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        dogDetector = DogDetector(this)
        dogDetector.view = this

        /*imageView.setOnClickListener {
            Intent(MediaStore.ACTION_IMAGE_CAPTURE).let {
                if (it.resolveActivity(packageManager) != null) {
                    startActivityForResult(it, REQUEST_IMAGE_CAPTURE)
                }
            }
        }*/

        imageView.setOnClickListener{
            intent = Intent()
            intent.setType("image/*")
            intent.setAction(Intent.ACTION_GET_CONTENT)

            startActivityForResult(Intent.createChooser(intent,"Selecione uma imagem"), SELECT_PICTURE)
        }
    }

    override fun onDestroy() {
        dogDetector.view = null
        super.onDestroy()
    }

    /*override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            (data?.extras?.get("data") as Bitmap).apply {
                Bitmap.createBitmap(this, 0, height / 2 - width / 2, width, width).let {
                    imageView.setImageBitmap(it)
                    imageView.scaleType = ImageView.ScaleType.CENTER_CROP
                    dogDetector.recognizeDog(bitmap = it)
                }
            }
        }
    }*/

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SELECT_PICTURE) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    try {
                        var it = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.data)
                        imageView.setImageBitmap(it)
                        dogDetector.recognizeDog(bitmap = it)

                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(this, "Canceled", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun displayDogBreed(dogBreed: String, winPercent: Float) {
        textView.text = String.format(Locale.FRANCE, getString(R.string.dog_result), dogBreed,
            winPercent)
    }

    override fun displayError() {
        Toast.makeText(this, R.string.error, Toast.LENGTH_SHORT).show()
    }

}
