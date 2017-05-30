package com.app.android.upbook.feature.user

/**
 * Created by bzumpano on 30/05/17.
 */
interface SignInContract {

    interface View {

        fun onBefore()
        fun onError()
        fun onComplete()
        fun onSuccess(authenticated: Boolean)

    }

    interface Presenter {

        fun signIn(email: String, password: String)

        fun cancel()
    }
}