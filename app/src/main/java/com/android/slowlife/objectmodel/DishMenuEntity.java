package com.android.slowlife.objectmodel;

import java.io.Serializable;
import java.util.ArrayList;
public class DishMenuEntity  implements Serializable {
    private String menuName;
    private ArrayList<DishEntity> dishList;

    public DishMenuEntity(){

    }

    public DishMenuEntity(String menuName, ArrayList dishList){
        this.menuName = menuName;
        this.dishList = dishList;
    }

    public ArrayList<DishEntity> getDishList() {
        return dishList;
    }

    public void setDishList(ArrayList<DishEntity> dishList) {
        this.dishList = dishList;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

}
