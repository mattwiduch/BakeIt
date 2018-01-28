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
package com.mattwiduch.bakeit.utils;

import android.util.Patterns;

/**
 * Provides static methods to format various strings across the app.
 */

public class StringUtils {

  /**
   * Removes trailing decimal point and zeros from given string.
   *
   * @param quantity string
   * @return Formatted quantity string
   */
  public static String formatQuantity(String quantity) {
    return quantity.replaceAll("\\.0*$", "");
  }

  /**
   * Formats measure string depending on units.
   *
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
   *
   * @param description recipe step description
   * @return description without step number
   */
  public static String removeStepNumber(String description) {
    return description.replaceAll("[0-9]+\\. *", "");
  }

  /**
   * Checks if supplied url is valid.
   *
   * @param url String containing url
   * @return true if valid
   */
  public static boolean checkUrl(String url) {
    return Patterns.WEB_URL.matcher(url).matches();
  }
}
