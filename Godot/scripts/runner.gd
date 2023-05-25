extends CharacterBody3D


@export var speed : float = 5.0
@export var jump_speed : float = 4.5
@export var hud : TextEdit


var gravity = ProjectSettings.get_setting("physics/3d/default_gravity")
var is_running = false
var is_jumping = false
#var score : int = 0
var already_counted : bool = false
var timer : Timer = Timer.new()
#var seconds : int = 0
var clock : String = "0:00:00"

var stats : Dictionary = {"running": {"score": 0, "seconds": 0}, "rowing": {"score": 0, "seconds": 0}, "biking": {"score": 0, "seconds": 0}}

@onready var camera_mount := $camera_mount
@onready var camera := $camera_mount/camera
@onready var mesh := $mesh
@onready var anim_tree := $mesh/AnimationTree
@onready var raycast := $RayCast3D



func _ready():
	timer.connect("timeout", update_clock)
	timer.wait_time = 1
	timer.one_shot = false
	add_child(timer)
	timer.start()
	
	load_stats()
	
## end _ready()


func load_stats():
	if FileAccess.file_exists("user://stats.json"):
		var file = FileAccess.open("user://stats.json", FileAccess.READ)
		var contents = JSON.parse_string(file.get_as_text())
		if(contents is Dictionary):
			stats = contents
			stats["running"]["score"] = int(contents["running"]["score"])
			stats["running"]["seconds"] = int(contents["running"]["seconds"])
			stats["rowing"]["score"] = int(contents["rowing"]["score"])
			stats["rowing"]["seconds"] = int(contents["rowing"]["seconds"])
			stats["biking"]["score"] = int(contents["biking"]["score"])
			stats["biking"]["seconds"] = int(contents["biking"]["seconds"])
			update_clock()
			update_hud()
	else:
		pass
	
## end load_stats()



func _unhandled_input(event : InputEvent):
	if event is InputEventMouseButton:
		Input.set_mouse_mode(Input.MOUSE_MODE_CAPTURED)
	elif event.is_action_pressed("ui_cancel"):
		Input.set_mouse_mode(Input.MOUSE_MODE_VISIBLE)
		
	if Input.get_mouse_mode() == Input.MOUSE_MODE_CAPTURED:
		if event is InputEventMouseMotion:
			camera_mount.rotate_y(-event.relative.x * 0.01)
			camera.rotate_x(-event.relative.y * 0.01)
			camera.rotation.x = clamp(camera.rotation.x, deg_to_rad(-30), deg_to_rad(60))
			
## end _unhandled_input()




func _physics_process(delta):
	update_animation_parameters()
	move_controls(delta)
	move_and_slide()
	check_hurdle()
	update_hud()
	if Input.is_action_pressed("ui_page_down"):
		next_level()
## end _physics_process()




func move_controls(delta):
	if not is_on_floor():
		velocity.y -= gravity * delta
	else:
		is_jumping = false
	if Input.is_action_just_pressed("jump") and is_on_floor():
		velocity.y = jump_speed
		is_jumping = true
	var z_direction : float = 0;
	if Input.is_action_pressed("forward"):
		z_direction = speed;
	if Input.is_action_pressed("backward"):
		z_direction = -speed;
	if Input.is_action_pressed("turn_left"):
		rotate_y(deg_to_rad(35) * delta)
	if Input.is_action_pressed("turn_right"):
		rotate_y(deg_to_rad(-35) * delta)
	var direction = (transform.basis * Vector3(0, 0, z_direction)).normalized()
	if direction:
		velocity.x = direction.x * speed
		velocity.z = direction.z * speed
	else:
		velocity.x = move_toward(velocity.x, 0, speed)
		velocity.z = move_toward(velocity.z, 0, speed)
	if direction != Vector3.ZERO:
		is_running = true
	else:
		is_running = false
		
## end move_controls()


func update_animation_parameters():
	if(is_running and not is_jumping): ##this may not be the best thing to check here, hurdle animation???
		anim_tree["parameters/conditions/is_idle"] = false
		anim_tree["parameters/conditions/is_running"] = true
		anim_tree["parameters/conditions/is_jumping"] = false
	elif(is_jumping):
		anim_tree["parameters/conditions/is_idle"] = false
		anim_tree["parameters/conditions/is_running"] = false
		anim_tree["parameters/conditions/is_jumping"] = true
	else:
		anim_tree["parameters/conditions/is_idle"] = true
		anim_tree["parameters/conditions/is_running"] = false
		anim_tree["parameters/conditions/is_jumping"] = false
		
## end update_animation_parameters()



func check_hurdle():
	if not is_on_floor() && raycast.get_collider() != null && not already_counted:
		stats["running"]["score"] += 1
		already_counted = true
	if is_on_floor() && already_counted:
		already_counted = false

## end check_hurdle()



func update_clock():
	stats["running"]["seconds"] += 1
	var hours : int = round(stats["running"]["seconds"] / 3600)
	var remaining : int = stats["running"]["seconds"] % 3600
	var minutes : int = round(remaining / 60)
	remaining %= 60
	clock = str(hours) + ":" + str("%02d" % minutes) + ":" + str("%02d" % remaining)
	
## end update_clock()


func update_hud():
	var current : String = "Score: " + str(stats["running"]["score"]) + "\nTime: " + clock
	hud.text = current
	
## end update_hud()



func next_level():
	var file := FileAccess.open("user://stats.json", FileAccess.WRITE)
	file.store_string(str(stats)) ## writes to a file at C:\Users\franc\AppData\Roaming\Godot\app_userdata\triathalon
	get_tree().change_scene_to_file("res://levels/rowing_event.tscn")
	
## end next_level()





























#################  WHITE SPACE FOR SCROLLING  ####################
