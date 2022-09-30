package rxjava.examples.utils;

import rx.Observable;
import rxjava.examples.model.Sound;

import static rxjava.examples.model.Sound.DAH;
import static rxjava.examples.model.Sound.DI;

public class MorseCodeUtils {
    public static Observable<Sound> toMorseCode(char ch){
        switch (ch){
            case 'A':
                return Observable.just(DI, DAH);
            case 'B':
                return Observable.just(DAH, DI, DI, DI);
            case 'C':
                return Observable.just(DAH, DI, DAH, DI);
            case 'P':
                return Observable.just(DI, DAH, DAH, DI);
            case 'R':
                return Observable.just(DI, DAH, DI);
            case 'S':
                return Observable.just(DI, DI, DI);
            case 'T':
                return Observable.just(DAH);
            default:
                return Observable.empty();

        }
    }
}
