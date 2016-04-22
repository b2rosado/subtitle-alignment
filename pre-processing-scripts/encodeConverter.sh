#!/bin/bash

ENCODING=$(file -bi $1 | sed -r 's/.*=//g')

if [ "$ENCODING" == "unknown-8bit" ]; then
	iconv -f iso-8859-1 -t utf-8 $1
elif [ "$ENCODING" == "utf-8" ]; then
	cat $1
else
	iconv -f $ENCODING -t utf-8 $1
fi
