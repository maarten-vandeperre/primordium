package com.primordium.core.coreutils.functional

abstract class Response<DATA_TYPE> {

    fun <NEW_TYPE> mapData(mapper: (data: DATA_TYPE) -> NEW_TYPE): Response<NEW_TYPE> {
        return if (this is SuccessResponse) {
            SuccessResponse(mapper(data))
        } else {
            ErrorResponse((this as ErrorResponse).errorMessages)
        }
    }

    fun <NEW_TYPE> map(mapper: (data: DATA_TYPE) -> Response<NEW_TYPE>): Response<NEW_TYPE> {
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

        fun flatten(list: List<Response<out Any>>): Response<List<Any>> {
            val errors = list.filterIsInstance(ErrorResponse::class.java).map { it.errorMessages }.flatten()
            return if (errors.isEmpty()) {
                success(list.filterIsInstance(SuccessResponse::class.java).map { it.data!! })
            } else {
                fail(errors)
            }
        }
    }
}

class ErrorResponse<DATA_TYPE>(
    val errorMessages: List<String>
) : Response<DATA_TYPE>()

class SuccessResponse<DATA_TYPE>(
    val data: DATA_TYPE
) : Response<DATA_TYPE>()