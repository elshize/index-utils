package edu.nyu.tandon.forwardindex;

import edu.nyu.tandon.utils.LineIterator;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

/**
 * @author michal.siedlaczek@nyu.edu
 */
public class DocumentMetadataIterator implements Iterator<DocumentMetadata> {

    private LineIterator input;

    protected DocumentMetadataIterator(LineIterator input) {
        this.input = input;
    }

    public static DocumentMetadataIterator fromFile(File file) throws IOException {
        return new DocumentMetadataIterator(LineIterator.fromFile(file));
    }

    public static DocumentMetadataIterator fromFile(String file) throws IOException {
        return fromFile(new File(file));
    }

    protected DocumentMetadata lineToDocumentMetadata(String line) {
        String[] fields = line.trim().split("\\s+");
        if (fields.length < 5) {
            throw new IllegalArgumentException(String.format("the following line contains too few fields:\n%s", line));
        }
        return new DocumentMetadata(
                Long.parseLong(fields[1]),
                fields[0],
                Long.parseLong(fields[2]),
                Integer.parseInt(fields[3]),
                Integer.parseInt(fields[4])
        );
    }

    @Override
    public boolean hasNext() {
        return input.hasNext();
    }

    @Override
    public DocumentMetadata next() {
        return lineToDocumentMetadata(input.next());
    }
}
