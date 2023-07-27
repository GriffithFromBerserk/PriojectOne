package com.example.gifts;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class ViewsActivity extends AppCompatActivity implements View.OnClickListener {
    private FloatingActionButton buttonBack;
    private RecyclerView recyclerView;
    private GiftAdapter adapter;
    private GiftsDatabaseHelper databaseHelper;
    List<Gift> gifts = new ArrayList<>();

    private String age;
    private String gender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_views);

        Gift gift = new Gift(1,"Компьютер", "Computer",15, "Мужской");

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        databaseHelper = new GiftsDatabaseHelper(this);

        buttonBack = findViewById(R.id.fabBack);
        buttonBack.setOnClickListener(this);

        age = getIntent().getStringExtra("age");
        gender = getIntent().getStringExtra("gender");

        List<Gift> filteredGifts = databaseHelper.getGiftsByAgeAndGender(Integer.parseInt(age), gender);

        adapter = new GiftAdapter(filteredGifts);
        recyclerView.setAdapter(adapter);
        databaseHelper.addGifts(gifts);

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.fabBack) {
            Intent intentBack = new Intent(getApplicationContext(), ChangeActivity.class);
            startActivity(intentBack);
            finish();
        }
    }

    // Дополнительных методы для редактирования данных подарков

    public void editGiftName(int position, String name) {
        Gift gift = adapter.getGiftList().get(position);
        gift.setName(name);
        databaseHelper.updateGift(gift);
        adapter.notifyItemChanged(position);
    }

    public void editGiftAge(int position, List<Integer> ageIds) {
        Gift gift = adapter.getGiftList().get(position);
        gift.setAgeIds(ageIds);
        databaseHelper.updateGift(gift);
        adapter.notifyItemChanged(position);
    }


    public void editGiftGender(int position, String gender) {
        Gift gift = adapter.getGiftList().get(position);
        gift.setGender(gender);
        databaseHelper.updateGift(gift);
        adapter.notifyItemChanged(position);
    }
}

