package com.tkmtwo.hc.header;

import static com.google.common.base.TkmTwoStrings.isBlank;

import com.google.common.base.Splitter;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.http.HttpHeaders;



/**
 *
 */
public final class HeaderSplitter {
  
  private static final Logger logger = LoggerFactory.getLogger(HeaderSplitter.class);

  public static final Splitter LIST_SPLITTER =
    Splitter.on("&&")
    .trimResults()
    .omitEmptyStrings();
  public static final Splitter NAMEVALUE_SPLITTER =
    Splitter.on("=")
    .limit(2)
    .trimResults()
    .omitEmptyStrings();
  
  
  public static HttpHeaders split(String headerSpec) {
    HttpHeaders hh = new HttpHeaders();
    if (isBlank(headerSpec)) { return hh; }
    
    for (String nvSpec : LIST_SPLITTER.split(headerSpec)) {
      List<String> nvList = NAMEVALUE_SPLITTER.splitToList(nvSpec);
      if (nvList.size() != 2) { continue; }
      
      if (isBlank(nvList.get(0)) || isBlank(nvList.get(1))) { continue; }
      logger.trace("Adding static {}:{}", nvList.get(0), nvList.get(1));
      hh.add(nvList.get(0), nvList.get(1));
    }

    return hh;
  }
      
      
  
  private static String evalSpel(ExpressionParser parser,
                                 EvaluationContext context,
                                 String spelExpression) {
    String es = null;
    es = (String) parser.parseExpression(spelExpression).getValue(context);
    return es;
  }
  
  public static HttpHeaders split(ExpressionParser parser,
                                  EvaluationContext context,
                                  String headerSpec) {
    HttpHeaders hh = new HttpHeaders();
    if (isBlank(headerSpec)) { return hh; }

    HttpHeaders hhSpecs = split(headerSpec);
    for (Map.Entry<String, List<String>> me : hhSpecs.entrySet()) {
      String hnSpec = me.getKey();
      String hn = evalSpel(parser, context, hnSpec);
      for (String hvSpec : me.getValue()) {
        String hv = evalSpel(parser, context, hvSpec);
        logger.trace("Adding evaled {}:{}", hn, hv);
        hh.add(hn, hv);
      }
    }
    return hh;
  }
  
  
  
}
