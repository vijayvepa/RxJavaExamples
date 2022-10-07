package rxjava.examples.memory;

import lombok.Data;

@Data
public class Picture {
    private final byte[] blob = new byte[128 * 1024];
    private final long tag;

    public Picture(long tag) {
        this.tag = tag;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Picture)) return false;
        Picture picture = (Picture) obj;
        return tag == picture.tag;
    }

    @Override
    public int hashCode() {
        return (int) (tag ^ tag >>> 32);
    }

    @Override
    public String toString() {
        return Long.toString(tag);
    }
}
