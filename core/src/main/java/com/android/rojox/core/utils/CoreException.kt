package com.android.rojox.core.utils

import java.lang.Exception

class CoreException(val code: CodeException): Exception(code.message) {
}

enum class CodeException(val message: String) {
    USER_NOT_FOUND("User Not found"),
    USER_REGISTERED("User already registered"),
    ACCOUNT_NOT_FOUND("Account Not found"),
    VALIDATE_ERROR("Error on validate account")
}