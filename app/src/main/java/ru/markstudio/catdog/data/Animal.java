package ru.markstudio.catdog.data;

import com.google.gson.annotations.SerializedName;

public class Animal{

    @SerializedName("url")
    private String pictureUrl;

    @SerializedName("title")
    private String title;

    public String getPictureUrl() {
        return pictureUrl;
    }

    public String getTitle() {
        return title;
    }

}
