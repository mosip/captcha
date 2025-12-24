package io.mosip.captcha.util;

import io.mosip.captcha.spi.CaptchaProvider;
import org.junit.Before;
import org.junit.Test;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class CaptchaProviderFactoryTest {

    private CaptchaProviderFactory factory;

    private CaptchaProvider provider1;
    private CaptchaProvider provider2;

    @Before
    public void setUp() throws Exception {
        factory = new CaptchaProviderFactory();

        // Mock providers
        provider1 = mock(CaptchaProvider.class);
        when(provider1.getProviderName()).thenReturn("provider1");

        provider2 = mock(CaptchaProvider.class);
        when(provider2.getProviderName()).thenReturn("provider2");

        // Set private 'providers' field using ReflectionUtils
        Field providersField = ReflectionUtils.findField(CaptchaProviderFactory.class, "providers");
        ReflectionUtils.makeAccessible(providersField); // makes private field accessible
        ReflectionUtils.setField(providersField, factory, Arrays.asList(provider1, provider2));

        // Set private 'mapping' field using ReflectionUtils
        Field mappingField = ReflectionUtils.findField(CaptchaProviderFactory.class, "mapping");
        ReflectionUtils.makeAccessible(mappingField);
        ReflectionUtils.setField(mappingField, factory, Map.of(
                "module1", "provider1",
                "module2", "provider2",
                "default", "provider1"
        ));
    }

    @Test
    public void testGetCaptchaProviderExistingModule() {
        CaptchaProvider result = factory.getCaptchaProvider("module1");
        assertNotNull(result);
        assertEquals("provider1", result.getProviderName());
    }

    @Test
    public void testGetCaptchaProviderDefaultModule() {
        CaptchaProvider result = factory.getCaptchaProvider("unknownModule");
        assertNotNull(result);
        assertEquals("provider1", result.getProviderName()); // default
    }

    @Test(expected = NoSuchElementException.class)
    public void testGetCaptchaProviderNoMatchingProvider() {
        // Remove default mapping to force failure
        Field mappingField = ReflectionUtils.findField(CaptchaProviderFactory.class, "mapping");
        ReflectionUtils.makeAccessible(mappingField);
        ReflectionUtils.setField(mappingField, factory, Map.of("module1", "providerX"));

        factory.getCaptchaProvider("module1"); // should throw exception
    }
}
