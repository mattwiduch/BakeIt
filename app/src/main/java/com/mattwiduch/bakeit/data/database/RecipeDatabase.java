/*
 * Copyright (C) 2018 Mateusz Widuch
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
@Database(entities = {Recipe.class, Ingredient.class,
    Step.class}, version = 1, exportSchema = false)
public abstract class RecipeDatabase extends RoomDatabase {

  public static final String DATABASE_NAME = "recipe_database";

  // The associated DAOs for the database
  public abstract RecipeDao recipeDao();
}
