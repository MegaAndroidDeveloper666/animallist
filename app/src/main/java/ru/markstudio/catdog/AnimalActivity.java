package ru.markstudio.catdog;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.tabs.TabLayout;

import ru.markstudio.catdog.data.Animal;
import ru.markstudio.catdog.data.AnimalType;

public class AnimalActivity extends AppCompatActivity {

    private TabLayout tlAnimals;
    private static final String SELECTED_TAB = "tab";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cat_dog);

        tlAnimals = findViewById(R.id.tlAnimalType);

        for (AnimalType type : AnimalType.values()) {
            TabLayout.Tab tab = tlAnimals.newTab();
            tab.setText(type.getTitle());
            tab.setTag(type.getQuery());
            tlAnimals.addTab(tab);
        }

        if (savedInstanceState != null) {
            tlAnimals.selectTab(tlAnimals.getTabAt(savedInstanceState.getInt(SELECTED_TAB, 0)));
        }

        tlAnimals.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.mainContainer, AnimalListFragment.newInstance(tab.getTag().toString()), tab.getTag().toString())
                        .commitAllowingStateLoss();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        if (savedInstanceState == null) {
            TabLayout.Tab tab = tlAnimals.getTabAt(tlAnimals.getSelectedTabPosition());
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.mainContainer, AnimalListFragment.newInstance(tab.getTag().toString()), tab.getTag().toString())
                    .commitAllowingStateLoss();
        }
        getSupportFragmentManager().addOnBackStackChangedListener(this::changeTabsVisibility);
    }

    public void changeTabsVisibility() {
        showTabs(getSupportFragmentManager().getBackStackEntryCount() == 0);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt(SELECTED_TAB, tlAnimals.getSelectedTabPosition());
        super.onSaveInstanceState(outState);
    }

    public void showAnimalFragment(Animal animal) {
        getSupportFragmentManager().beginTransaction()
                .add(R.id.mainContainer, AnimalFragment.newInstance(animal.getPictureUrl(), animal.getTitle()))
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.popBackStack();
        if (fragmentManager.getBackStackEntryCount() == 0)
            super.onBackPressed();
    }


    public void showTabs(boolean visible) {
        tlAnimals.setVisibility(visible ? View.VISIBLE : View.GONE);
    }
}
