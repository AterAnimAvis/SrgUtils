/*
 * SRG Utils
 * Copyright (c) 2021
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation version 2.1
 * of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */

package net.minecraftforge.srgutils.test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;

import com.google.common.jimfs.Configuration;
import com.google.common.jimfs.Jimfs;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import net.minecraftforge.srgutils.IMappingFile;
import net.minecraftforge.srgutils.IMappingFile.Format;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MappingTest {

    private static IMappingFile pg;
    private static IMappingFile reverse;

    @BeforeAll
    static void setup() throws IOException {
        pg = IMappingFile.load(TestData.getStream("/installer/input.pg"));
        reverse = pg.reverse();
    }

    @AfterAll
    static void teardown() {
        pg = null;
        reverse = null;
    }

    @ParameterizedTest
    @EnumSource(Format.class)
    void test(Format format) throws IOException, URISyntaxException {
        String filetype = format.name().toLowerCase(Locale.ROOT);

        try (FileSystem memoryFS = Jimfs.newFileSystem("memory", Configuration.unix())) {
            compare(
                pg,
                format,
                TestData.get("/installer/expected." + filetype),
                memoryFS.getPath("./output." + filetype)
            );

            compare(
                reverse,
                format,
                TestData.get("/installer/expected." + filetype + "-rev"),
                memoryFS.getPath("./output." + filetype + "-rev")
            );
        }
    }

    static void compare(IMappingFile file, Format format, Path expected, Path output) throws IOException {
        file.write(output, format, false);
        assertEquals(Files.readAllLines(expected), Files.readAllLines(output));
    }

}
