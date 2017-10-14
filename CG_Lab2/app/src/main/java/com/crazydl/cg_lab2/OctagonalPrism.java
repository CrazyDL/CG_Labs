package com.crazydl.cg_lab2;

public class OctagonalPrism {
    public class Vertrex{
        private float x, y, z;

        public Vertrex(float x, float y, float z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public void setX(float x) {
            this.x = x;
        }

        public void setY(float y) {
            this.y = y;
        }

        public void setZ(float z) {
            this.z = z;
        }

        public float getX() {
            return x;
        }

        public float getY() {
            return y;
        }

        public float getZ() {
            return z;
        }
    }

    static final int EDGES = 8;
    private float radius = 100, edgeLen = 2 * radius * (float)Math.sin(Math.PI / 8);
    private Vertrex[][] vertexes = new Vertrex[2][EDGES];

    public OctagonalPrism() {
        float sinAlph = radius * (float)Math.sin(Math.PI / 4);
        float cosAlph = radius * (float)Math.cos(Math.PI / 4);
        vertexes[0][0] = new Vertrex(radius, -radius, 0);
        vertexes[1][0] = new Vertrex(radius, radius, 0);

        vertexes[0][0] = new Vertrex(cosAlph, -radius, -sinAlph);
        vertexes[1][0] = new Vertrex(cosAlph, radius, -sinAlph);

        vertexes[0][0] = new Vertrex(0, -radius, -radius);
        vertexes[1][0] = new Vertrex(0, radius, -radius);

        vertexes[0][0] = new Vertrex(-cosAlph, -radius, -sinAlph);
        vertexes[1][0] = new Vertrex(-cosAlph, radius, -sinAlph);

        vertexes[0][0] = new Vertrex(-radius, -radius, 0);
        vertexes[1][0] = new Vertrex(-radius, radius, 0);

        vertexes[0][0] = new Vertrex(-cosAlph, -radius, sinAlph);
        vertexes[1][0] = new Vertrex(-cosAlph, radius, sinAlph);

        vertexes[0][0] = new Vertrex(0, -radius, radius);
        vertexes[1][0] = new Vertrex(0, radius, radius);

        vertexes[0][0] = new Vertrex(cosAlph, -radius, sinAlph);
        vertexes[1][0] = new Vertrex(cosAlph, radius, sinAlph);
    }
}
