package ru.markstudio.catdog;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ru.markstudio.catdog.data.AnimalType;
import ru.markstudio.catdog.data.QueryResponse;

public class AlimalListFragment extends Fragment {

    private AnimalType animalType;

    private AlimalListFragment(AnimalType animalType) {
        this.animalType = animalType;
    }

    public static AlimalListFragment newInstance(AnimalType animalType) {
        return new AlimalListFragment(animalType);
    }

    private AnimalViewModel animalViewModel;
    private Adapter adapter;

    private RecyclerView rvAnimals;
    private ProgressBar pbLoading;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_animal, container, false);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        animalViewModel = ViewModelProviders.of(AlimalListFragment.this).get(AnimalViewModel.class);
        animalViewModel.getShowLoader().observe(AlimalListFragment.this, loading -> pbLoading.setVisibility(loading ? View.VISIBLE : View.GONE));
        animalViewModel.getAnimalData().observe(AlimalListFragment.this, this::handleData);
    }

    private void handleData(QueryResponse data) {
        if (data == null) {
            Toast.makeText(getContext(), "Произошла ошибка при загрузке!", Toast.LENGTH_LONG).show();
        } else {
            adapter.setData(data.getData());
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        animalViewModel.requestAnimals(animalType.getQuery());
    }

    private void initViews(View view) {
        rvAnimals = view.findViewById(R.id.rvAnimals);
        rvAnimals.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        adapter = new Adapter(new ArrayList<>());
        rvAnimals.setAdapter(adapter);
        pbLoading = view.findViewById(R.id.pbLoading);
    }
}
