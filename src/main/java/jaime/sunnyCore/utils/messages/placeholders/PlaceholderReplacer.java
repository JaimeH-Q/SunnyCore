package jaime.uno.utils.placeholders;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface PlaceholderReplacer {

    default List<String> replaceList(List<String> list){
        List<String> stringList = new ArrayList<>();
        for(String s : list){
            stringList.add(replaceString(s));
        }
        return stringList;
    }
    String replaceString(String string);
}
