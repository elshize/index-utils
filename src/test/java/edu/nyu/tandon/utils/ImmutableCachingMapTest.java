package edu.nyu.tandon.utils;

import org.hamcrest.Matcher;
import org.hamcrest.collection.IsEmptyIterable;
import org.hamcrest.collection.IsIterableContainingInAnyOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * @author michal.siedlaczek@nyu.edu
 */
@RunWith(MockitoJUnitRunner.class)
public class ImmutableCachingMapTest {

    @Spy
    public ImmutableCachingMap<Integer, Integer> map = new ImmutableCachingMap<Integer, Integer>(5) {
        @Override
        protected Integer read(Integer key) {
            return key;
        }
    };

    @Test
    public void distinctElements() {
        // given
        assertThat(map.capacity, equalTo(5));
        // when
        get(1, 2, 3, 4, 5, 6, 7);
        // then
        assertThat(map.capacity, equalTo(0));
        verify(map, times(7)).read(anyInt());
        verify(map, times(2)).overflow();
        assertThat(map.cachedValues.keySet(), containsInAnyOrder(3, 4, 5, 6, 7));
        assertThat(map.cachedValues.size(), equalTo(5));
        assertThat(map.impacts.keySet(), IsEmptyIterable.emptyIterable());
    }

    @Test
    public void neverRemove() {
        // given
        assertThat(map.capacity, equalTo(5));
        // when
        get(1, 2, 1, 2, 1, 2, 1);
        // then
        assertThat(map.capacity, equalTo(0));
        verify(map, times(1)).read(1);
        verify(map, times(1)).read(2);
        verify(map, times(2)).read(anyInt());
        verify(map, times(2)).overflow();
        assertThat(map.cachedValues.keySet(), containsInAnyOrder(1, 2));
        assertThat(map.cachedValues.size(), equalTo(2));
        assertThat(map.impacts.get(1), equalTo(2));
        assertThat(map.impacts.get(2), equalTo(1));
    }

    @Test
    public void removeOne() {
        // given
        assertThat(map.capacity, equalTo(5));
        // when
        get(1, 2, 1, 3, 4, 5, 6);
        // then
        assertThat(map.capacity, equalTo(0));
        verify(map, times(6)).read(anyInt());
        verify(map, times(2)).overflow();
        assertThat(map.cachedValues.keySet(), containsInAnyOrder(1, 3, 4, 5, 6));
        assertThat(map.cachedValues.size(), equalTo(5));
        assertThat(map.impacts.get(1), equalTo(0));
        assertThat(map.impacts.size(), equalTo(1));
    }

    private void get(Integer ... keys) {
        for (Integer key : keys) map.get(key);
    }

}
