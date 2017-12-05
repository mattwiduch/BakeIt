package com.mattwiduch.bakeit.data.database.entries;

import static android.arch.persistence.room.ForeignKey.CASCADE;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
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
        childColumns = "recipe_id",
        onDelete = CASCADE),
    indices = {@Index("recipe_id")})
public class Ingredient {

  @PrimaryKey(autoGenerate = true)
  private Integer dbId;
  @ColumnInfo(name = "recipe_id")
  private Integer recipeId;
  @SerializedName("quantity")
  @Expose
  private Float quantity;
  @SerializedName("measure")
  @Expose
  private String measure;
  @SerializedName("ingredient")
  @Expose
  private String name;

  /**
   * This constructor is used by Room.
   *
   * @param recipeId Id of recipe that uses this ingredient
   * @param name Name of the ingredient
   * @param quantity Quantity needed
   * @param measure Measure to use
   */
  public Ingredient(int recipeId, String name, float quantity, String measure) {
    this.recipeId = recipeId;
    this.name = name;
    this.quantity = quantity;
    this.measure = measure;
  }

  public Integer getDbId() {
    return dbId;
  }

  public void setDbId(Integer dbId) {
    this.dbId = dbId;
  }

  public Integer getRecipeId() {
    return recipeId;
  }

  public void setRecipeId(Integer recipeId) {
    this.recipeId = recipeId;
  }

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