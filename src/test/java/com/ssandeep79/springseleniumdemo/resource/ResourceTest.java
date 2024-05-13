package com.ssandeep79.springseleniumdemo.resource;

import com.ssandeep79.springseleniumdemo.SpringBaseTestNGTest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.util.FileCopyUtils;
import org.testng.annotations.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

@Slf4j
public class ResourceTest extends SpringBaseTestNGTest {
    @Value("classpath:data/testdata.csv")
    private Resource classPathResource;
    @Value("file:C:/Data/IdeaProjects/springBootSeleniumAutomation/src/test/resources/data/testdata.csv")
    private Resource fileResource;
    @Value("https://www.google.com")
    private Resource urlResource;
    @Value("https://www.w3.org/TR/PNG/iso_8859-1.txt")
    private Resource downloadResource;

    @Value("${screenshot.path}/some.txt")
    private Path downloadPath;

    @Test
    public void testResource() throws IOException {
        log.info("------------------ {} -----------------", classPathResource.getURL());
        log.info(classPathResource.getFilename());
        Files.readAllLines(classPathResource.getFile().toPath()).forEach(System.out::println);

        log.info("------------------ {} -----------------", fileResource.getURL());
        log.info(fileResource.getFilename());
        Files.readAllLines(fileResource.getFile().toPath()).forEach(System.out::println);

        log.info("------------------ {} -----------------", urlResource.getURL());
        log.info(urlResource.getFilename());
        System.out.println(Arrays.toString(urlResource.getInputStream().readAllBytes()));
    }

    @Test (expectedExceptions = FileNotFoundException.class)
    public void testDownloadResource() throws IOException {
        log.info("------------------ {} -----------------", downloadResource.getURL());
        log.info(downloadResource.getFilename());
        FileCopyUtils.copy(downloadResource.getInputStream(), Files.newOutputStream(downloadPath));
    }
}
