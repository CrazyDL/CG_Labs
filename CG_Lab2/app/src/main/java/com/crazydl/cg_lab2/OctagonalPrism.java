package com.crazydl.cg_lab2;
/*
 *  Author: Denis Levshtanov
 *  8O-308b
 */
public class OctagonalPrism {
    static public class Vertex {
        private float x, y, z;

        public Vertex(float x, float y, float z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public float getX() {
            return x;
        }

        public float getZ() {
            return z;
        }

        public float getY() {
            return y;

        }
    }

    static final int EDGES = 8;
    public float radius = 200;
    public float h = radius * (float)Math.cos(Math.PI / 8);
    public float edLenH = radius * (float)Math.sin(Math.PI / 8);

    public Vertex[][] vrts;
    public float[][] planes = {{-1/h, -1/(h + edLenH) , 0, 1/(h + edLenH), 1/h, 1/(h + edLenH), 0, -1/(h + edLenH), 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 1/radius, -1/radius},
            {0, 1/(h + edLenH), 1/h, 1/(h + edLenH), 0, -1/(h + edLenH), -1/h, -1/(h + edLenH), 0, 0},
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1}};

    public OctagonalPrism() {
        vrts = new Vertex[2][EDGES];
        /*Matrix4f mtx = new Matrix4f(new float[]{-1/h, -1/(h + edLenH) , 1/h, 1/(h + edLenH), 1/h, 1/(h + edLenH), -1/h, -1/(h + edLenH), 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 1/radius, -1/radius,
                0, 1/(h + edLenH), 0, 1/(h + edLenH), 0, -1/(h + edLenH), 0, -1/(h + edLenH), 0, 0,
                1, 1, 1, 1, 1, 1, 1, 1, 1, 1});
        Matrix4f vrtx = new Matrix4f(new float[]{h, -radius, -edLenH, 0,
                h, radius, -edLenH, 0,
                edLenH, -radius, -h, 0,
                edLenH, radius, -h, 0,
                -edLenH, -radius, -h, 0,
                -edLenH, radius, -h, 0,
                -h, -radius, -edLenH, 0,
                -h, radius, -edLenH, 0,
                -h, -radius, edLenH, 0,
                -h, radius, edLenH, 0,
                -edLenH, -radius, h, 0,
                -edLenH, radius, h, 0,
                edLenH, -radius, h, 0,
                edLenH, radius, h, 0,
                h, -radius, edLenH, 0,
                h, radius, edLenH, 0,});

        vrtx.multiply(mtx);*/
        float sinAlph = radius * (float)Math.sin(Math.PI / 4);
        float cosAlph = radius * (float)Math.cos(Math.PI / 4);
        vrts[0][0] = new Vertex(h, -radius, -edLenH);
        vrts[1][0] = new Vertex(h, radius, -edLenH);

        vrts[0][1] = new Vertex(edLenH, -radius, -h);
        vrts[1][1] = new Vertex(edLenH, radius, -h);

        vrts[0][2] = new Vertex(-edLenH, -radius, -h);
        vrts[1][2] = new Vertex(-edLenH, radius, -h);

        vrts[0][3] = new Vertex(-h, -radius, -edLenH);
        vrts[1][3] = new Vertex(-h, radius, -edLenH);

        vrts[0][4] = new Vertex(-h, -radius, edLenH);
        vrts[1][4] = new Vertex(-h, radius, edLenH);

        vrts[0][5] = new Vertex(-edLenH, -radius, h);
        vrts[1][5] = new Vertex(-edLenH, radius, h);

        vrts[0][6] = new Vertex(edLenH, -radius, h);
        vrts[1][6] = new Vertex(edLenH, radius, h);

        vrts[0][7] = new Vertex(h, -radius, edLenH);
        vrts[1][7] = new Vertex(h, radius, edLenH);
    }
}
