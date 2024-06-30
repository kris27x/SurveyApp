package com.example.surveyapp.utils

/**
 * Utility object for handling Likert scale values.
 */
object LikertScale {
    const val STRONGLY_DISAGREE = 1
    const val DISAGREE = 2
    const val NEUTRAL = 3
    const val AGREE = 4
    const val STRONGLY_AGREE = 5

    /**
     * Validates if the given value is within the Likert scale range.
     *
     * @param value The value to validate.
     * @return True if the value is within the Likert scale range, false otherwise.
     */
    fun isValid(value: Int): Boolean {
        return value in STRONGLY_DISAGREE..STRONGLY_AGREE
    }

    /**
     * Converts a Likert scale value to its corresponding text representation.
     *
     * @param value The Likert scale value.
     * @return The text representation of the value.
     */
    fun toText(value: Int): String {
        return when (value) {
            STRONGLY_DISAGREE -> "Strongly Disagree"
            DISAGREE -> "Disagree"
            NEUTRAL -> "Neutral"
            AGREE -> "Agree"
            STRONGLY_AGREE -> "Strongly Agree"
            else -> "Invalid"
        }
    }

    /**
     * Converts a text representation of a Likert scale value to its corresponding integer value.
     *
     * @param text The text representation of the value.
     * @return The integer value of the Likert scale, or -1 if the text is invalid.
     */
    fun fromText(text: String): Int {
        return when (text) {
            "Strongly Disagree" -> STRONGLY_DISAGREE
            "Disagree" -> DISAGREE
            "Neutral" -> NEUTRAL
            "Agree" -> AGREE
            "Strongly Agree" -> STRONGLY_AGREE
            else -> -1
        }
    }
}
