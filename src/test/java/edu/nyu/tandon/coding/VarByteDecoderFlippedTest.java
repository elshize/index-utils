package edu.nyu.tandon.coding;

import com.sun.xml.internal.messaging.saaj.util.ByteInputStream;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.runners.Parameterized.Parameter;

/**
 * @author michal.siedlaczek@nyu.edu
 */
@RunWith(Parameterized.class)
public class VarByteDecoderFlippedTest {

    private static InputStream input(byte ... bytes) {
        return new ByteInputStream(bytes, bytes.length);
    }

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Parameters(name = "{index}: decodeInt({0})={1}")
    public static Iterable<Object[]> data() {
        return Arrays.asList(new Object[][] {
                { input((byte) 0b00000001), 1, null },
                { input((byte) 0b00010001), 17, null },
                { input((byte) 0b00010001, (byte) 0b00000001), 17, null },
                { input((byte) 0b10010001, (byte) 0b00000001), 2177, null },
                { input((byte) 0b10010001), 0, IllegalArgumentException.class },
                { input(), 0, IllegalArgumentException.class }
        });
    }

    @Parameter
    public InputStream input;

    @Parameter(value = 1)
    public int expected;

    @Parameter(value = 2)
    public Class<Throwable> exception;

    public VarByteDecoder decoder = VarByteDecoder.getFlipped();

    @Test
    public void decodeInt() throws IOException {
        if (exception != null) thrown.expect(exception);
        assertThat(decoder.decodeInt(input), equalTo(expected));
    }

}
