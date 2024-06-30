package com.example.surveyapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.surveyapp.models.*

/**
 * Room database class for the SurveyApp.
 * This database contains tables for User, Survey, Question, and Answer entities.
 */
@Database(entities = [User::class, Survey::class, Question::class, Answer::class], version = 1)
abstract class SurveyDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun surveyDao(): SurveyDao

    companion object {
        @Volatile
        private var INSTANCE: SurveyDatabase? = null

        /**
         * Returns the singleton instance of SurveyDatabase.
         *
         * @param context The context of the caller.
         * @return The singleton instance of SurveyDatabase.
         */
        fun getDatabase(context: Context): SurveyDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SurveyDatabase::class.java,
                    "survey_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
