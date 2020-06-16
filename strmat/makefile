include config.mk

all: enhanced_and_extended_suffix_arrays_regenfuss_2020.pdf
	latex enhanced_and_extended_suffix_arrays_regenfuss_2020.tex
	bibtex enhanced_and_extended_suffix_arrays_regenfuss_2020.aux
	latex enhanced_and_extended_suffix_arrays_regenfuss_2020.tex
	latex enhanced_and_extended_suffix_arrays_regenfuss_2020.tex
	pdflatex enhanced_and_extended_suffix_arrays_regenfuss_2020.tex

clean:
	rm -f *.aux *.bbl *.bcf *.blg *.dvi *.log *.run.xml *.toc

.PHONY: clean
