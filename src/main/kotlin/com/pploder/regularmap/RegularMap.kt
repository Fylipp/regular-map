package com.pploder.regularmap

import com.pploder.regularmap.err.ConstructorRetrievalException
import com.pploder.regularmap.err.IllegalParameterException
import com.pploder.regularmap.err.MismatchException
import com.pploder.regularmap.err.RegularMapException
import java.util.regex.Pattern
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.full.cast
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.jvm.jvmErasure

/**
 * Matches the given input with the pattern and maps the result to an instance of the given type.
 * If the type contains more than one constructor the one with the [RegularMapTarget] annotation will be used.
 * Only one constructor may be annotated with this annotation. All constructor parameters must be annotated with a single
 * relevant annotation. See [mapCall] for the list of relevant annotations.
 *
 * @param T The type of the map target
 * @param input The input character sequence that should be matched to the pattern
 * @return A new instance of the given type with all requested information injected via the relevant constructor
 */
inline fun <reified T> Pattern.mapObject(input: CharSequence) = mapCall(findRelevantConstructor<T>(), input)

/**
 * Matches the given input with the pattern and passes the requested information from the match to the given function.
 * All parameters must be annotated with a single relevant annotation.
 *
 * Relevant annotations are:
 * - [NamedGroup]
 * - [Group]
 * - [GroupCount]
 * - [GroupList]
 * - [NamedGroupOffset]
 * - [GroupOffset]
 *
 * @param T The return type of the function
 * @param input The input character sequence that should be matched to the pattern
 * @return The result of the given function after the requested information was passed to it
 * @throws [MismatchException] If the input does not match the pattern
 * @throws [IllegalParameterException] If the function parameters are annotated incorrectly
 */
fun <T> Pattern.mapCall(function: KFunction<T>, input: CharSequence): T {
    val matcher = matcher(input)!!

    if (!matcher.find()) {
        throw MismatchException(this, input.toString())
    }

    val namedGroups = mutableListOf<String>()
    val capturedGroups = mutableListOf<Int>()

    val params = function.parameters.map { Parameter(it, findRelevantAnnotation(it)) }
    val functionArgs = params.map {
        if (it.annotation is NamedGroup) {
            assertParameterType<String>(it.parameter)

            val group = it.annotation.groupName
            namedGroups.add(group)
            matcher.group(group)
        } else if (it.annotation is Group) {
            assertParameterType<String>(it.parameter)

            val group = it.annotation.groupId
            capturedGroups.add(group)
            matcher.group(group)
        } else if (it.annotation is GroupCount) {
            assertParameterType<Number>(it.parameter)

            it.parameter.type.jvmErasure.cast(matcher.groupCount())
        } else if (it.annotation is GroupList) {
            assertParameterType<Collection<String>>(it.parameter)

            (0..matcher.groupCount()).map { matcher.group(it) }
        } else if (it.annotation is NamedGroupOffset) {
            assertParameterType<Pair<Number, Number>>(it.parameter)

            Pair<Number, Number>(matcher.start(it.annotation.groupName), matcher.end(it.annotation.groupName))
        } else if (it.annotation is GroupOffset) {
            assertParameterType<Pair<Number, Number>>(it.parameter)

            Pair<Number, Number>(matcher.start(it.annotation.groupId), matcher.end(it.annotation.groupId))
        } else {
            throw RegularMapException("Annotation ${it.annotation.annotationClass.qualifiedName} is registered as a valid annotation but has not mapping functionality")
        }
    }

    return function.call(*functionArgs.toTypedArray())
}

inline fun <reified T> findRelevantConstructor(): KFunction<T> {
    val constructors = T::class.constructors

    if (constructors.size == 1) {
        return constructors.single()
    } else {
        val relevant = constructors.filter { it::class.annotations.filter { it is RegularMapTarget }.isNotEmpty() }

        if (relevant.isEmpty()) {
            throw ConstructorRetrievalException(T::class, "The class ${T::class} contains no constructor annotated with the ${RegularMapTarget::class.qualifiedName} annotation")
        } else if (relevant.size > 1) {
            throw ConstructorRetrievalException(T::class, "The class ${T::class} contains more than a single relevant constructor: $relevant")
        } else {
            return relevant.first()
        }
    }
}

internal fun findRelevantAnnotation(parameter: KParameter): Annotation {
    val relevant = parameter.annotations.filter { it.annotationClass in valueAnnotations }

    if (relevant.isEmpty()) {
        throw IllegalParameterException(parameter, "The parameter ${parameter.name} contains no relevant annotation")
    } else if (relevant.size > 1) {
        throw IllegalParameterException(parameter, "The parameter ${parameter.name} has more than a single relevant annotation: $relevant")
    } else {
        return relevant.first()
    }
}

internal inline fun <reified T : Any> assertParameterType(parameter: KParameter) {
    val parameterClass = parameter.type.jvmErasure
    val requiredClass = T::class
    if (parameterClass != requiredClass && !parameterClass.isSubclassOf(requiredClass)) {
        throw IllegalParameterException(parameter, "Parameter ${parameter.name} does not have required type ${T::class.qualifiedName}")
    }
}

internal data class Parameter(val parameter: KParameter, val annotation: Annotation)
