package com.primordium.core.coreutils.functional

import com.primordium.core.coreutils.functional.Response.Companion.fail
import com.primordium.core.coreutils.functional.Response.Companion.success
import java.util.function.Predicate

object ValidationUtils {
    fun validateIf(vararg validations: Validation): Response<Boolean> {
        val errorMessages = validations
            .filterNot { it.predicate.test(true) }
            .map {
                it.onError?.invoke(it.errorMessage)
                it.errorMessage
            }
        return if (errorMessages.isEmpty()) {
            success(true)
        } else {
            fail(errorMessages)
        }
    }
}

data class Validation(
    val predicate: Predicate<Boolean>,
    val errorMessage: String,
    val onError: ((errorMessage: String) -> Unit)? = null
)