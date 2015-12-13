package com.example.fm.projectmad;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

/**
 * Created by FM on 13/12/2015.
 */
public class AddTaskFragment extends Fragment{
    private EditText taskET;
    private EditText descriptionET;

    private Button addB;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.add_task_fragment, container, false);
        taskET = (EditText) v.findViewById(R.id.taskET);
        descriptionET = (EditText) v.findViewById(R.id.descriptionET);

        addB = (Button) v.findViewById(R.id.addB);
        addB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addTask();
                FragmentManager fm = getFragmentManager();
                AllTaskFragment allTaskFragment = new AllTaskFragment();
                fm.beginTransaction()
                        .replace(R.id.fl1, allTaskFragment)
                        .commit();
            }
        });

        return v;
    }

    //Adding a task
    private void addTask(){

        final String task = taskET.getText().toString().trim();
        final String description = descriptionET.getText().toString().trim();

        class AddTask extends AsyncTask<Void,Void,String> {

            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(getActivity(),"Adding...","Wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(getActivity(), s, Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Void... v) {
                HashMap<String,String> params = new HashMap<>();
                params.put(Config.KEY_TODO_TASK,task);
                params.put(Config.KEY_TODO_DESCRIPTION,description);

                RequestHandler rh = new RequestHandler();
                String res = rh.sendPostRequest(Config.URL_ADD, params);
                return res;
            }
        }

        AddTask at = new AddTask();
        at.execute();
    }
}

