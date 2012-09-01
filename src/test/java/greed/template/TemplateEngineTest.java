package greed.template;

import com.floreysoft.jmte.AnnotationProcessor;
import com.floreysoft.jmte.Engine;
import com.floreysoft.jmte.TemplateContext;
import com.floreysoft.jmte.token.AnnotationToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TemplateEngineTest {
    public static void main(String[] args) {
        Engine engine = new Engine();

        engine.registerAnnotationProcessor(new AnnotationProcessor<String>() {
            @Override
            public String getType() {
                return "testanno";
            }

            @Override
            public String eval(AnnotationToken annotationToken, TemplateContext templateContext) {
                return annotationToken.getReceiver() + " " + annotationToken.getArguments();

            }
        });

        Map<String, Object> model = new HashMap<String, Object>();
        List<String> list = new ArrayList<String>();
        list.add("1");
        list.add("2");
        model.put("list", list);
        String template = "${@testanno arg1,arg2}${foreach list li ,}${li}${end}";
        String result = engine.transform(template, model);
        System.out.println(result);
    }
}
