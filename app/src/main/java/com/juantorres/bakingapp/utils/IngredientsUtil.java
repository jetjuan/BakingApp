package com.juantorres.bakingapp.utils;

import com.juantorres.bakingapp.data.Ingredient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by juantorres on 11/14/17.
 */

public  class IngredientsUtil {

    private final static String CUP = "CUP";
    private final static String CUP_FRIENDLY = "cup";
    private final static String TBLSP = "TBLSP";
    private final static String TBLSP_FRIENDLY = "tablespoon";
    private final static String TSP = "TSP";
    private final static String TSP_FRIENDLY = "teaspoon";
    private final static String K = "K";
    private final static String K_FRIENDLY = "kg";
    private final static String G = "G";
    private final static String G_FRIENDLY = "g";
    private final static String OZ = "OZ";
    private final static String OZ_FRIENDLY = "oz";
    private final static String UNIT = "UNIT";
    private final static String UNIT_FRIENDLY = "unit";



    private final static Map<String, String> UnitsMap = new HashMap<String, String>();


    private static void initializeMap(){
        if(UnitsMap.isEmpty()){
            UnitsMap.put(CUP, CUP_FRIENDLY);
            UnitsMap.put(TBLSP, TBLSP_FRIENDLY);
            UnitsMap.put(TSP, TSP_FRIENDLY);
            UnitsMap.put(K, K_FRIENDLY);
            UnitsMap.put(G, G_FRIENDLY);
            UnitsMap.put(OZ, OZ_FRIENDLY);
            UnitsMap.put(UNIT, UNIT_FRIENDLY);
        }
    }

    private static String getIngredientString(Ingredient i){
        initializeMap();
        String friendlyMeasure = UnitsMap.get(i.getMeasure());


        if(i.getQuantity() > 1) friendlyMeasure += "s";
        friendlyMeasure = friendlyMeasure.replace(".0", "");

        String result = i.getQuantity() + " " + friendlyMeasure  + " of " +i.getName();
        result = result.replace(".0", "");

        return result;
    }

    public static String getIngredientsStrings(List<Ingredient> ingredients){
        StringBuilder builder = new StringBuilder();

        for (Ingredient ing : ingredients) {
            builder.append("&#8226; " + getIngredientString(ing) + "<br/>");
        }

        return new String(builder);
    }

}
