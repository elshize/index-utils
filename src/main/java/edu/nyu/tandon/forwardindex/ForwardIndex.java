package edu.nyu.tandon.forwardindex;

import edu.nyu.tandon.coding.Decoder;
import edu.nyu.tandon.utils.ImmutableCachingMap;

import java.io.*;

/**
 * @author michal.siedlaczek@nyu.edu
 */
public class ForwardIndex {

    public static class TermMap extends ImmutableCachingMap<Long, String> {

        private File termMapFile;
        private BufferedReader reader;
        private long readCount;

        public TermMap(File termMapFile, int capacity) throws FileNotFoundException {
            super(capacity);
            this.termMapFile = termMapFile;
            reader = new BufferedReader(new FileReader(termMapFile));
            readCount = 0;
        }

        protected String readLine() throws IOException {
            readCount++;
            return reader.readLine();
        }

        @Override
        protected String read(Long key) {
            try {
                if (key < readCount) {
                    reader.close();
                    reader = new BufferedReader(new FileReader(termMapFile));
                    readCount = 0;
                }
                while (key > readCount) readLine();
                return readLine();
            } catch (IOException e) {
                throw new RuntimeException("could not read key", e);
            }
        }

    }

    protected File indexFile;
    protected File metadataFile;
    protected File termMapFile;
    protected Decoder decoder;

    public ForwardIndex(File indexFile, File metadataFile, File termMapFile, Decoder decoder) throws IOException {
        this.indexFile = indexFile;
        this.metadataFile = metadataFile;
        this.termMapFile = termMapFile;
        this.decoder = decoder;
    }

    protected ForwardIndexReader getReader() {
        return new ForwardIndexReader(indexFile, metadataFile, decoder);
    }

    public TermMap getTermMap(int capacity) throws FileNotFoundException {
        return new TermMap(termMapFile, capacity);
    }

}
