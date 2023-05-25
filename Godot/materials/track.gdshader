shader_type spatial;

uniform vec3 track_color : source_color;
uniform sampler2D normal1;

void fragment() {
	vec3 normal = texture(normal1, UV).rgb;
	
	ALBEDO = track_color;
	NORMAL_MAP = normal;
}
