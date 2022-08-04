package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.input.ChaseCamera;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.DirectionalLight;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.system.AppSettings;
import java.util.Random;


public class Main extends SimpleApplication {

    private BulletAppState physics = new BulletAppState();
    private Dragon dragon;
    private SceneManager sceneManager;

 
    
    final private ActionListener sceneNavigation = new ActionListener(){
        @Override
        public void onAction(String arg0, boolean arg1, float arg2) {
            if(arg0.equals("go-lake")){
                sceneManager.loadScene("lake");
                dragon.moveHome();
            }
            else if(arg0.equals("go-desert")){
                sceneManager.loadScene("desert");
                dragon.moveHome();
            }
        }  
    };
    
    
    
    
    public static void main(String[] args) {
        Main app = new Main();
        app.showSettings = false;
        
        AppSettings dragonSettings = new AppSettings(true);
        dragonSettings.put("Width", 777);
        dragonSettings.put("Height", 456);
        dragonSettings.put("Title", "Dragon Game");
        app.setSettings(dragonSettings);
        
        app.start();
    }

    @Override
    public void simpleInitApp() {
        this.setDisplayFps(false);
        this.setDisplayStatView(false);
        this.flyCam.setEnabled(false);

        
        inputManager.addMapping("go-desert", new KeyTrigger(KeyInput.KEY_PGDN));
        inputManager.addMapping("go-lake", new KeyTrigger(KeyInput.KEY_PGUP));
        inputManager.addListener(sceneNavigation, "go-desert", "go-lake");
        
        
        stateManager.attach(physics);
        
        
        DirectionalLight sun = new DirectionalLight();
        sun.setDirection(new Vector3f(12f, -21.3f, -12.5f));


        sceneManager = new SceneManager(assetManager, "lake", "Scenes/lake.j3o");
        sceneManager.addScene("desert", "Scenes/desert.j3o");
        
        
        dragon = new Dragon(assetManager, inputManager);
        dragon.getMappings();
        

        
        rootNode.addLight(sun);
        rootNode.attachChild(sceneManager);
        sceneManager.attachChild(dragon);
        
        
        physics.getPhysicsSpace().add(sceneManager.getTerrain());
        physics.getPhysicsSpace().add(dragon.getBody());
        
        
        ChaseCamera camera = new ChaseCamera(cam, dragon.getBody(), inputManager);
        camera.setSmoothMotion(true);
        camera.setDefaultDistance(25.0f);
        
        
        for(int i = 0; i < 4; i++){
            Random rand = new Random();
            sceneManager.attachChild(new Castle(assetManager, new Vector3f(rand.nextFloat()*256-128, -6, rand.nextFloat()*256-128)));
        }

    }//end simpleInitApp()
    
    
    

    @Override
    public void simpleUpdate(float tpf) {
        //TODO: add update code
        if(dragon.isBreathingFire()){
            try{
                ( (Fireball)dragon.getChild(0) ).move(tpf);
            }
            catch(Exception e){
                //just move on then...
            }
        }
    }//end simpleUpdate()
    
    
    

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }//end simpleRender()


}//end Main class
