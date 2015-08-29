package net.adiwilaga.skilltest.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Donny Adiwilaga on 8/29/2015.
 */
@Table(name = "tlstatus")
public class statusmodel extends Model {


    @Expose
    @Column(name = "name")
    private String name;

    @Column(name = "ppimage")
    private String ppimage;

    @Expose
    @SerializedName("created_time")
    @Column(name = "datetime")
    private String datetime;

    @Expose
    @SerializedName("message")
    @Column(name = "status")
    private String status;

    @Column(name = "ordinal")
    private int ordinal;

    public static List<statusmodel> getAll() {
        return new Select()
                .from(statusmodel.class)
                .orderBy("ordinal ASC")
                .execute();
    }


    public static void Clean(){

        new Delete()
                .from(statusmodel.class)
                .execute();
    }

    public statusmodel() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPpimage() {
        return ppimage;
    }

    public void setPpimage(String ppimage) {
        this.ppimage = ppimage;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getOrdinal() {
        return ordinal;
    }

    public void setOrdinal(int ordinal) {
        this.ordinal = ordinal;
    }
}



