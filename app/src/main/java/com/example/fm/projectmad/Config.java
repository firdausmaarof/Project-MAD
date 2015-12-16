package com.example.fm.projectmad;

/**
 * Created by FM on 13/12/2015.
 */
public class Config {
    public static final String URL_ADD="http://madserver.comlu.com/project/add.php";
    public static final String URL_GET_ALL = "http://madserver.comlu.com/project/getAll.php";
    public static final String URL_GET_EMP = "http://madserver.comlu.com/project/get.php?id=";
    public static final String URL_UPDATE_EMP = "http://madserver.comlu.com/project/update.php";
    public static final String URL_DELETE_EMP = "http://madserver.comlu.com/project/delete.php?id=";

    public static final String KEY_TODO_ID = "id";
    public static final String KEY_TODO_TASK = "task";
    public static final String KEY_TODO_DESCRIPTION = "description";

    public static final String TAG_JSON_ARRAY="result";
    public static final String TAG_ID = "id";
    public static final String TAG_TASK = "task";
    public static final String TAG_DESCRIPTION = "description";

    //URL to our login.php file
    public static final String LOGIN_URL = "http://madserver.comlu.com/project/login/login.php";

    //Keys for email and password as defined in our $_POST['key'] in login.php
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PASSWORD = "password";

    //If server response is equal to this that means login is successful
    public static final String LOGIN_SUCCESS = "success";

    //Keys for Sharedpreferences
    //This would be the name of our shared preferences
    public static final String SHARED_PREF_NAME = "myloginapp";

    //This would be used to store the email of current logged in user
    public static final String EMAIL_SHARED_PREF = "email";

    //We will use this to store the boolean in sharedpreference to track user is loggedin or not
    public static final String LOGGEDIN_SHARED_PREF = "loggedin";
}
