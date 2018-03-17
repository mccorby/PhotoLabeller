package com.mccorby.photolabeller.trainer

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mccorby.photolabeller.AndroidClientApplication
import com.mccorby.photolabeller.R
import com.mccorby.photolabeller.config.SharedConfig
import com.mccorby.photolabeller.di.TrainingModule
import com.mccorby.photolabeller.model.IterationLogger
import com.mccorby.photolabeller.model.Stats
import kotlinx.android.synthetic.main.fragment_training.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import space.traversal.kapsule.Injects
import space.traversal.kapsule.inject
import space.traversal.kapsule.required

class TrainingFragment : Fragment(), Injects<TrainingModule>, TrainingView {

    companion object {
        fun newInstance(): TrainingFragment = TrainingFragment()
    }

    private val presenter by required { trainingPresenter }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_training, container, false)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        injectMembers()
    }

    override fun onStart() {
        super.onStart()
        trainModel()
    }

    override fun onStartTraining(config: SharedConfig) {
        with(loggerArea) {
            append(getString(R.string.training_started))
            append("\n")
        }

        modelName.text = getString(R.string.model_name, config.modelFilename)
        numberOfImages.text = getString(R.string.using_images, config.maxSamples)
        numberOfCategories.text = getString(R.string.with_categories, config.labels.size)
        epochs.text = getString(R.string.with_epochs, config.maxEpochs)
        modelDescription.text = getString(R.string.with_model_layers, config.featureLayerIndex + 1)
    }

    override fun showTrainingStats(stats: Stats) {
        loggerArea.append(stats.summary + "\n")
    }


    private fun injectMembers() {
        inject(AndroidClientApplication.appModule(context!!))
        presenter.attach(this)
        val trainer = TrainerImpl.instance
        trainer.iterationLogger = object : IterationLogger {
            override fun onIterationDone(message: String) {
                launch(UI) {
                    loggerArea.append(message + "\n")
                }
            }
        }
    }

    private fun trainModel() {
        presenter.train()
    }
}
