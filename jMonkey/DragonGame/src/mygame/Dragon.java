
package mygame;

import com.jme3.anim.AnimComposer;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

public class Dragon extends Node implements PhysicsCollisionListener, ActionListener, AnalogListener {
    
    
    ///////////////////////////////////////////  DATAFIELDS  ///////////////////////
    
    
    private AssetManager assetManager;
    private InputManager inputManager;
    
    private Spatial body;
    private CollisionShape bounds;
    private CharacterControl controls;
    private AnimComposer anim;
    
    private float speed = 8.0f;
    private float angularSpeed = 0.9f;
    private Vector3f orientation = new Vector3f(0, 0, 1);
    private Vector3f rotation = new Vector3f(0, 1, 0);
    //private Vector3f position = new Vector3f(0, 5f, 0);
    private Vector3f startPosition = new Vector3f(0, 5f, 0);
    //private Vector3f proportion = new Vector3f(1, 1, 1);
    
    private boolean breathingFire = false;
    
    
    
    
    
    //////////////////////////////////////////  CONSTRUCTORS             ///////////////////
    
    

    public Dragon(AssetManager assetManager, InputManager inputManager) {
        this.assetManager = assetManager;
        this.inputManager = inputManager;
        
        body = this.assetManager.loadModel("Models/dragon/dragon.j3o");
        bounds = CollisionShapeFactory.createBoxShape(body);
        bounds.setScale(new Vector3f(1.0f, 2.0f, 0.8f));
        body.addControl(new RigidBodyControl(bounds, 10.0f));
        getControls().setFriction(2.5f);

        anim = ((Node)body).getChild(0).getControl(AnimComposer.class);
        anim.setCurrentAction("idle");
        
        this.attachChild(body);
    }//end constructor
    
    
    
    
    
    
    
    
    /////////////////////////////////////  GETTERS AND SETTERS  ////////////////////////
    
    

    public Spatial getBody(){
        return this.body;
    }//end getBody()

    
    public boolean isBreathingFire(){
        return this.breathingFire;
    }
    
    public void setBreathingFire(boolean breathingFire){
        this.breathingFire = breathingFire;
    }
    
    
    
    
    
    ////////////////////////////////////////////// EASE of CODE ////////////////////////
    
    
    public RigidBodyControl getControls(){
        return body.getControl(RigidBodyControl.class);
    }//end getControls()

    
    
    
    
    
    
    ////////////////////////////////////////////  MOVEMENT  ///////////////////////////

    
    public void move(String direction, float timePerFrame){
        if(direction.equals("walk-forward")){
            body.getLocalRotation().getRotationColumn(2, orientation);
            getControls().setLinearVelocity(orientation.mult(speed));
        }
        else if(direction.equals("walk-backward")){
            body.getLocalRotation().getRotationColumn(2, orientation);
            getControls().setLinearVelocity(orientation.mult(-speed));
        }
        else if(direction.equals("turn-left")){
            getControls().setAngularVelocity(rotation.mult(angularSpeed));
        }
        else if(direction.equals("turn-right")){
            getControls().setAngularVelocity(rotation.mult(-angularSpeed));
        }
    }//end move()
    
    
    
    
    public void fly(float timePerFrame){
        body.getLocalRotation().getRotationColumn(2, orientation);
        Vector3f trajectory = orientation.mult(speed);
        Vector3f flightPath = new Vector3f(trajectory.x, trajectory.y+speed/2, trajectory.z);
        getControls().setLinearVelocity(flightPath);
    }//end fly()
    
    
    
    
    public void moveHome(){
        body.setLocalTranslation(startPosition);
        this.setLocalTranslation(body.getLocalTranslation());
    }//end moveHome()
    
    
    
    public void breathFire(){
        if(!isBreathingFire()){
            breathingFire = true;
            body.getLocalRotation().getRotationColumn(2, orientation);
            Vector3f location = body.getLocalTranslation();
            location = new Vector3f(location.x, location.y+3.4f, location.z);
            Fireball fireball = new Fireball(this, assetManager, location, orientation);
            this.attachChildAt(fireball, 0);
        }
    }//end breathFire()

    
    
    
    
    
    
    
    
    ///////////////////////////////////////////////  USER EVENTS  ///////////////////
    
    
    public void getMappings(){
        inputManager.addMapping("walk-forward", new KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping("turn-left",new KeyTrigger(KeyInput.KEY_A)); 
        inputManager.addMapping("turn-right",new KeyTrigger(KeyInput.KEY_D)); 
        inputManager.addMapping("walk-backward",new KeyTrigger(KeyInput.KEY_S));
        inputManager.addMapping("flying", new KeyTrigger(KeyInput.KEY_SPACE));
        inputManager.addMapping("burning", new KeyTrigger(KeyInput.KEY_LSHIFT));
        
        inputManager.addListener(this, "walk-forward", "walk-backward", "turn-left", "turn-right", "flying", "burning");
    }//end getMapping()
    
    
    
    
    
    
    
    
    ////////////////////////////////////////  EVENT LISTENERS  /////////////////////////
    

    @Override
    public void collision(PhysicsCollisionEvent arg0) {
        
    }//end collision()
    
    
    

    @Override
    public void onAction(String arg0, boolean arg1, float arg2) {
        if(arg1 && (arg0.contains("walk") || arg0.contains("turn"))){
            anim.setCurrentAction("walk");
        }
        else if(arg1 && arg0.equals("flying")){
            anim.setCurrentAction("fly");
        }
        else if(arg1 && arg0.equals("burning")){
            breathFire();
        }
        else{
            anim.setCurrentAction("idle");
            getControls().setLinearVelocity(new Vector3f(0, 0, 0));
            getControls().setAngularVelocity(new Vector3f(0, 0, 0));
        }
    }//end onAction()

    
    
    
    @Override
    public void onAnalog(String arg0, float arg1, float arg2) {
        if(!arg0.equals("flying")){
            move(arg0, arg2);
        }
        else{
            fly(arg2);
        }
    }//end onAnalog()

}//end Dragon class
