package com.pploder.regularmap.err

import kotlin.reflect.KParameter

/**
 * Exception for parameters that are not annotated properly or have an incorrect type.
 */
class IllegalParameterException(val parameter: KParameter, msg: String)  : RegularMapException(msg)
