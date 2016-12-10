package edu.nyu.tandon.forwardindex;

import java.util.Objects;

/**
 * @author michal.siedlaczek@nyu.edu
 */
public class DocumentMetadata {

    private long id;
    private String title;
    private long offset;
    private int size;
    private int count;

    public DocumentMetadata(long id, String title, long offset, int size, int count) {
        this.id = id;
        this.title = title;
        this.offset = offset;
        this.size = size;
        this.count = count;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public long getOffset() {
        return offset;
    }

    public int getSize() {
        return size;
    }

    public int getCount() {
        return count;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DocumentMetadata that = (DocumentMetadata) o;
        return id == that.id &&
                offset == that.offset &&
                size == that.size &&
                count == that.count &&
                Objects.equals(title, that.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, offset, size, count);
    }
}
