#!/bin/csh -f
#
# Runs the Chinese Factored parser on one or more files which are already word
# segmented, with one sentence per line, printing tags, trees, deps in XML.
# Input and output is in UTF-8.
#
# usage: ./lexparser-zh-utf8.csh fileToparse*
#
set scriptdir=`dirname $0`
java -mx1g -cp "$scriptdir/stanford-parser.jar:" edu.stanford.nlp.parser.lexparser.LexicalizedParser -tLPP edu.stanford.nlp.parser.lexparser.ChineseTreebankParserParams -tokenized -sentences newline -escaper edu.stanford.nlp.trees.international.pennchinese.ChineseEscaper -encoding UTF-8 -outputFormat "wordsAndTags,penn,typedDependencies" -outputFormatOptions "xml,removeTopBracket" $scriptdir/chineseFactored.ser.gz $*
