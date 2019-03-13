package com.data.cosmosdb.core;

import rx.Observable;

public interface RxJavaCosmosDBOperations {

    <T> Observable<T> save(T objectToSave);

    <T> Observable<T> update(T objectToSave);

    <T> Observable<T> remove(String documentLink);

    <T> Observable<T> findById(String documentLink);

    <T> Observable<T> query(String query);

}
