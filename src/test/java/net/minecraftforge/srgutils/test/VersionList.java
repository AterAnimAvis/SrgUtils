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

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import net.minecraftforge.srgutils.MinecraftVersion;

public class VersionList {

    @ParameterizedTest
    @CsvFileSource(resources="/versions.csv")
    void testParse(String version) {
        assertNotNull(MinecraftVersion.from(version), "Unable to parse " + version);
    }

    @Test
    void testSort() throws IOException, URISyntaxException {
        List<String> versions = Files
            .readAllLines(Paths.get(VersionList.class.getResource("/versions.csv").toURI()))
            .stream()
            .filter(line -> !line.startsWith("#"))
            .collect(Collectors.toList());

        List<String> sorted = versions
            .stream()
            .map(MinecraftVersion::from)
            .sorted(Collections.reverseOrder())
            .map(MinecraftVersion::toString)
            .peek(System.out::println)
            .collect(Collectors.toList());

        assertLinesMatch(versions, sorted, "Invalid sort");
    }
}
