package com.juantorres.bakingapp.utils;

/**
 * Created by juantorres on 1/7/18.
 */

public class ImageUtils {
    public static boolean isImage(String imageUrl){
        return imageUrl.endsWith(".png") || imageUrl.endsWith(".jpg");
    }
}
