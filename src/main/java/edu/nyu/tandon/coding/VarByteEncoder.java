package edu.nyu.tandon.coding;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @author michal.siedlaczek@nyu.edu
 */
public class VarByteEncoder implements Encoder {

    protected VarByteEncoder() {}

    public static VarByteEncoder get() {
        return new VarByteEncoder();
    }

    public static VarByteEncoder getFlipped() {
        return new VarByteEncoder() {

            @Override
            public int encodeLong(OutputStream output, long n) throws IOException {
                if (n < 0) throw new IllegalArgumentException("encoded number bust be non-negative");
                byte[] buffer = new byte[8];
                int p = 8;
                while (n > 0b01111111) {
                    buffer[--p] = (byte) ((n & 0b01111111) ^ 0b10000000);
                    n = n >>> 7;
                }
                buffer[--p] = (byte) (n ^ 0b10000000);
                buffer[7] = (byte) (buffer[7] & 0b01111111);
                output.write(buffer, p, 8 - p);
                return 8 - p;
            }

        };
    }

    @Override
    public int encodeInt(OutputStream output, int n) throws IOException {
        return encodeLong(output, n);
    }

    @Override
    public int encodeLong(OutputStream output, long n) throws IOException {
        if (n < 0) throw new IllegalArgumentException("encoded number bust be non-negative");
        byte[] buffer = new byte[8];
        int p = 8;
        while (n > 0b01111111) {
            buffer[--p] = (byte) (n & 0b01111111);
            n = n >>> 7;
        }
        buffer[--p] = (byte) n;
        buffer[7] = (byte) (buffer[7] ^ 0b10000000);
        output.write(buffer, p, 8 - p);
        return 8 - p;
    }
}
