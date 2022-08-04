
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
import java.util.Random;


public class Castle extends Node {
    
    
    /////////////////////////////////////////  DATAFIELDS  /////////////////////////
    
    private AssetManager assetManager;
    
    private Spatial body;
    private AnimComposer anim;
    
    private Vector3f position;
    
    
    
    
    /////////////////////////////////////////  CONSTRUCTORS  //////////////////////

    public Castle(AssetManager assetManager, Vector3f position) {
        this.assetManager = assetManager;
        this.position = position;
        
        body = this.assetManager.loadModel("Models/castle/castle.j3o");
        body.setLocalTranslation(position);
        Random rand = new Random();
        Vector3f orient = new Vector3f(rand.nextFloat(), 0, rand.nextFloat());
        Quaternion rotate = new Quaternion().fromAxes(new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), orient);
        body.setLocalRotation(rotate);
        
        anim = ((Node)body).getChild(0).getControl(AnimComposer.class);
        anim.setCurrentAction("stands");
        
        this.attachChild(body);
    }//end constructor
    
    
    
    
}//end Castle class
