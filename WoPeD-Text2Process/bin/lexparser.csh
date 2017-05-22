#!/bin/csh -f
#
# Runs the English PCFG parser on one or more files, printing trees only
# usage: ./lexparser.csh fileToparse+
#
set scriptdir=`dirname $0`
java -mx150m -cp "$scriptdir/stanford-parser.jar:" edu.stanford.nlp.parser.lexparser.LexicalizedParser -outputFormat "penn,typedDependencies" $scriptdir/englishPCFG.ser.gz $*
