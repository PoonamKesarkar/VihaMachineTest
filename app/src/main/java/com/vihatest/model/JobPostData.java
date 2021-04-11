package com.vihatest.model;

import java.io.Serializable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "JobPostTable")
public class JobPostData implements Serializable {
    @PrimaryKey(autoGenerate = true)
    public Integer id;

    @ColumnInfo(name = "userid")
    public int userId;

    @ColumnInfo(name = "jobtitle")
    public String jobtitle;

    @ColumnInfo(name = "location")
    public String location;

    @ColumnInfo(name = "profiletype")
    public int profiletype;

    @ColumnInfo(name = "salary")
    public String salary;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getJobtitle() {
        return jobtitle;
    }

    public void setJobtitle(String jobtitle) {
        this.jobtitle = jobtitle;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getProfiletype() {
        return profiletype;
    }

    public void setProfiletype(int profiletype) {
        this.profiletype = profiletype;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }
}
