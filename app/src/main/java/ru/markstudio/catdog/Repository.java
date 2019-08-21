package ru.markstudio.catdog;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.markstudio.catdog.data.QueryResponse;

public class Repository {

    private Api api;

    private static volatile Repository INSTANCE;

    public static Repository getInstance() {
        if (INSTANCE == null) {
            synchronized (Repository.class) {
                if (INSTANCE == null) {
                    INSTANCE = new Repository();
                }
            }
        }
        return INSTANCE;
    }

    private Repository() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASEURL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(new OkHttpClient())
                .build();
        api = retrofit.create(Api.class);
    }

    public Flowable<QueryResponse> requestAnimals(String animal) {
        return api.query(animal)
                .subscribeOn(Schedulers.io());
    }

}
