package com.example.firebase__2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class RegistrationActivity extends AppCompatActivity {

    private ImageView emailIcon;
    private List<User> listUser;
    private int idUser = 0;
    private TextView welcomLable, finishRegistText;
    private EditText registName, registEmail, registPassOne, registPassTwo;
    private Button btRegist, vtnOk;

    // Переменная хранящая доступ к базе данных
    private DatabaseReference myDataBase;

    // Группа в базе данных в которой будет сохранятся введённая информация
    private String USER_KEY = "User";

    // Облачное хранение
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);
        init();
    }

    // Иницилизация компонентов
    private void init() {
        // Инициализация основных компонентов
        btRegist = findViewById(R.id.btRegist);
        vtnOk = findViewById(R.id.vtnOk);
        finishRegistText = findViewById(R.id.finishRegistText);
        emailIcon = findViewById(R.id.emailIcon);
        welcomLable = findViewById(R.id.labale);
        registName = findViewById(R.id.registName);
        registEmail = findViewById(R.id.registEmail);
        registPassOne = findViewById(R.id.registPassOne);
        registPassTwo = findViewById(R.id.registPassTwo);
        listUser = new ArrayList<>();

        // Доступ к группе User
        myDataBase = FirebaseDatabase.getInstance().getReference(USER_KEY);
        mAuth = FirebaseAuth.getInstance();
    }

    // Слушатель нажатий для кнопки "Создать пользователя"
    public void onClickSave(View view) {

        // Беру id из базы данных
        String id = myDataBase.getKey();

        // введённые данные
        String name = registName.getText().toString();
        String email = registEmail.getText().toString();

        // Проверки
        if (registPassOne.getText().toString().equals(registPassTwo.getText().toString())) {

            String pass = registPassOne.getText().toString();

            // Проверка на заполненные данные пользователем
            if (!TextUtils.isEmpty(name)
                    && !TextUtils.isEmpty(email)
                    && !TextUtils.isEmpty(pass)) {

                // Регистрация (Работает хорошо)
                // Проверка на ввод данных от пользователя
                if (!TextUtils.isEmpty(registEmail.getText().toString())
                        && !TextUtils.isEmpty(registPassOne.getText().toString())) {
                    // Передача Email и Пароля
                    mAuth.createUserWithEmailAndPassword(registEmail.getText().toString(),
                                    registPassOne.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    // Проверка на регистрацию пользователя
                                    if (task.isSuccessful()) {
                                        sendEmailVer(); // Скрывает компоненты
                                        sendEmail(); // отсылает ссылку на email

                                        Toast.makeText(getApplicationContext(), "idUser -> " +
                                                idUser, Toast.LENGTH_SHORT).show(); // Отладка

                                        // Создание нового пользователя
                                        User user = new User(id, name, pass, email);

                                        // Пуш данных в DataBase
                                        myDataBase.push().setValue(user);
                                    } else {
                                        Toast.makeText(getApplicationContext(),
                                                "Введённые данные некорректны попробуйте ещё раз",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                } else {
                    Toast.makeText(this, "Вы не ввели все данные", Toast.LENGTH_SHORT).show();
                }

            } else {
                // Оповещение что поля пустые
                Toast.makeText(this, "Вы не ввели все данные", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Оповещения что поля пароля не совподают
            Toast.makeText(this, "Пароли не совподают", Toast.LENGTH_SHORT).show();
            registPassOne.setText("");
            registPassTwo.setText("");
        }
    }

    public void sendEmailVer() {
        // Не показываем
        welcomLable.setVisibility(View.GONE);
        registName.setVisibility(View.GONE);
        registEmail.setVisibility(View.GONE);
        registPassOne.setVisibility(View.GONE);
        registPassTwo.setVisibility(View.GONE);
        btRegist.setVisibility(View.GONE);

        // Показываем
        vtnOk.setVisibility(View.VISIBLE);
        emailIcon.setVisibility(View.VISIBLE);
        finishRegistText.setVisibility(View.VISIBLE);
    }

    // Верификация аккаунта
    private void sendEmail() {
        FirebaseUser user =  mAuth.getCurrentUser();

        // Проверка на то что наш user не равен null
        assert user != null;

        // Если нет
        user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(),
                            "Сообщение отправленно на вашу Email", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Сообщение не отправленно", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Метод для возрата на основное окно Аунтефикации из RegistrationActivity
    public void getExitRegist(View view) {
        onRegistActivity();
    }

    public void onRegistActivity() {
        Intent i = new Intent(RegistrationActivity.this, LoginActivity.class);
        startActivity(i);
    }
}