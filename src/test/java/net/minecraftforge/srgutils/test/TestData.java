package net.minecraftforge.srgutils.test;

import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TestData {

    public static Path get(String name) throws URISyntaxException {
        return Paths.get(TestData.class.getResource(name).toURI());
    }

    public static InputStream getStream(String name) {
        return TestData.class.getResourceAsStream(name);
    }

}
