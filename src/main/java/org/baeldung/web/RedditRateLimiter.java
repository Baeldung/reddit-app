package org.baeldung.web;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.google.common.base.Joiner;
import com.google.common.util.concurrent.RateLimiter;

@Aspect
@Component
@Scope("session")
public class RedditRateLimiter {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final RateLimiter rateLimiter;

    public RedditRateLimiter() {
        rateLimiter = RateLimiter.create(1);
    }

    @Before("execution(* org.baeldung.web.RedditTemplateWrapper.*(..))")
    public void tryExecute(JoinPoint joinPoint) {
        logger.info("Try acquire: " + joinPoint.getSignature().getName() + " (" + Joiner.on(',').join(joinPoint.getArgs()) + ")");
        rateLimiter.acquire();
        logger.info("Acquired Successfully" + joinPoint.getSignature().getName() + " (" + Joiner.on(',').join(joinPoint.getArgs()) + ")");
    }
}
