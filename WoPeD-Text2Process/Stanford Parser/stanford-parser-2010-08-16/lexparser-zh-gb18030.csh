#!/bin/csh -f
#
# Runs the Chinese Factored parser on one or more files which are already word
# segmented, with one sentence per line, printing trees and deps in a simple
# format.  Input and output is in GB18030.
#
# usage ./lexparser-zh-gb18030.csh fileToparse*
#
set scriptdir=`dirname $0`
java -mx1g -cp "$scriptdir/stanford-parser.jar:" edu.stanford.nlp.parser.lexparser.LexicalizedParser -tLPP edu.stanford.nlp.parser.lexparser.ChineseTreebankParserParams -tokenized -sentences newline -escaper edu.stanford.nlp.trees.international.pennchinese.ChineseEscaper -outputFormat "penn,typedDependencies" -outputFormatOptions "removeTopBracket" $scriptdir/chineseFactored.ser.gz $*
