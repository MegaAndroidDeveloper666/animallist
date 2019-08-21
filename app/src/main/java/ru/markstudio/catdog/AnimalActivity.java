package ru.markstudio.catdog;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.tabs.TabLayout;

import ru.markstudio.catdog.data.AnimalType;

public class AnimalActivity extends AppCompatActivity {

    private TabLayout tlAnimals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cat_dog);

        tlAnimals = findViewById(R.id.tlAnimalType);
        tlAnimals.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.mainContainer, AlimalListFragment.newInstance(AnimalType.getByQuery(tab.getTag().toString())), tab.getTag().toString())
                        .addToBackStack(tab.getTag().toString())
                        .commitAllowingStateLoss();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        for (AnimalType type : AnimalType.values()) {
            TabLayout.Tab tab = tlAnimals.newTab();
            tab.setText(type.getTitle());
            tab.setTag(type.getQuery());
            tlAnimals.addTab(tab);
        }
    }


}
