package com.juantorres.bakingapp.utils;

import android.content.Context;
import android.util.Log;

import com.juantorres.bakingapp.R;
import com.juantorres.bakingapp.RecipeListActivity;
import com.juantorres.bakingapp.data.Recipe;
import com.juantorres.bakingapp.data.json.RequestInterface;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by juantorres on 1/21/18.
 */

public class DownloadUtils{

    public void downloadRecipesJSON(Context context, Callback<List<Recipe>> callback){
        String apiDomain = context.getResources().getString(R.string.recipe_api_domain);


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(apiDomain)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final RequestInterface request = retrofit.create(RequestInterface.class);
        Call<List<Recipe>> call = request.getJSON();
        call.enqueue(callback);

    }

    public List<Recipe> downloadRecipesJSON(Context context){
        String apiDomain = context.getResources().getString(R.string.recipe_api_domain);
        List<Recipe> recipes = null;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(apiDomain)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RequestInterface request = retrofit.create(RequestInterface.class);
        Call<List<Recipe>> response = request.getJSON();

        try {
            return response.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return recipes;
    }


}
