package com.yuechu.dto;

import com.yuechu.entity.Dish;
import com.yuechu.entity.DishFlavor;
import com.yuechu.entity.Dish;
import com.yuechu.entity.DishFlavor;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
public class DishDto extends Dish {

    private List<DishFlavor> flavors = new ArrayList<>();

    private String categoryName;

    private Integer copies;
}
