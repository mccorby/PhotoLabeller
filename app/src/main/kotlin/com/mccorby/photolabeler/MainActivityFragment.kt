package com.mccorby.photolabeler

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.app.Fragment
import android.support.v4.content.FileProvider
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.mccorby.photolabeler.config.SharedConfig
import com.mccorby.photolabeler.filemanager.FileManager
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.fragment_main.view.*

private const val REQUEST_TAKE_PHOTO = 2

class Main2ActivityFragment : Fragment() {
    private lateinit var fileManager: FileManager
    private lateinit var config: SharedConfig
    private var currentPhotoPath = ""

    companion object {
        val labels = arrayOf("Airplane", "Automobile", "Bird", "Cat", "Deer", "Dog", "Frog", "Horse", "Ship", "Truck")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_main, container, false)

        view.labelImage.setOnClickListener {saveImageLabel()}
        buildLabels(view)
        injectMembers()

        return view
    }

    override fun onResume() {
        super.onResume()
        showImage()
    }

    private fun injectMembers() {
        config = SharedConfig(50, 3)
        fileManager = FileManager(context!!.getExternalFilesDir(Environment.DIRECTORY_PICTURES))
    }

    private fun saveImageLabel() {
        val selectedLabel = labelsDropdown.selectedItem
        fileManager.saveLabelImage(currentPhotoPath, selectedLabel.toString().toLowerCase())
    }

    private fun buildLabels(view: View) {
        val adapter = ArrayAdapter(activity, android.R.layout.simple_spinner_dropdown_item, labels)
        view.labelsDropdown.adapter = adapter
    }

    fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(activity!!.packageManager) != null) {
            // Create the File where the photo should go
            val photoFile = fileManager.createImageFile()
            currentPhotoPath = photoFile.absolutePath
            Log.d(MainActivity::class.java.simpleName, "Current photo path $currentPhotoPath")

            // Continue only if the File was successfully created
            val photoURI = FileProvider.getUriForFile(context!!,
                    "com.mccorby.photolabeler.fileprovider",
                    photoFile)
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == Activity.RESULT_OK) {
            Log.d(MainActivity::class.java.simpleName, "Photo path $currentPhotoPath")

            showImage()
        }
    }

    private fun showImage() {
        val location = "file://$currentPhotoPath"
        val imageSize = resources.getDimension(R.dimen.image_height).toInt()
        Picasso.with(context).load(location)
                .resize(imageSize, imageSize)
                .centerCrop()
                .error(R.mipmap.ic_launcher_round)
                .into(takenPhoto)

    }
}
