package edu.nyu.tandon.forwardindex;

import edu.nyu.tandon.coding.Decoder;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

/**
 * @author michal.siedlaczek@nyu.edu
 */
public class ForwardIndexIterator implements Iterator<Document>, AutoCloseable {

    protected InputStream indexInput;
    protected DocumentMetadataIterator documentMetadataIterator;
    protected Decoder decoder;

    public ForwardIndexIterator(InputStream indexInput, DocumentMetadataIterator documentMetadataIterator, Decoder decoder) {
        this.indexInput = indexInput;
        this.documentMetadataIterator = documentMetadataIterator;
        this.decoder = decoder;
    }

    @Override
    public boolean hasNext() {
        return documentMetadataIterator.hasNext();
    }

    @Override
    public Document next() {
        DocumentMetadata metadata = documentMetadataIterator.next();
        byte[] content = new byte[metadata.getSize()];
        try {
            if (indexInput.read(content) < content.length) {
                throw new IllegalStateException("could not read next document content: not enough bytes available");
            }
        } catch (IOException e) {
            throw new IllegalStateException("could not read next document content", e);
        }
        return new Document(metadata, content, decoder);
    }

    @Override
    public void close() throws Exception {
        indexInput.close();
    }
}
