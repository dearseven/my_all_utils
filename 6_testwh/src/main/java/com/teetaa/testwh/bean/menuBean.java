package com.teetaa.testwh.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/4/14.
 */

public class menuBean  implements Serializable {

    public String title;
    public boolean isTitle;
    public int ico;
    public menuBean(String title,boolean isTitle,int ico){
        this.title=title;
        this.isTitle=isTitle;
        this.ico=ico;
    }
}
