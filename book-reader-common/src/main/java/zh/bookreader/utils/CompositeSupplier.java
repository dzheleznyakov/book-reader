package zh.bookreader.utils;

import com.google.common.collect.ImmutableList;

import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

public class CompositeSupplier<E> implements Supplier<E> {
    private final List<Pair<BooleanSupplier, Supplier<E>>> fallbackStrategy;

    private CompositeSupplier(Builder<E> builder) {
        fallbackStrategy = builder.fallbackStrategy.build();
    }

    @Override
    public E get() {
        return fallbackStrategy.stream()
                .filter(pair -> pair.getLeft().getAsBoolean())
                .findFirst()
                .map(Pair::getRight)
                .map(Supplier::get)
                .orElse(null);
    }

    public static <E> Builder<E> main(BooleanSupplier mainTest, Supplier<E> mainSupplier) {
        return new Builder<>(mainTest, mainSupplier);
    }

    public static <E> Builder<E> main(BooleanSupplier mainTest, E value) {
        return new Builder<>(mainTest, () -> value);
    }

    public static class Builder<E> {
        private final ImmutableList.Builder<Pair<BooleanSupplier, Supplier<E>>> fallbackStrategy = ImmutableList.builder();
        private Supplier<E> finalSupplier;

        private Builder(BooleanSupplier mainTest, Supplier<E> mainSupplier) {
            fallbackStrategy.add(Pair.of(mainTest, mainSupplier));
        }

        public Builder<E> withFallback(BooleanSupplier test, Supplier<E> supplier) {
            fallbackStrategy.add(Pair.of(test, supplier));
            return this;
        }

        public Builder<E> withFallback(BooleanSupplier test, E value) {
            fallbackStrategy.add(Pair.of(test, () -> value));
            return this;
        }

        public Builder<E> withFallback(Supplier<E> supplier) {
            finalSupplier = supplier;
            return this;
        }

        public Builder<E> withFallback(E value) {
            finalSupplier = () -> value;
            return this;
        }

        public CompositeSupplier<E> build() {
            fallbackStrategy.add(Pair.of(() -> true, finalSupplier));
            return new CompositeSupplier<>(this);
        }
    }

    private static class Pair<L, R> {
        private final L left;
        private final R right;

        private Pair(L left, R right) {
            this.left = left;
            this.right = right;
        }

        public L getLeft() {
            return left;
        }

        public R getRight() {
            return right;
        }

        public static <L, R> Pair<L, R> of(L left, R right) {
            return new Pair<>(left, right);
        }
    }
}
