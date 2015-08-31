package net.adiwilaga.skilltest.api;

import net.adiwilaga.skilltest.model.data;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by Donny Adiwilaga on 8/30/2015.
 */
public interface fbAPI {
    @GET("/{user}/feed")
    void getStatus(@Query("access_token") String token,@Path("user") String user, Callback<data> res);
}
