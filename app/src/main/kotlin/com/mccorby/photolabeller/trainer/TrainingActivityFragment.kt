package com.mccorby.photolabeller.trainer

import android.content.Context
import android.os.Bundle
import android.os.Environment
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.mccorby.movierating.trainer.MovieTrainerImpl
import com.mccorby.photolabeller.R
import com.mccorby.photolabeller.config.SharedConfig
import com.mccorby.photolabeller.filemanager.FileManagerImpl
import com.mccorby.photolabeller.trainer.presentation.TrainingPresenter
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch

/**
 * A placeholder fragment containing a simple view.
 */
class TrainingActivityFragment : Fragment() {

    private lateinit var presenter: TrainingPresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_training, container, false)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        injectMembers()
    }

    private fun injectMembers() {
        val config = SharedConfig(50, 3)
        val trainer = MovieTrainerImpl.instance
//        trainer.config = config
        val fileManager = FileManagerImpl(context!!.getExternalFilesDir(Environment.DIRECTORY_PICTURES))
        with (receiver = trainer as MovieTrainerImpl) {
            trainer.wordVectorsPath = fileManager.loadFile("NewsWordVector.txt")
            trainer.dataDir = fileManager.rootDir()
        }

        presenter = TrainingPresenter(trainer, fileManager)
        trainModel()
    }

    private fun trainModel() {
        // The async function returns a Deferred<T> instance,
        // and calling await() on it you can get the actual result of the computation.
        launch(UI) {
            val result = async {
                presenter.train()
            }.await()

            Toast.makeText(activity, result.summary, Toast.LENGTH_LONG).show()
        }
    }
}
