package com.sai.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;

import sun.rmi.runtime.Log;

public class FlappyBird extends ApplicationAdapter {
	SpriteBatch batch;
	Texture bground;
	Texture[] birds;
    Texture gameover;
    int flapstate=0;
    float birdY=0;
    float velocity=0;
    int gamestate=0;
    float gravity=2;
    Texture toptube;
    Texture bottomtube;
    float gap = 400;
    float maxtubeoffset;
    Random randomgenerator;
    Circle birdcircle;
    Rectangle[] toprect;
    Rectangle[] botrect;
    //ShapeRenderer shaperenderer;
    BitmapFont font;
    float tubevelocity=4;
    int score=0;
    int scorintube=0;
    int numberoftubes=4;
    float[] tubeX = new float[numberoftubes];
    float[] tubeoffset= new float[numberoftubes];
    float distance;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		bground = new Texture("bg.png");
        gameover= new Texture("gameover.png");
		birds=new Texture[2];
        birds[0]=new Texture("bird.png");
        birds[1]=new Texture("bird2.png");
        toptube=new Texture("toptube.png");
        bottomtube=new Texture("bottomtube.png");
        maxtubeoffset = Gdx.graphics.getHeight()/2 -gap/2-100;
        randomgenerator = new Random();
        distance=Gdx.graphics.getWidth() *3/4;
        //shaperenderer = new ShapeRenderer();
        birdcircle=new Circle();
        toprect=new Rectangle[numberoftubes];
        botrect=new Rectangle[numberoftubes];
        font=new BitmapFont();
        font.setColor(Color.WHITE);
        font.getData().setScale(10);

        startgame();

	}

	public void startgame(){
        birdY=Gdx.graphics.getHeight()/2-birds[0].getHeight()/2;

        for(int i =0;i<numberoftubes;i++){
            tubeoffset[i]=(randomgenerator.nextFloat()-0.5f)* (Gdx.graphics.getHeight()-gap-200);
            tubeX[i]=Gdx.graphics.getWidth()/2-toptube.getWidth()/2 + Gdx.graphics.getWidth()+i *distance;
            toprect[i] =new Rectangle();
            botrect[i]=new Rectangle();
        }
    }
	@Override
	public void render () {

        batch.begin();
        batch.draw(bground,0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());


        if(gamestate==1){
            if(tubeX[scorintube]<Gdx.graphics.getWidth()/2){
                score++;
                Gdx.app.log("SCORE", String.valueOf(score));
                if(scorintube<numberoftubes-1){
                    scorintube++;

                }
                else{
                    scorintube=0;
                }

            }
            if(Gdx.input.justTouched()){
                velocity=-30;

            }
            for(int i =0;i<numberoftubes;i++){
                if(tubeX[i]<-toptube.getWidth()){
                    tubeX[i]+=numberoftubes*distance;
                }
                else{
                    tubeX[i] -=tubevelocity;

                }

                batch.draw(toptube,tubeX[i],Gdx.graphics.getHeight()/2+gap/2+tubeoffset[i]);
                batch.draw(bottomtube,tubeX[i],Gdx.graphics.getHeight()/2-gap/2-bottomtube.getHeight() +tubeoffset[i]);
                toprect[i]=new Rectangle(tubeX[i],Gdx.graphics.getHeight()/2+gap/2+tubeoffset[i],toptube.getWidth(),toptube.getHeight());
                botrect[i] =new Rectangle(tubeX[i],Gdx.graphics.getHeight()/2-gap/2-bottomtube.getHeight() +tubeoffset[i],bottomtube.getWidth(),bottomtube.getHeight());
            }



            if(birdY>0){
                velocity = velocity+gravity;
                birdY-=velocity;
            }
            else{
                gamestate=2;
            }



        }
        else if(gamestate==0){

            if(Gdx.input.justTouched()){
                gamestate=1;
            }
        }
        else if(gamestate==2){
            batch.draw(gameover,Gdx.graphics.getWidth()/2-gameover.getWidth()/2,Gdx.graphics.getHeight()/2-gameover.getHeight()/2);
            if(Gdx.input.justTouched()){
                gamestate=1;
                startgame();
                score=0;
                scorintube=0;
                velocity=0;
            }
        }
        if(flapstate==0){
            flapstate=1;
        }
        else{
            flapstate=0;
        }

        batch.draw(birds[flapstate] ,Gdx.graphics.getWidth()/2-birds[flapstate].getWidth()/2,birdY);
        font.draw(batch,String.valueOf(score),100,200);
        birdcircle.set(Gdx.graphics.getWidth()/2,birdY+birds[flapstate].getHeight()/2,birds[flapstate].getWidth()/2);


        //shaperenderer.begin(ShapeRenderer.ShapeType.Filled);
//        shaperenderer.setColor(Color.BLACK);
//        shaperenderer.circle(birdcircle.x,birdcircle.y,birdcircle.radius);

        for(int i =0;i<numberoftubes;i++){
//            shaperenderer.rect(tubeX[i],Gdx.graphics.getHeight()/2+gap/2+tubeoffset[i],toptube.getWidth(),toptube.getHeight());
//            shaperenderer.rect(tubeX[i],Gdx.graphics.getHeight()/2-gap/2-bottomtube.getHeight() +tubeoffset[i],bottomtube.getWidth(),bottomtube.getHeight());
        if(Intersector.overlaps(birdcircle,toprect[i])||Intersector.overlaps(birdcircle,botrect[i])){
                gamestate=2;
        }

        }
        //shaperenderer.end();
        batch.end();

	}
}
