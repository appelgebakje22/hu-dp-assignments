package nl.appelgebakje22.dp.lib;

import java.util.Optional;
import java.util.function.Supplier;

public class LazyOptional<T> {

	private static final LazyOptional<?> EMPTY = new LazyOptional<>(null);
	private final Supplier<T> supplier;
	private boolean hasTried = false;
	private T resolved;

	private LazyOptional(Supplier<T> supplier) {
		this.supplier = supplier;
	}

	public Optional<T> resolve() {
		if (hasTried) {
			return Optional.ofNullable(this.resolved);
		}
		this.hasTried = true;
		try {
			this.resolved = this.supplier.get();
		} catch (Throwable ignored) {
			return Optional.empty();
		}
		return Optional.of(this.resolved);
	}

	public static <T> LazyOptional<T> of(Supplier<T> supplier) {
		return new LazyOptional<>(supplier);
	}

	@SuppressWarnings("unchecked")
	public static <T> LazyOptional<T> empty() {
		return (LazyOptional<T>) EMPTY;
	}
}