package com.alarmcontrol.server.data.graphql;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.observables.ConnectableObservable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class CommonPublisher<T> {

  private final Flowable<T> publisher;
  protected Logger logger = LoggerFactory.getLogger(CommonPublisher.class);
  private ObservableEmitter<T> emitter;

  public CommonPublisher() {
    Observable<T> alertObservable = Observable.create(emitter -> registerEmitter(emitter));
    ConnectableObservable<T> connectableObservable = alertObservable.share().publish();
    connectableObservable.connect();
    publisher = connectableObservable.toFlowable(BackpressureStrategy.BUFFER);
  }

  private void registerEmitter(ObservableEmitter<T> emitter) {
    logger.info("Registering emitter for '{}' ...", this.getClass().getName());
    this.emitter = emitter;
    logger.info("Emitter registered");
  }

  protected void emit(T value) {
    if (emitter != null) {
      try {
        emitter.onNext(value);
        logger.info("Notified Subscribers of '{}'", this.getClass().getName());
      } catch (RuntimeException e) {
        logger.error("Cannot send to Subscribers of '" + this.getClass().getName() + "'", e);
      }
    }
  }

  public Flowable<T> getPublisher() {
    return publisher;
  }
}
