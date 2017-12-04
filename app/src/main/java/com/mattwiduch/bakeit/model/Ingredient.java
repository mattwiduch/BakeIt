package com.mattwiduch.bakeit.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Recipe ingredient model class.
 */

@Entity(tableName = "ingredients",
        foreignKeys = @ForeignKey(
          entity = Recipe.class,
          parentColumns = "id",
          childColumns = "recipe_id"))
public class Ingredient {

  @PrimaryKey
  public int id;

  @SerializedName("quantity")
  @Expose
  private Float quantity;
  @SerializedName("measure")
  @Expose
  private String measure;
  @SerializedName("ingredient")
  @Expose
  private String name;

  public Float getQuantity() {
    return quantity;
  }

  public void setQuantity(Float quantity) {
    this.quantity = quantity;
  }

  public String getMeasure() {
    return measure;
  }

  public void setMeasure(String measure) {
    this.measure = measure;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

}