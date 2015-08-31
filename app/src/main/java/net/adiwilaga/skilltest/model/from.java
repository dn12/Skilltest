package net.adiwilaga.skilltest.model;

import com.google.gson.annotations.Expose;

/**
 * Created by Donny Adiwilaga on 8/31/2015.
 */
public class from{
    @Expose
    private String name;

    @Expose
    private String id;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}