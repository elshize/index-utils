package edu.nyu.tandon.forwardindex;

import edu.nyu.tandon.forwardindex.ForwardIndex.TermMap;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * @author michal.siedlaczek@nyu.edu
 */
public class TermMapTest {

    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();

    @Test
    public void read() throws IOException {
        // given
        File f = testFolder.newFile();
        FileWriter writer = new FileWriter(f);
        writer.append("0\n1\n2\n3\n4\n5");
        writer.close();

        // when
        TermMap termMap = spy(new TermMap(f, 5));

        // then
        assertThat(termMap.read(0L), equalTo("0"));
        assertThat(termMap.read(1L), equalTo("1"));
        assertThat(termMap.read(2L), equalTo("2"));
        verify(termMap, times(3)).readLine();
        assertThat(termMap.read(0L), equalTo("0"));
        verify(termMap, times(4)).readLine();
        assertThat(termMap.read(5L), equalTo("5"));
        assertThat(termMap.read(4L), equalTo("4"));
        assertThat(termMap.read(3L), equalTo("3"));
        verify(termMap, times(18)).readLine();
    }

}
