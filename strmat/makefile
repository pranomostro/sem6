include config.mk

all: enhanced_and_extended_suffix_arrays_regenfuss_2020.pdf presentation.pdf

enhanced_and_extended_suffix_arrays_regenfuss_2020.pdf: enhanced_and_extended_suffix_arrays_regenfuss_2020.tex
	latex enhanced_and_extended_suffix_arrays_regenfuss_2020.tex
	bibtex enhanced_and_extended_suffix_arrays_regenfuss_2020.aux
	latex enhanced_and_extended_suffix_arrays_regenfuss_2020.tex
	latex enhanced_and_extended_suffix_arrays_regenfuss_2020.tex
	pdflatex enhanced_and_extended_suffix_arrays_regenfuss_2020.tex

presentation.pdf: presentation.tex
	bibtex presentation.aux
	pdflatex presentation.tex

clean:
	rm -f *.bbl *.bcf *.blg *.dvi *.log *.run.xml *.toc *.nav *.out *.snm

.PHONY: clean arbeit presentation
