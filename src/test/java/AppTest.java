
import exceptions.FailedCallException;
import context.ContextStore;
import models.*;
import org.junit.*;
import utils.*;
import java.util.List;

public class AppTest {

    static Printer log = new Printer(AppTest.class);

    @Before
    public void before(){
        ContextStore.loadProperties("test.properties", "secret.properties");
        FoodPlanner foodPlanner = new FoodPlanner();

        log.info("nice-user authentication is in progress...");
        UserAuthRequestModel userAuthRequestModel = new UserAuthRequestModel(
                "nice-user",
                "Test-123"
        );
        UserAuthResponseModel userAuthResponse = foodPlanner.signIn(userAuthRequestModel);
        ContextStore.put("jwtToken", userAuthResponse.getJwtToken());
        log.success("nice-user authentication is successful!");
    }

    @Test
    public void deleteUserTest() {
        FoodPlanner foodPlanner = new FoodPlanner();
        FoodPlanner.Auth foodPlannerAuth = new FoodPlanner.Auth();
        String deleteTestUser = StringUtilities.generateRandomString("user", 9, false, true);
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
        FoodPlanner foodPlanner = new FoodPlanner();
        String randomUsername = StringUtilities.generateRandomString("user", 9, false, true);
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
        FoodPlanner foodPlanner = new FoodPlanner();
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
        FoodPlanner.Auth foodPlannerAuth = new FoodPlanner.Auth();
        log.info("Acquiring the nice-user food data...");
        GetUserResponseModel getUserResponse = foodPlannerAuth.getUser();

        Assert.assertEquals("Username does not match!", getUserResponse.getUsername(),"nice-user");
        log.success("Username verified!");
        Assert.assertEquals("Email does not match!", getUserResponse.getEmail(),"nice-user@admin.com");
        log.success("Email verified!");
        Assert.assertEquals("Role does not match!", getUserResponse.getRoles().get(0).getName(),"ROLE_ADMIN");
        log.success("Role verified!");
        Assert.assertEquals("First food does not match!", getUserResponse.getMenu().get(0).getName(),"Lasagna");
        log.success("First food verified! -> lazanya");
        Assert.assertEquals("Second food does not match!", getUserResponse.getMenu().get(1).getName(),"Grilled Chicken");
        log.success("Second food verified! -> chicken breast");
        Assert.assertEquals("Third food does not match!", getUserResponse.getMenu().get(2).getName(),"Margarita Pizza");
        log.success("Third food verified! -> margarita pizza");

        log.success("getNiceUserTest PASSED!");
    }

    @Test
    public void addFoodTest() {
        FoodPlanner.Auth foodPlannerAuth = new FoodPlanner.Auth();
        String randomFoodName = StringUtilities.generateRandomString("food", 7, true, false);


        GetUserResponseModel.Food.Ingredient ingredient = new GetUserResponseModel.Food.Ingredient("rice", 1, "1");
        GetUserResponseModel.Food food = new GetUserResponseModel.Food(randomFoodName, "randomFood", List.of(ingredient), List.of("Pasta"), "Main", true, "test rice");
        GetUserResponseModel responseModel = foodPlannerAuth.addFood(food);

        Assert.assertEquals("Username does not match!", responseModel.getUsername(),"nice-user");
        log.success("Username verified!");
        Assert.assertEquals("Email does not match!", responseModel.getEmail(),"nice-user@admin.com");
        log.success("Email verified!");
        Assert.assertEquals("Role does not match!", responseModel.getRoles().get(0).getName(),"ROLE_ADMIN");
        log.success("Role verified!");
        Assert.assertEquals("Third ingredient does not match!", responseModel.getMenu().get(3).getName(),"Rice");
        log.success("Added food is verified! -> Rice");

        log.success("addFoodTest PASSED!");
    }

    @Test
    public void logoutTest() {
        FoodPlanner foodPlanner = new FoodPlanner();
        FoodPlanner.Auth foodPlannerAuth = new FoodPlanner.Auth();

        String logoutUser = StringUtilities.generateRandomString("user", 9, false, true);
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
        SimpleMessageResponseModel logoutResponse = foodPlannerAuth.logout();
        ContextStore.update("jwtToken", userAuthResponse.getJwtToken());

        Assert.assertEquals("Logout message does not match!", "Logged out successfully", logoutResponse.getMessage());
        log.success("Logout message is validated!");

        String randomFoodName = StringUtilities.generateRandomString("food", 7, true, false);

        GetUserResponseModel.Food.Ingredient ingredient = new GetUserResponseModel.Food.Ingredient("rice", 1, "1");
        GetUserResponseModel.Food food = new GetUserResponseModel.Food(randomFoodName, "randomFood", List.of(ingredient), List.of("Pasta"), "Main", true, "test rice");
        try {
            foodPlannerAuth.addFood(food);
        } catch (FailedCallException e) { log.success("logoutTest PASSED!");}

    }

}
