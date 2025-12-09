package jaime.sunnyCore.utils.messages.placeholders;

import java.util.List;

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
