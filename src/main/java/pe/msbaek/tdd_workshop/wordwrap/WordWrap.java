package pe.msbaek.tdd_workshop.wordwrap;

public class WordWrap {
    
    public static String wrap(String text, int width) {
        if (text == null || text.isEmpty()) {
            return "";
        }
        
        if (text.length() <= width) {
            return text;
        }
        
        int spaceIndex = text.lastIndexOf(' ', width);
        if (spaceIndex != -1) {
            return text.substring(0, spaceIndex) + "\n" + wrap(text.substring(spaceIndex + 1), width);
        }
        
        // 공백이 없으면 강제로 자름
        return text.substring(0, width) + "\n" + wrap(text.substring(width).trim(), width);
    }
}
