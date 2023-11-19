package com.example.leetspringboot.pseudo;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.util.stream.IntStream;

@Slf4j
class PseudoFilterChainTests {
    PseudoDispatcherServlet servlet = new PseudoDispatcherServlet();

    @Test
    void testDoFilter() throws Exception {
        var filters = IntStream.range(0, 5)
            .mapToObj(this::buildFilter)
            .toArray(PseudoFilter[]::new);
        var filterChain = buildFilterChain(filters);
        filterChain.doFilter();
    }

    PseudoFilter buildFilter(Object filterId) {
        return filterChain -> {
            log.info("before doFilter: {}", filterId);
            filterChain.doFilter();
            log.info("after doFilter: {}", filterId);
        };
    }

    /**
     * refer to {@link org.apache.catalina.core.ApplicationFilterChain#internalDoFilter(ServletRequest, ServletResponse)}
     *
     * @param filters filters
     * @return {@link PseudoFilterChain}
     */
    PseudoFilterChain buildFilterChain(PseudoFilter[] filters) {
        return new PseudoFilterChain() {
            private final int n = filters.length;
            private int pos = 0;

            @Override
            public void doFilter() throws Exception {
                if (pos < n) {
                    var filter = filters[pos++];
                    filter.doFilter(this);
                    return;
                }
                log.info("We fell off the end of the chain -- call the servlet instance");

                servlet.doDispatch();
            }
        };
    }

}
