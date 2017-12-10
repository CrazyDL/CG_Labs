package com.crazydl.cg_cp;
/*
 *  Author: Denis Levshtanov
 *  8O-308b
 */
public class MyMatrix3 {
    private float[] values;

    MyMatrix3(float[] values) {
        this.values = values;
    }

    MyMatrix3 multiply(MyMatrix3 other) {
        float[] result = new float[9];
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                for (int i = 0; i < 3; i++) {
                    result[row * 3 + col] += this.values[row * 3 + i] * other.values[i * 3 + col];
                }
            }
        }
        return new MyMatrix3(result);
    }

    Vertex transform(Vertex in) {
        return new Vertex(in.getX() * values[0] + in.getY() * values[3] + in.getZ() * values[6],
                in.getX() * values[1] + in.getY() * values[4] + in.getZ() * values[7],
                in.getX() * values[2] + in.getY() * values[5] + in.getZ() * values[8]);
    }

    Vertex transform(float x, float y, float z) {
        return new Vertex(x * values[0] +y * values[3] + z * values[6],
                x * values[1] + y * values[4] + z * values[7],
                x * values[2] + y * values[5] + z * values[8]);
    }
}
