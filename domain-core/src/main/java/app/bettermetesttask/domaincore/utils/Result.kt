package app.bettermetesttask.domaincore.utils

sealed class Result<out T> {

    companion object {
        inline fun <T> of(block: () -> T): Result<T> {
            return runCatching { block() }
                .fold({
                    Success(it)
                }, {
                    Error(it)
                })
        }
    }

    data class Success<out T>(val data: T) : Result<T>()

    data class Error(val error: Throwable) : Result<Nothing>()

    override fun toString(): String =
        when (this) {
            is Success<*> -> "Success[data= $data]"
            is Error -> "Error[throwable= $error]"
        }
}

inline fun <T> Result<T>.getOrDefault(action: () -> T): Result<T> {
    return if (this is Result.Error) Result.of(action)
    else this
}