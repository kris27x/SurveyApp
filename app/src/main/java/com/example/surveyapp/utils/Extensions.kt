package com.example.surveyapp.utils

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast

/**
 * Utility extensions and functions for common operations in the Survey app.
 */

/**
 * Extension function to show a toast message.
 *
 * @param message The message to display.
 * @param duration The duration of the toast (default is Toast.LENGTH_SHORT).
 */
fun Context.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}

/**
 * Extension function to hide the soft keyboard.
 */
fun View.hideKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}

/**
 * Validates if an EditText field is not empty.
 *
 * @param editText The EditText to validate.
 * @param errorMessage The error message to display if the field is empty.
 * @return True if the field is not empty, false otherwise.
 */
fun validateNotEmpty(editText: EditText, errorMessage: String): Boolean {
    return if (editText.text.toString().trim().isEmpty()) {
        editText.error = errorMessage
        false
    } else {
        true
    }
}

/**
 * Extension function to clear the text from an EditText.
 */
fun EditText.clearText() {
    this.text.clear()
}
