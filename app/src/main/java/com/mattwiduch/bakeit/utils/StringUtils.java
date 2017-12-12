package com.mattwiduch.bakeit.utils;

/**
 * Provides static methods to format various strings across the app.
 */

public class StringUtils {

  /**
   * Removes trailing decimal point and zeros from given string.
   * @param quantity string
   * @return Formatted quantity string
   */
  public static String formatQuantity(String quantity) {
    return quantity.replaceAll("\\.0*$", "");
  }

  /**
   * Formats measure string depending on units.
   * @param measure string
   * @return formatted measure string
   */
  public static String formatMeasure(String measure) {
    String formattedMeasure = measure.toLowerCase();

    if (formattedMeasure.contains("unit")) {
      formattedMeasure = "";
    } else if (formattedMeasure.contains("cup") || formattedMeasure.contains("tsp")
        || formattedMeasure.contains("tblsp")) {
      formattedMeasure = " " + formattedMeasure;
    }

    return formattedMeasure;
  }

  /**
   * Removes step number from step description.
   * @param description recipe step description
   * @return description without step number
   */
  public static String removeStepNumber(String description) {
    return description.replaceAll("[0-9]+\\. *", "");
  }

}
