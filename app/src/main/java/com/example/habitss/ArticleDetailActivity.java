package com.example.habitss;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

public class ArticleDetailActivity extends AppCompatActivity {

    private TextView articleTitle;
    private TextView articleContent;
    private ImageView articleImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_detail);

        articleTitle = findViewById(R.id.articleTitle);
        articleContent = findViewById(R.id.articleContent);
        articleImage = findViewById(R.id.articleImage);

        Intent intent = getIntent();
        String title = intent.getStringExtra("articleTitle");
        String content = intent.getStringExtra("articleContent");
        String imageUrl = intent.getStringExtra("articleImageUrl");

        articleTitle.setText(title);
        articleContent.setText(content);

        if (imageUrl != null && !imageUrl.isEmpty()) {
            Glide.with(this)
                    .load(imageUrl)
                    .apply(new RequestOptions()
                            .placeholder(R.drawable.placeholder)
                            .error(R.drawable.error))
                    .into(articleImage);
        } else {
            articleImage.setImageResource(R.drawable.placeholder); // Установите placeholder, если URL пуст
        }
    }
}
