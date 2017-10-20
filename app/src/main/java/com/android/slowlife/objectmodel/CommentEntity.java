package com.android.slowlife.objectmodel;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/2/10 0010.
 */

public class CommentEntity  implements Serializable {
    private Integer headImageUrl;
    private String name;
    private Integer rating;

    public Integer getHeadImageUrl() {
        return headImageUrl;
    }

    public void setHeadImageUrl(Integer headImageUrl) {
        this.headImageUrl = headImageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }
}
