package guru.qa.niffler.api;

import guru.qa.niffler.model.UserJson;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

import javax.annotation.Nullable;
import java.util.List;

public interface UsersApi {
    @GET("internal/users/current")
    Call<UserJson> currentUser(@Query("username") String username);

    @GET("internal/users/all")
    Call<List<UserJson>> allUsers(@Query("username") String username,
                                  @Query("searchQuery") String searchQuery);

    @GET("internal/friends/all")
    Call<List<UserJson>> friends(@Query("username") String username,
                                 @Query("searchQuery") @Nullable String searchQuery);

    @GET("internal/invitations/income")
    Call<List<UserJson>> incomeInvitations(@Query("username") String username,
                                           @Query("searchQuery") @Nullable String searchQuery);

    @GET("internal/invitations/outcome")
    Call<List<UserJson>> outcomeInvitations(@Query("username") String username,
                                            @Query("searchQuery") @Nullable String searchQuery);

    @POST("internal/users/update")
    Call<UserJson> updateUserInfo(@Body UserJson user);


    @POST("internal/invitations/send")
    Call<UserJson> sendInvitation(@Query("username") String username,
                                  @Query("targetUsername") String targetUsername);

    @POST("internal/invitations/accept")
    Call<UserJson> acceptInvitation(@Query("username") String username,
                                    @Query("targetUsername") String targetUsername);

    @POST("internal/invitations/decline")
    Call<UserJson> declineInvitation(@Query("username") String username,
                                    @Query("targetUsername") String targetUsername);
}
