# Alignerzilla - Subtitle Aligner

This repository contains Alignerzilla - a movie subtitle aligner - and some additional tools that have been developed to use with it.

This subtitle aligner was developed to obtain a Master Thesis in Information Systems and Computer Engineering.
In this repository it is also available the dissertation that describes the work done, explaining all the decisions.

**If you intend to use anything from this repository, please make a reference to this document.**

This repository contains three different directories:
* `pre-processing-scripts/` is used to remove noise and normalize the subtitle files, so that the aligner can use them.
* `alignerzilla/` contains the actual subtitle aligner. Alignerzilla expects the input data to be pre-processed by the aforementioned scripts.
* `dataset/` contains the subtitles (in raw state) and reference alignments we have used to evaluate the aligner.

## 1) How use *pre-processing scripts*
The **pre-processing-scripts** require an environment with **Bash** and **Perl**.
* First, navigate to `pre-processing-scripts/` directory;
* Then, you should place the raw subtitle files that you want to pre-process inside the folder `input/`;
* Finally, execute the following command: `$ sh normalizeAll.sh` or `$ ./normalizeAll.sh`.

If everything runs as expected, you should now have all the pre-processed subtitle files inside `output/` folder.

**Note:** the runtime of the *pre-processing-scripts* depends on the used machine and how many subtitles you want to normalize. It might take a while for many subtitles.

## 2) How to use *Alignerzilla*
**Alignerzilla** requires an environment with **Apache Ant** and **Java 7**. Also, make sure the subtitles you provide as input are already pre-processed by **pre-processing-scripts**.
* First, make sure you've followed the instructions in **1)**;
* Then, navigate to `alignerzilla/` directory;
* Now, you should place the pre-processed subtitle files inside the folder `data/`;
    * The subtitles placed in `data/`directory **should all be named as** `MovieName-LANGUAGE.srt`.
        * For example: `lotr-EN.srt` or `TheDarkKnight-PT.srt`.
    * As the goal is to align subtitle pairs, **every movie must have two different subtitle files, with different language tags**.
        * For example: `lotr-EN.srt` and `lotr-PT.srt`.
        * If you want to use same language subtitles `lotr-EN1.srt` and `lotr-EN2.srt` can be used.
* Then, make sure `list.txt` contains a list with the filenames of the subtitles you want to align.
    * One subtitle file per line.
    * If you want to aligner every subtitle in `data/`you can simply use the following command:
    `$ ls data/ > list.txt`
* Finally, execute one of the following commands: `$ sh run-alignerzilla.sh` or `$ ./run-alignerzilla.sh`.

If everything runs as expected, you should now have the aligned subtitles inside `results/` folder.

You can configure additional parameters editing `alignerzilla.config`.

**Note:** the runtime of *Alignerzilla* depends on the used machine and how many subtitles you want to align. It might take a while for many subtitles.