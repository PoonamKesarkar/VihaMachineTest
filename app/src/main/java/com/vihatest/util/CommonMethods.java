package com.vihatest.util;

import android.app.Activity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class CommonMethods {
    private static CommonMethods mInstance;
    public static CommonMethods getInstance() {
        return (mInstance != null) ? mInstance : (mInstance = new CommonMethods());
    }
    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();

        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
