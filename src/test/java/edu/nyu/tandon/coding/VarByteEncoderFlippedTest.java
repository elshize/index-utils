package edu.nyu.tandon.coding;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * @author michal.siedlaczek@nyu.edu
 */
@RunWith(Parameterized.class)
public class VarByteEncoderFlippedTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Parameterized.Parameters(name = "{index}: encodeInt({0})={1}")
    public static Iterable<Object[]> data() {
        return Arrays.asList(new Object[][] {
                { 0, new byte[] {(byte) 0b00000000}, null },
                { 1, new byte[] {(byte) 0b00000001}, null },
                { 17, new byte[] {(byte) 0b00010001}, null },
                { 2177, new byte[] {(byte) 0b10010001, (byte) 0b00000001}, null },
                { -1, new byte[] {}, IllegalArgumentException.class },
        });
    }

    @Parameterized.Parameter
    public int number;

    @Parameterized.Parameter(value = 1)
    public byte[] expected;

    @Parameterized.Parameter(value = 2)
    public Class<Throwable> exception;

    public VarByteEncoder encoder = VarByteEncoder.getFlipped();
    public ByteArrayOutputStream output = new ByteArrayOutputStream();

    @Test
    public void encodeInt() throws IOException {
        if (exception != null) thrown.expect(exception);
        encoder.encodeInt(output, number);
        assertThat(output.toByteArray(), equalTo(expected));
    }

}
