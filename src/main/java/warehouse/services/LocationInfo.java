package warehouse.services;

import org.springframework.stereotype.Service;
import warehouse.model.Location;
import warehouse.model.ProductType;

import java.util.HashMap;
import java.util.Map;

@Service
public class LocationInfo {
	Map<ProductType, Location> productTypeCoordinatesMap;

	public LocationInfo() {
		productTypeCoordinatesMap = new HashMap<>();
		productTypeCoordinatesMap.put(ProductType.bread, Location.of(0, 0));
		productTypeCoordinatesMap.put(ProductType.soap, Location.of(1, 1));
		productTypeCoordinatesMap.put(ProductType.salt, Location.of(2, 2));
		productTypeCoordinatesMap.put(ProductType.pasta, Location.of(3, 3));
		productTypeCoordinatesMap.put(ProductType.milk, Location.of(5, 5));
	}

	public Location find(ProductType product) {
		return productTypeCoordinatesMap.get(product);
	}

	public ProductType find(Location location) {
		return productTypeCoordinatesMap.entrySet().stream().filter(entry -> entry.getValue().equals(location)).findFirst().orElseThrow(() -> {
			throw new RuntimeException(location + " not defined");
		}).getKey();
	}
}
