package com.example.user.myapplication.injection;

import java.math.BigDecimal;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.subjects.BehaviorSubject;

/**
 * Class that pass data between components
 * */
@Singleton
public class RxDataPass {

    private BehaviorSubject<Integer> behaviorSubject;

    private BehaviorSubject<BigDecimal> itemEditTextOnTextChangedSubject;

    //Subscribe a position when item click
    public BehaviorSubject<Integer> getListItemClickSubject() {
        return behaviorSubject;
    }

    //Subscribe a string
    public BehaviorSubject<BigDecimal> getOnTextChangedSubject() {
        return itemEditTextOnTextChangedSubject;
    }


    @Inject
    public RxDataPass() {
        behaviorSubject
                = BehaviorSubject.create();
        itemEditTextOnTextChangedSubject
                = BehaviorSubject.create();
    }
}
