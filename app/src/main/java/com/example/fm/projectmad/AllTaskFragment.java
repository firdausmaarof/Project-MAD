package com.example.fm.projectmad;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by FM on 13/12/2015.
 */
public class AllTaskFragment extends Fragment{
    AddTaskListener addTaskCallback;
    ViewTaskListener viewTaskCallback;
    private ListView listView;
    private String JSON_STRING;

    public interface AddTaskListener {
        public void addTask();
    }

    public interface ViewTaskListener {
        public void viewTask(String taskId);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            addTaskCallback = (AddTaskListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " The MainActivity activity must " +
                    "implement AddTaskListener");
        }
        try {
            viewTaskCallback = (ViewTaskListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " The MainActivity activity must " +
                    "implement ViewTaskListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.all_task_fragment, container, false);
        FloatingActionButton floatingActionButton = (FloatingActionButton) v.findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTaskCallback.addTask();
            }
        });
        listView = (ListView) v.findViewById(R.id.list);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HashMap<String, String> map = (HashMap) parent.getItemAtPosition(position);
                String taskId = map.get(Config.TAG_ID).toString();
                viewTaskCallback.viewTask(taskId);
            }
        });
        getJSON();
        return v;
    }

    private void showTask(){
        JSONObject jsonObject = null;
        ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String, String>>();
        try {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);

            for(int i = 0; i<result.length(); i++){
                JSONObject jo = result.getJSONObject(i);
                String id = jo.getString(Config.TAG_ID);
                String task = jo.getString(Config.TAG_TASK);
                String description = jo.getString(Config.TAG_DESCRIPTION);

                HashMap<String,String> tasks = new HashMap<>();
                tasks.put(Config.TAG_ID,id);
                tasks.put(Config.TAG_TASK,task);
                tasks.put(Config.TAG_DESCRIPTION,description);
                list.add(tasks);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        ListAdapter adapter = new SimpleAdapter(
                getActivity(), list, R.layout.all_task_layout,
                new String[]{Config.TAG_TASK,Config.TAG_DESCRIPTION},
                new int[]{R.id.taskTV, R.id.descriptionTV});

        listView.setAdapter(adapter);
    }

    private void getJSON(){
        class GetJSON extends AsyncTask<Void,Void,String> {

            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(getActivity(),"Fetching Data","Wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                JSON_STRING = s;
                showTask();
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequest(Config.URL_GET_ALL);
                return s;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }
}
