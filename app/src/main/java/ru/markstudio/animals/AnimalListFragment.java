package ru.markstudio.animals;

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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ru.markstudio.animals.data.Animal;
import ru.markstudio.animals.data.AnimalType;

public class AnimalListFragment extends Fragment {

    private static final String ANIMAL_TYPE = "type";
    private static final String SCROLL = "scroll";
    private AnimalType animalType;
    private AnimalViewModel animalViewModel;
    private Adapter adapter = new Adapter();
    private RecyclerView rvAnimals;
    private ProgressBar pbLoading;

    public static AnimalListFragment newInstance(String animalType) {
        AnimalListFragment fragment = new AnimalListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ANIMAL_TYPE, animalType);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_animal_list, container, false);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        animalViewModel = ((AnimalActivity) getActivity()).getAnimalViewModel();
        animalViewModel.getShowLoader(getArguments().getString(ANIMAL_TYPE)).observe(AnimalListFragment.this, loading -> pbLoading.setVisibility(loading ? View.VISIBLE : View.GONE));
        animalViewModel.getAnimalDataUpdated().observe(AnimalListFragment.this, this::handleData);
    }

    private void handleData(Boolean success) {
        if (success) {
            setData();
        } else {
            Toast.makeText(getContext(), "Произошла ошибка при загрузке!", Toast.LENGTH_LONG).show();
        }
    }

    private void setData() {
        ArrayList<Animal> data = animalViewModel.getData(getArguments().getString(ANIMAL_TYPE));
        adapter.setData(data, position -> ((AnimalActivity) getActivity()).showAnimalFragment(data.get(position)));
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        animalType = AnimalType.getByQuery(getArguments().getString(ANIMAL_TYPE));
        initViews(view);
        animalViewModel.requestAnimals(animalType.getQuery());
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AnimalActivity) getActivity()).changeTabsVisibility();
        rvAnimals.getLayoutManager().scrollToPosition(animalViewModel.getScrollOffset(animalType.getQuery()));
    }

    @Override
    public void onPause() {
        animalViewModel.setScrollOffset(animalType.getQuery(), ((LinearLayoutManager)rvAnimals.getLayoutManager()).findFirstVisibleItemPosition());
        super.onPause();
    }

    private void initViews(View view) {
        rvAnimals = view.findViewById(R.id.rvAnimals);
        rvAnimals.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        setData();
        rvAnimals.setAdapter(adapter);
        pbLoading = view.findViewById(R.id.pbLoading);
    }

}
