precision mediump float;

uniform vec3 u_Light;
uniform vec3 u_Camera;
uniform int u_Select;
uniform sampler2D u_TextureUnit;

varying vec3 v_Normal;
varying vec3 v_Vertex;
varying vec3 v_Color;
varying vec2 v_Texture;

void main()
{
    vec3 n_Normal = normalize(v_Normal);
    vec3 lightvector = normalize(u_Light - v_Vertex);
    vec3 lookvector = normalize(u_Camera - v_Vertex);

    float ambient=0.0;
    float k_diffuse=1.0;
    float k_specular=1.0;

    float diffuse = max( dot ( n_Normal, lightvector ), 0.0 )*k_diffuse;
    vec3 reflectvector = reflect(-lightvector, n_Normal);
    float specular = k_specular * pow( max(dot(lookvector, reflectvector), 0.0), 40.0 );

    vec4 one = vec4(1.0, 1.0, 1.0, 1.0);//texture2D(u_TextureUnit, v_Texture);
    vec4 color = texture2D(u_TextureUnit, v_Texture);//vec4(v_Color, 1.0);
    if (u_Select == 1) {
        color = mix(color, 1.5 * one, 0.2);
    }
    gl_FragColor = mix(color, (ambient + diffuse + specular)*one, 0.2);
}