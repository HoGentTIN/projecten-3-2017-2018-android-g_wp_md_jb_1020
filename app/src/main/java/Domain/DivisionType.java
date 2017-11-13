package Domain;

/**
 * Created by pieter on 13/11/2017.
 */

public enum DivisionType {

    DAMES(7,2),
    HEREN(8,2);

    private final long roundLength;
    private final long pauseLength;

    private DivisionType(long roundLength, long pauseLength){

        this.roundLength = roundLength;
        this.pauseLength = pauseLength;
    }
}
