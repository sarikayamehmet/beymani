/*
 * beymani: Outlier and anamoly detection 
 * Author: Pranab Ghosh
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you
 * may not use this file except in compliance with the License. You may
 * obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0 
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package org.beymani.predictor;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.chombo.util.Utility;

/**
 * Predictor based on markov model
 * @author pranab
 *
 */
public class MarkovModelPredictor extends ModelBasedPredictor {
	private String[] states;
	private double[][] stateTranstionProb;
	private Map<String, List<String>> records = new HashMap<String, List<String>>(); 
	private Map<String, List<String>> stateSequences = new HashMap<String, List<String>>(); 
	private int stateSeqWindowSize;
	
	public MarkovModelPredictor(Map conf)   {
		states = conf.get("model.states").toString().split(",");
		String stateTranFile = conf.get("model.stateTransition.file").toString();
		int size = states.length;
		stateTranstionProb = new double[size][size];
		
		Scanner scanner;
		try {
			scanner = new Scanner(new FileInputStream(stateTranFile));
			 while (scanner.hasNextLine()){
			        String line = scanner.nextLine();
			        Utility.deseralizeTableRow(stateTranstionProb, line, ",",size,size);
			 }      		
		} catch (FileNotFoundException e) {
			throw new IllegalArgumentException("Failed to open state transition probability file");
		}

		 scanner.close();
		 stateSeqWindowSize =  Integer.parseInt(conf.get("state.seq.window.size").toString());
	}

	@Override
	public double execute(String entityID, String record) {
		List<String> recordPair = records.get(entityID);
		if (null == recordPair) {
			recordPair = new ArrayList<String>();;
		}
		recordPair.add(record);
		if (recordPair.size() > 2) {
			recordPair.remove(0);
		}
		
		String state = null;
		if (recordPair.size() == 2) {
			//get state
		}
		
		//state sequence
		List<String> stateSeq = stateSequences.get(entityID);
		if (null == stateSeq) {
			stateSeq = new ArrayList<String>();
		}
		stateSeq.add(state);
		if (stateSeq.size() > stateSeqWindowSize) {
			stateSeq.remove(0);
		}
		
		double score = getScore( stateSeq);
		return score;
	}
	
	private double getScore(List<String> stateSeq) {
		
		return 0; 
	}

}
