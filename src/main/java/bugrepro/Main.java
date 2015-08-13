package bugrepro;

import com.cloudant.sync.datastore.*;
import java.io.*;
import java.util.*;

class Main {

    static final String dbName = "testdb";

    public static void main(String[] args)
            throws DocumentNotFoundException, DatastoreNotCreatedException,
            DocumentException, IOException {

        // Create a DatastoreManager using application internal storage path
        File path = new File(/* YOUR_PATH_HERE */);
        DatastoreManager manager = new DatastoreManager(path.getAbsolutePath());

        Datastore ds = manager.openDatastore(dbName);

        // Create a document
        MutableDocumentRevision revision = new MutableDocumentRevision();
        Map<String, Object> body = new HashMap<String, Object>();
        body.put("animal", "cat");
        revision.body = DocumentBodyFactory.create(body);
        DocumentRevision saved = ds.createDocumentFromRevision(revision);

        // Read a document
        DocumentRevision aRevision = ds.getDocument(revision.getId());

        ds.close();
        manager.deleteDatastore(dbName);
    }
}
