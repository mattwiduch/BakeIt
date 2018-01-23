package com.mattwiduch.bakeit.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import com.mattwiduch.bakeit.data.database.entries.Ingredient;
import com.mattwiduch.bakeit.data.database.entries.Recipe;
import com.mattwiduch.bakeit.data.database.entries.Step;
import com.mattwiduch.bakeit.ui.step_detail.CompositeStep;
import java.util.ArrayList;
import java.util.List;

public class TestUtils {

  public static final int TEST_RECIPE_ID = 1;
  public static final int TEST_RECIPE_SERVINGS = 6;
  public static final int TEST_NUMBER_OF_STEPS = 10;
  public static final int TEST_CURRENT_STEP = 4;
  public static final int TEST_FIRST_STEP = 0;
  public static final int TEST_LAST_STEP = TEST_NUMBER_OF_STEPS - 1;
  public static final String TEST_RECIPE_NAME = "Doughnuts";
  public static final String TEST_IMAGE_URL = "http://lorempixel.com/200/200/food";
  public static final String TEST_STEP_NAME = "Make Dough";
  public static final String TEST_STEP_DESC = "Step description";

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

  public static Step createStep(int recipeId, int stepNumber, String shortDescription,
      String description, String videoURL, String thumbnailURL) {
    return new Step(recipeId, stepNumber, shortDescription, description, videoURL, thumbnailURL);
  }

  public static List<Recipe> createRecipeList(int size) {
    List<Recipe> recipeList = new ArrayList<>();
    for (int i = 0; i < size; i++) {
      recipeList.add(createRecipe(i, TEST_RECIPE_NAME, i, ""));
    }
    return recipeList;
  }

  public static List<Ingredient> createIngredientsList(int size) {
    List<Ingredient> ingredientList = new ArrayList<>();
    for (int i = 0; i < size; i++) {
      ingredientList.add(new Ingredient(TEST_RECIPE_ID, "Fancy ingredient", i, "TSP"));
    }
    return ingredientList;
  }

  public static List<Step> createStepList(int recipeId, int size) {
    List<Step> stepList = new ArrayList<>();
    for (int i = 0; i < size; i++) {
      stepList.add(createStep(recipeId, i, TEST_STEP_NAME, TEST_STEP_DESC, "", ""));
    }
    return stepList;
  }

  public static CompositeStep createCompositeStep(int recipeId, int stepNumber, boolean hasImage,
      boolean hasVideo, boolean isOnline) {
    CompositeStep step = new CompositeStep();
    step.setConnected(isOnline);
    String imageUrl = hasImage ? TEST_IMAGE_URL : "";
    String videoUrl = hasVideo ? TEST_IMAGE_URL : "";
    step.setRecipe(TestUtils.createRecipe(recipeId, TEST_RECIPE_NAME, TEST_RECIPE_SERVINGS, ""));
    step.setStepList(TestUtils.createStepList(recipeId, TEST_NUMBER_OF_STEPS));
    step.setStep(TestUtils.createStep(recipeId, stepNumber, TEST_STEP_NAME, TEST_STEP_DESC,
        videoUrl, imageUrl));
    return step;
  }
}
