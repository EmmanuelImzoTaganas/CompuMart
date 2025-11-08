package compumart.compumart.repositories;

import com.mongodb.client.result.UpdateResult;
import compumart.compumart.model.Cart;
import org.bson.Document;
import org.bson.types.ObjectId;
import com.mongodb.client.model.ReplaceOptions;
import java.util.HashMap;
import java.util.Map;

public class CartRepository extends BaseRepository<Cart> {

    public CartRepository() {
        initCollection("cart"); // initialize the "carts" collection
    }

    @Override
    protected Cart convert(Document document) {
        Cart cart = new Cart();
        ObjectId id = document.getObjectId("_id");
        cart.setId(id);

        cart.setEmail(document.getString("email"));
        cart.setName(document.getString("name"));

        Document cartDoc = (Document) document.get("cart");
        if (cartDoc != null) {
            Map<String, Integer> map = new HashMap<>();
            for (String key : cartDoc.keySet()) {
                map.put(key, cartDoc.getInteger(key, 0));
            }
            cart.getCart().putAll(map);
        }

        return cart;
    }

    public Cart findByEmail(String email) {
        Document doc = collection.find(new Document("email", email)).first();
        return doc != null ? convert(doc) : null;
    }

    public void saveOrUpdate(Cart cart) {
        System.out.println("=== Saving cart for email: " + cart.getEmail());
        System.out.println("Cart content: " + cart.getCart());

        Document doc = cart.toDocument();
        // Remove _id so we rely on email filter (optional, if using email)
        doc.remove("_id");

        // Filter by email (or by _id if you prefer)
        Document filter = new Document("email", cart.getEmail());
        // Option 1: use replaceOne with upsert:
        ReplaceOptions options = new ReplaceOptions().upsert(true);
        UpdateResult result = collection.replaceOne(filter, doc, options);

        System.out.println("UpdateResult matchedCount=" + result.getMatchedCount()
                + ", modifiedCount=" + result.getModifiedCount()
                + ", upsertedId=" + result.getUpsertedId());

        // Option 2: (alternate) use updateOne with $set:
        // Document update = new Document("$set", new Document("cart", new Document(cart.getCart())));
        // UpdateOptions opts = new UpdateOptions().upsert(true);
        // UpdateResult r2 = collection.updateOne(filter, update, opts);
        // System.out.println("updateOne result: " + r2);
    }

}