package com.app.android.upbook.data.source.repository

import org.jsoup.Connection
import org.jsoup.Connection.Response
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

/**
 * Created by bzumpano on 30/05/17.
 */
class UserRepository {

    val enableSSL = false

    fun signIn(userId: String, password: String): Boolean {

        val home = get(HOME_URL)
        val sessionId = extractSessionId(home.parse().html())
        var authenticated: Boolean = false

        sessionId?.let {
            val response = authenticate(sessionId!!, userId, password)
            authenticated = isAuthenticated(response.parse())
        }

        return authenticated
    }

    private fun isAuthenticated(html: Document): Boolean {
        return html.getElementsByAttributeValue("class", "feedbackbar")[0].getElementsByTag("img").isEmpty()
    }

    private fun get(url: String) = Jsoup.connect(url).validateTLSCertificates(enableSSL).execute()

    private fun authenticate(sessionId: String, userId: String, password: String): Response {

        val page = get(SIGN_IN_URL.format(sessionId))

        val html = page.parse()

        val form = html.getElementsByTag("form")

        val response = Jsoup.connect(form.attr("action"))
                .validateTLSCertificates(enableSSL)
                .method(Connection.Method.POST)
                .data("ssl_flag", "Y")
                .data("func", "login-session")
                .data("login_source", "LOGIN-BOR")
                .data("bor_library", "UEP50")
                .data(PARAM_USER_ID, userId)
                .data(PARAM_PASSWORD, password)
                .cookie(PARAM_ALEPH_SESSION_ID, sessionId)
                .execute()

        return response

    }

    fun extractSessionId(html: String): String? {

        val regex = REGEX_EXTRACT_SESSION_ID.toRegex()
        var sessionId: String? = regex.find(html)?.groups!![1]!!.value

        return sessionId
    }

    companion object {

        val BASE_URL = "https://www.athena.biblioteca.unesp.br"

        val HOME_URL = BASE_URL + "/F?RN=" + Math.round(Math.random()*1000000000)

        val SIGN_IN_URL = BASE_URL + "/F/%s?func=BOR-INFO"

        val REGEX_EXTRACT_SESSION_ID = ".ALEPH_SESSION_ID = (.+); path = \\/"

        val PARAM_ALEPH_SESSION_ID = "ALEPH_SESSION_ID"

        val PARAM_PASSWORD = "bor_verification"

        val PARAM_USER_ID = "bor_id"
    }
}
