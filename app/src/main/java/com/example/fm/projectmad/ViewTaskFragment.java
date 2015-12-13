package com.example.fm.projectmad;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by FM on 13/12/2015.
 */
public class ViewTaskFragment extends Fragment {
    private EditText idET;
    private EditText taskET;
    private EditText descriptionET;

    private Button updateB;
    private Button deleteB;

    private String id;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.view_task_fragment, container, false);
        idET = (EditText) v.findViewById(R.id.idET);
        taskET = (EditText) v.findViewById(R.id.taskET);
        descriptionET = (EditText) v.findViewById(R.id.descriptionET);

        updateB = (Button) v.findViewById(R.id.updateB);
        updateB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateTask();
            }
        });
        deleteB = (Button) v.findViewById(R.id.deleteB);
        deleteB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmDeleteTask();
            }
        });

        Bundle bundle = this.getArguments();
        id = bundle.getString("ID");
        idET.setText(id);

        getTask();

        return v;
    }

    private void getTask(){
        class GetTask extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(getActivity(),"Fetching...","Wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                showTask(s);
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequestParam(Config.URL_GET_EMP,id);
                return s;
            }
        }
        GetTask gt = new GetTask();
        gt.execute();
    }

    private void showTask(String json){
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);
            JSONObject c = result.getJSONObject(0);
            String task = c.getString(Config.TAG_TASK);
            String description = c.getString(Config.TAG_DESCRIPTION);

            taskET.setText(task);
            descriptionET.setText(description);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void updateTask(){
        final String task = taskET.getText().toString().trim();
        final String description = descriptionET.getText().toString().trim();

        class UpdateTask extends AsyncTask<Void,Void,String>{
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(getActivity(),"Updating...","Wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(getActivity(), s, Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Void... params) {
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put(Config.KEY_TODO_ID,id);
                hashMap.put(Config.KEY_TODO_TASK,task);
                hashMap.put(Config.KEY_TODO_DESCRIPTION,description);

                RequestHandler rh = new RequestHandler();

                String s = rh.sendPostRequest(Config.URL_UPDATE_EMP,hashMap);

                return s;
            }
        }

        UpdateTask ut = new UpdateTask();
        ut.execute();
    }

    private void deleteTask(){
        class DeleteTask extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(getActivity(), "Updating...", "Wait...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(getActivity(), s, Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequestParam(Config.URL_DELETE_EMP, id);
                return s;
            }
        }

        DeleteTask dt = new DeleteTask();
        dt.execute();
    }

    private void confirmDeleteTask(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setMessage("Are you sure you want to delete this task?");

        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        deleteTask();
                        FragmentManager fm = getFragmentManager();
                        AllTaskFragment allTaskFragment = new AllTaskFragment();
                        fm.beginTransaction()
                                .replace(R.id.fl1, allTaskFragment)
                                .commit();
                    }
                });

        alertDialogBuilder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
