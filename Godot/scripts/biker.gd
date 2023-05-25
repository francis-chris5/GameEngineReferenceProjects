extends CharacterBody3D


@export var speed : float = 5.0
@export var hud : TextEdit

var gravity = ProjectSettings.get_setting("physics/3d/default_gravity")
var saw_left = false
var saw_right = false
var is_idle : bool = true
var is_pedaling : bool = false
var timer : Timer = Timer.new()
var gate_timer : Timer = Timer.new()
var clock : String = "0:00:00"

var stats : Dictionary = {"running": {"score": 0, "seconds": 0}, "rowing": {"score": 0, "seconds": 0}, "biking": {"score": 0, "seconds": 0}}

@onready var camera_mount := $camera_mount
@onready var camera := $camera_mount/camera
@onready var anim_tree := $AnimationTree
@onready var right_ray := $right_ray
@onready var left_ray := $left_ray



func _ready():
	randomize()
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
	check_gate()
	update_hud()
	if Input.is_action_pressed("ui_page_up"):
		previous_level()
## end _physics_process()




func update_animation_parameters():
	if is_idle:
		anim_tree["parameters/conditions/is_pedaling"] = false
		anim_tree["parameters/conditions/is_idle"] = true
	elif is_pedaling:
		anim_tree["parameters/conditions/is_pedaling"] = true
		anim_tree["parameters/conditions/is_idle"] = false
		
## end update_animation_parameters()



func check_gate():
	if left_ray.get_collider() != null || right_ray.get_collider() != null:
		if left_ray.get_collider() != null:
			saw_left = true
		if right_ray.get_collider() != null:
			saw_right = true
		gate_timer.connect("timeout", no_pass)
		gate_timer.wait_time = 0.2
		gate_timer.one_shot = true
		add_child(gate_timer)
		gate_timer.start()
	if left_ray.get_collider() != null && right_ray.get_collider() != null:
		stats["biking"]["score"] += 1
		var collider = left_ray.get_collider()
		collider.position = Vector3(randi_range(-45, 45), 12, randi_range(-45, 45))
		collider.rotate_y(randi_range(0, 360))
		saw_left = false
		saw_right = false
	if (left_ray.get_collider() != null && saw_right) || (right_ray.get_collider() != null && saw_left):
		stats["biking"]["score"] += 1
		var collider
		if left_ray.get_collider() != null:
			collider = left_ray.get_collider()
		else:
			collider = right_ray.get_collider()
		collider.position = Vector3(randi_range(-45, 45), 12, randi_range(-45, 45))
		collider.rotate_y(randi_range(0, 360))
		saw_left = false
		saw_right = false
		
## end check_gate()



func no_pass():
	saw_left = false
	saw_right = false
	
## end no_pass()



func move_controls(delta):
	if not is_on_floor():
		velocity.y -= gravity * delta
	var z_direction : float = 0;
	if Input.is_action_pressed("forward"):
		z_direction = -speed;
	if Input.is_action_pressed("backward"):
		z_direction = speed;
	if Input.is_action_pressed("turn_left"):
		rotate_y(deg_to_rad(42) * delta)
	if Input.is_action_pressed("turn_right"):
		rotate_y(deg_to_rad(-42) * delta)
	var direction = (transform.basis * Vector3(0, 0, z_direction)).normalized()
	if direction:
		velocity.x = direction.x * speed
		velocity.z = direction.z * speed
	else:
		velocity.x = move_toward(velocity.x, 0, speed)
		velocity.z = move_toward(velocity.z, 0, speed)
	if velocity != Vector3.ZERO:
		is_pedaling = true
		is_idle = false
	else:
		is_pedaling = false
		is_idle = true
## end move_controls()




func update_clock():
	stats["biking"]["seconds"] += 1
	var hours : int = round(stats["biking"]["seconds"] / 3600)
	var remaining : int = stats["biking"]["seconds"] % 3600
	var minutes : int = round(remaining / 60)
	remaining %= 60
	clock = str(hours) + ":" + str("%02d" % minutes) + ":" + str("%02d" % remaining)
	
## end update_clock()




func update_hud():
	var current : String = "Score: " + str(stats["biking"]["score"]) + "\nTime: " + clock
	hud.text = current
	
## end update_hud()




func previous_level():
	var file := FileAccess.open("user://stats.json", FileAccess.WRITE)
	file.store_string(str(stats)) ## writes to a file at C:\Users\franc\AppData\Roaming\Godot\app_userdata\triathalon
	get_tree().change_scene_to_file("res://levels/rowing_event.tscn")
	
## end next_level()



























###############################  WHITE SPACE FOR SCROLLING  #################
