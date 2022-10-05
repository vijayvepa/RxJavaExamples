package rxjava.examples.errorhandling;

import rx.Observable;
import rxjava.examples.model.*;

public class InsuranceHandler {
    void insurance(Observable<Person> person, Observable<InsuranceHandler> insurance) {
        Observable<Health> health = person.flatMap(this::checkHealth);
        Observable<Income> income = person.flatMap(this::determineIncome);
        Observable<Score> score = Observable.zip(health, income, this::assess).map(this::translate);
        Observable<Agreement> agreement = Observable.zip(insurance, score.filter(Score::isHigh), this::prepare);

        Observable<Tracking> mail = agreement.filter(Agreement::postalMailRequired)
                .flatMap(this::print)
                .flatMap(this::deliver);
    }

    void insuranceWithFixedFallbacks(Observable<Person> person, Observable<InsuranceHandler> insurance) {
        Observable<Health> health = person.flatMap(this::checkHealth);
        Observable<Income> income = person.flatMap(this::determineIncome).onErrorReturn(error -> Income.no());
        Observable<Score> score = Observable.zip(health, income, this::assess).map(this::translate);
        Observable<Agreement> agreement = Observable.zip(insurance, score.filter(Score::isHigh), this::prepare);

        Observable<Tracking> mail = agreement.filter(Agreement::postalMailRequired)
                .flatMap(this::print)
                .flatMap(this::deliver);
    }

    void insuranceWithDynamicFallbacks(Observable<Person> person, Observable<InsuranceHandler> insurance) {
        Observable<Health> health = person.flatMap(this::checkHealth);

        Observable<Income> income = person.flatMap(this::determineIncome).onErrorResumeNext(person.flatMap(this::guessIncome));


        Observable<Score> score = Observable.zip(health, income, this::assess).map(this::translate);
        Observable<Agreement> agreement = Observable.zip(insurance, score.filter(Score::isHigh), this::prepare);

        Observable<Tracking> mail = agreement.filter(Agreement::postalMailRequired)
                .flatMap(this::print)
                .flatMap(this::deliver);
    }

    void insuranceWithDynamicFallbacksAlt(Observable<Person> person, Observable<InsuranceHandler> insurance) {
        Observable<Health> health = person.flatMap(this::checkHealth);

        Observable<Income> income = person
                .flatMap(this::determineIncome)
                .flatMap(Observable::just, th -> Observable.empty(), Observable::empty)
                .concatWith(person.flatMap(this::guessIncome))
                .first();


        Observable<Score> score = Observable.zip(health, income, this::assess).map(this::translate);
        Observable<Agreement> agreement = Observable.zip(insurance, score.filter(Score::isHigh), this::prepare);

        Observable<Tracking> mail = agreement.filter(Agreement::postalMailRequired)
                .flatMap(this::print)
                .flatMap(this::deliver);
    }

    void insuranceWithDynamicFallbacksAlt2(Observable<Person> person, Observable<InsuranceHandler> insurance) {
        Observable<Health> health = person.flatMap(this::checkHealth);

        Observable<Income> income = person
                .flatMap(this::determineIncome)
                .flatMap(Observable::just, th -> Observable.empty(), Observable::empty);


        Observable<Score> score = Observable.zip(health, income, this::assess).map(this::translate);
        Observable<Agreement> agreement = Observable.zip(insurance, score.filter(Score::isHigh), this::prepare);

        Observable<Tracking> mail = agreement.filter(Agreement::postalMailRequired)
                .flatMap(this::print)
                .flatMap(this::deliver);
    }

    void insuranceWithDynamicFallbacksAlt3(Observable<Person> person, Observable<InsuranceHandler> insurance) {
        Observable<Health> health = person.flatMap(this::checkHealth);

        Observable<Income> income = person
                .flatMap(this::determineIncome)
                .onErrorResumeNext(throwable -> {
                    if (throwable instanceof NullPointerException) {
                        return Observable.error(throwable);
                    } else {
                        return person.flatMap(this::guessIncome);
                    }
                });


        Observable<Score> score = Observable.zip(health, income, this::assess).map(this::translate);
        Observable<Agreement> agreement = Observable.zip(insurance, score.filter(Score::isHigh), this::prepare);

        Observable<Tracking> mail = agreement.filter(Agreement::postalMailRequired)
                .flatMap(this::print)
                .flatMap(this::deliver);
    }

    private <R> Observable<Income> guessIncome(Person person) {
        return Observable.just(new Income());
    }

    private <R> Observable<Tracking> deliver(Agreement agreement) {
        return Observable.just(new Tracking());
    }

    private Observable<Agreement> print(Agreement agreement) {
        return Observable.just(agreement);
    }

    private Agreement prepare(InsuranceHandler insuranceHandler, Score score) {
        return new Agreement();
    }


    private <R> Score translate(Score score) {
        return score;
    }

    private Score assess(Health h, Income i) {
        return new Score();
    }

    private Observable<Income> determineIncome(Person person) {
        return Observable.just(new Income());
    }

    private Observable<Health> checkHealth(Person person) {

        return Observable.just(new Health());
    }
}
