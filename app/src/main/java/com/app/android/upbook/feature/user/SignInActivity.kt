package com.app.android.upbook.feature.user

import android.app.ProgressDialog
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.app.android.upbook.R
import com.app.android.upbook.data.source.repository.UserRepository

/**
 * A login screen that offers login via email/password.
 */
class SignInActivity : AppCompatActivity(), SignInContract.View {

    @BindView(R.id.input_email)
    lateinit var mEmailView: EditText

    @BindView(R.id.password)
    lateinit var mPasswordView: EditText

    @BindView(R.id.login_form)
    lateinit var mProgressView: View

    @BindView(R.id.login_progress)
    lateinit var mLoginFormView: View

    var presenter : SignInPresenter = SignInPresenter(this, createRepository())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sign_in_activity)

        ButterKnife.bind(this)

        mPasswordView.setOnEditorActionListener(TextView.OnEditorActionListener { _, id, _ ->
            if (id == R.id.login || id == EditorInfo.IME_NULL) {
                attemptLogin()
                return@OnEditorActionListener true
            }
            false
        })
    }

    var dialog: ProgressDialog? = null

    override fun onBefore() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        runOnUiThread({
            dialog = ProgressDialog(this)
            dialog!!.isIndeterminate = true
            dialog!!.setTitle("Autenticando usuário")
            dialog!!.setMessage("Aguarde...")

            dialog!!.show()
        })

    }

    override fun onError() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onSuccess(authenticated: Boolean) {

        val view = findViewById(R.id.login_form)
        val message = Snackbar.make(view, "Authenticated: " + authenticated, Snackbar.LENGTH_INDEFINITE)

        message.setAction("OK") { message.dismiss() }
        message.show()
    }

    override fun onComplete() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        runOnUiThread({ dialog?.hide() })

    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    @OnClick(R.id.email_sign_in_button)
    fun attemptLogin() {

        resetErrors()

        // Store values at the time of the login attempt.
        val email = mEmailView.text.toString()
        val password = mPasswordView.text.toString()

        var cancel = false
        var focusView: View? = null

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)) {
            mPasswordView.error = getString(R.string.error_field_required)
            focusView = mPasswordView
            cancel = true
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.error = getString(R.string.error_field_required)
            focusView = mEmailView
            cancel = true
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView!!.requestFocus()
        } else {
            presenter.signIn(email, password)
        }
    }

    private fun createRepository(): UserRepository {
        return UserRepository()
    }

    private fun resetErrors() {
        mEmailView.error = null
        mPasswordView.error = null
    }
}

