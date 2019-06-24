package com.alarmcontrol.server.data.graphql.alert.publisher;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.observables.ConnectableObservable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class AlertAddedPublisher {

  private Logger logger = LoggerFactory.getLogger(AlertAddedPublisher.class);
  private final Flowable<AlertAdded> publisher;
  private ObservableEmitter<AlertAdded> emitter;

  public AlertAddedPublisher() {
    Observable<AlertAdded> alertObservable = Observable.create(emitter -> registerEmitter(emitter));
    ConnectableObservable<AlertAdded> connectableObservable = alertObservable.share().publish();
    connectableObservable.connect();
    publisher = connectableObservable.toFlowable(BackpressureStrategy.BUFFER);
  }

  private void registerEmitter(ObservableEmitter<AlertAdded> emitter) {
    logger.info("Registering emitter...");
    this.emitter = emitter;
    logger.info("Emitter registered");
  }

  public void emitAlertAdded(Long alertId) {
    if(emitter != null){
      try {
        emitter.onNext(new AlertAdded(alertId));
        logger.info("Notified Subscribers that Alert " + alertId + " was added!");
      } catch (RuntimeException e) {
        logger.error("Cannot send AlertAdded", e);
      }
    }
  }

  public Flowable<AlertAdded> getPublisher() {
    return publisher;
  }
}
