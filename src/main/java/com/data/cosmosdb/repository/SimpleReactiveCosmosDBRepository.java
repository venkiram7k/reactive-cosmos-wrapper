package com.data.cosmosdb.repository;

import com.data.cosmosdb.core.RxJavaCosmosDBOperations;
import org.reactivestreams.Publisher;
import org.springframework.data.repository.util.ReactiveWrapperConverters;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import rx.Observable;
import rx.Single;

import java.io.Serializable;

public class SimpleReactiveCosmosDBRepository<T, ID extends Serializable> implements ReactiveCosmosDBRepository<T, ID>{


    private final RxJavaCosmosDBOperations operations;

    public SimpleReactiveCosmosDBRepository(final RxJavaCosmosDBOperations rxJavaCosmosDBOperations){
        this.operations = rxJavaCosmosDBOperations;
    }

    protected Mono mapMono(Single single) {
        return ReactiveWrapperConverters.toWrapper(single , Mono.class);
    }

    protected Flux mapFlux(Observable observable) {
        return ReactiveWrapperConverters.toWrapper(observable, Flux.class);
    }

    @Override
    public RxJavaCosmosDBOperations getCosmosDBOperations() {
        return operations;
    }

    @Override
    public <S extends T> Mono<S> save(S s) {
        return mapMono(operations.save(s).toSingle());

    }

    @Override
    public Mono<Void> deleteById(ID id) {
        return mapMono(operations.remove(id.toString()).toSingle());
    }

    @Override
    public Mono<T> findById(ID id) {
        return mapMono(operations.findById(id.toString()).toSingle());
    }

    @Override
    public Mono<Void> delete(T t) {
        return null;
    }

    @Override
    public <S extends T> Flux<S> saveAll(Iterable<S> iterable) {
        return null;
    }

    @Override
    public <S extends T> Flux<S> saveAll(Publisher<S> publisher) {
        return null;
    }



    @Override
    public Mono<T> findById(Publisher<ID> publisher) {
        return null;
    }

    @Override
    public Mono<Boolean> existsById(ID id) {
        return null;
    }

    @Override
    public Mono<Boolean> existsById(Publisher<ID> publisher) {
        return null;
    }

    @Override
    public Flux<T> findAll() {
        return null;
    }

    @Override
    public Flux<T> findAllById(Iterable<ID> iterable) {
        return null;
    }

    @Override
    public Flux<T> findAllById(Publisher<ID> publisher) {
        return null;
    }

    @Override
    public Mono<Long> count() {
        return null;
    }

    @Override
    public Mono<Void> deleteById(Publisher<ID> publisher) {
        return null;
    }

    @Override
    public Mono<Void> deleteAll(Iterable<? extends T> iterable) {
        return null;
    }

    @Override
    public Mono<Void> deleteAll(Publisher<? extends T> publisher) {
        return null;
    }

    @Override
    public Mono<Void> deleteAll() {
        return null;
    }
}
