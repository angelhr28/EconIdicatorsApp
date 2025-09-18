package com.yourapp.econ.core.common

sealed class AppError(msg: String, cause: Throwable? = null) : Exception(msg, cause) {
    class Network(msg: String, cause: Throwable? = null) : AppError(msg, cause)
    class Http(val code: Int, msg: String) : AppError(msg)
    class Parsing(msg: String) : AppError(msg)
    class Auth(msg: String) : AppError(msg)
    class Validation(msg: String) : AppError(msg)
    class Unknown(msg: String, cause: Throwable? = null) : AppError(msg, cause)
}
