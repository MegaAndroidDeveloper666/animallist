package ru.markstudio.catdog;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.HashMap;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import ru.markstudio.catdog.data.Animal;

public class AnimalViewModel extends ViewModel {

    private Repository repository = Repository.getInstance();
    private LocalRepository localRepository = LocalRepository.getInstance();

    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private HashMap<String, MutableLiveData<Boolean>> showLoader = new HashMap<>();
    private MutableLiveData<Boolean> animalDataUpdated = new MutableLiveData<>();

    public MutableLiveData<Boolean> getShowLoader(String type) {
        if (!showLoader.containsKey(type)) {
            showLoader.put(type, new MutableLiveData<>());
        }
        return showLoader.get(type);
    }

    public MutableLiveData<Boolean> getAnimalDataUpdated() {
        return animalDataUpdated;
    }

    @Override
    protected void onCleared() {
        localRepository.saveData();
        super.onCleared();
        if (!compositeDisposable.isDisposed()) {
            compositeDisposable.dispose();
        }
    }

    public void requestAnimals(String animal) {
        compositeDisposable.add(
                repository.requestAnimals(animal)
                        .doOnSubscribe(subscription -> showLoader.get(animal).postValue(true))
                        .doOnComplete(() -> showLoader.get(animal).postValue(false))
                        .doOnError(throwable -> {
                            showLoader.get(animal).postValue(false);
                            animalDataUpdated.postValue(false);
                        })
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(queryResponse -> {
                            localRepository.saveAnimalList(animal, queryResponse.getData());
                            animalDataUpdated.postValue(true);
                        })

        );
    }

    public ArrayList<Animal> getData(String type) {
        return localRepository.getAnimalData(type);
    }

    public void initAnimal(String type) {
        localRepository.initAnimal(type);
    }

    public void setScrollOffset(String type, Integer offset) {
        localRepository.saveScrollPosition(type, offset);
    }

    public int getScrollOffset(String query) {
        return localRepository.getScrollOffset(query);
    }

}