package com.ssandeep79.springseleniumdemo.resource;

import com.ssandeep79.springseleniumdemo.SpringBaseTestNGTest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.FileCopyUtils;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

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
    @Value("https://www.w3.org/TR/2003/REC-PNG-20031110/iso_8859-1.txt")
    private Resource downloadResource;
    @Value("classpath:data/testdata2.csv")
    private Resource dynamicDownloadResource;
    @Value("${screenshot.path}")
    private Path path;
    @Value("${screenshot.path}/some.txt")
    private Path downloadPath;



    @Autowired
    private ResourceLoader resourceLoader;

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

    @Test
    public void testDownloadResource() throws IOException {
        log.info("------------------ {} -----------------", downloadResource.getURL());
        log.info(downloadResource.getFilename());
        FileCopyUtils.copy(downloadResource.getInputStream(), Files.newOutputStream(downloadPath));
    }

    @DataProvider
    public Object[] getDynamicDownloadResource() throws IOException {
        return  Files.readAllLines(dynamicDownloadResource.getFile().toPath())
                    .stream()
                    .map(s -> s.split(","))
                    .toArray(Object[][]::new);
    }

    @Test (dataProvider = "getDynamicDownloadResource")
    public void testDynamicDownloadResource(String urlToDownload, String outFile) throws IOException {
        log.info("------------------ {} -----------------", urlToDownload);
        log.info(outFile);
        FileCopyUtils.copy(resourceLoader.getResource(urlToDownload).getInputStream(),
            Files.newOutputStream(path.resolve(outFile)));
    }
}
