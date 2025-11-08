package compumart.compumart.model;

import org.bson.Document;

import java.util.HashMap;
import java.util.Map;

public class Cart extends BaseModel {

    private String email;
    private String name;
    private Map<String, Integer> cart = new HashMap<>();

    public Cart() {}

    public Cart(String email, String name, Map<String, Integer> cart) {
        this.email = email;
        this.name = name;
        if (cart != null) this.cart.putAll(cart);
    }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Map<String, Integer> getCart() { return cart; }

    public void addToCart(String productId, int quantity) {
        cart.put(productId, cart.getOrDefault(productId, 0) + quantity);
    }

    @Override
    public Document toDocument() {
        Document doc = new Document();
        if (getId() != null) doc.append("_id", getId());
        doc.append("email", email)
                .append("name", name)
                .append("cart", new Document(cart)); // store as Document
        return doc;
    }

    public static Cart fromDocument(Document doc) {
        Cart cart = new Cart();
        if (doc.containsKey("_id")) cart.setId(doc.getObjectId("_id"));
        cart.setEmail(doc.getString("email"));
        cart.setName(doc.getString("name"));

        Document cartDoc = (Document) doc.get("cart");
        if (cartDoc != null) {
            Map<String, Integer> map = new HashMap<>();
            for (String key : cartDoc.keySet()) {
                map.put(key, cartDoc.getInteger(key, 0));
            }
            cart.getCart().putAll(map);
        }
        return cart;
    }
}