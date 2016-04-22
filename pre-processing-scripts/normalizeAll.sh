SUBTITLE_FILES=$(ls input);

mkdir -p output/tmp;

for file in $SUBTITLE_FILES; do
	bash encodeConverter.sh input/$file > output/tmp/$file;
	perl normalizeSRT.pl output/tmp/$file > output/$file;
done

rm -r output/tmp;
