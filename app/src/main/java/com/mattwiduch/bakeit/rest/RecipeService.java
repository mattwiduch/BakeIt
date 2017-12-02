package com.mattwiduch.bakeit.rest;

import com.mattwiduch.bakeit.model.Recipe;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Contains methods used to execute HTTP requests.
 */

public interface RecipeService {

  @GET("/topher/2017/May/59121517_baking/baking.json")
  Call<List<Recipe>> getRecipes();

}