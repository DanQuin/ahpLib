package mtd.priority;

import java.util.ArrayList;

public class PriorityResultList extends ArrayList<PriorityResult> {
    
    private int haveThisMethod(FactoryPriorityMethod.PriorityMethodEnum priorityMethodType){
        for(int i = 0; i < size(); i++){
            if(get(i).getType() == priorityMethodType){
                return i;
            }
        }
        return -1;
    }
    
    public AbstractPriorityMethod getForType(FactoryPriorityMethod.PriorityMethodEnum type){
        int idx = haveThisMethod(type);
        if(idx < 0) return null;
        return get(idx).getMethod();
    }
}