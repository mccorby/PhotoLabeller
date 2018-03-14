package com.mccorby.movierating.trainer

import android.content.Context
import android.os.Bundle
import android.os.Environment
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.mccorby.movierating.R
import com.mccorby.movierating.filemanager.FileManagerImpl
import com.mccorby.movierating.model.Logger
import com.mccorby.movierating.trainer.presentation.TrainingPresenter
import kotlinx.android.synthetic.main.fragment_training.*
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
        val trainer = MovieTrainerImpl.instance
        val fileManager = FileManagerImpl(context!!.getExternalFilesDir(Environment.DIRECTORY_PICTURES))
        with (trainer as MovieTrainerImpl) {
            trainer.wordVectorsPath = fileManager.loadFile("NewsWordVector2.txt")
            trainer.dataDir = fileManager.rootDir()
        }
        val trainingListener = TrainerLogger(object: Logger {
            override fun log(message: String) {
                println(message)
//                Log.d(TrainingActivityFragment::class.java.simpleName, message)
            }

        })
        presenter = TrainingPresenter(trainer, trainingListener)
        trainModel()
    }

    private fun trainModel() {
        // The async function returns a Deferred<T> instance,
        // and calling await() on it you can get the actual result of the computation.
        launch(UI) {
            val result = async {
                presenter.train()
            }.await()
            loggerArea.append(result.summary)
            Toast.makeText(activity, result.summary, Toast.LENGTH_LONG).show()
        }
    }
}
