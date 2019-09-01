package ru.markstudio.animals;

import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

import ru.markstudio.animals.data.Animal;

import static android.content.Context.MODE_PRIVATE;

public class LocalRepository {

    private static final String DATA = "data";
    private static final String SCROLL = "scroll";
    private static volatile LocalRepository INSTANCE;
    private HashMap<String, Integer> scrollPosition;
    private HashMap<String, ArrayList<Animal>> data;


    private LocalRepository() {
        SharedPreferences sharedPreferences = AnimalApplication.getAppContext().getSharedPreferences("app", MODE_PRIVATE);
        Gson gson = new Gson();
        Type typeData = new TypeToken<HashMap<String, ArrayList<Animal>>>() {
        }.getType();
        Type typeScroll = new TypeToken<HashMap<String, Integer>>() {
        }.getType();
        data = gson.fromJson(sharedPreferences.getString(DATA, ""), typeData);
        if (data == null){
            data = new HashMap<>();
        }
        scrollPosition = gson.fromJson(sharedPreferences.getString(SCROLL, ""), typeScroll);
        if (scrollPosition == null) {
            scrollPosition = new HashMap<>();
        }
    }

    public static LocalRepository getInstance() {
        if (INSTANCE == null) {
            synchronized (LocalRepository.class) {
                if (INSTANCE == null) {
                    INSTANCE = new LocalRepository();
                }
            }
        }
        return INSTANCE;
    }

    public void saveAnimalList(String animal, ArrayList<Animal> data) {
        this.data.put(animal, data);
    }

    public void initAnimal(String type) {
        if (!data.containsKey(type)) {
            data.put(type, new ArrayList<>());
        }
        if (!scrollPosition.containsKey(type)) {
            scrollPosition.put(type, 0);
        }
    }

    public ArrayList<Animal> getAnimalData(String type) {
        return data.get(type);
    }

    public void saveScrollPosition(String type, Integer offset) {
        scrollPosition.put(type, offset);
        saveData();
    }

    public int getScrollOffset(String query) {
        return scrollPosition.get(query);
    }

    public void saveData() {
        Gson gson = new Gson();
        String jsonData = gson.toJson(data);
        String jsonScroll = gson.toJson(scrollPosition);
        SharedPreferences sharedPreferences = AnimalApplication.getAppContext().getSharedPreferences("app", MODE_PRIVATE);
        SharedPreferences.Editor ed = sharedPreferences.edit();
        ed.putString(DATA, jsonData);
        ed.putString(SCROLL, jsonScroll);
        ed.apply();
    }
}
