package com.example.user.myapplication.injection;


import com.example.user.myapplication.helpers.Const;
import com.example.user.myapplication.model.response.Currency;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;


/**
 * A Server Api Singleton/Injector Class
 * Loads data from api.
 * */
@Singleton
public class DataApi {
  private static Retrofit retrofit = null;

  @Inject
  protected DataApi () {
    //Create rest service
    retrofit = new Retrofit.Builder()
            .baseUrl(Const.dataSetUrl.CURRENCIES_API_END_POINT)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build();
  }

  public DataSetService getAPIService() {
    return retrofit.create(DataSetService.class);
  }
  /**
   * Method to choose different base currency just change param str value.
   * @param baseCurrencyStr The base currency value: current = Euro (EUR).
   * @return the string in query Api value.
  **/
  public Map<String, String> getQueriesForApi(String baseCurrencyStr){
    Map<String, String> queries = new HashMap<>();

    queries.put("base", baseCurrencyStr);
    return queries;
  }

  /**
   * Get Currencies Class who contains the Rates
   * */
  public interface DataSetService {
    @GET("latest")
    Observable<Currency> getCurrencies(@QueryMap Map<String, String> filters);
  }

}
