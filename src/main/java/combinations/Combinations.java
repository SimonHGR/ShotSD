package combinations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class Combinations<E> implements Spliterator<Set<E>> {

  public static <E> Stream<Set<E>> kFrom(int k, Collection<E> in) {
    return StreamSupport.stream(new Combinations<>(k, in), false);
  }

  private final int[] positions;
  private final List<E> items;
  private final int limit;
  private boolean hasMore;

  private Combinations(int k, Collection<E> in) {
    this.positions = new int[k];
    this.items = new ArrayList<>(in);
    this.limit = this.items.size();
    for (int x = 0; x < k; x++) {
      positions[x] = limit - k + x;
    }
    this.hasMore = positions[0] >= 0;
  }

  private int reducePosition(int n) {
    if (--positions[n] < n) {
      if (n < positions.length - 1) {
        positions[n] = reducePosition(n + 1) - 1;
      }
    }
    return positions[n];
  }

  @Override
  public boolean tryAdvance(Consumer<? super Set<E>> action) {
    boolean success = this.hasMore;
    if (hasMore) {
      Set<E> rv = new HashSet<>();
      for (int i = 0; i < this.positions.length; i++) {
        rv.add(this.items.get(positions[i]));
      }
      this.hasMore = reducePosition(0) >= 0;
      action.accept(rv);
    }
    return success;
  }

  @Override
  public Spliterator<Set<E>> trySplit() {
    return null; // not splittable
  }

  @Override
  public long estimateSize() {
    return 0;
  }

  @Override
  public int characteristics() {
    return Spliterator.IMMUTABLE | Spliterator.DISTINCT | Spliterator.NONNULL;
  }

  private void showArray() {
    StringBuilder sb = new StringBuilder("positions [ ");
    for (int x = 0; x < this.positions.length; x++) {
      sb.append(this.positions[x]).append(", ");
    }
    sb.setLength(sb.length() - 2);
    sb.append(" ]");
    System.out.println(sb);
  }
}
