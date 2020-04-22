all:
	find . -name "*.java" | javac -encoding utf8 @/dev/stdin
	
run:
	java Test

clean:
	find . -name "*.class" -exec rm {} \;
	find . -name "*~" -exec rm {} \;