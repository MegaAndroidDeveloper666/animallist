package ru.markstudio.animals;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.tabs.TabLayout;

import ru.markstudio.animals.data.Animal;
import ru.markstudio.animals.data.AnimalType;

public class AnimalActivity extends AppCompatActivity {

    private static final String SELECTED_TAB = "tab";
    private TabLayout tlAnimals;
    private AnimalViewModel animalViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        animalViewModel = ViewModelProviders.of(AnimalActivity.this).get(AnimalViewModel.class);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cat_dog);

        tlAnimals = findViewById(R.id.tlAnimalType);

        for (AnimalType type : AnimalType.values()) {
            TabLayout.Tab tab = tlAnimals.newTab();
            tab.setText(type.getTitle());
            tab.setTag(type.getQuery());
            tlAnimals.addTab(tab);
            animalViewModel.initAnimal(type.getQuery());
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

    public AnimalViewModel getAnimalViewModel() {
        return animalViewModel;
    }
}
