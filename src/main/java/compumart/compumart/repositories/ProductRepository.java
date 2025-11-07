package compumart.compumart.repositories;

import compumart.compumart.model.Product;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class ProductRepository extends BaseRepository<Product> {

    public ProductRepository() {
        initCollection("products");
    }

    @Override
    protected Product convert(Document document) {
        return Product.fromDocument(document);
    }

    // Find by SKU
    public Product findBySKU(String sku) {
        Document doc = collection.find(new Document("sku", sku)).first();
        return doc != null ? Product.fromDocument(doc) : null;
    }

    public Product findActiveBySKU(String sku) {
        Document doc = collection.find(
                new Document("sku", sku).append("isActive", true)
        ).first();
        return doc != null ? Product.fromDocument(doc) : null;
    }

    // Activate / Deactivate by _id
    public void deactivateProduct(String id) {
        Document updateDoc = new Document("$set", new Document("isActive", false));
        collection.updateOne(new Document("_id", id), updateDoc);
    }

    public void activateProduct(String id) {
        Document updateDoc = new Document("$set", new Document("isActive", true));
        collection.updateOne(new Document("_id", id), updateDoc);
    }

    public void updateStock(String id, int newStock) {
        Document updateDoc = new Document("$set", new Document("stock", newStock));
        collection.updateOne(new Document("_id", id), updateDoc);
    }

    public List<Product> findAll() {
        List<Product> list = new ArrayList<>();
        for (Document doc : collection.find()) {
            list.add(Product.fromDocument(doc));
        }
        return list;
    }
}
