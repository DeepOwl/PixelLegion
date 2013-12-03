package com.legion.samplegame;

import com.legion.framework.Screen;
import com.legion.framework.implementation.AndroidGame;

public class SampleGame extends AndroidGame {
    @Override
    public Screen getInitScreen() {
        return new LoadingScreen(this); 
    }
    
}