shader_type spatial;

uniform vec3 water_color : source_color;
uniform float water_metallic : hint_range(0.0, 1.0) = 0;
uniform float water_roughness : hint_range(0.0, 1.0) = 0.02;

uniform sampler2D normal_1;
uniform sampler2D normal_2;

uniform vec2 wave_1 = vec2(2.0, 0.0);
uniform vec2 wave_2 = vec2(0.0, 0.1);

uniform float time_scale : hint_range(0.0, 0.2, 0.005) = 0.025;

void fragment() {
	vec2 time_1 = (TIME * wave_1) * time_scale;
	vec2 time_2 = (TIME * wave_2) * time_scale;
	
	vec3 normal = mix(texture(normal_1, UV + time_1).rgb, texture(normal_2, UV + time_2).rgb, 0.7);
	
	ALBEDO = water_color;
	METALLIC = water_metallic;
	ROUGHNESS = water_roughness;
	ALPHA = 0.70;
	NORMAL_MAP = normal;
}
