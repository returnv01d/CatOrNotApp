package pl.wmi.ino.catrecognizer

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.design.widget.Snackbar
import android.support.v4.content.FileProvider
import android.support.v7.app.AppCompatActivity;
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.graphics.Color;
import android.media.MediaPlayer
import android.os.PersistableBundle
import android.support.v4.content.ContextCompat
import android.util.Log
import android.widget.Button

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.concurrent.schedule


class MainActivity : AppCompatActivity() {

    var odinstalujPressed = 0
    var mCurrentPhotoPath = ""

    companion object {
        var day = true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        if (day)
            setTheme(R.style.AppTheme)
        else
            setTheme(R.style.AppThemeNight)

        Log.d("Day", day.toString())

        setContentView(R.layout.activity_main)


        button4.setOnClickListener{
            day = true
            recreate()
        }
        button5.setOnClickListener{
            day = false
            recreate()
        }

        setSupportActionBar(toolbar)

        photoButton.setOnClickListener {
            if (odinstalujPressed == 1) {
                dispatchTakePictureIntent()
            }
            else{
                resultTextView.text = "Aplikacja nie działa"
            }

        }

    }




    
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)


        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings-> {
                // Tutaj dajesz co ma się stać jak klikniesz w coś o id action_settings z menu
                true
            }
            R.id.action_odinstaluj-> {
                odinstalujPressed = 1
                resultTextView.text = ""
            }
        }
        return super.onOptionsItemSelected(item)
    }

    val REQUEST_TAKE_PHOTO = 2

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(packageManager)?.also {
                // Create the File where the photo should go
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    null
                }
                // Continue only if the File was successfully created
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        this,
                        "com.example.android.fileprovider",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO)
                }
            }
        }
    }

    val REQUEST_IMAGE_CAPTURE = 1


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            setPic()
        } else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
        }
    }



    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            mCurrentPhotoPath = absolutePath
        }
    }
    private fun setPic() {
        // Get the dimensions of the View
        val targetW: Int = 1000
        val targetH: Int = 1000

        val bmOptions = BitmapFactory.Options().apply {
            // Get the dimensions of the bitmap
            inJustDecodeBounds = true
            BitmapFactory.decodeFile(mCurrentPhotoPath, this)
            val photoW: Int = outWidth
            val photoH: Int = outHeight

            // Determine how much to scale down the image
            val scaleFactor: Int = Math.min(photoW / targetW, photoH / targetH)

            // Decode the image file into a Bitmap sized to fill the View
            inJustDecodeBounds = false
            inSampleSize = scaleFactor
            inPurgeable = true
        }
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions)?.also { bitmap ->
            imageView.setImageBitmap(bitmap)
        }
        resultTextView.setTextColor(ContextCompat.getColor(this, R.color.colorPrimaryDark))
        resultTextView.text = "Myślę"
        val context = this
        Timer("SettingUp", false).schedule(2500) {
            val (sound, color, text) =
                    if (RandomResult.nextResult())
                        Triple(R.raw.cat, Color.GREEN,"Kot")
                    else
                        Triple(R.raw.not_cat, Color.RED, "Nie kot")
            runOnUiThread {
                resultTextView.text = text
                resultTextView.setTextColor(color)
            }
            val mp = MediaPlayer.create(context, sound);
            mp?.start()
        }
    }



}
