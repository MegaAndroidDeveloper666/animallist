package ru.markstudio.animals;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.squareup.picasso.Picasso;

public class AnimalFragment extends Fragment {

    private static final String TEXT = "text";
    private static final String PHOTO = "photo";


    public static AnimalFragment newInstance(String photoUrl, String text) {
        AnimalFragment fragment = new AnimalFragment();
        Bundle bundle = new Bundle();
        bundle.putString(TEXT, text);
        bundle.putString(PHOTO, photoUrl);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_animal, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle args = getArguments();
        ((TextView)view.findViewById(R.id.tvText)).setText(args.getString(TEXT));
        Picasso.get().load(args.getString(PHOTO)).into((ImageView) view.findViewById(R.id.ivAnimal));
    }


    @Override
    public void onResume() {
        super.onResume();
        ((AnimalActivity)getActivity()).changeTabsVisibility();
    }
}
