package configurations;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import io.dropwizard.lifecycle.Managed;

import java.net.UnknownHostException;

public class ManagedMongoClient extends MongoClient implements Managed {

    public ManagedMongoClient(MongoClientURI uri) throws UnknownHostException {
        super(uri);
    }

    @Override
    public void start() throws Exception {

    }

    @Override
    public void stop() throws Exception {
        close();
    }
}
