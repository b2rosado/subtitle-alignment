use 5.010;
use strict;
use warnings;

my $filename = $ARGV[0];
my $file;
open($file, '<:encoding(iso-8859-1)', $filename)
or
die "ERROR: Could not open file '$filename' $!";

my $text = "";
my $header = "";

sub printText{
	if($text =~ m/^$/){									# if empty text, do not print header
		$header = "";
	}else{
		print $header;
		$header = "";
		$text =~ s/([\.]{3}|[^.!?])$/$1>>/g;
		print $text . "\n\n";
		$text = "";
	}
}

sub appendText{
	chomp($_[0]);

	if( $_[0] !~ m/^-$/){								# do not append deleted DESCRIPTIONS
		$text = $text . " " . $_[0];
		$text =~ s/([\.]+ )[\.]+/$1/g;
		$text =~ s/^ //g;
	}
}

foreach my $line (<$file>){
	$line =~ s/^(?:\357\273\277)//g; 					# remove invisible chars
	$line =~ s/\r//g;									# remove invisible line breaks
	$line =~ s/<[\/\w\s="#]+>//g;						# remove tags
	#$line =~ s/\[?[A-Z].+\]?\n?.+[A-Z]{5,}.+\]?//g;	# remove DESCRIPTIONS old
	$line =~ s/^[A-Z ]{3,}:(.+)$/$1/;					# remove DESCRIPTIONS and keep the rest
	$line =~ s/^\[.+\]$|^[A-Z].+[A-Z]{5,}.+$|^[A-Z ]{3,}:$/-/;	# remove DESCRIPTIONS (not a empty line)

	if(	$line =~ m/^[0-9]+$/ or		# id line
		$line =~ m/-->/){			# timestamp line
		$header = $header.$line;	# store header
	}else{
		if($line =~ m/^$/){			# empty line
			printText();			# print entire block
		}else{
			appendText($line);		# there is text to append
		}
	}
}
printText();
close($file);
