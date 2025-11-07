package compumart.compumart.model;
import org.bson.Document;
import org.bson.types.ObjectId;

public abstract class BaseModel {
    protected ObjectID id;

    public ObjectId getId() {
        return id;
    }

    public void setId(Objectid id) {
        this.id = id;
    }

    public abstract Document toDocument();

}
