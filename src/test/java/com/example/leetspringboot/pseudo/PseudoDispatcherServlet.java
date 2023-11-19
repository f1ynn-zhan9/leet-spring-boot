package com.example.leetspringboot.pseudo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.NestedServletException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @see org.springframework.web.servlet.DispatcherServlet
 */
@Slf4j
class PseudoDispatcherServlet {
    /**
     * @see org.springframework.web.servlet.DispatcherServlet#doDispatch(HttpServletRequest, HttpServletResponse)
     * @see org.springframework.web.servlet.DispatcherServlet#processDispatchResult(HttpServletRequest, HttpServletResponse, HandlerExecutionChain, ModelAndView, Exception)
     */
    void doDispatch() throws Exception {
        PseudoHandlerExecutionChain mappedHandler = null;
        try {
            mappedHandler = new PseudoHandlerExecutionChain(buildInterceptorList());

            if (!mappedHandler.applyPreHandle()) {
                return;
            }

            log.info("Actually invoke the handler");

            mappedHandler.applyPostHandle();
            mappedHandler.triggerAfterCompletion(null);

        } catch (Throwable e) {
            triggerAfterCompletion(mappedHandler,
                new NestedServletException("Handler processing failed", e));
        }
    }

    /**
     * @param ex {@link Exception}
     * @see org.springframework.web.servlet.DispatcherServlet#triggerAfterCompletion(HttpServletRequest, HttpServletResponse, HandlerExecutionChain, Exception)
     */
    void triggerAfterCompletion(PseudoHandlerExecutionChain mappedHandler, Exception ex) throws Exception {
        if (mappedHandler != null) {
            mappedHandler.triggerAfterCompletion(ex);
        }
        throw ex;
    }

    List<PseudoHandlerInterceptor> buildInterceptorList() {
        return IntStream.range(0, 5)
            .mapToObj(this::buildInterceptor)
            .collect(Collectors.toList());
    }

    PseudoHandlerInterceptor buildInterceptor(Object id) {
        return new PseudoHandlerInterceptor() {
            @Override
            public boolean preHandle() throws Exception {
                log.info("Interceptor preHandle: {}", id);
                return true;
            }

            @Override
            public void postHandle() throws Exception {
                log.info("Interceptor postHandle: {}", id);
            }

            @Override
            public void afterCompletion(Exception ex) throws Exception {
                if (Objects.isNull(ex)) {
                    log.info("Interceptor afterCompletion: {}", id);
                } else {
                    log.error("Interceptor afterCompletion with exception: {}", id, ex);
                }
            }
        };
    }

}
