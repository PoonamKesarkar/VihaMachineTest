package com.vihatest.viewmodel;

import android.os.AsyncTask;
import android.util.Log;

import com.vihatest.database.DaoAccess;
import com.vihatest.model.JobPostData;
import com.vihatest.model.RecruiterData;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class RecruiterViewModel extends ViewModel {
    String TAG = this.getClass().getName();
     MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    MutableLiveData<Boolean> isError = new MutableLiveData<>();
    MutableLiveData<Long> inserValue = new MutableLiveData<>();
    MutableLiveData<RecruiterData> recruiterData = new MutableLiveData<>();
    public LiveData<List<RecruiterData>> recruiterDataList;

   public RecruiterViewModel(){
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
    public LiveData<RecruiterData> isLoginData() {
        return recruiterData;
    }
    public void insertRecruiterData(RecruiterData recruiterData,DaoAccess recruiterDao) {
        new InsertRecruiterAsyncTask(recruiterDao,isError,isLoading,inserValue).execute(recruiterData);
    }
    public void insertJobPostData(JobPostData jobPostData, DaoAccess recruiterDao) {
        new InsertJobDataAsyncTask(recruiterDao,isError,isLoading,inserValue).execute(jobPostData);
    }
    public void loginRecruiterData(String email,String password,DaoAccess recruiterDao) {
        new LoginRecruiterAsyncTask(recruiterDao,isError,isLoading,inserValue).execute(email,password);
    }

    public static class InsertRecruiterAsyncTask extends AsyncTask<RecruiterData, Void, Long> {
        private DaoAccess recruiterDao;
        MutableLiveData<Boolean> isLoading;
        MutableLiveData<Boolean> isError;
        MutableLiveData<Long> inserValue;
        private InsertRecruiterAsyncTask(DaoAccess recruiterDao, MutableLiveData<Boolean> isError, MutableLiveData<Boolean> isLoading,MutableLiveData<Long> inserValue) {
            this.recruiterDao = recruiterDao;
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
        protected Long doInBackground(RecruiterData... recruiter) {
            long value=recruiterDao.insertRecruiterData(recruiter[0]);
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

    public static class InsertJobDataAsyncTask extends AsyncTask<JobPostData, Void, Long> {
        private DaoAccess recruiterDao;
        MutableLiveData<Boolean> isLoading;
        MutableLiveData<Boolean> isError;
        MutableLiveData<Long> inserValue;
        private InsertJobDataAsyncTask(DaoAccess recruiterDao, MutableLiveData<Boolean> isError, MutableLiveData<Boolean> isLoading,MutableLiveData<Long> inserValue) {
            this.recruiterDao = recruiterDao;
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
        protected Long doInBackground(JobPostData... recruiter) {
            long value=recruiterDao.insertJobPostData(recruiter[0]);
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

    private class LoginRecruiterAsyncTask extends AsyncTask<String, Void, RecruiterData>{
        private DaoAccess recruiterDao;
        MutableLiveData<Boolean> isLoading;
        MutableLiveData<Boolean> isError;
        MutableLiveData<Long> inserValue;
        private LoginRecruiterAsyncTask(DaoAccess recruiterDao, MutableLiveData<Boolean> isError, MutableLiveData<Boolean> isLoading,MutableLiveData<Long> inserValue) {
            this.recruiterDao = recruiterDao;
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
        protected RecruiterData doInBackground(String... strings) {
            RecruiterData recruiterData= recruiterDao.checkRecruiterLogin(strings[0],strings[1]);
            return recruiterData;
        }

        @Override
        protected void onPostExecute(RecruiterData aVoid) {
            super.onPostExecute(aVoid);
            Log.d("Insert","Inser successfuly onPostExecute:"+aVoid);
            recruiterData.setValue(aVoid);
            isLoading.setValue(false);

        }
    }
}
