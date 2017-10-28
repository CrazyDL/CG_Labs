package com.crazydl.cg_lab2;
/*
 *  Author: Denis Levshtanov
 *  8O-308b
 */
public class MyMatrix4 {
    private float[] values;

    MyMatrix4(float[] values) {
        this.values = values;
    }

    MyMatrix4 multiply(MyMatrix4 other) {
        float[] result = new float[16];
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                for (int i = 0; i < 4; i++) {
                    result[row * 4 + col] += this.values[row * 4 + i] * other.values[i * 4 + col];
                }
            }
        }
        return new MyMatrix4(result);
    }

    float[][] multiply(float[][] other) {
        float[][] result = new float[4][10];
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 10; col++) {
                for (int i = 0; i < 4; i++) {
                    result[row][col] += this.values[row * 4 + i] * other[i][col];
                }
            }
        }
        return result;
    }

    OctagonalPrism.Vertex transform(OctagonalPrism.Vertex in) {
        return new OctagonalPrism.Vertex(in.getX() * values[0] + in.getY() * values[4] + in.getZ() * values[8],
                in.getX() * values[1] + in.getY() * values[5] + in.getZ() * values[9],
                in.getX() * values[2] + in.getY() * values[6] + in.getZ() * values[10]);
    }
}
