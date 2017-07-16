package com.pploder.regularmap.err

import kotlin.reflect.KClass

/**
 * Exception for when no relevant constructor was found.
 */
class ConstructorRetrievalException(val clazz: KClass<*>, msg: String) : RegularMapException(msg)
