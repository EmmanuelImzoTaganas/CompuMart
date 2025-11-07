package compumart.compumart.model;

import org.bson.Document;
import java.util.Date;

public class Product extends BaseModel {
    private String sku;          // immutable after creation
    private String name;
    private String type;         // keyboard, mouse, storage, memory, monitor
    private String model;
    private String image;
    private int stock;
    private double price;
    private boolean isActive;
    private Date createdAt;

    public Product() {
        this.isActive = true;
        this.createdAt = new Date();
    }

    // Getters and Setters
    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }  // Only set once when creating

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }

    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }

    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }

    // MongoDB conversion
    @Override
    public Document toDocument() {
        Document doc = new Document();
        if (getId() != null) doc.append("_id", getId());
        doc.append("sku", sku)
                .append("name", name)
                .append("type", type)
                .append("model", model)
                .append("image", image)
                .append("stock", stock)
                .append("price", price)
                .append("isActive", isActive)
                .append("createdAt", createdAt);
        return doc;
    }

    public static Product fromDocument(Document doc) {
        Product product = new Product();
        if (doc.containsKey("_id")) product.setId(doc.getObjectId("_id"));
        product.setSku(doc.getString("sku"));
        product.setName(doc.getString("name"));
        product.setType(doc.getString("type"));
        product.setModel(doc.getString("model"));
        product.setImage(doc.getString("image"));
        product.setStock(doc.getInteger("stock", 0));
        product.setPrice(doc.getDouble("price") != null ? doc.getDouble("price") : 0.0);
        product.setActive(doc.getBoolean("isActive", true));
        product.setCreatedAt(doc.getDate("createdAt"));
        return product;
    }
}
