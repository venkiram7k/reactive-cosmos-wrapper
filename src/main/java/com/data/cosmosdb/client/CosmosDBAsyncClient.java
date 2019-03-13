package com.data.cosmosdb.client;

import com.microsoft.azure.cosmosdb.ConnectionMode;
import com.microsoft.azure.cosmosdb.ConnectionPolicy;
import com.microsoft.azure.cosmosdb.ConsistencyLevel;
import com.microsoft.azure.cosmosdb.rx.AsyncDocumentClient;
import rx.Scheduler;
import rx.schedulers.Schedulers;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class CosmosDBAsyncClient {

private static AsyncDocumentClient client;

    public static ExecutorService executorService = Executors.newFixedThreadPool(100);
    public static Scheduler scheduler = Schedulers.from(executorService);

    public synchronized static AsyncDocumentClient getClient(){

        if(client!=null){
            return client;
        }

        return new AsyncDocumentClient.Builder()
                .withServiceEndpoint(CosmosDBConnection.host)
                .withMasterKeyOrResourceToken(CosmosDBConnection.accessKey)
                .withConnectionPolicy(getConnetionPolicy())
                .withConsistencyLevel(ConsistencyLevel.Eventual)
                .build();

    }

    public static ConnectionPolicy getConnetionPolicy(){

        ConnectionPolicy connectionPolicy = new ConnectionPolicy();

        connectionPolicy.setConnectionMode(ConnectionMode.Gateway);
        connectionPolicy.setMaxPoolSize(10);
        connectionPolicy.setRequestTimeoutInMillis(1000);
        connectionPolicy.setIdleConnectionTimeoutInMillis(1000);
        connectionPolicy.setEnableEndpointDiscovery(true);
        connectionPolicy.setUsingMultipleWriteLocations(true);
        return connectionPolicy;
    }
}
