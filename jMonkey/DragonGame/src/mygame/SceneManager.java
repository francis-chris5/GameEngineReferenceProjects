
package mygame;

import com.jme3.asset.AssetManager;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.util.HashMap;
import java.util.Map;


public class SceneManager extends Node {
    
    ///////////////////////////////////////////  DATAFIELDS  //////////////////////
    
    
    private AssetManager assetManager;
    private Map<String, Spatial> sceneMap = new HashMap<>();
    private Spatial currentScene;


    
    /////////////////////////////////////////  CONSTRUCTORS  ///////////////////////
    
    
    public SceneManager(AssetManager assetManager, String defaultSceneName, String defaultSceneFile) {
        this.assetManager = assetManager;
        sceneMap.put(defaultSceneName, this.assetManager.loadModel(defaultSceneFile));
        this.attachChild(sceneMap.get(defaultSceneName));
        currentScene = sceneMap.get(defaultSceneName);
    }
    
    
    
    ////////////////////////////////////////  SCENE MAPPING  ////////////////////////////
    
    
    public void addScene(String name, String modelFile){
        sceneMap.put(name, this.assetManager.loadModel(modelFile));
    }//end assScene()

    
    
    public void removeScene(String name){
        
    }//end removeScene()
    
    
    
    public Node loadScene(String name){
        this.detachChild(currentScene);
        this.attachChild(sceneMap.get(name));
        currentScene = sceneMap.get(name);
        return this;
    }//end loadScene()
    
    
    
    
    //////////////////////////////////////////  OTHER METHODS  /////////////////////
    
    public Spatial getTerrain(){
        return this.getChild("terrain");
    }

    
}//end SceneManager class
