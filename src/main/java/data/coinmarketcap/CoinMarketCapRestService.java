package data.coinmarketcap;

import retrofit2.Call;
import retrofit2.http.GET;

import java.util.List;

public interface CoinMarketCapRestService {
    String baseUrl = "https://api.coinmarketcap.com";

    @GET("v1/ticker?limit=50")
    Call<List<Currency>> listTickers();
}
