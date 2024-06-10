package com.example.habitss.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.habitss.ArticleDetailActivity;
import com.example.habitss.R;
import com.example.habitss.adapter.ArticleAdapter;
import com.example.habitss.model.Article;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ArticlesActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private RecyclerView recyclerView;
    private ArticleAdapter articleAdapter;
    private List<Article> articles;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_articles);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);


        articles = new ArrayList<>();
        articleAdapter = new ArticleAdapter(this, articles, new ArticleAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Article article) {
                Intent intent = new Intent(ArticlesActivity.this, ArticleDetailActivity.class);
                intent.putExtra("articleId", article.getId());
                intent.putExtra("articleTitle", article.getTitle());
                intent.putExtra("articleContent", article.getContent());
                intent.putExtra("articleImageUrl", article.getImageUrl());
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(articleAdapter);

        loadArticlesFromFirebase();

        // Установка слушателя для бокового меню
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.nav_home) {
                    Intent intent = new Intent(ArticlesActivity.this, MainActivity.class);
                    startActivity(intent);
                    return true;
                } else if (itemId == R.id.nav_activity1) {
                    // Handle navigation to articles
                    Intent intent = new Intent(ArticlesActivity.this, ArticlesActivity.class);
                    startActivity(intent);
                    return true;
                } else if (itemId == R.id.nav_activity2) {
                    Intent intent = new Intent(ArticlesActivity.this, ProfileActivity.class);
                    startActivity(intent);
                } else if (itemId == R.id.nav_activity3) {
                    Intent intent = new Intent(ArticlesActivity.this, AboutActivity.class);
                    startActivity(intent);
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        // Обработчик нажатия кнопки в ActionBar для открытия/закрытия бокового меню
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Показать кнопку меню в ActionBar

        // Обработчик нажатия на значок меню в ActionBar для открытия/закрытия бокового меню
        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });
    }


    private void loadArticlesFromFirebase() {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("atricles");
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                articles.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Article article = dataSnapshot.getValue(Article.class);
                    if (article != null) {
                        article.setId(dataSnapshot.getKey());
                        Log.d("ArticlesActivity", "Article ID: " + article.getId() + " Title: " + article.getTitle() + " Image URL: " + article.getImageUrl());
                        articles.add(article);
                    } else {
                        Log.d("ArticlesActivity", "Article is null");
                    }
                }
                articleAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("ArticlesActivity", "Failed to load articles", error.toException());
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_home) {
            // Handle home action
        } else if (id == R.id.nav_activity1) {
            Intent intent = new Intent(this, ArticlesActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_activity2) {
            // Handle profile action
        } else if (id == R.id.nav_activity3) {
            showAlertDialog("Инструкция", "Инструкция по использованию приложения.");
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showAlertDialog(String title, String message) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", (dialog, id) -> dialog.dismiss())
                .create()
                .show();
    }
}
