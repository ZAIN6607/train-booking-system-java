package com.loose.coupling.coupling;

public class UserDatabaseProvider implements UserDataProvider{
    public String getUserDetails() {

        return "User Details from database";
    }
}