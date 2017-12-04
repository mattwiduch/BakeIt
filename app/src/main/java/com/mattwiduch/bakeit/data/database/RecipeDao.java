package com.mattwiduch.bakeit.data.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import com.mattwiduch.bakeit.data.database.entries.Recipe;
import java.util.List;

/**
 * Database access object. Provides queries for inserting and getting recipes.
 */

@Dao
public interface RecipeDao {

  @Insert (onConflict = OnConflictStrategy.REPLACE)
  void bulkInsert(Recipe... recipes);

  @Query("SELECT * FROM recipes ORDER BY name ASC")
  LiveData<List<Recipe>> getAllRecipes();
}
