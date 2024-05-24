package com.example.habitss.adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.habitss.R;
import com.example.habitss.activity.MainActivity;
import com.example.habitss.bottomSheetFragment.CreateTaskBottomSheetFragment;
import com.example.habitss.database.DatabaseClient;
import com.example.habitss.databinding.ItemTaskBinding;
import com.example.habitss.model.Task;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private MainActivity context;
    private List<Task> taskList;
    public SimpleDateFormat dateFormat = new SimpleDateFormat("EE dd MMM yyyy", Locale.US);
    public SimpleDateFormat inputDateFormat = new SimpleDateFormat("dd-M-yyyy", Locale.US);
    Date date = null;
    String outputDateString = null;
    CreateTaskBottomSheetFragment.setRefreshListener setRefreshListener;

    public TaskAdapter(MainActivity context, List<Task> taskList,  CreateTaskBottomSheetFragment.setRefreshListener setRefreshListener) {
        this.context = context;
        this.taskList = taskList;
        this.setRefreshListener = setRefreshListener;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        ItemTaskBinding binding = ItemTaskBinding.inflate(inflater, viewGroup, false);
        return new TaskViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = taskList.get(position);
        holder.binding.title.setText(task.getTaskTitle());
        holder.binding.description.setText(task.getTaskDescrption());
        holder.binding.time.setText(task.getLastAlarm());
        holder.binding.status.setText(task.isComplete() ? "COMPLETED" : "UPCOMING");
        holder.binding.options.setOnClickListener(view -> showPopUpMenu(view, position));

        try {
            date = inputDateFormat.parse(task.getDate());
            outputDateString = dateFormat.format(date);

            String[] items1 = outputDateString.split(" ");
            String day = items1[0];
            String dd = items1[1];
            String month = items1[2];

            holder.binding.day.setText(day);
            holder.binding.date.setText(dd);
            holder.binding.month.setText(month);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showPopUpMenu(View view, int position) {
        final Task task = taskList.get(position);
        PopupMenu popupMenu = new PopupMenu(context, view);
        popupMenu.getMenuInflater().inflate(R.menu.menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.menuDelete) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context, R.style.AppTheme_Dialog);
                alertDialogBuilder.setTitle(R.string.delete_confirmation).setMessage(R.string.sureToDelete).
                        setPositiveButton(R.string.yes, (dialog, which) -> {
                            deleteTaskFromId(task.getTaskId(), position);
                        })
                        .setNegativeButton(R.string.no, (dialog, which) -> dialog.cancel()).show();
            } else if (itemId == R.id.menuUpdate) {
                CreateTaskBottomSheetFragment createTaskBottomSheetFragment = new CreateTaskBottomSheetFragment();
                createTaskBottomSheetFragment.setTaskId(task.getTaskId(), true, setRefreshListener, context);
                createTaskBottomSheetFragment.show(context.getSupportFragmentManager(), createTaskBottomSheetFragment.getTag());
            } else if (itemId == R.id.menuComplete) {
                AlertDialog.Builder completeAlertDialog = new AlertDialog.Builder(context, R.style.AppTheme_Dialog);
                completeAlertDialog.setTitle(R.string.confirmation).setMessage(R.string.sureToMarkAsComplete).
                        setPositiveButton(R.string.yes, (dialog, which) -> showCompleteDialog(task.getTaskId(), position))
                        .setNegativeButton(R.string.no, (dialog, which) -> dialog.cancel()).show();
            }
            return false;
        });
        popupMenu.show();
    }


    public void showCompleteDialog(int taskId, int position) {
        Dialog dialog = new Dialog(context, R.style.AppTheme);
        dialog.setContentView(R.layout.dialog_completed_theme);
        Button close = dialog.findViewById(R.id.closeButton);
        close.setOnClickListener(view -> {
            deleteTaskFromId(taskId, position);
            dialog.dismiss();
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();
    }


    private void deleteTaskFromId(int taskId, int position) {
        class GetSavedTasks extends AsyncTask<Void, Void, List<Task>> {
            @Override
            protected List<Task> doInBackground(Void... voids) {
                DatabaseClient.getInstance(context)
                        .getAppDatabase()
                        .dataBaseAction()
                        .deleteTaskFromId(taskId);

                return taskList;
            }

            @Override
            protected void onPostExecute(List<Task> tasks) {
                super.onPostExecute(tasks);
                removeAtPosition(position);
                setRefreshListener.refresh();
            }
        }
        GetSavedTasks savedTasks = new GetSavedTasks();
        savedTasks.execute();
    }

    private void removeAtPosition(int position) {
        taskList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, taskList.size());
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public class TaskViewHolder extends RecyclerView.ViewHolder {
        private final ItemTaskBinding binding;

        public TaskViewHolder(ItemTaskBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
