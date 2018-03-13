package com.mccorby.movierating.labeller

import android.content.Context
import android.os.Bundle
import android.os.Environment
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.mccorby.movierating.R
import com.mccorby.movierating.config.SharedConfig
import com.mccorby.movierating.filemanager.FileManagerImpl
import com.mccorby.movierating.labeller.presentation.LabellingPresenter
import com.mccorby.movierating.labeller.presentation.LabellingView
import com.mccorby.movierating.model.Stats
import com.mccorby.movierating.trainer.MovieTrainerImpl
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch

class MainActivityFragment : Fragment(), LabellingView {

    private var currentPhotoPath = ""
    private lateinit var presenter: LabellingPresenter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_main, container, false)

        return view
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        injectMembers()
        // TODO Load model on start
//        loadModel()
    }

    override fun onResume() {
        super.onResume()
        showImage()
    }

    private fun loadModel() {
        launch(UI) {
            val stats = async {
                presenter.loadModel()
            }.await()
            onModelLoaded(stats)
        }
    }

    override fun onModelLoaded(stats: Stats) {
        Toast.makeText(activity, stats.summary, Toast.LENGTH_LONG).show()

        val result = presenter.predict("I went and saw this movie last night after being coaxed to by a few friends of mine. I'll admit that I was reluctant to see it because from what I knew of Ashton Kutcher he was only able to do comedy. I was wrong. Kutcher played the character of Jake Fischer very well, and Kevin Costner played Ben Randall with such professionalism. The sign of a good movie is that it can toy with our emotions. This one did exactly that. The entire theater (which was sold out) was overcome by laughter during the first half of the movie, and were moved to tears during the second half. While exiting the theater I not only saw many women in tears, but many full grown men as well, trying desperately not to let anyone see them crying. This movie was great, and I suggest that you go see it before you judge.")
        Toast.makeText(activity, result.summary, Toast.LENGTH_LONG).show()
    }

    private fun trainModel() {
    }

    private fun injectMembers() {
        val config = SharedConfig(50, 3)
        val fileManager = FileManagerImpl(context!!.getExternalFilesDir(Environment.DIRECTORY_PICTURES))
        val trainer = MovieTrainerImpl.instance
        // TODO This to be fixed injection SharedConfig in Trainer
//        trainer.config = config
        presenter = LabellingPresenter(this, fileManager, trainer)
    }

    private fun showImage() {
        val location = "file://$currentPhotoPath"
        val imageSize = resources.getDimension(R.dimen.image_height).toInt()
        Picasso.with(context).load(R.mipmap.ic_launcher_round)
                .resize(imageSize, imageSize)
                .centerCrop()
                .error(R.mipmap.ic_launcher_round)
                .into(takenPhoto)
    }
}
