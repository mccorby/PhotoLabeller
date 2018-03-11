package com.mccorby.photolabeler

import android.content.Context
import android.os.Bundle
import android.os.Environment
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.mccorby.photolabeler.config.SharedConfig
import com.mccorby.photolabeler.filemanager.FileManagerImpl
import com.mccorby.photolabeler.presentation.TrainingPresenter
import com.mccorby.photolabeler.trainer.TrainerImpl
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
        val trainer = TrainerImpl(config)
        val fileManager = FileManagerImpl(context!!.getExternalFilesDir(Environment.DIRECTORY_PICTURES))
        presenter = TrainingPresenter(trainer, fileManager)
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
