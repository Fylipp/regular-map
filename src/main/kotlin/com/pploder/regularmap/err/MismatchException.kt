package com.pploder.regularmap.err

import java.util.regex.Pattern

/**
 * Exception for when the input mismatches the pattern.
 */
class MismatchException(val pattern: Pattern, val input: String) : RegularMapException("The input <$input> does not match the pattern /${pattern.pattern()}/")
