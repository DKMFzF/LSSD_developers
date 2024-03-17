package com.example.firebase__2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

// Обязательно прикреплять класс к манифесту

public class ShowActivity extends AppCompatActivity {

    private TextView tvName, tvSubName, tvGmail;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Метод для запуска layout - та
        setContentView(R.layout.show_lyaout);

        init();
        getIntentMain();
    }

    // метод инициализации компонентов
    private void init() {
        tvName = findViewById(R.id.tvName);
        tvSubName = findViewById(R.id.tvSubName);
        tvGmail = findViewById(R.id.tvGmail);
    }

    // Метод получения переменных из readActivity
    private void getIntentMain() {
        Intent i = getIntent();

        // Проверка входимых аргументов из ReadActivity
        if (i != null) {
            // Выводим текст из переданных аргументов с помощью .getStringExtra и его ключа user_name
            tvName.setText(i.getStringExtra(Constant.USER_NAME));
            tvSubName.setText(i.getStringExtra(Constant.USER_SUB_NAME));
            tvGmail.setText(i.getStringExtra(Constant.USER_GMAIL));
        }
    }
}
