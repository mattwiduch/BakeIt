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
package com.mattwiduch.bakeit.db;

import static com.mattwiduch.bakeit.util.LiveDataTestUtil.getValue;
import static com.mattwiduch.bakeit.util.TestUtils.TEST_RECIPE_ID;
import static com.mattwiduch.bakeit.util.TestUtils.TEST_RECIPE_NAME;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import android.arch.persistence.room.Room;
import android.database.sqlite.SQLiteException;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import com.mattwiduch.bakeit.data.database.RecipeDatabase;
import com.mattwiduch.bakeit.data.database.entries.Ingredient;
import com.mattwiduch.bakeit.data.database.entries.Recipe;
import com.mattwiduch.bakeit.data.database.entries.Step;
import com.mattwiduch.bakeit.util.TestUtils;
import java.util.Arrays;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class DatabaseTest {
  private RecipeDatabase database;

  @Before
  public void initDb() {
    database = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(),
        RecipeDatabase.class).build();
  }

  @After
  public void closeDb() {
    database.close();
  }

  @Test
  public void insertIngredientsWithoutRecipe() {
    try {
      database.recipeDao().insertIngredients(TestUtils.createIngredientsList(10));
      throw new AssertionError("must fail because recipe does not exist");
    } catch (SQLiteException ex) {}
  }

  @Test
  public void insertStepsWithoutRecipe() {
    try {
      database.recipeDao().insertSteps(TestUtils.createStepList(TEST_RECIPE_ID, 10));
      throw new AssertionError("must fail because recipe does not exist");
    } catch (SQLiteException ex) {}
  }

  @Test
  public void insertRecipesAndLoad() throws InterruptedException {
    database.recipeDao().insertRecipes(TestUtils.createRecipe(TEST_RECIPE_ID,
          TestUtils.TEST_RECIPE_NAME, 6, ""));

    Recipe recipe = getValue(database.recipeDao().getRecipe(TEST_RECIPE_ID));
    assertThat(recipe, notNullValue());
    assertThat(recipe.getId(), is(TEST_RECIPE_ID));
    assertThat(recipe.getName(), is(TEST_RECIPE_NAME));
    assertThat(recipe.getServings(), is(6));
    assertThat(recipe.getImage(), is(""));

    database.recipeDao().insertRecipes(TestUtils.createRecipe(TEST_RECIPE_ID,
        TestUtils.TEST_RECIPE_NAME, 8, ""));

    recipe = getValue(database.recipeDao().getRecipe(TEST_RECIPE_ID));
    assertThat(recipe.getServings(), is(8));

    database.recipeDao().insertRecipes(TestUtils.createRecipe(5, "Ice Cream", 4, ""));

    List<Recipe> recipes = getValue(database.recipeDao().getAllRecipes());
    assertThat(recipes.size(), is(2));
    assertThat(recipes.get(1).getId(), is(5));
    assertThat(recipes.get(1).getName(), is("Ice Cream"));
    assertThat(recipes.get(1).getServings(), is(4));
    assertThat(recipes.get(1).getImage(), is(""));
  }

  @Test
  public void insertIngredientsAndLoad() throws InterruptedException {
    database.beginTransaction();
    try {
      database.recipeDao().insertRecipes(TestUtils.createRecipe(TEST_RECIPE_ID,
        TestUtils.TEST_RECIPE_NAME, 6, ""));
      database.recipeDao().insertIngredients(Arrays.asList(
          new Ingredient(1, "Flour", 2, "CUP"),
          new Ingredient(1, "Butter", 1, "TBSP")
      ));
      database.setTransactionSuccessful();
    } finally {
      database.endTransaction();
    }

    List<Ingredient> ingredients = getValue(database.recipeDao().getIngredientsForRecipe(TEST_RECIPE_ID));
    assertThat(ingredients.size(), is(2));

    assertThat(ingredients.get(0).getRecipeId(), is(TEST_RECIPE_ID));
    assertThat(ingredients.get(0).getName(), is("Flour"));
    assertThat(ingredients.get(0).getQuantity(), is(2f));
    assertThat(ingredients.get(0).getMeasure(), is("CUP"));

    assertThat(ingredients.get(1).getRecipeId(), is(TEST_RECIPE_ID));
    assertThat(ingredients.get(1).getName(), is("Butter"));
    assertThat(ingredients.get(1).getQuantity(), is(1f));
    assertThat(ingredients.get(1).getMeasure(), is("TBSP"));
  }

  @Test
  public void insertIngredientsAndLoadList() throws InterruptedException {
    database.beginTransaction();
    try {
      database.recipeDao().insertRecipes(TestUtils.createRecipe(TEST_RECIPE_ID,
          TestUtils.TEST_RECIPE_NAME, 6, ""));
      database.recipeDao().insertIngredients(Arrays.asList(
          new Ingredient(1, "Flour", 2, "CUP"),
          new Ingredient(1, "Butter", 1, "TBSP")
      ));
      database.setTransactionSuccessful();
    } finally {
      database.endTransaction();
    }

    List<Ingredient> ingredients = database.recipeDao().getIngredientsData(TEST_RECIPE_ID);
    assertThat(ingredients.size(), is(2));

    assertThat(ingredients.get(0).getRecipeId(), is(TEST_RECIPE_ID));
    assertThat(ingredients.get(0).getName(), is("Flour"));
    assertThat(ingredients.get(0).getQuantity(), is(2f));
    assertThat(ingredients.get(0).getMeasure(), is("CUP"));

    assertThat(ingredients.get(1).getRecipeId(), is(TEST_RECIPE_ID));
    assertThat(ingredients.get(1).getName(), is("Butter"));
    assertThat(ingredients.get(1).getQuantity(), is(1f));
    assertThat(ingredients.get(1).getMeasure(), is("TBSP"));
  }

  @Test
  public void insertStepsAndLoad() throws InterruptedException {
    database.beginTransaction();
    try {
      database.recipeDao().insertRecipes(TestUtils.createRecipe(TEST_RECIPE_ID,
          TestUtils.TEST_RECIPE_NAME, 6, ""));
      database.recipeDao().insertSteps(Arrays.asList(
          new Step(TEST_RECIPE_ID, 1, "Intro", "Description",
              "", "imageUrl"),
          new Step(TEST_RECIPE_ID, 2, "Prep", "Description2",
              "videoUrl", "")
          ));
      database.setTransactionSuccessful();
    } finally {
      database.endTransaction();
    }

    List<Step> steps = getValue(database.recipeDao().getStepsForRecipe(TEST_RECIPE_ID));
    assertThat(steps.size(), is(2));
    assertThat(steps.get(0).getRecipeId(), is(TEST_RECIPE_ID));
    assertThat(steps.get(0).getStepNumber(), is(1));
    assertThat(steps.get(0).getShortDescription(), is("Intro"));
    assertThat(steps.get(0).getDescription(), is("Description"));
    assertThat(steps.get(0).getVideoURL(), is(""));
    assertThat(steps.get(0).getThumbnailURL(), is("imageUrl"));

    assertThat(steps.get(1).getRecipeId(), is(TEST_RECIPE_ID));
    assertThat(steps.get(1).getStepNumber(), is(2));
    assertThat(steps.get(1).getShortDescription(), is("Prep"));
    assertThat(steps.get(1).getDescription(), is("Description2"));
    assertThat(steps.get(1).getVideoURL(), is("videoUrl"));
    assertThat(steps.get(1).getThumbnailURL(), is(""));
  }

  @Test
  public void loadSingleStep() throws InterruptedException {
    database.beginTransaction();
    try {
      database.recipeDao().insertRecipes(TestUtils.createRecipe(TEST_RECIPE_ID,
          TestUtils.TEST_RECIPE_NAME, 6, ""));
      database.recipeDao().insertSteps(Arrays.asList(
          new Step(TEST_RECIPE_ID, 1, "Intro", "Description",
              "", "imageUrl"),
          new Step(TEST_RECIPE_ID, 2, "Prep", "Description2",
              "videoUrl", "")
      ));
      database.setTransactionSuccessful();
    } finally {
      database.endTransaction();
    }

    Step step = getValue(database.recipeDao().getStep(TEST_RECIPE_ID, 2));
    assertThat(step.getRecipeId(), is(TEST_RECIPE_ID));
    assertThat(step.getStepNumber(), is(2));
    assertThat(step.getShortDescription(), is("Prep"));
    assertThat(step.getDescription(), is("Description2"));
    assertThat(step.getVideoURL(), is("videoUrl"));
    assertThat(step.getThumbnailURL(), is(""));
  }
}
