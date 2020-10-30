package zh.bookreader.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.function.Supplier;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@DisplayName("Test CompositeSupplier")
class CompositeSupplierTest {
    @Test
    @DisplayName("Return from main supplier when main test is true")
    void mainTestIsTrue() {
        assertSupplier(1, CompositeSupplier.main(() -> true, () -> 1).build());
    }

    @Test
    @DisplayName("Return null when main test is false and there is no fallback")
    void mainTestIsFalse_NoFallback() {
        assertSupplier(null, CompositeSupplier.main(() -> false, () -> 1).build());
    }

    @Test
    @DisplayName("Use fallback when main test is false")
    void mainTestIsFalse_ThereIsFallback() {
        assertSupplier(0, CompositeSupplier.main(() -> false, () -> 1)
                .withFallback(() -> 0)
                .build());
    }

    @Test
    @DisplayName("If main test fails, use intermediate fallback if it exists and its test is true")
    void mainTestIsFalse_IntermediateFallbackTestIsTrue() {
        assertSupplier(2, CompositeSupplier.main(() -> false, () -> 1)
                .withFallback(() -> true, () -> 2)
                .build());
    }

    @Test
    @DisplayName("If main test fails and intermediate test fails, fallback to the final supplier (or null)")
    void mainTestIsFalse_IntermediateTestIsFalse() {
        assertSupplier(null, CompositeSupplier.main(() -> false, () -> 1)
                .withFallback(() -> false, () -> 2)
                .build());
    }

    @Test
    @DisplayName("Return the first chained supplier whose test does not fail")
    void chainFallbacks() {
        assertSupplier(3, CompositeSupplier.main(() -> false, () -> 1)
                .withFallback(() -> false, () -> 2)
                .withFallback(() -> true, () -> 3)
                .build());
        assertSupplier(2, CompositeSupplier.main(() -> false, () -> 1)
                .withFallback(() -> true, () -> 2)
                .withFallback(() -> true, () -> 3)
                .build());
        assertSupplier(1, CompositeSupplier.main(() -> true, () -> 1)
                .withFallback(() -> true, () -> 2)
                .withFallback(() -> true, () -> 3)
                .build());
        assertSupplier(0, CompositeSupplier.main(() -> false, () -> 1)
                .withFallback(() -> false, () -> 2)
                .withFallback(() -> false, () -> 3)
                .withFallback(() -> 0)
                .build());
    }

    @Test
    @DisplayName("Final fallback can be value")
    void finalFallbackCanBeValue() {
        assertSupplier(0, CompositeSupplier.main(() -> false, () -> 1)
                .withFallback(0)
                .build());
    }

    private void assertSupplier(Integer expected, Supplier<Integer> supplier) {
        Integer value = supplier.get();

        assertThat(value, is(expected));
    }
}