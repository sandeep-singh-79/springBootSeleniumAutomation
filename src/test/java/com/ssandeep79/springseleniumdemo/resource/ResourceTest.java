package com.ssandeep79.springseleniumdemo.resource;

import com.ssandeep79.springseleniumdemo.SpringBaseTestNGTest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.file.Files;

public class ResourceTest extends SpringBaseTestNGTest {
    @Value("classpath:data/testdata.csv")
    private Resource classPathResource;
    @Value("file:C:/Data/IdeaProjects/springBootSeleniumAutomation/src/test/resources/data/testdata.csv")
    private Resource fileResource;
    @Value("https://www.google.com")
    private Resource urlResource;

    @Test
    public void testResource() throws IOException {
        System.out.println("------------------" + classPathResource.getURL() + "-----------------");
        System.out.println(classPathResource.getFilename());
        Files.readAllLines(classPathResource.getFile().toPath()).forEach(System.out::println);

        System.out.println("------------------" + fileResource.getURL() + "-----------------");
        System.out.println(fileResource.getFilename());
        Files.readAllLines(fileResource.getFile().toPath()).forEach(System.out::println);

        System.out.println("------------------" + urlResource.getURL() + "-----------------");
        System.out.println(urlResource.getFilename());
        System.out.println(urlResource.getInputStream().readAllBytes());
    }
}
