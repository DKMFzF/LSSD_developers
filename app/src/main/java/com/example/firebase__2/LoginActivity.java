package com.example.firebase__2;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private EditText usrName, pass;
    private Button exitAcc, btRegist, btLogin, startMain;
    private TextView openAcc, labale;

    // Инстанция регистрации пользователя
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout); // Важная строчка
        init();
    }

    // Функция входа в акаунт
    @Override
    protected void onStart() {
        super.onStart();

        // Для декора
        usrName.setText("");
        pass.setText("");

        // Создание объекта проверки на авторизацию пользователя
        FirebaseUser cUser = mAuth.getCurrentUser();

        // Проверка
        if (cUser != null) {
            exitShowAcc();

            Toast.makeText(this, "Данные есть",
                    Toast.LENGTH_SHORT).show(); // Отладка
        } else {
            Toast.makeText(this, "Данных нет",
                    Toast.LENGTH_SHORT).show(); // Отладка
        }
    }


    // Инициализация компонентов
    private void init() {

        btRegist = findViewById(R.id.btRegist);
        btLogin = findViewById(R.id.btLogin);
        labale = findViewById(R.id.labale);

        usrName = findViewById(R.id.usrName);
        pass = findViewById(R.id.pass);

        // Инициализация аунтификатора
        mAuth = FirebaseAuth.getInstance();
    }

    // Регистрация акаунта
    public void onClickSiginUp(View view) {
        onRegistActivity();
    }

    // Вход в систему с помощтю существующего акаунта
    public void onClickSiginIn(View view) {
        if (!TextUtils.isEmpty(usrName.getText().toString())
                && !TextUtils.isEmpty(pass.getText().toString())) {
            mAuth.signInWithEmailAndPassword(usrName.getText().toString(),
                    pass.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(getApplicationContext(),
                                "Пользователь вошел в систему",
                                Toast.LENGTH_SHORT).show(); // Отладка

                        // Метод фиксирующий верификацию email
                        exitShowAcc();

                    } else {
                        Toast.makeText(getApplicationContext(),
                                "Такого пользователя не существует",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            Toast.makeText(this, "Вы не ввели все данные", Toast.LENGTH_SHORT).show();
        }
    }


    public void exitShowAcc() {
        FirebaseUser user = mAuth.getCurrentUser();

        assert user != null;
        if (user.isEmailVerified()) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();

            // тут должна быть проверка в БД
        } else {
            Toast.makeText(this,
                    "Подтвердите Email", Toast.LENGTH_SHORT).show();
        }
    }

    // Переход в Main
    public void getMain(View view) {
        onRegistActivity();
    }

    public void onRegistActivity() {
        Intent i = new Intent(LoginActivity.this, RegistrationActivity.class);
        startActivity(i);
    }
}
