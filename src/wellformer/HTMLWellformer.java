package wellformer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HTMLWellformer {
    public static String makeWellformed(String src) {
        src = removeMiscellaneousTags(getBody(src));

        XMLSyntaxChecker xmlSyntaxChecker = new XMLSyntaxChecker();
        src = xmlSyntaxChecker.check(src);

        src = getBody(src);
        return src;
    }

    private static String getBody(String src) {
        String result = src;
        String expression = "<body.*?</body>";
        Matcher matcher = Pattern.compile(expression).matcher(result);
        if (matcher.find()) {
            result = matcher.group(0);
        }
        return result;
    }

    private static String removeMiscellaneousTags(String src) {
        return src
                .replaceAll("<script.*?</script>", "")
                .replaceAll("<!--.*?-->", "")
                .replaceAll("&nsbp;?", "");
    }
}
