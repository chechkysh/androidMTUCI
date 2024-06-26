package app.web.chechkysh.common.exceptions

sealed class Failure(var msg: String?, var retryAction: () -> Unit) : Throwable() {

    class Api(msg: String? = null) : Failure(msg, {})

    class Timeout(msg: String? = null) : Failure(msg, {})

    class NoInternet(msg: String? = null) : Failure(msg, {})

    class Unknown(msg: String? = null) : Failure(msg, {})

}