package net.adiwilaga.skilltest.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.google.gson.annotations.Expose;

import java.util.List;

/**
 * Created by Donny Adiwilaga on 8/26/2015.
 */
@Table(name = "tv")
public class tv extends Model{

    @Expose
    @Column(name = "name")
    String name;
    @Expose
    @Column(name = "url")
    String url;
    @Expose
    @Column(name = "iconurl")
    String iconurl;

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public String getIconurl() {
        return iconurl;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setIconurl(String iconurl) {
        this.iconurl = iconurl;
    }


    public tv() {
        super();
    }


    public static List<tv> getAll() {
        return new Select()
                .from(tv.class)
                .execute();
    }
}
