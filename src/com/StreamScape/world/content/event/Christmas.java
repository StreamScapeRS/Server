package com.StreamScape.world.content.event;

import com.StreamScape.model.GameObject;
import com.StreamScape.model.Position;
import com.StreamScape.world.content.CustomObjects;
import com.StreamScape.world.content.combat.strategy.impl.KreeArra;

public class Christmas {

    private static GameObject object;

    public static void init() {
        homeObjects();
        kreeSpawn();
        flockleaderGeerlin();
        unicorn();
    }

    public static void homeObjects() {
        object = new GameObject(42598, new Position(3100, 2981, 0));
        CustomObjects.spawnGlobalObject(object);
        object = new GameObject(12003, new Position(3099, 2982, 0));
        CustomObjects.spawnGlobalObject(object);


        object = new GameObject(12003, new Position(3101, 2980, 0));
        CustomObjects.spawnGlobalObject(object);
        object = new GameObject(12003, new Position(3102, 2980, 0));
        CustomObjects.spawnGlobalObject(object);
        object = new GameObject(12003, new Position(3104, 2983, 0));
        CustomObjects.spawnGlobalObject(object);
        object = new GameObject(12003, new Position(3103, 2984, 0));
        CustomObjects.spawnGlobalObject(object);
        object = new GameObject(12003, new Position(3101, 2984, 0));
        CustomObjects.spawnGlobalObject(object);
        object = new GameObject(12003, new Position(3099, 2983, 0));
        CustomObjects.spawnGlobalObject(object);
        object = new GameObject(12003, new Position(3104, 2981, 0));
        CustomObjects.spawnGlobalObject(object);
        object = new GameObject(11356, new Position(3097, 2982, 0));
        CustomObjects.spawnGlobalObject(object);

        //bank
        object = new GameObject(12003, new Position(3096, 2995, 0));
        CustomObjects.spawnGlobalObject(object);

        object = new GameObject(12003, new Position(3106, 2995, 0));
        CustomObjects.spawnGlobalObject(object);
        object = new GameObject(12003, new Position(3124, 2978, 0));
        CustomObjects.spawnGlobalObject(object);

        object = new GameObject(12003, new Position(3083, 2979, 0));
        CustomObjects.spawnGlobalObject(object);
        object = new GameObject(12003, new Position(3083, 2987, 0));
        CustomObjects.spawnGlobalObject(object);

        object = new GameObject(12003, new Position(3111, 2997, 0));
        CustomObjects.spawnGlobalObject(object);
        object = new GameObject(12003, new Position(3111, 3000, 0));
        CustomObjects.spawnGlobalObject(object);
        object = new GameObject(12003, new Position(3112, 3000, 0));
        CustomObjects.spawnGlobalObject(object);
        object = new GameObject(12003, new Position(3112, 2997, 0));
        CustomObjects.spawnGlobalObject(object);
        object = new GameObject(12003, new Position(2134, 2978, 0));
        CustomObjects.spawnGlobalObject(object);
        object = new GameObject(12004, new Position(3124, 2987, 0));
        CustomObjects.spawnGlobalObject(object);

        //santa spawn
        KreeArra.spawn(1552,new Position(3093,2982,0));

    }


    public static void flockleaderGeerlin() {
        KreeArra.spawn(6822,new Position(3467,3264,0));
        KreeArra.spawn(6822,new Position(3466,3277,0));
        KreeArra.spawn(6822,new Position(3465,3288,0));
        KreeArra.spawn(6822,new Position(3467,3302,0));
        KreeArra.spawn(6822,new Position(3480,3310,0));
        KreeArra.spawn(6822,new Position(3487,3304,0));
        KreeArra.spawn(6822,new Position(3493,3303,0));
        KreeArra.spawn(6822,new Position(3497,3302,0));
        KreeArra.spawn(6822,new Position(3498,3263,0));
        KreeArra.spawn(6822,new Position(3503,3264,0));
        KreeArra.spawn(6822,new Position(3501,3274,0));
        KreeArra.spawn(6822,new Position(3507,3283,0));
        KreeArra.spawn(6822,new Position(3511,3283,0));
        KreeArra.spawn(6822,new Position(3509,3287,0));
        KreeArra.spawn(6822,new Position(1,1,0));
    }

    public static void unicorn(){
        KreeArra.spawn(6822,new Position(3494,3318,0));
        KreeArra.spawn(6822,new Position(3499,3318,0));
        KreeArra.spawn(6822,new Position(3500,3313,0));
        KreeArra.spawn(6822,new Position(3511,3320,0));
        KreeArra.spawn(6822,new Position(3513,3314,0));
        KreeArra.spawn(6822,new Position(3512,3303,0));
        KreeArra.spawn(6822,new Position(3516,3304,0));
        KreeArra.spawn(6822,new Position(3520,3302,0));
        KreeArra.spawn(6822,new Position(3519,3293,0));
        KreeArra.spawn(6822,new Position(3490,3285,0));
        KreeArra.spawn(6822,new Position(3496,3279,0));
        KreeArra.spawn(6822,new Position(3481,3283,0));
        KreeArra.spawn(6822,new Position(3475,3283,0));
        KreeArra.spawn(6822,new Position(3478,3294,0));

    }

    public static void kreeSpawn(){
        KreeArra.spawn(6822,new Position(3472,3276,0));
        KreeArra.spawn(6822,new Position(3503,3283,0));
        KreeArra.spawn(6822,new Position(3488,3269,0));
        KreeArra.spawn(6822,new Position(3478,3301,0));
        KreeArra.spawn(6822,new Position(3472,3276,0));
        KreeArra.spawn(6822,new Position(3466,3301,0));
        KreeArra.spawn(6822,new Position(3499,3298,0));
    }
}
