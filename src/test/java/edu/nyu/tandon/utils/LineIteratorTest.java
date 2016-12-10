package edu.nyu.tandon.utils;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * @author michal.siedlaczek@nyu.edu
 */
@RunWith(Parameterized.class)
public class LineIteratorTest {

    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();

    @Parameters(name = "{index}: {0} -> {1}")
    public static Iterable<Object[]> data() {
        return Arrays.asList(
                new Object[] { "", Collections.emptyList() },
                new Object[] { "line1\nline2", Arrays.asList("line1", "line2") },
                new Object[] { "line1\nline2\n", Arrays.asList("line1", "line2") }
        );
    }

    @Parameter
    public String fileContent;

    @Parameter(value = 1)
    public List<String> expectedLines;

    @Test
    public void testIterator() throws IOException {

        // given
        File file = testFolder.newFile();
        Writer writer = new FileWriter(file);
        writer.write(fileContent);
        writer.close();

        try (LineIterator iterator = LineIterator.fromFile(file)) {
            List<String> actualLines = new ArrayList<>();
            iterator.forEachRemaining(actualLines::add);
            assertThat(actualLines, equalTo(expectedLines));
        }
    }

}
