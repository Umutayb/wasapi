import context.ContextStore;
import models.*;
import okhttp3.Headers;
import retrofit2.Call;
import retrofit2.http.*;

public class FoodPlanner extends ApiUtilities {

    FoodPlannerServices petStoreServices = new ServiceGenerator()
            .setRequestLogging(true)
            .printHeaders(true)
            .generate(FoodPlannerServices.class);

    public SimpleMessageResponseModel signUp(UserSignUpModel userSignUpModel){
        log.info("Signing up a new user");
        Call<SimpleMessageResponseModel> signUpCall = petStoreServices.signUp(userSignUpModel);
        return perform(signUpCall, true, true);
    }

    public UserAuthResponseModel signIn(UserAuthRequestModel userAuthRequestModel){
        log.info("Signing in...");
        Call<UserAuthResponseModel> signInCall = petStoreServices.signIn(userAuthRequestModel);
        return perform(signInCall, true, true);
    }

    static class Auth extends ApiUtilities {
        FoodPlannerServices.Authorized petStoreServicesAuth = new ServiceGenerator(
                new Headers.Builder().add("Authorization", "Bearer " + ContextStore.get("jwtToken").toString()).build()
        ).setRequestLogging(true).printHeaders(true).generate(FoodPlannerServices.Authorized.class);

        public GetUserResponseModel addFood(GetUserResponseModel.Food foodModel){
            log.info("Adding food for the user...");
            Call<GetUserResponseModel> checkCall = petStoreServicesAuth.addFood(foodModel);
            return perform(checkCall, true, true);
        }

        public void deleteUser(String userId){
            log.info("Deleting the user with id: " + userId);
            Call<Void> deleteUserCall = petStoreServicesAuth.deleteUser(userId);
            getResponseForCode(30, 200, deleteUserCall);
        }

        public GetUserResponseModel getUser(){
            log.info("Acquiring the user...");
            Call<GetUserResponseModel> getUserCall = petStoreServicesAuth.getUser();
            return perform(getUserCall, true, true);
        }

        public SimpleMessageResponseModel logout(){
            log.info("Logging out the user...");
            Call<SimpleMessageResponseModel> logoutUserCall = petStoreServicesAuth.logout();
            return perform(logoutUserCall, true, true);
        }
    }

    public interface FoodPlannerServices {

        String BASE_URL = "http://localhost:8080/";

        @POST("/api/auth/signin")
        Call<UserAuthResponseModel> signIn(@Body UserAuthRequestModel userAuthRequestModel);

        @POST("/api/auth/signup")
        Call<SimpleMessageResponseModel> signUp(@Body UserSignUpModel userSignUpModel);

        interface Authorized {

            String BASE_URL = "http://localhost:8080/";

            @POST("/api/user/add-food")
            Call<GetUserResponseModel> addFood(@Body GetUserResponseModel.Food foodModel);

            @DELETE("/api/auth/{userId}/delete")
            Call<Void> deleteUser(@Path("userId") String userId);

            @GET("/api/user")
            Call<GetUserResponseModel> getUser();

            @GET("/api/logout")
            Call<SimpleMessageResponseModel> logout();
        }
    }

}
