using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class player_move : MonoBehaviour{

    public float speed;
    private bool faceLeft = false;
    private Rigidbody2D player;
    private Animator animator;
    public LayerMask blue_mask;

    void Start(){
        player = GetComponent<Rigidbody2D>();
        animator = GetComponent<Animator>();
    }//end Start()



    void Update(){
      float hor = Input.GetAxis("Horizontal");
      float ver = Input.GetAxis("Vertical");

      if(hor < 0 && !faceLeft){
        faceLeft = true;
      }
      else if(hor > 0 && faceLeft){
        faceLeft = false;
      }


      if(faceLeft){
        player.transform.localScale *= new Vector2(-1 * player.transform.localScale.x, player.transform.localScale.y);
      }
      else{
        player.transform.localScale *= new Vector2(player.transform.localScale.x, player.transform.localScale.y);
      }




      player.velocity = new Vector2(hor * speed, ver * speed);


      float slap = Input.GetAxis("Jump");
      if(slap != 0){
        animator.SetBool("attack", true);
      }
      else{
        animator.SetBool("attack", false);
      }


      Vector2 origin = player.transform.position;
      Vector2 direction = Vector2.right;
      float distance = 0.2f;
      RaycastHit2D ray = Physics2D.Raycast(origin, direction, distance, blue_mask);

      if(ray && animator.GetBool("attack")){
        Debug.Log("Got a hit " + ray.collider.name);
        Destroy(ray.collider.gameObject);
        //ray.collider.gameObject.transform.rotation = Quaternion.Euler(0.0f, 0.0f, -90f);
      }


    }//end Update()


}//end player_move definition
