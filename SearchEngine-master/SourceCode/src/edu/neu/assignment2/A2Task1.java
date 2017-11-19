package edu.neu.assignment2;

import edu.neu.download.PropertyFileManager;
import edu.neu.graph.GraphCreater;

public class A2Task1 {

	public static void main(String args[]) {
		new PropertyFileManager();

		GraphCreater g = new GraphCreater();
		System.out.println("------- Assignment 2 Task 1 Started ----------");

		g.createGraph();

		System.out.println("------- Assignment 2 Task 1 Completed ----------");

	}
}
