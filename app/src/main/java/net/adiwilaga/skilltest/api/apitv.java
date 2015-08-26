package net.adiwilaga.skilltest.api;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

import net.adiwilaga.skilltest.model.tv;

import java.util.List;

/**
 * Created by Donny Adiwilaga on 8/26/2015.
 */
public interface apitv {

    @GET("/gettv.php")
    void gettv(@Query("all") String all, Callback<List<tv>> res);
}
