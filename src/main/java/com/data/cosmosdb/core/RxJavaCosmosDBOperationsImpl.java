package com.data.cosmosdb.core;

import com.microsoft.azure.cosmosdb.RequestOptions;
import com.microsoft.azure.cosmosdb.rx.AsyncDocumentClient;
import com.data.cosmosdb.client.CosmosDBAsyncClient;
import rx.Observable;

public class RxJavaCosmosDBOperationsImpl implements RxJavaCosmosDBOperations {

    AsyncDocumentClient client = CosmosDBAsyncClient.getClient();

    String collectionLink;

    public void RxJavaCosmosDBOperationsImpl(String collectionLink){
        this.collectionLink = collectionLink;
    }

    @Override
    public <T> Observable<T> save(T objectToSave) {

        return (Observable<T>) client.createDocument(collectionLink,objectToSave,new RequestOptions(),true);
    }

    @Override
    public <T> Observable<T> update(T objectToSave) {
        return (Observable<T>) client.upsertDocument(collectionLink,objectToSave,new RequestOptions(),false);
    }

    @Override
    public <T> Observable<T> remove(String documentLink) {
        return (Observable<T>) client.deleteDocument(documentLink,new RequestOptions());
    }

    @Override
    public <T> Observable<T> findById(String documentLink) {

        return (Observable<T>) client.readDocument(documentLink,new RequestOptions());

    }

}
