package com.example.user.myapplication.rx;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.subjects.BehaviorSubject;

/**
 * Class that pass data between components
 * */
@Singleton
public class RxDataPass {

    private static final BehaviorSubject<Integer> behaviorSubject
            = BehaviorSubject.create();

    private static final BehaviorSubject<String> itemEditTextOnTextChangedSubject
            = BehaviorSubject.create();

    public static BehaviorSubject<Integer> getListItemClickSubject() {
        return behaviorSubject;
    }

    //Subscribe a string
    public static BehaviorSubject<String> getOnTextChangedSubject() {
        return itemEditTextOnTextChangedSubject;
    }


    @Inject
    public RxDataPass() {
    }
}
