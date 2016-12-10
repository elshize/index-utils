package edu.nyu.tandon.coding;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @author michal.siedlaczek@nyu.edu
 */
public interface Encoder {
    int encodeInt(OutputStream output, int n) throws IOException;
    int encodeLong(OutputStream output, long n) throws IOException;
}
