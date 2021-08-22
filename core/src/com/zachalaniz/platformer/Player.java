package com.zachalaniz.platformer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

//General class for a player
public class Player {
    public float WIDTH = 1;
    public float HEIGHT = 1;
    public float MAX_VELOCITY = 4f;
    public float JUMP_VELOCITY = 12f;
    public float DAMPING = 0.87f;
    enum State {
        Standing, Walking, Jumping
    }
    public Vector2 position = new Vector2();
    public Vector2 velocity = new Vector2();
    public State state = State.Walking;
    public boolean facesRight = true;
    public boolean grounded = false;
    public Texture playerTexture;

    public Player(Texture texture){
        this.playerTexture = texture;
    }

    public boolean left() {
        return Gdx.input.isKeyPressed(Input.Keys.LEFT);
    }

    public boolean right() {
        return Gdx.input.isKeyPressed(Input.Keys.RIGHT);
    }

    public boolean up() {
        return Gdx.input.isKeyPressed(Input.Keys.UP);
    }
}
