package com.mccorby.photolabeller.labeller

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.Fragment
import android.support.v4.content.FileProvider
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.*
import android.widget.Toast
import com.mccorby.photolabeller.AndroidClientApplication
import com.mccorby.photolabeller.R
import com.mccorby.photolabeller.di.LabellingModule
import com.mccorby.photolabeller.model.Stats
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.fragment_main.view.*
import space.traversal.kapsule.Injects
import space.traversal.kapsule.inject
import space.traversal.kapsule.required
import java.io.File

interface OnLabellingActionsListener {
    fun onModelLoaded()
}

class MainFragment : Fragment(), LabellingView, Injects<LabellingModule>, OnLabelSelectedListener {

    companion object {
        const val REQUEST_TAKE_PHOTO = 2
        val labels = arrayOf("Airplane", "Automobile", "Bird", "Cat", "Deer", "Dog", "Frog", "Horse", "Ship", "Truck")
        fun newInstance(): MainFragment = MainFragment()
    }

    private var currentPhotoPath = ""
    private val labelAdapter by lazy { LabelAdapter(labels, this) }
    private val presenter by required { labellingPresenter }

    private var onLabellingActionListener: OnLabellingActionsListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        retainInstance = true
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        when (context is OnLabellingActionsListener) {
            true -> onLabellingActionListener = context
            false -> println("It would be good if the activity implemented OnLabellingActionsListener but not mandatory")
        }
        injectMembers()
        loadModel()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_main, container, false)

        view.takenPhoto.setOnClickListener { dispatchTakePictureIntent() }
        view.takenPhoto.viewTreeObserver.addOnPreDrawListener(
                object : ViewTreeObserver.OnPreDrawListener {
                    override fun onPreDraw(): Boolean {
                        takenPhoto.viewTreeObserver.removeOnPreDrawListener(this)
                        calculatePhotoButtonOffset(view)
                        return true
                    }
                })
        view.photoButton.setOnClickListener { dispatchTakePictureIntent() }
        calculatePhotoButtonOffset(view)
        buildLabels(view)

        return view
    }

    private fun calculatePhotoButtonOffset(view: View) {
        val offset = view.takenPhoto.height - (view.photoButton.height / 2)
        view.photoButton.translationY = offset.toFloat()
    }

    private fun loadModel() {
        presenter.loadModel()
    }

    override fun onModelLoaded(stats: Stats) {
        onLabellingActionListener?.onModelLoaded()
        Toast.makeText(activity, stats.summary, Toast.LENGTH_LONG).show()
    }

    override fun onResume() {
        super.onResume()
        when (currentPhotoPath.isNotEmpty()) {
            true -> showImage()
            false -> Unit
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detach()
    }

    private var saveAction: MenuItem? = null

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater!!.inflate(R.menu.labelling_actions, menu)
        saveAction = menu!!.findItem(R.id.action_save)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_save -> {
            presenter.saveLabelledImage(currentPhotoPath, view!!.selectedLabel.text.toString().toLowerCase())
            true
        }
        else -> false
    }

    private fun injectMembers() {
        inject(AndroidClientApplication.appModule(context!!))
        presenter.attach(this)
    }

    private fun buildLabels(view: View) {
        view.labelList.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(activity)
            adapter = labelAdapter
        }
    }

    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        // Ensure that there's a camera activity to handle the intent
        takePictureIntent.resolveActivity(activity!!.packageManager)?.let {
            // Create the File where the photo should go
            val photoFile = presenter.createImageFile()
            currentPhotoPath = photoFile.absolutePath
            Log.d(MainActivity::class.java.simpleName, "Current photo path $currentPhotoPath")

            // Continue only if the File was successfully created
            val photoURI = FileProvider.getUriForFile(context!!,
                    "com.mccorby.photolabeller.fileprovider",
                    photoFile)
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?): Unit =
            when {
                requestCode == REQUEST_TAKE_PHOTO && resultCode == Activity.RESULT_OK -> {
                    Log.d(MainActivity::class.java.simpleName, "Photo path $currentPhotoPath")

                    showImage()
                }
                else -> Unit
            }

    private fun showImage() {
        val location = "file://$currentPhotoPath"

        Picasso.with(context).load(location)
                .fit()
                .centerCrop()
                .into(takenPhoto)
        takenPhoto.isClickable = false
        calculatePhotoButtonOffset(view!!)
        photoButton.visibility = View.VISIBLE
        predict()
    }

    private fun predict() {
        when {
            currentPhotoPath.isEmpty() -> Unit
            else -> presenter.predict(File(currentPhotoPath))
        }
    }

    override fun onPrediction(stats: Stats) {
        labelAdapter.setScores(stats.scores)
        labelAdapter.notifyDataSetChanged()
        view!!.selectedLabel.text = labels[stats.selectedResult]
        saveAction?.isVisible = true
        Log.d(MainActivity::class.java.simpleName, stats.summary)
    }

    override fun onLabelSelected(label: String, score: Double) {
        view!!.selectedLabel.text = label
    }
}
