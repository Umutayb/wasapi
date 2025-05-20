db = db.getSiblingDB("food-planner");

db.createCollection("Roles");

db.Roles.insertMany([
  { "_id": ObjectId("000000000000000000000001"), "name": "ROLE_USER" },
  { "_id": ObjectId("000000000000000000000002"), "name": "ROLE_ADMIN" },
  { "_id": ObjectId("000000000000000000000003"), "name": "ROLE_MODERATOR" }
]);

db.createCollection("Users");

db.Users.insertOne({
  "_id": ObjectId("682c646cd176f11c48d861e0"),
  "username": "nice-user",
  "email": "nice-user@admin.com",
  "password": "$2a$10$E8ulfBRsuKrgQqLWytcbkuKx3SU3tz1L4grAM3m0N9i/SwqTYvnFu",
  "roles": [
    {
      "_id": ObjectId("000000000000000000000002"),
      "name": "ROLE_ADMIN"
    }
  ],
  "menu": [
    {
      "id": "food1",
      "name": "Lasagna",
      "ingredients": [
        {
          "name": "lasagna noodles",
          "quantity": 1,
          "unit": "pack"
        }
      ],
      "categories": ["Pasta"],
      "courseType": "Main",
      "primarilyCarbohydrate": true,
      "recipe": "Layer and bake"
    },
    {
      "id": "food2",
      "name": "Grilled Chicken",
      "ingredients": [
        {
          "name": "chicken breast",
          "quantity": 1,
          "unit": "piece"
        }
      ],
      "categories": ["Chicken"],
      "courseType": "Main",
      "primarilyCarbohydrate": false,
      "recipe": "Grill it"
    },
    {
      "id": "food3",
      "name": "Margarita Pizza",
      "ingredients": [
        {
          "name": "mozzarella",
          "quantity": 100,
          "unit": "g"
        }
      ],
      "categories": ["Pizza"],
      "courseType": "Main",
      "primarilyCarbohydrate": true,
      "recipe": "Bake with tomato and cheese"
    }
  ]
});
