package com.vihatest.database;

import com.vihatest.model.ApplicantData;
import com.vihatest.model.JobPostData;
import com.vihatest.model.RecruiterData;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface DaoAccess {

    @Query("SELECT * FROM JobPostTable")
    List<JobPostData> getAllJobData();

    @Query("SELECT * FROM JobPostTable where  location like :location")
    List<JobPostData> getFilterJobData(String location);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertRecruiterData(RecruiterData recruiterData);

    @Query("SELECT * FROM RecruiterTable where email= :email and password= :password")
    RecruiterData checkRecruiterLogin(String email, String password);


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertApplicantData(ApplicantData applicantData);

    @Query("SELECT * FROM ApplicantTable where email= :email and password= :password")
    ApplicantData checkApplicantLogin(String email, String password);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertJobPostData(JobPostData jobPostData);
}
