
import wasapi.exceptions.FailedCallException;
import context.ContextStore;
import models.*;
import org.junit.*;
import utils.*;
import java.util.List;

public class AppTest {

    static Printer log = new Printer(AppTest.class);
    FoodPlanner foodPlanner = new FoodPlanner();

    @Before
    public void before(){
        ContextStore.loadProperties("test.properties", "secret.properties");

        log.info("nice-user authentication is in progress...");
        UserAuthRequestModel userAuthRequestModel = new UserAuthRequestModel(
                "nice-user",
                "Test-123"
        );
        UserAuthResponseModel userAuthResponse = foodPlanner.signIn(userAuthRequestModel);
        log.info(userAuthResponse.getJwtToken());
        ContextStore.put("jwtToken", userAuthResponse.getJwtToken());
        log.success("nice-user authentication is successful!");
    }

    @Test
    public void deleteUserTest() {
        FoodPlanner.Auth foodPlannerAuth = new FoodPlanner.Auth(ContextStore.get("jwtToken"));
        String deleteTestUser = StringUtilities.generateRandomString(
                "user",
                9,
                false,
                true
        );
        ContextStore.put("deleteTestUser", deleteTestUser);
        UserSignUpModel userSignUpModel = new UserSignUpModel(
                deleteTestUser,
                deleteTestUser + "@user.com",
                "Test-123",
                List.of("ROLE_USER"));

        SimpleMessageResponseModel userSignUpResponse = foodPlanner.signUp(userSignUpModel);
        Assert.assertEquals("Sign up test is failed!" ,"User registered successfully!", userSignUpResponse.getMessage());

        SimpleMessageResponseModel deleteMessageResponse = foodPlannerAuth.deleteUserWithUsername(deleteTestUser);
        Assert.assertEquals("deleteMessageResponse does not match!", "User with name " + deleteTestUser + " deleted successfully!", deleteMessageResponse.getMessage());
        log.success("User deleted successfully!");

        try {
            UserAuthRequestModel userAuthRequestModel = new UserAuthRequestModel(
                    deleteTestUser,
                    "Test-123"
            );
            foodPlanner.signIn(userAuthRequestModel);
        }
        catch (FailedCallException e) {log.success("deleteUserTest PASSED!");}
    }

    @Test
    public void signUpTest() {
        String randomUsername = StringUtilities.generateRandomString(
                "user",
                9,
                false,
                true
        );
        ContextStore.put("randomUsername", randomUsername);
        UserSignUpModel userSignUpModel = new UserSignUpModel(
                randomUsername,
                randomUsername + "@user.com",
                "Test-123",
                List.of("ROLE_USER"));

        SimpleMessageResponseModel userSignUpResponse = foodPlanner.signUp(userSignUpModel);
        Assert.assertEquals("Sign up test is failed!" ,"User registered successfully!", userSignUpResponse.getMessage());
        log.success("signUpTest PASSED!");
    }

    @Test
    public void signInTest() {
        UserAuthRequestModel userAuthRequestModel = new UserAuthRequestModel(
                ContextStore.get("randomUsername"),
                "Test-123"
        );
        UserAuthResponseModel userAuthResponse = foodPlanner.signIn(userAuthRequestModel);
        Assert.assertEquals("username does not match!", ContextStore.get("randomUsername"), userAuthResponse.getUsername());
        ContextStore.put("testUserId", userAuthResponse.getId());
        log.success("signInTest PASSED!");
    }

    @Test
    public void getNiceUserTest() {
        FoodPlanner.Auth foodPlannerAuth = new FoodPlanner.Auth(ContextStore.get("jwtToken"));
        log.info("Acquiring the nice-user food data...");
        GetUserResponseModel getUserResponse = foodPlannerAuth.getUser();

        Assert.assertEquals("Username does not match!", "nice-user", getUserResponse.getUsername());
        log.success("Username verified!");
        Assert.assertEquals("Email does not match!", "nice-user@admin.com", getUserResponse.getEmail());
        log.success("Email verified!");
        Assert.assertEquals("Role does not match!", "ROLE_ADMIN", getUserResponse.getRoles().get(0).getName());
        log.success("Role verified!");
        Assert.assertEquals("First food does not match!", "Lasagna", getUserResponse.getMenu().get(0).getName());
        log.success("First food verified! -> lazanya");
        Assert.assertEquals("Second food does not match!", "Grilled Chicken", getUserResponse.getMenu().get(1).getName());
        log.success("Second food verified! -> chicken breast");
        Assert.assertEquals("Third food does not match!", "Margarita Pizza", getUserResponse.getMenu().get(2).getName());
        log.success("Third food verified! -> margarita pizza");
        log.success("getNiceUserTest PASSED!");
    }

    @Test
    public void addFoodTest() {
        FoodPlanner.Auth foodPlannerAuth = new FoodPlanner.Auth(ContextStore.get("jwtToken"));
        String randomFoodName = StringUtilities.generateRandomString(
                "food",
                7,
                true,
                false
        );
        GetUserResponseModel.Food.Ingredient ingredient = new GetUserResponseModel.Food.Ingredient(
                "rice",
                1,
                "1"
        );
        GetUserResponseModel.Food food = new GetUserResponseModel.Food(
                randomFoodName,
                "randomFood",
                List.of(ingredient),
                List.of("Pasta"),
                "Main",
                true,
                "test recipe 01"
        );
        GetUserResponseModel responseModel = foodPlannerAuth.addFood(food);

        Assert.assertEquals("Username does not match!", "nice-user", responseModel.getUsername());
        log.success("Username verified!");
        Assert.assertEquals("Email does not match!", "nice-user@admin.com", responseModel.getEmail());
        log.success("Email verified!");
        Assert.assertEquals("Role does not match!", "ROLE_ADMIN", responseModel.getRoles().get(0).getName());
        log.success("Role verified!");
        Assert.assertTrue("Added random food not found!", responseModel.getMenu().stream().anyMatch(f -> f.getName().equals(randomFoodName)));
        log.success("Random food is verified! -> " + randomFoodName);
        log.success("addFoodTest PASSED!");
    }

    @Test
    public void logoutTest() {
        FoodPlanner foodPlanner = new FoodPlanner();
        FoodPlanner.Auth foodPlannerAuth = new FoodPlanner.Auth(ContextStore.get("jwtToken"));

        String logoutUser = StringUtilities.generateRandomString(
                "user",
                9,
                false,
                true
        );
        ContextStore.put("logoutUser", logoutUser);
        UserSignUpModel userSignUpModel = new UserSignUpModel(
                logoutUser,
                logoutUser + "@user.com",
                "Test-123",
                List.of("ROLE_USER"));

        SimpleMessageResponseModel userSignUpResponse = foodPlanner.signUp(userSignUpModel);
        Assert.assertEquals("Sign up test is failed!" ,"User registered successfully!", userSignUpResponse.getMessage());
        log.success("Signed in as logoutUser -> " + logoutUser);


        UserAuthRequestModel userAuthRequestModel = new UserAuthRequestModel(
                ContextStore.get("logoutUser"),
                "Test-123"
        );

        UserAuthResponseModel userAuthResponse = foodPlanner.signIn(userAuthRequestModel);
        ContextStore.update("jwtToken", userAuthResponse.getJwtToken());

        SimpleMessageResponseModel logoutResponse = foodPlannerAuth.logout();
        Assert.assertEquals("Logout message does not match!", "Logged out successfully", logoutResponse.getMessage());
        log.success("Logout message is validated!");

        try {
            String randomFoodName = StringUtilities.generateRandomString(
                    "food",
                    7,
                    true,
                    false
            );
            GetUserResponseModel.Food.Ingredient ingredient = new GetUserResponseModel.Food.Ingredient(
                    "rice",
                    1,
                    "1"
            );
            GetUserResponseModel.Food food = new GetUserResponseModel.Food(
                    randomFoodName,
                    "randomFood",
                    List.of(ingredient),
                    List.of("Pasta"),
                    "Main",
                    true,
                    "test recipe 02"
            );
            foodPlannerAuth.addFood(food);
        } catch (FailedCallException e) { log.success("logoutTest PASSED!");}

    }
}
