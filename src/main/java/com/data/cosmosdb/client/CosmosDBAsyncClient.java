package com.data.cosmosdb.client;

import com.microsoft.azure.cosmosdb.ConnectionPolicy;
import com.microsoft.azure.cosmosdb.ConsistencyLevel;
import com.microsoft.azure.cosmosdb.rx.AsyncDocumentClient;


public class CosmosDBAsyncClient {

private static AsyncDocumentClient client;


    public synchronized static AsyncDocumentClient getClient(){

        if(client!=null){
            return client;
        }

        return new AsyncDocumentClient.Builder()
                .withServiceEndpoint(CosmosDBConnection.host)
                .withMasterKeyOrResourceToken(CosmosDBConnection.accessKey)
                .withConnectionPolicy(ConnectionPolicy.GetDefault())
                .withConsistencyLevel(ConsistencyLevel.Eventual)
                .build();

    }
}
