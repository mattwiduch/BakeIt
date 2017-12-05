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
 * Recipe step model class.
 */

@Entity(tableName = "steps",
    foreignKeys = @ForeignKey(
        entity = Recipe.class,
        parentColumns = "id",
        childColumns = "recipe_id",
        onDelete = CASCADE),
    indices = {@Index("recipe_id")})
public class Step {

  @PrimaryKey(autoGenerate = true)
  private Integer dbId;
  @SerializedName("id")
  @Expose
  private Integer stepNumber;
  @ColumnInfo(name = "recipe_id")
  private Integer recipeId;
  @SerializedName("shortDescription")
  @Expose
  private String shortDescription;
  @SerializedName("description")
  @Expose
  private String description;
  @SerializedName("videoURL")
  @Expose
  private String videoURL;
  @SerializedName("thumbnailURL")
  @Expose
  private String thumbnailURL;

  /**
   * This constructor is used by Room.
   *
   * @param recipeId Id of related recipe
   * @param stepNumber Unique step number
   * @param shortDescription Short description of step
   * @param description Step description
   * @param videoURL Step video URL
   * @param thumbnailURL Step thumbnail URL
   */
  public Step(int recipeId, int stepNumber, String shortDescription, String description,
      String videoURL,
      String thumbnailURL) {
    this.recipeId = recipeId;
    this.stepNumber = stepNumber;
    this.shortDescription = shortDescription;
    this.description = description;
    this.videoURL = videoURL;
    this.thumbnailURL = thumbnailURL;
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

  public Integer getStepNumber() {
    return stepNumber;
  }

  public void setStepNumber(Integer stepNumber) {
    this.stepNumber = stepNumber;
  }

  public String getShortDescription() {
    return shortDescription;
  }

  public void setShortDescription(String shortDescription) {
    this.shortDescription = shortDescription;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getVideoURL() {
    return videoURL;
  }

  public void setVideoURL(String videoURL) {
    this.videoURL = videoURL;
  }

  public String getThumbnailURL() {
    return thumbnailURL;
  }

  public void setThumbnailURL(String thumbnailURL) {
    this.thumbnailURL = thumbnailURL;
  }

}