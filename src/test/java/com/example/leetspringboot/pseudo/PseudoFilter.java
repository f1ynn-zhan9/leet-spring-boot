package com.example.leetspringboot.pseudo;

/**
 * refer to {@link javax.servlet.Filter}
 */
public interface PseudoFilter {
    void doFilter(PseudoFilterChain filterChain) throws Exception;
}
