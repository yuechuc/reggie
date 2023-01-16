package com.yuechu.dto;

import com.yuechu.entity.Setmeal;
import com.yuechu.entity.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
