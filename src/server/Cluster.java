package server;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class Cluster implements Serializable{

	private static final long serialVersionUID = 1L;
	private Tuple centroid;
	private Set<Integer> clusteredData; 

	Cluster(Tuple centroid){
		this.centroid = centroid;
		clusteredData = new HashSet<Integer>();
		
	}
		
	Tuple getCentroid(){
		return centroid;
	}
	
	void computeCentroid(Data data){
		for(int i=0;i<centroid.getLength();i++){
			centroid.getItem(i).update(data, clusteredData);
		}
		
	}
	//return true if the tuple is changing cluster
	boolean addData(int id){
		return clusteredData.add(id);
	}
	
	//verifica se una transazione è clusterizzata nell'array corrente
	boolean contain(int id){
		return clusteredData.contains(id);
	}
	

	//remove the tuplethat has changed the cluster
	void removeTuple(int id){
		clusteredData.remove(id);
	}
	
	public String toString(){
		String str="Centroid=(";
		for(int i=0;i<centroid.getLength();i++)
			str+=centroid.getItem(i);
		str+=")\n";
		return str;
	}
	
	public String toString(Data data){
		String str="Centroid=(";
		for(int i=0;i<centroid.getLength();i++)
			str+=centroid.getItem(i)+ " ";
		str+=")\nExamples:\n";
		int array[] = new int[clusteredData.size()];
		int k = 0;
		for(Object o : clusteredData.toArray()) {
			array[k++] = Integer.parseInt(o.toString());
		}		
		for(int i=0;i<array.length;i++){
			str+="[";
			for(int j=0;j<data.getNumberOfExplanatoryAttribute();j++)
				str+=data.getAttributeValue(array[i], j)+" ";
			str+="] dist = "+getCentroid().getDistance(data.getItemSet(array[i]))+"\n";

		}
		str+="AvgDistance="+getCentroid().avgDistance(data, array) + "\n";
		return str;
	}

}
