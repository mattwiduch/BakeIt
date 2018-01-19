package com.mattwiduch.bakeit.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import com.mattwiduch.bakeit.data.database.entries.Recipe;
import java.util.ArrayList;
import java.util.List;

public class TestUtils {

  public static boolean isConnected(Context context) {
    ConnectivityManager connectivityManager
        = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
    return activeNetworkInfo != null && activeNetworkInfo.isConnected();
  }

  public static Recipe createRecipe(int id, String name, int servings, String imgUrl) {
    Recipe recipe = new Recipe();
    recipe.setId(id);
    recipe.setName(name);
    recipe.setServings(servings);
    recipe.setImage(imgUrl);
    return recipe;
  }

  public static List<Recipe> createRecipeList(int size) {
    List<Recipe> recipeList = new ArrayList<>();
    for (int i = 0; i < size; i++) {
      recipeList.add(createRecipe(i, "Muffins", i, ""));
    }
    return recipeList;
  }
}
