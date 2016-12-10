package edu.nyu.tandon.forwardindex;

import edu.nyu.tandon.coding.Decoder;
import edu.nyu.tandon.utils.ImmutableCachingMap;
import edu.nyu.tandon.utils.LineIterator;

import java.io.*;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;

/**
 * @author michal.siedlaczek@nyu.edu
 */
public class ForwardIndexReader implements Iterable<Document> {

    protected Decoder decoder;
    protected File indexFile;
    protected File metadataFile;

    public ForwardIndexReader(File indexFile, File metadataFile, Decoder decoder) {
        this.indexFile = indexFile;
        this.metadataFile = metadataFile;
        this.decoder = decoder;
    }

    @Override
    public Iterator<Document> iterator() {
        try {
            return getIterator();
        } catch (IOException e) {
            throw new RuntimeException("could not retrieve the iterator", e);
        }
    }

    public ForwardIndexIterator getIterator() throws IOException {
        return new ForwardIndexIterator(
                new FileInputStream(indexFile),
                new DocumentMetadataIterator(LineIterator.fromFile(metadataFile)),
                decoder);
    }

}
