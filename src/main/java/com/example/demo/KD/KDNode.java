package com.example.demo.KD;

import com.example.demo.Business;
import com.example.demo.Distance;

import java.util.List;
import java.util.ArrayList;

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
            if (x0.getCoordinates()[split] > next.x.getCoordinates()[split])
                next = next.Right;
            else
                next = next.Left;
        }
        return parent;
    }
 
    public KDNode Insert(Business p)
    {
        KDNode parent = FindParent(p);
        if (equal(p, parent.x, 2) == true)
            return null;
 
        KDNode newNode = new KDNode(p, parent.axis + 1 < 2 ? parent.axis + 1
                : 0);
        newNode.Parent = parent;
 
        if (p.getCoordinates()[parent.axis] > parent.x.getCoordinates()[parent.axis])
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

        double x1Lat = x1.getCoordinates()[0];
        double x1Long = x1.getCoordinates()[1];
        double x2Lat = x2.getCoordinates()[0];
        double x2Long = x2.getCoordinates()[1];
        if (!(x1.getAddr().equals(x2.getAddr())) || !(x1.getName().equals(x2.getName())) || (x1Lat != x2Lat) || x1Long != x2Long) {
            return true;
        }
        return false;
 
        
    }
 
    double distance2(Business x1, Business x2, int dim)
    {
        double S = 0;
        double x1Lat = x1.getCoordinates()[0];
        double x1Long = x1.getCoordinates()[1];
        double x2Lat = x2.getCoordinates()[0];
        double x2Long = x2.getCoordinates()[1];
        S += (x1Lat - x2Lat) * (x1Lat - x2Lat);
        S += (x1Long - x2Long) * (x1Long - x2Long);
        return S;
    }

    public double distance(double lat1, double lon1, double lat2, double lon2) {
        return Math.max(Math.abs(lat1 - lat2), Math.abs(lon1 - lon2));
	}
}
