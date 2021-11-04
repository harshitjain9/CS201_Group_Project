package com.example.demo.KD;

public class KDNode
{
    int axis;
    public double[] x;
    int id;
    boolean checked;
    boolean orientation;
 
    KDNode Parent;
    KDNode Left;
    KDNode Right;
 
    public KDNode(double[] x0, int axis0)
    {
        x = new double[2];
        axis = axis0;
        for (int k = 0; k < 2; k++)
            x[k] = x0[k];
 
        Left = Right = Parent = null;
        checked = false;
        id = 0;
    }
 
    public KDNode FindParent(double[] x0)
    {
        KDNode parent = null;
        KDNode next = this;
        int split;
        while (next != null)
        {
            split = next.axis;
            parent = next;
            if (x0[split] > next.x[split])
                next = next.Right;
            else
                next = next.Left;
        }
        return parent;
    }
 
    public KDNode Insert(double[] p)
    {
        //x = new double[2];
        KDNode parent = FindParent(p);
        if (equal(p, parent.x, 2) == true)
            return null;
 
        KDNode newNode = new KDNode(p, parent.axis + 1 < 2 ? parent.axis + 1
                : 0);
        newNode.Parent = parent;
 
        if (p[parent.axis] > parent.x[parent.axis])
        {
            parent.Right = newNode;
            newNode.orientation = true; //
        } else
        {
            parent.Left = newNode;
            newNode.orientation = false; //
        }
 
        return newNode;
    }
 
    boolean equal(double[] x1, double[] x2, int dim)
    {
        for (int k = 0; k < dim; k++)
        {
            if (x1[k] != x2[k])
                return false;
        }
 
        return true;
    }
 
    double distance2(double[] x1, double[] x2, int dim)
    {
        double S = 0;
        for (int k = 0; k < dim; k++)
            S += (x1[k] - x2[k]) * (x1[k] - x2[k]);
        return S;
    }
}
