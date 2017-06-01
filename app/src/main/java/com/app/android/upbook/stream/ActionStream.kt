package com.app.android.upbook.stream

import com.app.android.upbook.BuildConfig
import java.util.concurrent.Callable

/**
 * Created by bzumpano on 29/05/17.
 */
open class ActionStream<T>(val method: Callable<T>) {

    private var onBefore: () -> Unit = {}
    private var onSuccess: (T) -> Unit = {}
    private var onError: (E: Exception) -> Unit = {}
    private var onComplete: () -> Unit = {}

    var thread : Thread? = null

    fun onBefore(action: () -> Unit): ActionStream<T> {
        this.onBefore = action
        return this
    }

    fun onSuccess(action: (T) -> Unit): ActionStream<T> {
        this.onSuccess = action
        return this
    }

    fun onError(action: (E : Exception) -> Unit): ActionStream<T> {
        this.onError = action
        return this
    }

    fun onComplete(action: () -> Unit): ActionStream<T> {
        this.onComplete = action
        return this
    }

    fun run() : Unit {

        thread = Thread({
            try {
                onBefore()
                onSuccess(method.call())
            }
            catch (e: Exception) {
                onError(e)

                if (BuildConfig.DEBUG) {
                    e.printStackTrace()
                }
            }
            finally {
                onComplete()
            }
        })

        thread!!.start()
    }

    fun cancel() : Unit {
        thread?.interrupt()
    }
}
