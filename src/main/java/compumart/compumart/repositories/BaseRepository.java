package compumart.compumart.repositories;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import compumart.compumart.model.BaseModel;
import compumart.compumart.utils.MongoDBConnectionManager;
import org.bson.Document;
import org.bson.types.ObjectId;

import static com.mongodb.client.model.Filters.eq;

public abstract class BaseRepository<T extends BaseModel> {

    protected MongoCollection<Document> collection;

    public BaseRepository() {
        // do not initialize collection here
    }

    protected void initCollection(String collectionName) {
        MongoDatabase database = MongoDBConnectionManager.getInstance().getDatabase();
        this.collection = database.getCollection(collectionName);
    }

    public void insert(T entity) {
        Document document = entity.toDocument();
        this.collection.insertOne(document);
        entity.setId(document.getObjectId("_id"));
    }

    public T findById(ObjectId id) {
        Document doc = collection.find(eq("_id", id)).first();
        return doc == null ? null : this.convert(doc);
    }

    public void update(ObjectId id, T newEntity) {
        Document newDoc = newEntity.toDocument();
        this.collection.replaceOne(eq("_id", id), newDoc);
    }

    public boolean delete(ObjectId id) {
        return this.collection.deleteOne(eq("_id", id)).getDeletedCount() > 0;
    }

    protected abstract T convert(Document document);
}
