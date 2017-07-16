package com.pploder.regularmap

/**
 * Marks a constructor as the injection target for when multiple constructors would be available.
 */
@Target(AnnotationTarget.CONSTRUCTOR)
@Retention(AnnotationRetention.RUNTIME)
annotation class RegularMapTarget

/**
 * The list of recognized parameter annotations.
 */
internal val valueAnnotations = arrayOf(NamedGroup::class, Group::class, GroupCount::class, GroupList::class, NamedGroupOffset::class, GroupOffset::class)

/**
 * Asks for the value of a named group.
 */
@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class NamedGroup(val groupName: String)

/**
 * Asks for the value of a captured group.
 */
@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class Group(val groupId: Int)

/**
 * Asks for the number of captured groups.
 */
@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class GroupCount

/**
 * Asks for a list of groups.
 */
@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class GroupList

/**
 * Asks for the offset of a named group.
 */
@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class NamedGroupOffset(val groupName: String)

/**
 * Asks for the offset of a captured group.
 */
@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class GroupOffset(val groupId: Int)
