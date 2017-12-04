attribute vec4 a_Position;
attribute vec3 a_Normal;
attribute vec3 a_Color;
attribute vec2 a_Texture;

uniform mat4 u_Matrix;
uniform mat4 u_ModelMatrix;

varying vec3 v_Vertex;
varying vec3 v_Normal;
varying vec3 v_Color;
varying vec2 v_Texture;

void main()
{
    vec4 umn = a_Position;
    v_Vertex = (u_ModelMatrix * umn).xyz;
    gl_Position = u_Matrix * umn;
    vec3 n_Normal = normalize(a_Normal);
    v_Normal = (u_ModelMatrix * vec4(n_Normal, 1.0)).xyz;
    v_Color = a_Color;
    v_Texture = a_Texture;
}