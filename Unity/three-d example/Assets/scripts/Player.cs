using System.Collections;
using System.Collections.Generic;
using UnityEngine;

using UnityEngine.SceneManagement;

public class Player : MonoBehaviour{

    public float speed;
    private Rigidbody player;
    public GameObject graphics;
    private Animator animator;

    private int hitPoints = 100;
    private int expPoints = 0;


    void Start(){
        player = GetComponent<Rigidbody>();
        animator = graphics.GetComponent<Animator>();
    }//end Start()

    void Update(){
        float hor = Input.GetAxis("Horizontal");
        float ver = Input.GetAxis("Vertical");


        Vector3 direction = new Vector3(hor * speed, 0.0f, ver * speed);
        //store angle in a variable to track current direction
        // yaw_x = cos(angle + turn) - sin(anlgle + turn)
        // yaw_z = sin(angle + turn) + cos(angle + turn)
        // then quaternion.euler to move that angle --in radians

        if(direction.magnitude > 0.0f){
          animator.SetFloat("speed", 3);
          gameObject.transform.forward = direction;
          direction.y = -2.3f;
          player.velocity = direction;
        }
        else{
          animator.SetFloat("speed", -5);
        }


        float kick = Input.GetAxis("Jump"); //defaults to space bar
        if(kick != 0){
          animator.SetBool("kick", true);
        }
        else{
          animator.SetBool("kick", false);
        }

        Debug.Log("Hit Points: " + hitPoints);
        Debug.Log("Exp Points: " + expPoints);
        Debug.Log("********************");


    }//end Update()


    //BETTER IN 3D than what we did in 2D
    // RaycastHit ray;
    // if(Physics.Raycast(position, direction, out ray, distance, layer_mask)){
    // //do stuff
    //}
    void OnCollisionEnter(Collision collision){
      if(collision.gameObject.name == "gateway"){
        if(SceneManager.GetActiveScene().name == "desert" && expPoints > 3){
          SceneManager.LoadScene("swamp");
        }
        else if(SceneManager.GetActiveScene().name == "swamp"){
          SceneManager.LoadScene("hills");
        }
        else if(SceneManager.GetActiveScene().name == "hills"){
          SceneManager.LoadScene("desert");
        }
      }
      else if(collision.gameObject.tag == "blue_guy" && animator.GetBool("kick")){
        Debug.Log("got a hit..." + collision.gameObject.name);
        Destroy(collision.gameObject); //probably better stuff to do here
        expPoints++;
      }
    }//end OnCollisionEnter()

}//end Player class
