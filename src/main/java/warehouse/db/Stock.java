package warehouse.db;

import org.springframework.stereotype.Service;
import warehouse.model.ProductType;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class Stock {
	Map<ProductType, AtomicInteger> stock = Collections.synchronizedMap(new HashMap<>());

	public Stock() {
		stock.put(ProductType.bread, new AtomicInteger(10));
		stock.put(ProductType.pasta, new AtomicInteger(10));
		stock.put(ProductType.milk, new AtomicInteger(10));
		stock.put(ProductType.salt, new AtomicInteger(10));
		stock.put(ProductType.soap, new AtomicInteger(10));
	}

	public synchronized Map<ProductType, Integer> getStock() {
		return stock.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().get()));
	}

	public synchronized void putToStock(ProductType productType) {
		stock.get(productType).incrementAndGet();
	}

	public synchronized void pickFromStock(ProductType productType) {
		stock.get(productType).decrementAndGet();
	}
}
