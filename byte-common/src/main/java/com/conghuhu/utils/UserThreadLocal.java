package com.conghuhu.utils;


import com.conghuhu.entity.User;

/**
 * // 线程变量隔离,没用到
 * @author conghuhu
 * @create 2021-10-12 10:16
 */
public class UserThreadLocal {

    private UserThreadLocal() {
    }

    private static final ThreadLocal<User> LOCAL = new ThreadLocal<>();

    public static void put(User user) {
        LOCAL.set(user);
    }

    public static User get() {
        return LOCAL.get();
    }

    public static void remove() {
        LOCAL.remove();
    }
}
