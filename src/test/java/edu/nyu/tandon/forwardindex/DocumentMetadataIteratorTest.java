package edu.nyu.tandon.forwardindex;

import edu.nyu.tandon.utils.LineIterator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

/**
 * @author michal.siedlaczek@nyu.edu
 */
@RunWith(MockitoJUnitRunner.class)
public class DocumentMetadataIteratorTest {

    @Mock
    public LineIterator lineIterator;

    @Test
    public void lineToDocumentMetadata() {

        // given
        when(lineIterator.hasNext())
                .thenReturn(TRUE)
                .thenReturn(TRUE)
                .thenReturn(FALSE);
        when(lineIterator.next())
                .thenReturn("clueweb09-en0000-00-00000 1 0 208 102")
                .thenReturn("clueweb09-en0000-00-00001 2 208 86 49");
        DocumentMetadataIterator iterator = new DocumentMetadataIterator(lineIterator);

        // when
        List<DocumentMetadata> actual = new ArrayList<>();
        iterator.forEachRemaining(actual::add);

        // then
        assertThat(actual, equalTo(Arrays.asList(
                new DocumentMetadata(1, "clueweb09-en0000-00-00000", 0, 208, 102),
                new DocumentMetadata(2, "clueweb09-en0000-00-00001", 208, 86, 49)
        )));
    }

}
