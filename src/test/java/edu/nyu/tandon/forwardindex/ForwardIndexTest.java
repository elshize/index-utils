package edu.nyu.tandon.forwardindex;

import edu.nyu.tandon.coding.Encoder;
import edu.nyu.tandon.coding.VarByteDecoder;
import edu.nyu.tandon.coding.VarByteEncoder;
import edu.nyu.tandon.forwardindex.ForwardIndex.TermMap;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsIterableContainingInOrder.contains;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * @author michal.siedlaczek@nyu.edu
 */
public class ForwardIndexTest {

    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();

    private ForwardIndex forwardIndex;

    public File setupIndex() throws IOException {
        File file = testFolder.newFile();
        Encoder encoder = VarByteEncoder.get();
        FileOutputStream o = new FileOutputStream(file);
        // a
        encoder.encodeInt(o, 0);
        // b
        encoder.encodeInt(o, 1);
        // c
        encoder.encodeInt(o, 2);
        // d
        encoder.encodeInt(o, 3);
        // e
        encoder.encodeInt(o, 4);
        // c
        encoder.encodeInt(o, 2);
        o.close();
        return file;
    }

    public File setupMetadata() throws IOException {
        File file = testFolder.newFile();
        writeLines(file, new String[] {
                "Document1 0 0 3 3",
                "Document2 1 3 3 3"
        });
        return file;
    }

    public File setupTermMap() throws IOException {
        File file = testFolder.newFile();
        writeLines(file, new String[] { "a", "b", "c", "d", "e" });
        return file;
    }

    public void writeLines(File file, String[] lines) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        for (String line : lines) {
            writer.append(line);
            writer.newLine();
        }
        writer.close();
    }

    @Before
    public void setupFI() throws IOException {
        forwardIndex = new ForwardIndex(setupIndex(), setupMetadata(), setupTermMap(), VarByteDecoder.get());
    }

    @Test
    public void integration() throws IOException {
        ForwardIndexIterator reader = forwardIndex.getReader().getIterator();
        TermMap termMap = forwardIndex.getTermMap(10);
        Document document;
        List<String> terms;

        // Document 1
        document = reader.next();
        assertThat(document.getMetadata().getId(), equalTo(0L));
        assertThat(document.getMetadata().getTitle(), equalTo("Document1"));
        terms = StreamSupport.stream(document.spliterator(), false)
                .map(termMap::get)
                .collect(Collectors.toList());
        assertThat(terms, contains("a", "b", "c"));

        // Document 2
        document = reader.next();
        assertThat(document.getMetadata().getId(), equalTo(1L));
        assertThat(document.getMetadata().getTitle(), equalTo("Document2"));
        terms = StreamSupport.stream(document.spliterator(), false)
                .map(termMap::get)
                .collect(Collectors.toList());
        assertThat(terms, contains("d", "e", "c"));

        assertThat(reader.hasNext(), equalTo(false));
    }

}
