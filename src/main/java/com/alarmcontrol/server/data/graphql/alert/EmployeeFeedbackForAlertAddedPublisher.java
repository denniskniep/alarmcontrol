package com.alarmcontrol.server.data.graphql.alert;

import com.alarmcontrol.server.data.graphql.models.EmployeeFeedbackForAlertAdded;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.observables.ConnectableObservable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

  @Component
  public class EmployeeFeedbackForAlertAddedPublisher {

    private Logger logger = LoggerFactory.getLogger(EmployeeFeedbackForAlertAddedPublisher.class);
    private final Flowable<EmployeeFeedbackForAlertAdded> publisher;
    private ObservableEmitter<EmployeeFeedbackForAlertAdded> emitter;

    public EmployeeFeedbackForAlertAddedPublisher() {
      Observable<EmployeeFeedbackForAlertAdded> alertObservable = Observable.create(emitter -> registerEmitter(emitter));
      ConnectableObservable<EmployeeFeedbackForAlertAdded> connectableObservable = alertObservable.share().publish();
      connectableObservable.connect();
      publisher = connectableObservable.toFlowable(BackpressureStrategy.BUFFER);
    }

    private void registerEmitter(ObservableEmitter<EmployeeFeedbackForAlertAdded> emitter) {
      logger.info("Registering emitter...");
      this.emitter = emitter;
      logger.info("Emitter registered");
    }

    public void emitEmployeeFeedbackForAlertAdded(Long alertId, Long employeeId) {
      if(emitter != null){
        try {
          emitter.onNext(new EmployeeFeedbackForAlertAdded(alertId, employeeId));
          logger.info("Notified Subscribers that Employee "+employeeId+" sent feedback for Alert " + alertId);
        } catch (RuntimeException e) {
          logger.error("Cannot send EmployeeFeedbackForAlertAdded", e);
        }
      }
    }

    public Flowable<EmployeeFeedbackForAlertAdded> getPublisher() {
      return publisher;
    }
  }
