package com.hp.ipg.test.framework.genericLib.testExecution;

import org.apache.logging.log4j.ThreadContext;
import org.slf4j.Logger;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.ITestContext;
import org.testng.annotations.BeforeMethod;
import java.lang.reflect.Method;
import java.util.UUID;

public class TestBase extends AbstractTestNGSpringContextTests {

    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(TestBase.class);

    @BeforeMethod(alwaysRun = true, enabled = true)
    public void baseInitialize(Method m, ITestContext context) throws Exception {

        ThreadContext.push(Long.toString(Math.abs(UUID.randomUUID().hashCode())) + ":" + m.getName());

        LOGGER.info(String.format("\n%s\nStarting %s.%s\n%s",
                "+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++",
                m.getDeclaringClass().getName(),
                m.getName(),
                "+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++"));
    }
}

