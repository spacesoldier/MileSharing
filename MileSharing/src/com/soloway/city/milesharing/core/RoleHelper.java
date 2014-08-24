package com.soloway.city.milesharing.core;

/**
 * Created by pens on 24.08.14.
 */
public class RoleHelper {

    public static final int ROLE_DRIVER = 0;
    public static final int ROLE_PASS = 1;

    private static int mRrole = ROLE_PASS;

    public static int getRole() {
        return mRrole;
    }

    public static void setRole(int role) {
        mRrole = role;
    }
}
