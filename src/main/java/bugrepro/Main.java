package bugrepro;

import com.cloudant.sync.datastore.*;
import com.cloudant.sync.replication.*;
import com.cloudant.sync.query.*;
import com.cloudant.sync.notifications.*;

import com.google.common.eventbus.Subscribe;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.concurrent.CountDownLatch;


class Main {

    // E.g., "/home/mike/scratch/sync-android-bug/databases"
    // Be sure it exists!
    private final String datastoreManagerPath = /* YOUR_PATH_HERE */;

    // E.g., "https://mikerhodes.cloudant.com/animaldb"
    // Include creds inline, "https://username:password@mikerhodes.blah...."
    private final String remoteURL = /* YOUR_REMOTE_DATABASE_URL_HERE */;

    static final String localDatabaseName = "testDb";

    public static void main(String[] args)
            throws DocumentNotFoundException, DatastoreNotCreatedException,
            DocumentException, IOException, URISyntaxException, InterruptedException {

        // Create the local database
        File path = new File(datastoreManagerPath);
        DatastoreManager manager = new DatastoreManager(path.getAbsolutePath());
        Datastore ds = manager.openDatastore(localDatabaseName);

        // Set up a replication from the local to remote database
        PullReplication pull = new PullReplication();
        pull.source = new URI(remoteURL);
        pull.target = ds;
        Replicator replicator = ReplicatorFactory.oneway(pull);

        // Use a CountDownLatch to provide a lightweight way to wait for completion
        CountDownLatch latch = new CountDownLatch(1);
        Listener listener = new Listener(latch);
        replicator.getEventBus().register(listener);
        replicator.start();
        latch.await();
        replicator.getEventBus().unregister(listener);
        if (replicator.getState() != Replicator.State.COMPLETE) {
            System.out.println("Error replicating TO remote");
            System.out.println(listener.error);
        } else {
            System.out.println("Replication complete");
        }

        ds.close();
        im.close();
        manager.deleteDatastore(dbName);
    }

    /* Simple latch used to wait for replication to complete */
    private static class Listener {
        private final CountDownLatch latch;
        public ErrorInfo error = null;

        Listener(CountDownLatch latch) {
            this.latch = latch;
        }

        @Subscribe
        public void complete(ReplicationCompleted event) {
            latch.countDown();
        }

        @Subscribe
        public void error(ReplicationErrored event) {
            this.error = event.errorInfo;
            latch.countDown();
        }
    }
}
