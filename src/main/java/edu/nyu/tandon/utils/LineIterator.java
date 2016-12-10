package edu.nyu.tandon.utils;

import java.io.*;
import java.util.Iterator;

/**
 * @author michal.siedlaczek@nyu.edu
 */
public class LineIterator implements Iterator<String>, AutoCloseable {

    private BufferedReader reader;
    private String nextLine;

    protected LineIterator(BufferedReader reader) throws IOException {
        this.reader = reader;
        nextLine = reader.readLine();
    }

    public static LineIterator fromFile(File file) throws IOException {
        return new LineIterator(new BufferedReader(new FileReader(file)));
    }

    public static LineIterator fromFile(String file) throws IOException {
        return fromFile(new File(file));
    }

    @Override
    public boolean hasNext() {
        return nextLine != null;
    }

    @Override
    public String next() {
        String line = nextLine;
        try {
            nextLine = reader.readLine();
        } catch (IOException e) {
            throw new IllegalStateException("could not read next line", e);
        }
        return line;
    }

    @Override
    public void close() throws IOException {
        reader.close();
    }
}
