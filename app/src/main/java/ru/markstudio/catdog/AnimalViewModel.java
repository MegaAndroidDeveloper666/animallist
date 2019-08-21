package ru.markstudio.catdog;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.reactivestreams.Subscription;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import ru.markstudio.catdog.data.QueryResponse;

public class AnimalViewModel extends ViewModel {

    private Repository repository = Repository.getInstance();

    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private MutableLiveData<Boolean> showLoader = new MutableLiveData<>();
    private MutableLiveData<QueryResponse> animalData = new MutableLiveData<>();

    public MutableLiveData<Boolean> getShowLoader() {
        return showLoader;
    }

    public MutableLiveData<QueryResponse> getAnimalData() {
        return animalData;
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
                            animalData.postValue(null);
                        })
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(queryResponse -> animalData.postValue(queryResponse))

        );
    }

}