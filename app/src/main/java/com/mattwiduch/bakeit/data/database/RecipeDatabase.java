package com.mattwiduch.bakeit.data.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.util.Log;
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

  private static final String LOG_TAG = RecipeDatabase.class.getSimpleName();
  private static final String DATABASE_NAME = "recipe_database";

  // For singleton instantiation
  private static final Object LOCK = new Object();
  private static RecipeDatabase sInstance;

  // Create RecipeDatabase singleton to prevent having multiple instances of the database opened
  // at the same time
  public static RecipeDatabase getDatabase(final Context context) {
    if (sInstance == null) {
      synchronized (LOCK) {
        sInstance = Room.databaseBuilder(context.getApplicationContext(), RecipeDatabase.class,
              DATABASE_NAME).build();
        Log.d(LOG_TAG, "Created new database");
      }
    }
    return sInstance;
  }

  // The associated DAOs for the database
  public abstract RecipeDao recipeDao();
}
