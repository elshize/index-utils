package edu.nyu.tandon.forwardindex;

import edu.nyu.tandon.coding.Decoder;
import edu.nyu.tandon.coding.VarByteDecoder;

import java.io.ByteArrayInputStream;
import java.util.Iterator;

/**
 * @author michal.siedlaczek@nyu.edu
 */
public class Document implements Iterable<Long> {

    protected DocumentMetadata metadata;
    protected byte[] content;
    protected Decoder decoder;

    public Document(DocumentMetadata metadata, byte[] content, Decoder decoder) {
        this.metadata = metadata;
        this.content = content;
        this.decoder = decoder;
    }

    public DocumentMetadata getMetadata() {
        return metadata;
    }

    public byte[] getContent() {
        return content;
    }

    @Override
    public Iterator<Long> iterator() {
        return new Iterator<Long>() {

            private int remainingTerms = metadata.getCount();
            private ByteArrayInputStream input = new ByteArrayInputStream(content);

            @Override
            public boolean hasNext() {
                return remainingTerms > 0;
            }

            @Override
            public Long next() {
                remainingTerms--;
                try {
                    return decoder.decodeLong(input);
                } catch (Exception e) {
                    throw new IllegalStateException("could not decode next number", e);
                }
            }
        };
    }
}
