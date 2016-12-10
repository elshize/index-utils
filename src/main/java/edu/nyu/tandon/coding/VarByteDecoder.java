package edu.nyu.tandon.coding;

import java.io.IOException;
import java.io.InputStream;
import java.util.function.BiPredicate;

/**
 * @author michal.siedlaczek@nyu.edu
 */
public class VarByteDecoder implements Decoder {

    /**
     * This predicate returns true if the byte b (first argument)
     * is the last byte of a number.
     * The second argument is (b & 0b01111111).
     */
    protected BiPredicate<Integer, Integer> isLastByte;

    public static VarByteDecoder get() {
        return new VarByteDecoder((b, v) -> !b.equals(v));
    }

    public static VarByteDecoder getFlipped() {
        return new VarByteDecoder((b, v) -> b.equals(v));
    }

    protected VarByteDecoder(BiPredicate<Integer, Integer> isLastByte) {
        this.isLastByte = isLastByte;
    }

    @Override
    public int decodeInt(InputStream stream) throws IOException {
        return (int) decodeLong(stream);
    }

    @Override
    public long decodeLong(InputStream stream) throws IOException {
        int b;
        int n = 0;
        while ((b = stream.read()) != -1) {
            n <<= 7;
            int val = b & 0b01111111;
            n |= val;
            if (isLastByte.test(b, val)) return n;
        }
        throw new IllegalArgumentException("stream ended before the last byte could be retrieved");
    }

}
