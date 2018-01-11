package com.mattwiduch.bakeit.data.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import com.mattwiduch.bakeit.data.database.entries.Ingredient;
import com.mattwiduch.bakeit.data.database.entries.Recipe;
import com.mattwiduch.bakeit.data.database.entries.Step;

/**
 * {@link RecipeDatabase} database for the application including a table for {@link Recipe},
 * {@link Ingredient} and {@link Step} with appropriate DAOs.
 */

// List of the entry classes
@Database(entities = {Recipe.class, Ingredient.class, Step.class}, version = 1, exportSchema = false)
public abstract class RecipeDatabase extends RoomDatabase {

  public static final String DATABASE_NAME = "recipe_database";

  // The associated DAOs for the database
  public abstract RecipeDao recipeDao();
}
