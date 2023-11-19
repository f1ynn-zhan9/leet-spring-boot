package com.example.leetspringboot.pseudo;

import org.springframework.lang.Nullable;

/**
 * refer to {@link org.springframework.web.servlet.HandlerInterceptor}
 */
public interface PseudoHandlerInterceptor {
    default boolean preHandle() throws Exception {
        return true;
    }

    default void postHandle() throws Exception {
    }

    default void afterCompletion(@Nullable Exception ex) throws Exception {
    }

}
