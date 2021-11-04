package com.example.demo.KD;

import com.example.demo.Business;

public class KDNode
{
    int axis;
    public Business x;
    int id;
    boolean checked;
    boolean orientation;
 
    KDNode Parent;
    KDNode Left;
    KDNode Right;
 
    public KDNode(Business x0, int axis0)
    {
        x = new Business();
        axis = axis0;
        x = x0;
        Left = Right = Parent = null;
        checked = false;
        id = 0;
    }
 
    public KDNode FindParent(Business x0)
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
 
    public KDNode Insert(Business p)
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
 
    boolean equal(Business x1, Business x2, int dim)
    {
        if (!(x1.getAddr().equals(x2.getAddr())) || !(x1.getName().equals(x2.getName())) || (x1.getLatitude() != x2.getLatitude()) || x1.getLongitude() != x2.getLongitude()) {
            return true;
        }
        return false;
 
        
    }
 
    double distance2(Business x1, Business x2, int dim)
    {
        double S = 0;
        S += (x1.getLatitude() - x2.getLatitude()) * (x1.getLatitude() - x2.getLatitude());
        S += (x1.getLongitude() - x2.getLongitude()) * (x1.getLongitude() - x2.getLongitude()));
        return S;
    }
}
