package com.example.habitss.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.habitss.R;
import com.example.habitss.adapter.TaskAdapter;
import com.example.habitss.bottomSheetFragment.CreateTaskBottomSheetFragment;
import com.example.habitss.bottomSheetFragment.ShowCalendarViewBottomSheet;
import com.example.habitss.broadcastReceiver.AlarmBroadcastReceiver;
import com.example.habitss.database.DatabaseClient;
import com.example.habitss.model.Task;

import com.bumptech.glide.Glide;
import com.example.habitss.ProfileActivity;
import com.example.habitss.R;
import com.example.habitss.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;




public class MainActivity extends BaseActivity implements CreateTaskBottomSheetFragment.setRefreshListener {

    Button goToProfileButton;

    RecyclerView taskRecycler;
    TextView addTask;
    TaskAdapter taskAdapter;
    List<Task> tasks = new ArrayList<>();
    ImageView noDataImage;
    ImageView calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        taskRecycler = findViewById(R.id.taskRecycler);
        addTask = findViewById(R.id.addTask);
        noDataImage = findViewById(R.id.noDataImage);
        calendar = findViewById(R.id.calendar);

        setUpAdapter();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        ComponentName receiver = new ComponentName(this, AlarmBroadcastReceiver.class);
        PackageManager pm = getPackageManager();
        pm.setComponentEnabledSetting(receiver, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
        Glide.with(getApplicationContext()).load(R.drawable.first_note).into(noDataImage);

        addTask.setOnClickListener(view -> {
            CreateTaskBottomSheetFragment createTaskBottomSheetFragment = new CreateTaskBottomSheetFragment();
            createTaskBottomSheetFragment.setTaskId(0, false, this, MainActivity.this);
            createTaskBottomSheetFragment.show(getSupportFragmentManager(), createTaskBottomSheetFragment.getTag());
        });

        getSavedTasks();

        calendar.setOnClickListener(view -> {
            ShowCalendarViewBottomSheet showCalendarViewBottomSheet = new ShowCalendarViewBottomSheet();
            showCalendarViewBottomSheet.show(getSupportFragmentManager(), showCalendarViewBottomSheet.getTag());
        });
    }

    public void setUpAdapter() {
        taskAdapter = new TaskAdapter(this, tasks, this);
        taskRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        taskRecycler.setAdapter(taskAdapter);
    }

    private void getSavedTasks() {

        class GetSavedTasks extends AsyncTask<Void, Void, List<Task>> {
            @Override
            protected List<Task> doInBackground(Void... voids) {
                tasks = DatabaseClient
                        .getInstance(getApplicationContext())
                        .getAppDatabase()
                        .dataBaseAction()
                        .getAllTasksList();
                return tasks;
            }

            @Override
            protected void onPostExecute(List<Task> tasks) {
                super.onPostExecute(tasks);
                noDataImage.setVisibility(tasks.isEmpty() ? View.VISIBLE : View.GONE);
                setUpAdapter();
            }
        }

        GetSavedTasks savedTasks = new GetSavedTasks();
        savedTasks.execute();
    }

    @Override
    public void refresh() {
        getSavedTasks();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.action_about_author) {
            return true;
        } else if (itemId == R.id.action_about_app) {
            showAlertDialog("О программе", "Приложение, разработанное для помощи в управлении вашими ежедневными привычками и достижении ваших целей. Оно предоставляет простой и эффективный способ отслеживать, анализировать и улучшать ваш образ жизни.");
            return true;
        } else if (itemId == R.id.action_instruction) {
            showAlertDialog("Инструкция", "Здесь должна быть инструкция по использованию приложения");
            return true;
        }  else if (itemId == R.id.action_profile) {
            Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
            startActivity(intent);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void showAlertDialog(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Пользователь нажал на кнопку OK
                        dialog.dismiss(); // Закрываем диалоговое окно
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public static void start(Activity activity) {
        Intent intent = new Intent(activity, MainActivity.class);
        activity.startActivity(intent);
    }
}

