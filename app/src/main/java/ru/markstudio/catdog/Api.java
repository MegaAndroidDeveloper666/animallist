package ru.markstudio.catdog;


import io.reactivex.Flowable;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ru.markstudio.catdog.data.QueryResponse;

public interface Api {

    public static final String BASEURL = "https://kot3.com/xim/";

    @GET("api.php")
    Flowable<QueryResponse> query(
            @Query("query") String parameter
    );

}
