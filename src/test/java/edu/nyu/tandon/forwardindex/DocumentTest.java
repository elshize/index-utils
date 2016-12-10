package edu.nyu.tandon.forwardindex;

import edu.nyu.tandon.coding.Decoder;
import edu.nyu.tandon.coding.VarByteDecoder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * @author michal.siedlaczek@nyu.edu
 */
@RunWith(Parameterized.class)
public class DocumentTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    public static Object[] testCase(byte[] content, int size, int count, List<Long> expected, Class<? extends Throwable> e, Decoder decoder) {
        return new Object[] { new Document(new DocumentMetadata(0, "", 0, size, count), content, decoder), expected, e };
    }

    @Parameters
    public static Iterable<Object[]> data() {
        return Arrays.asList(
                // Flipped
                testCase(new byte[] { 0b1 }, 1, 1, Collections.singletonList(1L), null, VarByteDecoder.getFlipped()),
                testCase(new byte[] { (byte) 0b10000001 }, 1, 1, Collections.singletonList(1L), IllegalStateException.class, VarByteDecoder.getFlipped()),
                testCase(new byte[] { (byte) 0b10010001, 0b1, 0b10 }, 3, 2, Arrays.asList(2177L, 2L), null, VarByteDecoder.getFlipped()),
                testCase(new byte[] { (byte) 0b10010001, 0b1, 0b10 }, 3, 3, Arrays.asList(2177L, 2L), IllegalStateException.class, VarByteDecoder.getFlipped()),
                // Regular
                testCase(new byte[] { (byte) 0b10000001 }, 1, 1, Collections.singletonList(1L), null, VarByteDecoder.get()),
                testCase(new byte[] { 0b1 }, 1, 1, Collections.singletonList(1L), IllegalStateException.class, VarByteDecoder.get()),
                testCase(new byte[] { (byte) 0b10001, (byte) 0b10000001, (byte) 0b10000010 }, 3, 2, Arrays.asList(2177L, 2L), null, VarByteDecoder.get()),
                testCase(new byte[] { (byte) 0b10001, (byte) 0b10000001, (byte) 0b10000010 }, 3, 3, Arrays.asList(2177L, 2L), IllegalStateException.class, VarByteDecoder.get())
        );
    }

    @Parameter
    public Document document;

    @Parameter(value = 1)
    public List<Long> expectedTermIds;

    @Parameter(value = 2)
    public Class<Throwable> exception;

    @Test
    public void iterator() {
        if (exception != null) thrown.expect(exception);
        List<Long> actual = new ArrayList<>();
        document.iterator().forEachRemaining(actual::add);
        assertThat(actual, equalTo(expectedTermIds));
    }
}
