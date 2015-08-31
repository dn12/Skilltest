package net.adiwilaga.skilltest.model;

import com.google.gson.annotations.Expose;

import java.util.List;

/**
 * Created by Donny Adiwilaga on 8/31/2015.
 */
public class data {
    @Expose
    private List<statusmodel> data;

    public List<statusmodel> getData() {
        return data;
    }

    public void setData(List<statusmodel> data) {
        this.data = data;
    }
}
