package com.example.fm.projectmad;

import android.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity implements AllTaskFragment.AddTaskListener, AllTaskFragment.ViewTaskListener{
    private static FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentManager = getFragmentManager();

        AllTaskFragment f1 = new AllTaskFragment();
        fragmentManager.beginTransaction()
                .replace(R.id.fl1, f1)
                .commit();
    }

    @Override
    public void addTask() {
        AddTaskFragment addTaskFragment = new AddTaskFragment();
        fragmentManager.beginTransaction()
                .replace(R.id.fl1, addTaskFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void viewTask(String taskId) {
        ViewTaskFragment viewTaskFragment = new ViewTaskFragment();
        Bundle bundle = new Bundle();
        bundle.putString("ID", taskId);
        viewTaskFragment.setArguments(bundle);
        fragmentManager.beginTransaction()
                .replace(R.id.fl1, viewTaskFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onBackPressed(){
        if (fragmentManager.getBackStackEntryCount() > 0) {
            Log.i("MainActivity", "popping backstack");
            fragmentManager.popBackStack();
        } else {
            Log.i("MainActivity", "nothing on backstack, calling super");
            super.onBackPressed();
        }
    }
}
