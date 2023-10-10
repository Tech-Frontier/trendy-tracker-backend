package com.trendyTracker.Job.repository;


import java.util.Optional;

public interface MessageConsumer<T> {
    public void registJobUrl(T mesage, Optional<String> header) throws Exception;

    public void updateJobUrl(T message, Optional<String> header) throws Exception;

    public void deleteJobUrl();
}
