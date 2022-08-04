using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class computer_movement : MonoBehaviour{

    public float speed;
    private bool faceLeft = true;
    private Rigidbody2D opponent;
    private Animator animator;
    public LayerMask green_mask;

    private int frameCounter = 3000;
    private Vector2 direction;
    private bool tracking = false;
    private GameObject target;

    void Start(){
        opponent = GetComponent<Rigidbody2D>();
        animator = GetComponent<Animator>();
    }//end Start()



    void Update(){
      if(frameCounter > 222){
        animator.SetBool("attack", false);
        frameCounter = 0;

        if(!tracking){
          direction = new Vector2(Random.Range(-1.0f, 1.0f) * speed, Random.Range(-1.0f, 1.0f));
        }
        else{
          Vector2 dir = (target.transform.position - opponent.transform.position).normalized;
          direction = new Vector2(dir.x * speed, dir.y * speed);
        }


        if(direction.x < 0 && !faceLeft){
          faceLeft = true;
        }
        else if(direction.x > 0 && faceLeft){
          faceLeft = false;
        }


        if(faceLeft){
          opponent.transform.localScale *= new Vector2(-1 * opponent.transform.localScale.x, opponent.transform.localScale.y);
        }
        else{
          opponent.transform.localScale *= new Vector2(opponent.transform.localScale.x, opponent.transform.localScale.y);
        }
      }

      opponent.velocity = direction;

      Vector2 checking = Vector2.right;
      if(faceLeft){
        checking = Vector2.left;
      }

      RaycastHit2D spot = Physics2D.Raycast(opponent.transform.position, checking, 2.0f, green_mask);
      if(spot){
        target = spot.collider.gameObject;
        tracking = true;
      }


      RaycastHit2D slap = Physics2D.Raycast(opponent.transform.position, checking, 0.2f, green_mask);
      if(slap && Random.Range(0.0f, 1.0f) < 0.2 ){
        Debug.Log("Blue got a hit...");
        animator.SetBool("attack", true);
        tracking = false;
        frameCounter = 210;
      }

      frameCounter++;
    }//end Update()




}//end comp_move class
