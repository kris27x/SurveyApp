package com.example.surveyapp.utils

import android.content.Context
import android.widget.Toast

/**
 * Utility extensions for common operations.
 */

/**
 * Shows a toast message.
 *
 * @param message The message to display.
 * @param duration The duration of the toast.
 */
fun Context.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}
