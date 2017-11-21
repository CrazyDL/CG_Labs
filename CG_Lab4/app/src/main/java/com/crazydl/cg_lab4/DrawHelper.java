package com.crazydl.cg_lab4;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

public class DrawHelper {
    private final int COORDS_PER_VERTEX = 3;
    private final int mProgram;
    private FloatBuffer vertexBuffer;
    private ShortBuffer drawListBuffer;

    private int mPositionHandle;
    private int mColorHandle;
    private int muMVPMatrixHandle;

    public int getMuMVPMatrixHandle() {
        return muMVPMatrixHandle;
    }

    private final String vertexShaderCode =
            "uniform mat4 uMVPMatrix;   \n" +

            "attribute vec4 vPosition;  \n" +
            "void main(){               \n" +
            " gl_Position = uMVPMatrix * vPosition; \n" +

            "}  \n";

    private final String fragmentShaderCode =
            "precision mediump float;" +
                    "uniform vec4 vColor;" +
                    "void main() {" +
                    "  gl_FragColor = vColor;" +
                    "}";

    private int loadShader(int type, String shaderCode) {
        int shader = GLES20.glCreateShader(type);

        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        return shader;
    }

    public DrawHelper() {
        int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
        int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);

        mProgram = GLES20.glCreateProgram();

        GLES20.glAttachShader(mProgram, vertexShader);
        GLES20.glAttachShader(mProgram, fragmentShader);
        GLES20.glLinkProgram(mProgram);

        muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
        mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");
    }


    public void draw(GL10 gl, float[] trianglesCoords) {
        int vertexCount = trianglesCoords.length / COORDS_PER_VERTEX;
        ByteBuffer bb = ByteBuffer.allocateDirect(trianglesCoords.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(trianglesCoords);
        vertexBuffer.position(0);

        GLES20.glUseProgram(mProgram);
        GLES20.glEnableVertexAttribArray(mPositionHandle);
        GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                0, vertexBuffer);

        GLES20.glUniform4f(mColorHandle, 0, 0.7f, 1f, 1f);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount);
    }
}