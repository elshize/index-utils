package edu.nyu.tandon.coding;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author michal.siedlaczek@nyu.edu
 */
public interface Decoder {
    int decodeInt(InputStream input) throws IOException;
    long decodeLong(InputStream input) throws IOException;
}
