package com.javarush.games.racer;

public class FinishLine extends GameObject {

    private boolean isVisible = false;

    public boolean isCrossed(PlayerCar playerCar){
        boolean result = false;
        if (playerCar.y<this.y) result = true;
        return result;
    }

    public void show(){
        isVisible = true;
    }

    public void move(int boost){
        if (isVisible) y += boost;
    }

    public FinishLine() {
        super(RacerGame.ROADSIDE_WIDTH, -1 * ShapeMatrix.FINISH_LINE.length, ShapeMatrix.FINISH_LINE);

    }
}
