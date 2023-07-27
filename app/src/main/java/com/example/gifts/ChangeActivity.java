package com.example.gifts;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ChangeActivity extends AppCompatActivity implements View.OnClickListener {
private EditText gender;
private EditText age;
private Button button2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change);

        gender = findViewById(R.id.editGender);
        age = findViewById(R.id.editAge);
        button2 = findViewById(R.id.button2);
        button2.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String AGEstr = age.getText().toString();
        String GENDERstr = gender.getText().toString();

        Intent intent2 = new Intent(this, ViewsActivity.class);
        if (gender.getText().toString().equals("Мужской")) {
            intent2.putExtra("gender", GENDERstr);
            intent2.putExtra("age", AGEstr);
            startActivity(intent2);
            finish();
        } else if (gender.getText().toString().equals("Женский")){
            intent2.putExtra("gender", GENDERstr);
            intent2.putExtra("age", AGEstr);
            startActivity(intent2);
            finish();
        } else {
            Toast.makeText(getApplicationContext(),
            "К сожалению, пока приложение не поддерживает иной способ ввода названия пола, " +
            "кроме как \"Мужской\" или \"Женский\". Пожалуйста, введите корректное название пола.",
            Toast.LENGTH_LONG).show();
        }

    }
}