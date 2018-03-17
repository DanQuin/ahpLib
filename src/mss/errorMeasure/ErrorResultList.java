package mss.errorMeasure;

import mtd.priority.FactoryPriorityMethod;

import java.util.ArrayList;

public class ErrorResultList extends ArrayList<ErrorResult>{
    
    
    public int haveThisMethod(
            FactoryErrorMethod.ErrorMethodEnum errorMethodType,
            FactoryPriorityMethod.PriorityMethodEnum priorityMethodType){
        for(int i = 0; i < size(); i++){
            if(get(i).getMethod().getType() == errorMethodType &&
                    get(i).getPriorityMethod().getType() == priorityMethodType){
                return i;
            }
        }
        return -1;
    }
    
    public AbstractErrorMeasureMethod getForType(
            FactoryErrorMethod.ErrorMethodEnum errorMethodType, FactoryPriorityMethod.PriorityMethodEnum priorityMethodType){
        for(int i = 0; i < size(); i++){
            ErrorResult errorResult = get(i);
            if(errorResult.getMethod().getType() == errorMethodType &&
                    errorResult.getPriorityMethod().getType() == priorityMethodType){
                return errorResult.getMethod();
            }
        }
        return null;
    }
}
