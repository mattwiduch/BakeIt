package com.mattwiduch.bakeit.data.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import com.mattwiduch.bakeit.data.database.entries.Ingredient;
import com.mattwiduch.bakeit.data.database.entries.Recipe;
import com.mattwiduch.bakeit.data.database.entries.Step;
import java.util.List;

/**
 * {@link Dao} which provides an api for data operations on Recipes with the {@link RecipeDatabase}
 */
@Dao
public interface RecipeDao {

  /**
   * Inserts a list of {@link Recipe} into the recipes table. If there is a conflicting id
   * the recipe entry uses the {@link OnConflictStrategy} of replacing the recipe data. The
   * required uniqueness of these values is defined in the {@link Recipe}.
   *
   * @param recipes A list of recipes to insert
   */
  @Insert (onConflict = OnConflictStrategy.REPLACE)
  void insertRecipes(Recipe... recipes);

  /**
   * Inserts a list of {@link Ingredient} into the ingredients table. If there is a conflicting id
   * the entry uses the {@link OnConflictStrategy} of replacing the ingredient data. The
   * required uniqueness of these values is defined in the {@link Ingredient}.
   *
   * @param ingredients A list of ingredients to insert
   */
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  void insertIngredients(List<Ingredient> ingredients);

  /**
   * Inserts a list of {@link Step} into the steps table. If there is a conflicting id
   * the entry uses the {@link OnConflictStrategy} of replacing the step data. The
   * required uniqueness of these values is defined in the {@link Step}.
   *
   * @param steps A list of steps to insert
   */
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  void insertSteps(List<Step> steps);

  /**
   * Gets all the recipes from the database in alphabetical order.
   *
   * @return {@link LiveData} with list of recipes
   */
  @Query("SELECT * FROM recipes ORDER BY name ASC")
  LiveData<List<Recipe>> getAllRecipes();

  /**
   * Gets all the ingredients for a given recipe from the database.
   *
   * @return {@link LiveData} with list of ingredients
   */
  @Query("SELECT * FROM ingredients WHERE recipe_id = :recipeId")
  LiveData<List<Ingredient>> getIngredientsForRecipe(int recipeId);

  /**
   * Gets all the steps for a given recipe from the database in execution order.
   *
   * @return {@link LiveData} with list of steps
   */
  @Query("SELECT * FROM steps WHERE recipe_id = :recipeId ORDER BY stepNumber ASC")
  LiveData<List<Step>> getStepsForRecipe(int recipeId);
}
