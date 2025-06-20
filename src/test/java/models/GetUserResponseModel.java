package models;

import java.util.List;

public class GetUserResponseModel {

    public String id;
    public String username;
    public String email;
    public String password;
    public List<Food> menu;
    public List<Role> roles;

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public List<Food> getMenu() {
        return menu;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public static class Food{
       public String name;
       public String id;
       public List<Ingredient> ingredients;
       public List<String> categories;
       public String courseType;
       public boolean primarilyCarbohydrate;
       public String recipe;

        public Food() {
        }

        public Food(String name, String id, List<Ingredient> ingredients, List<String> categories, String courseType, boolean primarilyCarbohydrate, String recipe) {
            this.name = name;
            this.id = id;
            this.ingredients = ingredients;
            this.categories = categories;
            this.courseType = courseType;
            this.primarilyCarbohydrate = primarilyCarbohydrate;
            this.recipe = recipe;
        }

        public String getName() {
            return name;
        }

        public String getId() {
            return id;
        }

        public List<Ingredient> getIngredients() {
            return ingredients;
        }

        public List<String> getCategories() {
            return categories;
        }

        public String getCourseType() {
            return courseType;
        }

        public boolean isPrimarilyCarbohydrate() {
            return primarilyCarbohydrate;
        }

        public String getRecipe() {
            return recipe;
        }

        public static class Ingredient {
            public String name;
            public int quantity;
            public String unit;

            public Ingredient() {
            }

            public Ingredient(String name, int quantity, String unit) {
                this.name = name;
                this.quantity = quantity;
                this.unit = unit;
            }

            public String getName() {
                return name;
            }

            public int getQuantity() {
                return quantity;
            }

            public String getUnit() {
                return unit;
            }
        }
    }

    public static class Role {
        String id;
        String name;

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }
    }
}
