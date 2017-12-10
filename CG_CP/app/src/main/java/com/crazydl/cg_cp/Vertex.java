package com.crazydl.cg_cp;
/*
 *  Author: Denis Levshtanov
 *  8O-308b
 */
public class Vertex {
    private float x, y, z;

    Vertex(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    float getX() {
        return x;
    }

    float getZ() {
        return z;
    }

    float getY() {
        return y;
    }
}
