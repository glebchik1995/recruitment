package com.java.recruitment.service;

import org.springframework.core.io.Resource;

import java.io.IOException;

public interface IFileManager {

    void upload(byte[] data, String key) throws IOException;
    Resource download(String key) throws IOException;
    void delete(String key) throws IOException;
}
