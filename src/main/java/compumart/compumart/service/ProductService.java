package compumart.compumart.service;

import compumart.compumart.model.Product;
import java.util.List;

public class ProductService {

    public List<Product> getAllProducts() {
        System.out.println("Getting all products - placeholder");
        return List.of();
    }

    public List<Product> searchProducts(String query) {
        System.out.println("Searching products for: " + query);
        return List.of();
    }
}