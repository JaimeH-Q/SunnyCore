package jaime.uno.utils.placeholders;

import java.util.List;
import java.util.Map;

public class EmptyItemPlaceholderReplacer implements PlaceholderReplacer {

    @Override
    public List<String> replaceList(List<String> list) {
        return list;
    }

    @Override
    public String replaceString(String string) {
        return string;
    }
}
