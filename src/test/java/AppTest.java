
import exceptions.FailedCallException;
import context.ContextStore;
import models.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
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
    public void signUpTest() {
        FoodPlanner foodPlanner = new FoodPlanner();
        UserSignUpModel userSignUpModel = new UserSignUpModel(
                "test-user",
                "test-user@user.com",
                "Test-123",
                List.of("ROLE_USER"));

        UserSignUpResponseModel userSignUpResponse = foodPlanner.signUp(userSignUpModel);
        Assert.assertEquals("Sign up test is failed!" ,"User registered successfully!", userSignUpResponse.getMessage());
        log.success("signUpTest PASSED!");
    }

    @Test
    public void signInTest() {
        FoodPlanner foodPlanner = new FoodPlanner();
        UserAuthRequestModel userAuthRequestModel = new UserAuthRequestModel(
                "test-user",
                "Test-123"
        );
        UserAuthResponseModel userAuthResponse = foodPlanner.signIn(userAuthRequestModel);
        Assert.assertEquals("username does not match!", userAuthResponse.getUsername(),"test-user");
        ContextStore.put("testUserId", userAuthResponse.getId());
        log.success("signInTest PASSED!");
    }

    @Test
    public void deleteUserTest() {
        FoodPlanner foodPlanner = new FoodPlanner();
        FoodPlanner.Auth foodPlannerAuth = new FoodPlanner.Auth();

        foodPlannerAuth.deleteUser(ContextStore.get("testUserId").toString());
        UserAuthRequestModel userAuthRequestModel = new UserAuthRequestModel(
                "test-user",
                "Test-123"
        );
        try {
           foodPlanner.signIn(userAuthRequestModel);
        }
        catch (FailedCallException e) {
            log.success("deleteUserTest PASSED!");
        }
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

        GetUserResponseModel.Food.Ingredient ingredient = new GetUserResponseModel.Food.Ingredient("rice", 1, "1");
        GetUserResponseModel.Food food = new GetUserResponseModel.Food("Rice", "seven", List.of(ingredient), List.of("Pasta"), "Main", true, "test rice");
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

}
