package com.example.firebase__2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private Button exitAcc;
    private TextView openAcc;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        init();
    }

    @Override
    protected void onStart(){
        super.onStart();

        FirebaseUser user = mAuth.getCurrentUser();

        assert user != null;
        String useName = "Вы вошли как -> " + user.getEmail();
        openAcc.setText(useName);

    }


    public void onClickExitAcc(View view) {
        FirebaseAuth.getInstance().signOut();
        showLoginAcc();
    }

    public void showLoginAcc() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void init(){
        exitAcc = findViewById(R.id.exitAcc);
        openAcc = findViewById(R.id.openAcc);

        mAuth = FirebaseAuth.getInstance();
    }
}
