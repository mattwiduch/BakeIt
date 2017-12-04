package com.mattwiduch.bakeit.data.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import com.mattwiduch.bakeit.data.database.entries.Recipe;
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
  void bulkInsert(Recipe... recipes);

  /**
   * Gets all the recipes from the database in alphabetical order.
   *
   * @return {@link LiveData} with list of recipes
   */
  @Query("SELECT * FROM recipes ORDER BY name ASC")
  LiveData<List<Recipe>> getAllRecipes();
}
