package com.mattwiduch.bakeit;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import com.mattwiduch.bakeit.ui.recipe_list.RecipeListFragment;
import dagger.android.AndroidInjection;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;
import javax.inject.Inject;

/**
 * Holds the fragment containing recipe list.
 */
public class MainActivity extends AppCompatActivity implements HasSupportFragmentInjector {
  @Inject
  DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    AndroidInjection.inject(this);
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_recipe_list);

    if (savedInstanceState == null) {
      RecipeListFragment recipeListFragment = new RecipeListFragment();
      getSupportFragmentManager().beginTransaction()
          .replace(R.id.recipe_list_container, recipeListFragment)
          .commitAllowingStateLoss();
    }
  }

  @Override
  public DispatchingAndroidInjector<Fragment> supportFragmentInjector() {
    return dispatchingAndroidInjector;
  }
}
