package com.example.leetspringboot.pseudo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @see org.springframework.web.servlet.HandlerExecutionChain
 */
@Slf4j
public class PseudoHandlerExecutionChain {
    List<PseudoHandlerInterceptor> interceptorList;
    int interceptorIndex = -1;

    public PseudoHandlerExecutionChain(List<PseudoHandlerInterceptor> interceptorList) {
        this.interceptorList = interceptorList;
    }

    /**
     * refer to {@link org.springframework.web.servlet.HandlerExecutionChain#applyPreHandle(HttpServletRequest, HttpServletResponse)}
     *
     * @return {@code true} if the execution chain should proceed with the
     * next interceptor or the handler itself. Else, DispatcherServlet assumes
     * that this interceptor has already dealt with the response itself.
     */
    boolean applyPreHandle() throws Exception {
        for (int i = 0; i < this.interceptorList.size(); i++) {
            var interceptor = this.interceptorList.get(i);
            if (!interceptor.preHandle()) {
                return false;
            }
            this.interceptorIndex = i;
        }
        return true;
    }

    /**
     * refer to {@link org.springframework.web.servlet.HandlerExecutionChain#applyPostHandle(HttpServletRequest, HttpServletResponse, ModelAndView)}
     */
    void applyPostHandle() throws Exception {
        for (int i = this.interceptorIndex; i >= 0; i--) {
            var interceptor = this.interceptorList.get(i);
            interceptor.postHandle();
        }
    }

    /**
     * refer to {@link org.springframework.web.servlet.HandlerExecutionChain#triggerAfterCompletion(HttpServletRequest, HttpServletResponse, Exception)}
     */
    void triggerAfterCompletion(@Nullable Exception ex) {
        for (int i = this.interceptorIndex; i >= 0; i--) {
            var interceptor = this.interceptorList.get(i);
            try {
                interceptor.afterCompletion(ex);
            } catch (Throwable e) {
                log.error("interceptor.afterCompletion failed", e);
            }
        }
    }

}
