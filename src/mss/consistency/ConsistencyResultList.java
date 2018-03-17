package mss.consistency;

import mtd.priority.FactoryPriorityMethod;

import java.util.ArrayList;

public class ConsistencyResultList extends ArrayList<ConsistencyResult>{
    
    public AbstractConsistencyMethod getForType(
            FactoryConsistencyMethod.ConsistencyMethodEnum consistencyMethodType,
            FactoryPriorityMethod.PriorityMethodEnum priorityMethodType){
        ConsistencyResult consistencyResult;
        for(int i = 0; i < size(); i++){
            consistencyResult = get(i);
            if(consistencyResult.isType(consistencyMethodType, priorityMethodType)){
                return consistencyResult.getMethod();
            }
        }
        return null;
    }
}
