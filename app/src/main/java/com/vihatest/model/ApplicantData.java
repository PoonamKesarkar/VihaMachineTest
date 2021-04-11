package com.vihatest.model;

import java.io.Serializable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "ApplicantTable")
public class ApplicantData implements Serializable {
    @PrimaryKey(autoGenerate = true)
    public Integer id;

    @ColumnInfo(name = "firstname")
    public String firstname;

    @ColumnInfo(name = "lastname")
    public String lastname;

    @ColumnInfo(name = "email")
    public String email;

    @ColumnInfo(name = "phone")
    public String phone;

    @ColumnInfo(name = "password")
    public String password;

    @ColumnInfo(name = "profiletype")
    public int profiletype;

    @ColumnInfo(name = "experience")
    public String experience;

    @ColumnInfo(name = "payment_shift")
    public Double payment_shift;

    @ColumnInfo(name = "location")
    public String location;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getProfiletype() {
        return profiletype;
    }

    public void setProfiletype(int profiletype) {
        this.profiletype = profiletype;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public Double getPayment_shift() {
        return payment_shift;
    }

    public void setPayment_shift(Double payment_shift) {
        this.payment_shift = payment_shift;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
