package ru.markstudio.catdog;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import ru.markstudio.catdog.data.Animal;

public class AnimalViewModel extends ViewModel {

    private Repository repository = Repository.getInstance();

    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private MutableLiveData<Boolean> showLoader = new MutableLiveData<>();
    private MutableLiveData<Boolean> animalDataUpdated = new MutableLiveData<>();

    private ArrayList<Animal> data = new ArrayList<>();

    public MutableLiveData<Boolean> getShowLoader() {
        return showLoader;
    }

    public MutableLiveData<Boolean> getAnimalDataUpdated() {
        return animalDataUpdated;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (!compositeDisposable.isDisposed()) {
            compositeDisposable.dispose();
        }
    }

    public void requestAnimals(String animal) {
        compositeDisposable.add(
                repository.requestAnimals(animal)
                        .doOnSubscribe(subscription -> showLoader.postValue(true))
                        .doOnComplete(() -> showLoader.postValue(false))
                        .doOnError(throwable -> {
                            showLoader.postValue(false);
                            animalDataUpdated.postValue(false);
                        })
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(queryResponse -> {
                            data = queryResponse.getData();
                            animalDataUpdated.postValue(true);
                        })

        );
    }

    public ArrayList<Animal> getData() {
        return data;
    }
}