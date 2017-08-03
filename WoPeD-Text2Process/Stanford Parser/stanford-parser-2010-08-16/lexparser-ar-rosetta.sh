#!/bin/bash
#
# Runs the Arabic factored parser on one or more files that are already ATB
# segmented, with one sentence per line, printing tags and trees in XML.
#
# Usage: lexparser-ar-rosetta.sh [-sgml] fileToParse*

scriptdir=`dirname $0`

ARGS="-sentences newline"

if [ $# -eq 0 -o "$1" == "-h" ]; then
 echo "Usage: $0 [-sgml] fileToParse*" >&2
 echo "-sgml: input files are in NIST MT sgml format." >&2
 echo >&2
 exit
elif [ "$1" == "-sgml" ]; then
 shift
 ARGS="-parseInside seg -sentences onePerElement"
fi

java -mx6g -cp "$scriptdir/stanford-parser.jar:" edu.stanford.nlp.parser.lexparser.LexicalizedParser -tLPP edu.stanford.nlp.parser.lexparser.ArabicTreebankParserParams -tokenized $ARGS -escaper edu.stanford.nlp.international.arabic.IBMArabicEscaper -maxLength 120 -MAX_ITEMS 500000 -writeOutputFiles -outputFormat "wordsAndTags,penn" -outputFormatOptions "xml,markHeadNodes,removeTopBracket,includePunctuationDependencies" $scriptdir/arabicFactored.ser.gz $*

