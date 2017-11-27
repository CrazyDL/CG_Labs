package com.crazydl.cg_lab6;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

class DrawHelperLight {
    private static final float COLOR_R = 1;
    private static final float COLOR_G = 1;
    private static final float COLOR_B = 0;

    private int mPositionHandle;
    private int mColorHandle;
    private int muMVPMatrixHandle;
    private final int mProgram;

    int getMuMVPMatrixHandle() {
        return muMVPMatrixHandle;
    }

    private final String vertexShaderCode =
            "attribute vec4 a_Position;\n" +
                    "    void main() {\n" +
                    "        gl_Position = a_Position;\n" +
                    "    }";

    private final String fragmentShaderCode =
            "precision mediump float;\n" +
                    "uniform vec4 u_Color;\n" +
                    " \n" +
                    "void main() {\n" +
                    "    gl_FragColor = u_Color;\n" +
                    "}";

    private int loadShader(int type, String shaderCode) {
        int shader = GLES20.glCreateShader(type);

        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        return shader;
    }

    DrawHelperLight(int program) {
        int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
        int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);

        mProgram = GLES20.glCreateProgram();

        GLES20.glAttachShader(mProgram, vertexShader);
        GLES20.glAttachShader(mProgram, fragmentShader);
        GLES20.glLinkProgram(mProgram);

        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "a_Position");
        mColorHandle = GLES20.glGetUniformLocation(mProgram, "u_Color");
        //muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "u_modelViewProjectionMatrix");
    }


    void draw(float[] trianglesCoords) {
        int COORDS_PER_VERTEX = 3;
        int vertexCount = trianglesCoords.length / COORDS_PER_VERTEX;

        ByteBuffer bbv = ByteBuffer.allocateDirect(trianglesCoords.length * 4);
        bbv.order(ByteOrder.nativeOrder());
        FloatBuffer vertexBuffer = bbv.asFloatBuffer();
        vertexBuffer.put(trianglesCoords);
        vertexBuffer.position(0);

        GLES20.glUseProgram(mProgram);

        GLES20.glEnableVertexAttribArray(mPositionHandle);
        GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                0, vertexBuffer);


        GLES20.glUniform4f(mColorHandle, COLOR_R, COLOR_G, COLOR_B, 1f);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount);
    }
}