package com.primordium.core.coreutils.functional

abstract class Response<DATA_TYPE> {

    fun <NEW_TYPE> map(mapper: (data: DATA_TYPE) -> NEW_TYPE): Response<NEW_TYPE> {
        return if (this is SuccessResponse) {
            SuccessResponse(mapper(data))
        } else {
            ErrorResponse((this as ErrorResponse).errorMessages)
        }
    }

    fun <NEW_TYPE> flatMap(mapper: (data: DATA_TYPE) -> Response<NEW_TYPE>): Response<NEW_TYPE> {
        return if (this is SuccessResponse) {
            mapper(this.data)
        } else {
            ErrorResponse((this as ErrorResponse).errorMessages)
        }
    }

    companion object {
        fun <DATA_TYPE> fail(errorMessages: List<String>): Response<DATA_TYPE> {
            return ErrorResponse(errorMessages)
        }
        fun <DATA_TYPE> fail(errorMessage: String): Response<DATA_TYPE> {
            return ErrorResponse(listOf(errorMessage))
        }

        fun <DATA_TYPE> success(data: DATA_TYPE): Response<DATA_TYPE> {
            return SuccessResponse(data)
        }
    }
}

class ErrorResponse<DATA_TYPE>(
    val errorMessages: List<String>
) : Response<DATA_TYPE>()

class SuccessResponse<DATA_TYPE>(
    val data: DATA_TYPE
) : Response<DATA_TYPE>()