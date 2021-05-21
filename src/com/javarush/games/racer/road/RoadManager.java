package com.javarush.games.racer.road;

import com.javarush.engine.cell.Game;
import com.javarush.games.racer.PlayerCar;
import com.javarush.games.racer.RacerGame;

import java.util.ArrayList;
import java.util.List;

public class RoadManager {
    public static final int LEFT_BORDER = RacerGame.ROADSIDE_WIDTH;
    public static final int RIGHT_BORDER = RacerGame.WIDTH - LEFT_BORDER;
    private static final int FIRST_LANE_POSITION = 16;
    private static final int FOURTH_LANE_POSITION = 44;
    private static final int PLAYER_CAR_DISTANCE = 12;

    private int passedCarsCount = 0;

    private List<RoadObject> items = new ArrayList<>();

    public int getPassedCarsCount() {
        return passedCarsCount;
    }

    private boolean isRoadSpaceFree(RoadObject object){
        boolean isCol = false;
        for (int i = 0; i < items.size(); i++) {
            isCol = items.get(i).isCollisionWithDistance(object, PLAYER_CAR_DISTANCE);
            if (isCol) break;
        }
        return !isCol;
    }

    private void generateMovingCar(Game game){
        int check = game.getRandomNumber(100);
        if (check < 10 && !isMovingCarExists())
            addRoadObject(RoadObjectType.DRUNK_CAR, game);
    }

    private boolean isMovingCarExists(){
        boolean isExists=false;
        for (RoadObject roadObject:items) {
            if (roadObject.type == RoadObjectType.DRUNK_CAR)
                isExists = true;
            else
                isExists = false;
        }
        return isExists;
    }

    private void generateRegularCar(Game game){
        int check = game.getRandomNumber(100);
        int carTypeNumber = game.getRandomNumber(4);
        if (check < 30) addRoadObject(RoadObjectType.values()[carTypeNumber], game);
    }

    public boolean checkCrush(PlayerCar playerCar){
        boolean isDead=false;
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).isCollision(playerCar)) isDead = true;
        }
        return isDead;
    }
    private void deletePassedItems(){
        List<RoadObject> itemX = new ArrayList<>(items);
        for (RoadObject ro : itemX) {
            if (ro.y >= RacerGame.HEIGHT){
                items.remove(ro);
                if (ro.type != RoadObjectType.THORN)
                    ++passedCarsCount;
            }
        }
    }

    public void generateNewRoadObjects(Game game){
        generateThorn(game);
        generateRegularCar(game);
        generateMovingCar(game);
    }

    private void generateThorn(Game game){
        int check = game.getRandomNumber(100);
        if (check < 10 && !isThornExists())
            addRoadObject(RoadObjectType.THORN, game);
    }

    private boolean isThornExists(){
        boolean isExists=false;
        for (RoadObject roadObject:items) {
            if (roadObject.type == RoadObjectType.THORN)
                isExists = true;
            else
                isExists = false;
        }
        return isExists;
    }

    public void draw(Game game){
        for (int i = 0; i < items.size(); i++) {
            items.get(i).draw(game);
        }
    }

    public void move(int boost){
        for (int i = 0; i < items.size(); i++) {
            items.get(i).move(boost+items.get(i).speed, items);
        }
        deletePassedItems();
    }

    private void addRoadObject(RoadObjectType type, Game game){
        int x = game.getRandomNumber(FIRST_LANE_POSITION, FOURTH_LANE_POSITION);
        int y = -1 * RoadObject.getHeight(type);
        RoadObject roadObject = createRoadObject(type, x, y);
        if (isRoadSpaceFree(roadObject)) items.add(roadObject);


    }

    private RoadObject createRoadObject(RoadObjectType type, int x, int y){
        if (type == RoadObjectType.THORN)
            return new Thorn(x,y);
        else if (type == RoadObjectType.DRUNK_CAR)
            return new MovingCar(x,y);
        else
            return new Car(type, x, y);
    }

}
