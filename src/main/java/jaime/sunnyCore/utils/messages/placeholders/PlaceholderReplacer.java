package jaime.sunnyCore.utils.messages.placeholders;

import java.util.ArrayList;
import java.util.List;

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
