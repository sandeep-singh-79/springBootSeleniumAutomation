package com.ssandeep79.springseleniumdemo.googletest.props;

import com.ssandeep79.springseleniumdemo.SpringBaseTestNGTest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.testng.annotations.Test;

import java.io.IOException;

@Slf4j
public class PropsTest extends SpringBaseTestNGTest {
    @Autowired
    private ResourceLoader loader;

    @Test
    public void testPropsWithResourceLoader () throws IOException {
        log.info(String
                     .valueOf(PropertiesLoaderUtils
                                    .loadProperties(loader
                                                        .getResource("data/my.properties"))));
    }
}
