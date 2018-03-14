package com.mccorby.movierating.trainer

import com.mccorby.movierating.model.Logger
import org.deeplearning4j.nn.api.Model
import org.deeplearning4j.optimize.api.IterationListener

class TrainerLogger(private val logger: Logger) : IterationListener {
    private var printIterations = 10
    private var invoked = false
    private var iterCount: Int = 0

    override fun iterationDone(model: Model?, iteration: Int) {
        if (printIterations <= 0)
            printIterations = 1
        if (iterCount % printIterations == 0) {
            invoke()
            val result = model!!.score()
            logger.log("Score at iteration $iterCount is $result")
        }
        iterCount++
    }

    override fun invoked(): Boolean {
        return invoked
    }

    override fun invoke() {
        invoked = true
    }
}