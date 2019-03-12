package com.data.cosmosdb.repository;

import com.data.cosmosdb.core.RxJavaCosmosDBOperations;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import java.io.Serializable;

public interface ReactiveCosmosDBRepository <T, ID extends Serializable> extends ReactiveCrudRepository<T, ID> {


    RxJavaCosmosDBOperations getCosmosDBOperations();
}
