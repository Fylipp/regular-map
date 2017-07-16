package com.pploder.regularmap.err

/**
 * Base class for all custom exceptions.
 */
open class RegularMapException : Exception {

    constructor(message: String) : super(message)

    constructor(cause: Throwable) : super(cause)

    constructor(message: String, cause: Throwable) : super(message, cause)

}
