
package mygame;

import com.jme3.anim.AnimComposer;
import com.jme3.anim.tween.Tween;
import com.jme3.anim.tween.Tweens;
import com.jme3.anim.tween.action.Action;
import com.jme3.asset.AssetManager;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;


public class Fireball extends Node {
    
    
    ////////////////////////////////////  DATAFIELDS  ////////////////////////////
    
    private AssetManager assetManager;
    
    private Spatial body;
    private AnimComposer anim;
    
    private float speed = 37f;
    private Vector3f startPosition;
    private Vector3f trajectory;




    //////////////////////////////////  CONSTRUCTORS  ///////////////////////////

    public Fireball(Dragon dragon, AssetManager assetManager, Vector3f startPosition, Vector3f trajectory) {
        this.assetManager = assetManager;
        this.startPosition = startPosition;
        this.trajectory = trajectory;
        
        body = this.assetManager.loadModel("Models/fireball/fireball.j3o");
        body.setLocalTranslation(startPosition);
        Quaternion rotate = new Quaternion().fromAxes(new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), trajectory);
        body.setLocalRotation(rotate);
        
        
        anim = ((Node)body).getChild(0).getControl(AnimComposer.class);
        
        Action flame = anim.action("burning");
        Tween finished = Tweens.callMethod(this, "destroy");
        Action burns = anim.actionSequence("burnout", flame, finished);
        anim.setCurrentAction("burnout");
        
        
        this.attachChild(body);
        
    }//end constuctor
    
    
    
    
    /////////////////////////////////////////////  OTHER METHODS  /////////////////
    
    public void move(float timePerFrame){
       Vector3f position = this.getLocalTranslation();
            Vector3f move = new Vector3f(
                    position.x + trajectory.x * speed * timePerFrame,
                    position.y + trajectory.y * speed * timePerFrame,
                    position.z + trajectory.z * speed * timePerFrame
            );
            this.setLocalTranslation(move);
    }//end move()
    
    
    
    public void destroy(){
        ( (Dragon) parent).setBreathingFire(false);
        parent.detachChild(this);
    }
    
    
    
}//end Fireball class
