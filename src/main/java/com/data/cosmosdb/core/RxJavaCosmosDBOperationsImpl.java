package com.data.cosmosdb.core;

import com.data.cosmosdb.client.CosmosDBAsyncClient;
import com.data.cosmosdb.repository.SimpleReactiveCosmosDBRepository;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.microsoft.azure.cosmosdb.*;
import com.microsoft.azure.cosmosdb.rx.AsyncDocumentClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import rx.Observable;
import rx.schedulers.Schedulers;

import java.io.IOException;
import java.util.stream.Collectors;

@Component
public class RxJavaCosmosDBOperationsImpl implements RxJavaCosmosDBOperations {

    private static Logger log = LoggerFactory.getLogger(RxJavaCosmosDBOperationsImpl.class);

    AsyncDocumentClient client = CosmosDBAsyncClient.getClient();

//    ObjectMapper objectMapper = getInstance();
//
//    public static ObjectMapper getInstance() {
//        SimpleModule module = new SimpleModule();
//        module.addDeserializer(BaseCondition.class, new ConditionJsonDeserializer());
//        module.addDeserializer(BaseRewardRule.class, new RewardRuleJsonDeserializer());
//
//        return new ObjectMapper()
//            .registerModule(module)
//            .registerModule(new JavaTimeModule())
//            .setSerializationInclusion(JsonInclude.Include.NON_NULL)
//            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//    }

    @Autowired
    ObjectMapper objectMapper;

    public String collectionLink;

    public RxJavaCosmosDBOperationsImpl(){

        this.collectionLink = String.format("/dbs/%s/colls/%s", "promotions", "promotion");;
    }

    @Override
    public <T> Observable<T> save(T objectToSave) {

        //log.info(" infraevent id={}, aggregateId = {}",((InfraEvent)objectToSave).getId(), ((InfraEvent)objectToSave).getAggregateId());


        try {
            return (Observable<T>) client.createDocument(collectionLink,new Document(objectMapper.writeValueAsString(objectToSave)),
                    new RequestOptions(),true).map(documentResourceResponse -> {

                log.info("deserializing object {} ", documentResourceResponse);
                log.info("object to save class {} ", objectToSave.getClass());


                try {
                    return objectMapper.readValue(documentResourceResponse.getResource().toJson(), objectToSave.getClass());
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return Observable.empty();
                // return objectToSave;
            } );
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return Observable.empty();
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

        log.info("finding by id ={}",documentLink);

        // return (Observable<T>) client.readDocument(documentLink,new RequestOptions());
        String QUERY="SELECT * FROM promotions WHERE promotions.body.productId='05391527072263'";

        FeedOptions queryOptions = new FeedOptions();
        queryOptions.setMaxItemCount(10);
        queryOptions.setEnableCrossPartitionQuery(true);

        Observable<FeedResponse<Document>> queryObservable =
                client.queryDocuments(collectionLink,
                        QUERY, queryOptions);

        queryObservable
                .observeOn(Schedulers.io())
                .subscribe(
                        page -> {
                            // we want to make sure heavyWork() doesn't block any of netty IO threads
                            // so we use observeOn(scheduler) to switch from the netty thread to user's thread.
                            //heavyWork();

                            System.out.println("Got a page of query result with " +
                                    page.getResults().size() + " document(s)"
                                    + " and request charge of " + page.getRequestCharge());


                            System.out.println("Document Ids " + page.getResults().stream().map(d -> d.getId())
                                    .collect(Collectors.toList()));
                        },
                        // terminal error signal
                        e -> {
                            e.printStackTrace();
                            // completionLatch.countDown();
                        },

                        // terminal completion signal
                        () -> {
                            //completionLatch.countDown();
                        });

        return (Observable<T>) queryObservable;
    }

    @Override
    public <T> Observable<T> query(String query) {
        return null;
    }

}
