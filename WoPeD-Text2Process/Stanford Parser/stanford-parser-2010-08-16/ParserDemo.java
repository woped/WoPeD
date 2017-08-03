import java.util.*;
import edu.stanford.nlp.trees.*;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;

class ParserDemo {
  public static void main(String[] args) {
    LexicalizedParser lp = new LexicalizedParser("englishPCFG.ser.gz");
    lp.setOptionFlags(new String[]{"-maxLength", "80", "-retainTmpSubcategories"});

    String[] sent = { "This", "is", "an", "easy", "sentence", "." };
    Tree parse = (Tree) lp.apply(Arrays.asList(sent));
    parse.pennPrint();
    System.out.println();

    TreebankLanguagePack tlp = new PennTreebankLanguagePack();
    GrammaticalStructureFactory gsf = tlp.grammaticalStructureFactory();
    GrammaticalStructure gs = gsf.newGrammaticalStructure(parse);
    Collection tdl = gs.typedDependenciesCollapsed();
    System.out.println(tdl);
    System.out.println();

    TreePrint tp = new TreePrint("penn,typedDependenciesCollapsed");
    tp.printTree(parse);
  }

}
