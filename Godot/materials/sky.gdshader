shader_type sky;

uniform vec3 skycolor : source_color;
uniform vec3 horizon : source_color;
uniform vec3 cloud_color: source_color;

uniform sampler2D normal;

void sky() {
	float fade = clamp(EYEDIR.y/0.7, 0.0, 1.0);
	vec3 color = mix(horizon, skycolor, vec3(fade));
	vec3 clouds = texture(normal, SKY_COORDS).rgb;
	vec3 final = mix(color, clouds, 0.5);
	
	COLOR = final;
}
