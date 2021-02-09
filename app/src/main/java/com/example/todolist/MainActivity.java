package com.example.todolist;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    DBHelper dbHelper;
    ArrayAdapter<String> mAdapter;
    ListView lstTask;
    AlertDialog dialogAddTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DBHelper(this);
        lstTask = (ListView) findViewById(R.id.lsTask);

        loadTaskList();

        findViewById(R.id.action_add_task).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddTittleDialog();
            }
        });

    }

    private void showAddTittleDialog() {

        if (dialogAddTask == null){
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            View view = LayoutInflater.from(this).inflate(
                    R.layout.customdialog,
                    (ViewGroup) findViewById(R.id.layoutDialogContainer)
            );
            builder.setView(view);

            dialogAddTask = builder.create();
            if (dialogAddTask.getWindow() != null){
                dialogAddTask.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            }

            final EditText inputTittle = view.findViewById(R.id.textMassage);
            inputTittle.requestFocus();

            view.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogAddTask.dismiss();
                    String task = String.valueOf(inputTittle.getText());
                    dbHelper.insertNewTask(task);
                    loadTaskList();
                }
            });

            view.findViewById(R.id.buttonNo).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogAddTask.dismiss();
                }
            });
        }
        dialogAddTask.show();
    }


    private void loadTaskList() {

        ArrayList<String> taskList = dbHelper.getTaskList();
        if (mAdapter == null){
            mAdapter = new ArrayAdapter<String> (this,R.layout.activity_todolist,R.id.task_title,taskList);
        lstTask.setAdapter(mAdapter);
        }
        else {
            mAdapter.clear();
            mAdapter.addAll(taskList);
            mAdapter.notifyDataSetChanged();
        }
    }


    public void deleteTask(View view){

        View parent = (View)view.getParent();
        TextView taskTextView = (TextView)parent.findViewById(R.id.task_title);
        Log.e("String", (String) taskTextView.getText());
        String task =String.valueOf(taskTextView.getText());
        dbHelper.deleteTask(task);
        loadTaskList();

    }

}