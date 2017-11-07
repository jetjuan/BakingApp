package com.juantorres.bakingapp.data.json;

import com.juantorres.bakingapp.data.Recipe;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by juantorres on 11/6/17.
 */

public interface RequestInterface {
    @GET("topher/2017/May/59121517_baking/baking.json")
    Call<List<Recipe>> getJSON();
}
