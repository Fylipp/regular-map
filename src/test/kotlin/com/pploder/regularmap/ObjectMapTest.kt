package com.pploder.regularmap

import org.junit.Test
import java.util.regex.Pattern
import kotlin.test.assertEquals

class ObjectMapTest {

    @Test
    fun namedGroupsTest() {
        val pattern = Pattern.compile("(?<foo>[a-z]+)[ ](?<bar>[a-z]+)", Pattern.CASE_INSENSITIVE)

        val mapped = pattern.mapObject<NamedGroupTestStructure>("Lorem ipsum")

        assertEquals(NamedGroupTestStructure("Lorem", "ipsum"), mapped)
    }

    @Test
    fun groupsTest() {
        val pattern = Pattern.compile("([a-z]+)[ ]([a-z]+)", Pattern.CASE_INSENSITIVE)

        val mapped = pattern.mapObject<GroupTestStructure>("Lorem ipsum")

        assertEquals(GroupTestStructure("Lorem ipsum", "Lorem", "ipsum"), mapped)
    }

    @Test
    fun groupCountTest() {
        val pattern = Pattern.compile("(.)[ ]([123])([456])([789])")

        val mapped = pattern.mapObject<GroupCountTestStructure>("A 159")

        assertEquals(GroupCountTestStructure(4), mapped)
    }

    @Test
    fun groupListTest() {
        val pattern = Pattern.compile("(.)[ ]([123])([456])([789])")

        val mapped = pattern.mapObject<GroupListTestStructure>("A 248")

        assertEquals(GroupListTestStructure(listOf("A 248", "A", "2", "4", "8")), mapped)
    }

    @Test
    fun namedGroupOffsetTest() {
        val pattern = Pattern.compile("(?<foo>.{3})(?<bar>.{1,5})")

        val mapped = pattern.mapObject<NamedGroupOffsetTestStructure>("foobar")

        assertEquals(NamedGroupOffsetTestStructure(Pair(0, 3), Pair(3, 6)), mapped)
    }

    @Test
    fun groupOffsetTest() {
        val pattern = Pattern.compile("(.{2})(.{1,5})")

        val mapped = pattern.mapObject<GroupOffsetTestStructure>("foobar")

        assertEquals(GroupOffsetTestStructure(Pair(0, 2), Pair(2, 6)), mapped)
    }

}

data class NamedGroupTestStructure(@NamedGroup("foo") val foo: String, @NamedGroup("bar") val bar: String)
data class GroupTestStructure(@Group(0) val all: String, @Group(1) val first: String, @Group(2) val second: String)
data class GroupCountTestStructure(@GroupCount val count: Int)
data class GroupListTestStructure(@GroupList val list: List<String>)
data class NamedGroupOffsetTestStructure(@NamedGroupOffset("foo") val fooOffset: Pair<Int, Int>, @NamedGroupOffset("bar") val barOffset: Pair<Int, Int>)
data class GroupOffsetTestStructure(@GroupOffset(1) val firstOffset: Pair<Int, Int>, @GroupOffset(2) val secondOffset: Pair<Int, Int>)
