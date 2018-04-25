package hr.fer.andriod.hw0036492049.services;

import hr.fer.andriod.hw0036492049.model.User;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Service that manages users
 * Created by pavao on 28.06.17..
 */

public interface UserService {
    /**
     * Gets one user from the given relative path
     *
     * @param relativePath relative paht
     * @return Call object that may have one user in response body if everything succeeded
     */
    @GET("{path}")
    Call<User> getUser(@Path("path") String relativePath);
}
