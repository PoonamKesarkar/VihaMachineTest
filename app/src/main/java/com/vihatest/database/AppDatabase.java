package com.vihatest.database;


import com.vihatest.model.ApplicantData;
import com.vihatest.model.JobPostData;
import com.vihatest.model.RecruiterData;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {RecruiterData.class, ApplicantData.class, JobPostData.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract DaoAccess recruiterDao();
}
