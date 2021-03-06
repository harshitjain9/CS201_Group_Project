package com.example.demo.KD;

import java.util.ArrayList;
import java.util.List;

import com.example.demo.Business;
import com.example.demo.Distance;
public class KDTree
{
    public KDNode Root;
 
    int TimeStart, TimeFinish;
    int CounterFreq;
 
    double d_min;
    KDNode nearest_neighbour;
 
    int KD_id;
 
    int nList;
 
    KDNode CheckedNodes[];
    int checked_nodes;
    KDNode List[];
 
    double x_min[], x_max[];
    boolean max_boundary[], min_boundary[];
    int n_boundary;
 
    public KDTree(int i)
    {
        Root = null;
        KD_id = 1;
        nList = 0;
        List = new KDNode[i];
        CheckedNodes = new KDNode[i];
        max_boundary = new boolean[2];
        min_boundary = new boolean[2];
        x_min = new double[2];
        x_max = new double[2];
    }
 
    public boolean add(Business business)
    {
        if (nList >= 2000000 - 1)
            return false; // can't add more points
 
        if (Root == null)
        {
            Root = new KDNode(business, 0);
            Root.id = KD_id++;
            List[nList++] = Root;
        } else
        {
            KDNode pNode;
            if ((pNode = Root.Insert(business)) != null)
            {
                pNode.id = KD_id++;
                List[nList++] = pNode;
            }
        }
        return true;
    }
 
    public KDNode find_nearest(Business x, double inputLatitude, double inputLongitude, double inputRadius)
    {
        if (Root == null)
            return null;
 
        checked_nodes = 0;
        KDNode parent = Root.FindParent(x);
        nearest_neighbour = parent;
        d_min = Root.distance(x.getCoordinates()[0], x.getCoordinates()[1], inputLatitude, inputLongitude);
 
        if (parent.equal(x, parent.x, 2) == true)
            return nearest_neighbour;
 
        search_parent(parent, x);
        uncheck();
 
        return nearest_neighbour;
    }

    public ArrayList<KDNode> find_nearest_list(Business x, double inputLatitude, double inputLongitude, double inputRadius)
    {
        if (Root == null)
            return null;

        ArrayList<KDNode> list = new ArrayList<KDNode>();
 
        checked_nodes = 0;
        KDNode parent = Root.FindParent(x);
        nearest_neighbour = parent;
        double dist = Root.distance(x.getCoordinates()[0], x.getCoordinates()[1], inputLatitude, inputLongitude);
        if (dist < d_min) {
            d_min = dist;
        }
 
        if (dist < inputRadius) {
            list.add(nearest_neighbour);
        }
 
        search_parent(parent, x);
        uncheck();
 
        return list;
    }

    


    public double find_nearest_distance() {
        return d_min;
    }
 
    public void check_subtree(KDNode node, Business x)
    {
        if ((node == null) || node.checked)
            return;
 
        CheckedNodes[checked_nodes++] = node;
        node.checked = true;
        set_bounding_cube(node, x);
 
        int dim = node.axis;
        double d = node.x.getCoordinates()[dim] - x.getCoordinates()[dim];
 
        if (d * d > d_min)
        {
            if (node.x.getCoordinates()[dim] > x.getCoordinates()[dim])
                check_subtree(node.Left, x);
            else
                check_subtree(node.Right, x);
        } else
        {
            check_subtree(node.Left, x);
            check_subtree(node.Right, x);
        }
    }
 
    public void set_bounding_cube(KDNode node, Business x)
    {
        if (node == null)
            return;
        int d = 0;
        double dx;
        for (int k = 0; k < 2; k++)
        {
            dx = node.x.getCoordinates()[k] - x.getCoordinates()[k];
            if (dx > 0)
            {
                dx *= dx;
                if (!max_boundary[k])
                {
                    if (dx > x_max[k])
                        x_max[k] = dx;
                    if (x_max[k] > d_min)
                    {
                        max_boundary[k] = true;
                        n_boundary++;
                    }
                }
            } else
            {
                dx *= dx;
                if (!min_boundary[k])
                {
                    if (dx > x_min[k])
                        x_min[k] = dx;
                    if (x_min[k] > d_min)
                    {
                        min_boundary[k] = true;
                        n_boundary++;
                    }
                }
            }
            d += dx;
            if (d > d_min)
                return;
 
        }
 
        if (d < d_min)
        {
            d_min = d;
            nearest_neighbour = node;
        }
    }
 
    public KDNode search_parent(KDNode parent, Business x)
    {
        for (int k = 0; k < 2; k++)
        {
            x_min[k] = x_max[k] = 0;
            max_boundary[k] = min_boundary[k] = false; //
        }
        n_boundary = 0;
 
        KDNode search_root = parent;
        while (parent != null && (n_boundary != 2 * 2))
        {
            check_subtree(parent, x);
            search_root = parent;
            parent = parent.Parent;
        }
 
        return search_root;
    }
 
    public void uncheck()
    {
        for (int n = 0; n < checked_nodes; n++)
            CheckedNodes[n].checked = false;
    }

    public List<KDNode> searchKdTree(final double[] query, final double cut, final int depth, KDNode node) {    
		final int p = depth % node.x.getCoordinates().length;
		List<KDNode> result = new ArrayList<KDNode>();
		boolean inside = true;
		for (int i = 0; i < node.x.getCoordinates().length; i++) {
			if (Math.abs(query[i] - node.x.getCoordinates()[i]) > cut) {
				inside = false;
				break;
			}
		}
		if (inside) {
			result.add(node);
		}
		if (node.Left != null) {
			if ( (query[p] - cut) <= node.Left.x.getCoordinates()[p] ) {
				result.addAll( searchKdTree(query, cut, depth + 1, node.Left) );
			}
		}
		if (node.Right != null) {
			if ( (query[p] + cut) >= node.Right.x.getCoordinates()[p] ) {
				result.addAll( searchKdTree(query, cut, depth + 1, node.Right) );
			}
		}
		return result;
	}
 
}
