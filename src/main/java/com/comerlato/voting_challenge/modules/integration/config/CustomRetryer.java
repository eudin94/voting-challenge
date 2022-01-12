package com.comerlato.voting_challenge.modules.integration.config;

import com.comerlato.voting_challenge.modules.integration.exception.IntegrationException;
import feign.RetryableException;
import feign.Retryer;
import org.springframework.http.HttpStatus;

import java.util.concurrent.TimeUnit;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

public class CustomRetryer extends Retryer.Default {
    private final int maxAttempts;
    private final long period;
    private final long maxPeriod;
    private int attempts;

    public CustomRetryer() {
        this(100L, TimeUnit.SECONDS.toMillis(1L), 5);
    }

    public CustomRetryer(long period, long maxPeriod, int maxAttempts) {
        this.period = period;
        this.maxPeriod = maxPeriod;
        this.maxAttempts = maxAttempts;
        this.attempts = 1;
    }

    @Override
    protected long currentTimeMillis() {
        return System.currentTimeMillis();
    }

    @Override
    public void continueOrPropagate(RetryableException e) {
        if (this.attempts++ >= this.maxAttempts) {
            if (e.status() == -1)
                throw new IntegrationException(INTERNAL_SERVER_ERROR, e);
            throw new IntegrationException(HttpStatus.valueOf(e.status()), e);
        } else {
            long interval;
            if (e.retryAfter() != null) {
                interval = e.retryAfter().getTime() - this.currentTimeMillis();
                if (interval > this.maxPeriod) {
                    interval = this.maxPeriod;
                }
                if (interval < 0L) {
                    return;
                }
            } else {
                interval = this.getNextMaxInterval();
            }
            try {
                Thread.sleep(interval);
            } catch (InterruptedException var5) {
                Thread.currentThread().interrupt();
                throw e;
            }
        }
    }

    long getNextMaxInterval() {
        long interval = (long) ((double) this.period * Math.pow(1.5D, (double) (this.attempts - 1)));
        return Math.min(interval, this.maxPeriod);
    }

}
