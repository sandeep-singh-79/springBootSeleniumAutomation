package com.ssandeep79.springseleniumdemo.window;

import com.ssandeep79.springseleniumdemo.SpringBaseTestNGTest;
import com.ssandeep79.springseleniumdemo.page.window.MainPage;
import com.ssandeep79.springseleniumdemo.page.window.PageA;
import com.ssandeep79.springseleniumdemo.page.window.PageB;
import com.ssandeep79.springseleniumdemo.page.window.PageC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestPropertySource;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.testng.Assert.assertTrue;

@TestPropertySource(properties = "browser=chrome")
public class WindowSwitchTest extends SpringBaseTestNGTest {
    @Autowired
    private MainPage mainPage;
    @Autowired
    private PageA pageA;
    @Autowired
    private PageB pageB;
    @Autowired
    private PageC pageC;

    @BeforeClass
    public void setup() {
        mainPage.goTo();
        mainPage.isAt();
        mainPage.launchAllWindows();
    }

    @Test
    public void switchWindowTest () {
        assertTrue(pageA.isAt());
        pageA.addToArea("Hello from Page A");

        assertTrue(pageB.isAt());
        pageB.addToArea("Hello from Page B");
    }

    @DataProvider
    public Object[] dataProvider() {
        return new Object[] {
                3, 4, 1, 5, 6, 2
        };
    }

    @Test (dataProvider = "dataProvider")
    public void switchWithWindowAspect (int number) {
        pageA.addToArea(number + "\n");
        pageB.addToArea(number * 2 + "\n");
        pageC.addToArea(number * 3 + "\n");
    }
}
