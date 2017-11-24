package com.crazydl.cg_lab4;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

class DrawHelper {
    private static final float COLOR_R = 0;
    private static final float COLOR_G = 0.78f;
    private static final float COLOR_B = 1;
    private static float xLightPosition = 0;
    private static float yLightPosition = 0;
    private static float zLightPosition = 5f;

    private int mPositionHandle;
    private int mNormalHandle;
    private int mColorHandle;
    private int muMVPMatrixHandle;
    private int mLightPositionHandle;
    private final int mProgram;

    int getMuMVPMatrixHandle() {
        return muMVPMatrixHandle;
    }

    private final String vertexShaderCode =
            "uniform mat4 u_modelViewProjectionMatrix;"+
                    "attribute vec3 a_vertex;"+
                    "attribute vec3 a_normal;"+
                    "uniform vec4 a_color;"+
                    "varying vec3 v_vertex;"+
                    "varying vec3 v_normal;"+
                    "varying vec4 v_color;"+
                    "void main() {"+
                    "v_vertex=a_vertex;"+
                    "vec3 n_normal=normalize(a_normal);"+
                    "v_normal=n_normal;"+
                    "v_color=a_color;"+
                    "gl_Position = u_modelViewProjectionMatrix * vec4(a_vertex,1.0);"+
                    "}";

    private final String fragmentShaderCode =
            "precision mediump float;"+

                    "uniform vec3 u_lightPosition;"+
                    "varying vec3 v_vertex;"+
                    "varying vec3 v_normal;"+
                    "varying vec4 v_color;"+
                    "void main() {"+
                    "vec3 n_normal=normalize(v_normal);"+
                    "vec3 lightvector = normalize(u_lightPosition - v_vertex);"+
                    "float ambient=0.3;"+
                    "float k_diffuse=0.8;"+
                    "float diffuse = k_diffuse * max(dot(n_normal, lightvector), 0.0);"+
                    "vec4 one=vec4(1.0,1.0,1.0,1.0);"+
                    "vec4 lightColor = (ambient+diffuse)*one;"+
                    "gl_FragColor = mix(lightColor,v_color, 0.6);"+
                    "}";

    private int loadShader(int type, String shaderCode) {
        int shader = GLES20.glCreateShader(type);

        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        return shader;
    }

    DrawHelper() {
        int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
        int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);

        mProgram = GLES20.glCreateProgram();

        GLES20.glAttachShader(mProgram, vertexShader);
        GLES20.glAttachShader(mProgram, fragmentShader);
        GLES20.glLinkProgram(mProgram);

        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "a_vertex");
        mNormalHandle = GLES20.glGetAttribLocation(mProgram, "a_normal");
        mColorHandle = GLES20.glGetUniformLocation(mProgram, "a_color");
        muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "u_modelViewProjectionMatrix");
        mLightPositionHandle = GLES20.glGetUniformLocation(mProgram, "u_lightPosition");
    }


    void draw(float[] trianglesCoords, float[] normalCoords) {
        int COORDS_PER_VERTEX = 3;
        int vertexCount = trianglesCoords.length / COORDS_PER_VERTEX;

        ByteBuffer bbv = ByteBuffer.allocateDirect(trianglesCoords.length * 4);
        bbv.order(ByteOrder.nativeOrder());
        FloatBuffer vertexBuffer = bbv.asFloatBuffer();
        vertexBuffer.put(trianglesCoords);
        vertexBuffer.position(0);

        ByteBuffer bbn = ByteBuffer.allocateDirect(normalCoords.length * 4);
        bbn.order(ByteOrder.nativeOrder());
        FloatBuffer normalBuffer = bbn.asFloatBuffer();
        normalBuffer.put(normalCoords);
        normalBuffer.position(0);

        GLES20.glUseProgram(mProgram);

        GLES20.glEnableVertexAttribArray(mPositionHandle);
        GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                0, vertexBuffer);

        GLES20.glEnableVertexAttribArray(mNormalHandle);
        GLES20.glVertexAttribPointer(mNormalHandle, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                0,normalBuffer);

        GLES20.glUniform4f(mColorHandle, COLOR_R, COLOR_G, COLOR_B, 1f);

        GLES20.glUniform3f(mLightPositionHandle, xLightPosition, yLightPosition, zLightPosition);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount);
    }
}