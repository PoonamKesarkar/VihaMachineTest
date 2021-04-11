package com.vihatest.viewmodel;

import android.os.AsyncTask;
import android.util.Log;

import com.vihatest.database.DaoAccess;
import com.vihatest.model.ApplicantData;
import com.vihatest.model.JobPostData;
import com.vihatest.model.RecruiterData;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ApplicantViewModel extends ViewModel {
    String TAG = this.getClass().getName();
    MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    MutableLiveData<Boolean> isError = new MutableLiveData<>();
    MutableLiveData<Long> inserValue = new MutableLiveData<>();
    MutableLiveData<ApplicantData> applicantData = new MutableLiveData<>();
    MutableLiveData<List<JobPostData>> jobDataList= new MutableLiveData<>();

    public ApplicantViewModel(){
        inserValue.setValue((long) -1);
    }

    public LiveData<Boolean> error() {
        Log.d(TAG, "return isError  : " + isError);
        return isError;
    }
    public LiveData<Boolean> isLoading() {
        Log.d(TAG, "return isLoading  : " + isLoading);
        return isLoading;
    }
    public LiveData<Long> isInsert() {
        Log.d(TAG, "return isLoading  : " + inserValue);
        return inserValue;
    }
    public LiveData<ApplicantData> isLoginData() {
        return applicantData;
    }
    public LiveData<List<JobPostData>> getAllJobData() {
        return jobDataList;
    }

    public void getJobData(String filter ,DaoAccess recruiterDao) {
        new getJobDataAsyncTask(recruiterDao,isError,isLoading).execute(filter);
    }
    public void insertApplicantData(ApplicantData applicantData, DaoAccess recruiterDao) {
        new InsertApplicantAsyncTask(recruiterDao,isError,isLoading,inserValue).execute(applicantData);
    }

    public void loginRecruiterData(String email,String password,DaoAccess recruiterDao) {
       new LoginApplicantAsyncTask(recruiterDao,isError,isLoading,inserValue).execute(email,password);
    }

    public static class InsertApplicantAsyncTask extends AsyncTask<ApplicantData, Void, Long> {
        private DaoAccess daoAccess;
        MutableLiveData<Boolean> isLoading;
        MutableLiveData<Boolean> isError;
        MutableLiveData<Long> inserValue;
        private InsertApplicantAsyncTask(DaoAccess daoAccess, MutableLiveData<Boolean> isError, MutableLiveData<Boolean> isLoading,MutableLiveData<Long> inserValue) {
            this.daoAccess = daoAccess;
            this.isLoading = isLoading;
            this.isError = isError;
            this.inserValue = inserValue;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            isLoading.setValue(true);
        }

        @Override
        protected Long doInBackground(ApplicantData... applicantData) {
            long value=daoAccess.insertApplicantData(applicantData[0]);
            Log.d("Insert","Inser successfuly doInBackground:"+value);
            return value;
        }

        @Override
        protected void onPostExecute(Long aVoid) {
            super.onPostExecute(aVoid);
            Log.d("Insert","Inser successfuly onPostExecute:"+aVoid);
            isLoading.setValue(false);
            inserValue.setValue(aVoid);

        }
    }

    private class LoginApplicantAsyncTask extends AsyncTask<String, Void, ApplicantData>{
        private DaoAccess daoAccess;
        MutableLiveData<Boolean> isLoading;
        MutableLiveData<Boolean> isError;
        MutableLiveData<Long> inserValue;
        private LoginApplicantAsyncTask(DaoAccess daoAccess, MutableLiveData<Boolean> isError, MutableLiveData<Boolean> isLoading,MutableLiveData<Long> inserValue) {
            this.daoAccess = daoAccess;
            this.isLoading = isLoading;
            this.isError = isError;
            this.inserValue = inserValue;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            isLoading.setValue(true);
        }
        @Override
        protected ApplicantData doInBackground(String... strings) {
            ApplicantData applicantData= daoAccess.checkApplicantLogin(strings[0],strings[1]);
            return applicantData;
        }

        @Override
        protected void onPostExecute(ApplicantData aVoid) {
            super.onPostExecute(aVoid);
            Log.d("Insert","Inser successfuly onPostExecute:"+aVoid);
            applicantData.setValue(aVoid);
            isLoading.setValue(false);

        }
    }

    private class getJobDataAsyncTask extends  AsyncTask<String, Void, List<JobPostData>>{
        private DaoAccess daoAccess;
        MutableLiveData<Boolean> isLoading;
        MutableLiveData<Boolean> isError;
        public getJobDataAsyncTask(DaoAccess daoAccess, MutableLiveData<Boolean> isError, MutableLiveData<Boolean> isLoading) {
            this.daoAccess = daoAccess;
            this.isLoading = isLoading;
            this.isError = isError;
        }

        @Override
        protected List<JobPostData> doInBackground(String... strings) {
            List<JobPostData> jobPostData=new ArrayList<>();
            if(strings[0]==null){
             jobPostData= daoAccess.getAllJobData();
            }else{
                Log.d("FliterData","doInBackground:"+strings[0]);
                jobPostData= daoAccess.getFilterJobData(strings[0]);
            }
            Log.d("FliterData","doInBackground:"+jobPostData.size());
            return jobPostData;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            isLoading.setValue(true);
        }


        @Override
        protected void onPostExecute(List<JobPostData> jobPostData) {
            super.onPostExecute(jobPostData);
            isLoading.setValue(false);
            jobDataList.setValue(jobPostData);
        }
    }
}
