Run mongodb in `\docker-compose` folder with command:

```
docker compose -f mongo.yml up -d
```

Run application. 

Use postman collection in `\postman` folder to testing API. 
If you don't have Postman app installed, use `https://web.postman.co/`, login or register 
and use `https://web.postman.co/workspace`. Use `import` button to import postman collection.

### Flow:

1. Call the "signup" endpoint. This will lead to the creation of a new user and return an authorization token.
2. Use the token to invoke other methods. In the Authorization tab, select "Bearer Token" and paste the token obtained during the registration of the new user.

   ![image](https://github.com/valera7979/twiter/assets/9870157/63fecdfb-63a7-4cfd-a1ec-c9fe4185bf19)

4. For an existing user, the token can be obtained by authenticating through the "/signin" method.
