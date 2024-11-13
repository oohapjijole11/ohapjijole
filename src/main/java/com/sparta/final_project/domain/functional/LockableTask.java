package com.sparta.final_project.domain.functional;

@FunctionalInterface
public interface LockableTask <T> {
    T execute() throws RuntimeException;
}
