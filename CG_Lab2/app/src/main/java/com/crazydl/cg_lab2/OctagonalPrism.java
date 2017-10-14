package com.crazydl.cg_lab2;

public class OctagonalPrism {
    public class Vertex {
        private float x, y, z;

        public Vertex(float x, float y, float z) {
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
    public float radius = 200, edgeLen = 2 * radius * (float)Math.sin(Math.PI / 8);
    public Vertex[][] vrts;

    public OctagonalPrism() {
        vrts = new Vertex[2][EDGES];

        float sinAlph = radius * (float)Math.sin(Math.PI / 4);
        float cosAlph = radius * (float)Math.cos(Math.PI / 4);
        vrts[0][0] = new Vertex(radius, -radius, 0);
        vrts[1][0] = new Vertex(radius, radius, 0);

        vrts[0][1] = new Vertex(cosAlph, -radius, -sinAlph);
        vrts[1][1] = new Vertex(cosAlph, radius, -sinAlph);

        vrts[0][2] = new Vertex(0, -radius, -radius);
        vrts[1][2] = new Vertex(0, radius, -radius);

        vrts[0][3] = new Vertex(-cosAlph, -radius, -sinAlph);
        vrts[1][3] = new Vertex(-cosAlph, radius, -sinAlph);

        vrts[0][4] = new Vertex(-radius, -radius, 0);
        vrts[1][4] = new Vertex(-radius, radius, 0);

        vrts[0][5] = new Vertex(-cosAlph, -radius, sinAlph);
        vrts[1][5] = new Vertex(-cosAlph, radius, sinAlph);

        vrts[0][6] = new Vertex(0, -radius, radius);
        vrts[1][6] = new Vertex(0, radius, radius);

        vrts[0][7] = new Vertex(cosAlph, -radius, sinAlph);
        vrts[1][7] = new Vertex(cosAlph, radius, sinAlph);
    }
}
