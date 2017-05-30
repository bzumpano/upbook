package com.app.android.upbook.feature.user

import com.app.android.upbook.data.source.repository.UserRepository
import com.app.android.upbook.stream.ActionStream
import java.util.concurrent.Callable

/**
 * Created by bzumpano on 30/05/17.
 */
class SignInPresenter(val view: SignInContract.View, val repository: UserRepository): SignInContract.Presenter {

    var stream: ActionStream<Boolean>? = null

    override fun signIn(email: String, password: String) : Unit {
        stream = ActionStream<Boolean>(Callable { repository.signIn(email, password) })

        stream!!.onBefore { view.onBefore() }
                .onSuccess {authenticated -> view.onSuccess(authenticated as Boolean)}
                .onError { view.onError() }
                .onComplete { view.onComplete() }
                .run()
    }

    override fun cancel() {
        stream?.cancel()
    }
}
