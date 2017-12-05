package com.mattwiduch.bakeit.ui.recipe_list;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;
import com.mattwiduch.bakeit.data.RecipeRepository;

/**
 * Factory method that allows us to create a ViewModel with a constructor that takes a
 * {@link RecipeRepository}
 */

public class RecipeListModelFactory extends ViewModelProvider.NewInstanceFactory {

  private final RecipeRepository mRepository;

  public RecipeListModelFactory(RecipeRepository repository) {
    mRepository = repository;
  }

  @NonNull
  @Override
  public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
    //noinspection unchecked
    return (T) new RecipeListViewModel(mRepository);
  }
}
