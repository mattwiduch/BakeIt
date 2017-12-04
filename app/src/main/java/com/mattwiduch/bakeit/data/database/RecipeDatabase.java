package com.mattwiduch.bakeit.data.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import com.mattwiduch.bakeit.data.database.entries.Ingredient;
import com.mattwiduch.bakeit.data.database.entries.Recipe;
import com.mattwiduch.bakeit.data.database.entries.Step;

/**
 * Room database layer on top of an SQLite database that stores recipe data..
 */

@Database(entities = {Recipe.class, Ingredient.class, Step.class}, version = 1)
public abstract class RecipeDatabase extends RoomDatabase {

  private static RecipeDatabase INSTANCE;

  // Create RecipeDatabase singleton to prevent having multiple instances of the database opened
  // at the same time
  public static RecipeDatabase getDatabase(final Context context) {
    if (INSTANCE == null) {
      synchronized (RecipeDatabase.class) {
        if (INSTANCE == null) {
          INSTANCE = Room.databaseBuilder(context.getApplicationContext(), RecipeDatabase.class,
              "recipe_database").build();
        }
      }
    }
    return INSTANCE;
  }

  public abstract RecipeDao recipeDao();
}
