CEDICT.DOC - README file for CEDICT - Chinese-English Dictionary
Date: 21 December 2005Preface 
~~~~~~~ 

Sometime in 2000 the web page of the originator of the CEDICT project,
Paul Denisowski, disappeared from the web and his e-mail addresses
stopped working.  To continue the work, I have taken the last versions
of the dictionary and have started correcting and adding to them with
the same intent as Paul, to provide a freely available, searchable
Chinese/English dictionary.  Below is the original README file from
Paul.

Introduction
~~~~~~~~~~~~
The objective of the CEDICT project is to create an online, downloadable
(as opposed to searchable-only) public-domain Chinese-English dictionary.
For the most part, the project is modelled on Jim Breen's highly successful
EDICT (Japanese-English dictionary) project and is intended to be a
collaborative effort, with users providing entries and corrections to
the main file.  For specific limitations regarding its use, please see
the CEDICT license included in Appendix A of this document.

History
~~~~~~~
Since the project was only started in October 1997, it's probably a bit
of a stretch to say that it has a "history".  Actually, the project
in part grew out of my (very limited) involvement in the EDICT project,
which gave me some very useful practice in compiling dictionary entries.
After beginning the PhD program in Linguistics at UNC-CH, I had decided
to dust off my (very dusty) Chinese to help in my academic research
of Asian languages (particularly Vietnamese).  To this end I began
to keep a list of words I encountered in my reading, and this list
formed the original (c. 500 entry) CEDICT, which I first posted to my
UNC web site (http://www.unc.edu/~pauld) sometime in November 1997.  Since
then the list has grown at a fairly steady pace.  In April 1998, the CEDICT
project was moved to a new location (www.mindspring.com/~paul_denisowski).
In May 1998, CEDICT began to be mirrored at the Monash Nihongo ftp archive
(ftp.cc.monash.edu.au/pub/nihongo/) thanks to the very generous effort of
Jim Breen.  Now it is hosted at http://www.mandarintools.com/cedict.html

Contributors
~~~~~~~~~~~~
Although CEDICT started out as a one-person project, contributions from
the Internet community have become the major source of new entries.
Contributors thus far to the project are (in chronological
order):

   Ocrat, Mike Wright, Wenke Wei, Sharlene Liu, Richard Warmington,
   Erik Peterson, Derek Chadwick, Dave Hiebeler, Steve Swales, Carl Hoffman

(Please let me know if I've left someone off the list)

Another important contributor is the creator of NJSTAR, Hongbo Ni
(hongbo@njstar.com.au : http://www.njstar.com.au) who provided 
the tools needed to generate the NJSTAR dictionary index files.  
Although I originally used a home-brew Perl script to do this, 
the newer method is a _substantial_ and much appreciated improvement 
over my own meager efforts.

Last, but certainly not least of the CEDICT contributors is Jim Breen,
who not only provided the inspiration for CEDICT through the highly
successful EDICT project, but who has also very graciously allowed 
CEDICT to be mirrored at the Monash Nihongo ftp site 
(ftp.cc.monash.edu.au/pub/nihongo/).

Contribution Guidelines

~~~~~~~~~~~~~~~~~~~~~~~

Contributions are always warmly welcome.

THE TECHNICAL GUIDELINES

The CEDICT format is:
     CHINESE [pinyin] /English definition 1/English definition 2/.../
Please send entries in Big5 encoding

Pinyin tones should be indicated by numbers 1-5, as follows:
   1=level tone, 2=rising tone, 3=mid-rising tone, 4=falling tone, 5=neutral tone

Please indicate the neutral tone (5) (e.g. xue2 sheng5 instead of xue2 sheng)

I've also been seperating pinyin syllables with a space for
readability, e.g. [zhong1 guo2] instead of [zhong1guo2]

Avoid using square brackets for anything but pinyin.  Use parenthesis
instead: e.g. /Beijing (capital of mainland China)/ and not /Beijing
[....]/

Avoid using hyphens (-) in the pinyin

I'm also trying to keep all the pinyin lowercase, even for proper nouns.

Please mail all contributions to cedict@chinesetools.com

THE NON-TECHNICAL GUIDELINES

In order to preserve the public-domain/freeware aspect of this dictionary, please do not send in entries from copyrighted sources, especially from electronic media.  This also includes web sites with copyrighted material.  If in doubt, please ask  the copyright owner first.

Please make sure to check your contributions against the latest
version of CEDICT.  Editing for duplicates has become a more serious
issue as the dictionary grows in size.  If you want to submit
additional definitions for existing entries, please submit ONLY the
new definition in the normal CEDICT format (I have a script that will
merge them).

Please try to observe the CEDICT format.  Again, I don't mind writing
scripts to clean up contributions that don't meet the above format,
but it's impractical to do this except for very large files

Please be careful about pinyin tones, especially when it comes to
hanzi that change tone (such as "bu" - not, or "yi" - one) and final
hanzi (as in "xue2 sheng5").  Since I'm not a native speaker I have to
rely on the goodwill of others to correct pinyin/hanzi errors.

Although I've gotten some suggestions about splitting CEDICT up into
specialized dictionaries (esp for proper names and technical terms, as
EDICT has done), for the time being, there's no need to seperate
entries by subject matter.
  
Revision History (since 16 December 1997)
~~~~~~~~~~~~~~~~~
Version:

16 December 1997
   1337 entries 
   Contributions/Corrections by Paul Denisowski
17 December 1997
   1677 entries
   Contributions/Corrections by Paul Denisowski, Ocrat 
28 December 1997
   2077 entries
   Contributions/Corrections by Paul Denisowski
2 January 1998
   2115 entries
   Contributions/Corrections by Paul Denisowski
   NJSTAR dictionary format version now supplied using tools
   provided by NJSTAR's creator, Hongbo Ni 
9 January 1998
   2751 entries
   Corrections by Mike Wright
   Contributions/Corrections by Paul Denisowski
20 January 1998
   3252 entries
   Corrections by Ocrat
   Contributions/Corrections by Paul Denisowski
01 February 1998
   3947 entries
   Contributions/Corrections by Paul Denisowski
14 March 1998
   4570 entries
   Corrections by Wenke Wei and Sharlene Liu 
   Contributions/Corrections by Paul Denisowski
29 March 1998
   7419 entries
   VOA vocabulary files contributed by Ocrat (c. 4500 entries)
   Contributions/Corrections by Paul Denisowski
06 April 1998
   Project moves to new website:  www.mindspring.com/~paul_denisowski/cedict.html
   7720 entries
   Contributions/Corrections by Richard Warmington, Paul Denisowski
11 April 1998
   7850 entries
   Contributions/Corrections by Paul Denisowski
24 April 1998
   8447 entries
   Contributions/Corrections by Erik Peterson, Paul Denisowski
4 May 1998
   11349 entries
   Over 3000 entries contributed by Derek Chadwick
   Contributions/Corrections by Paul Denisowski
11 May 1998
   11564 entries
   Contributions/Corrections by Richard Warmington, Paul Denisowski
   Jim Breen adds CEDICT to Monash Nihongo ftp archive:
   ftp.cc.monash.edu.au/pub/nihongo/
01 June 1998
   12132 entries
   Contributions/Corrections by Ocrat, Paul Denisowski
05 July 1998
   12221 entries
   Contributions/Corrections by Dave Hiebeler, Steve Swales, Carl Hoffman, Paul Denisowski
1 Septemeber 1998
   16830 entries
   Contributions/Corrections by Dave Hiebeler, Erik Peterson, Paul Denisowski
1 November 1998
   23510 entries
   Merged in public-domain cchelp file (Author: Stephen G. Simpson simpson@math.psu.edu)
2 September 2000
   23484 entries
   Fixed formatting errors, regularized the way u: is represented,
   added a 5 to indicate light tone on pinyin with no tone number,
   reordered dictionary by pinyin, and removed some duplicate entries.
5 January 2001
   23481 entries
   Made corrections and removed duplicate entries suggested by Richard
   Warmington.
26 June 2002
   23483 entries
   Ran the dictionary through a spell checker and fixes dozens of
   English definition spelling errors.
9 January 2003
  B5:  Unique Words: 22947, Definitions: 23519
   Added in contributions from Sebastien Bruggeman and others.  Fixed
   spelling and pinyin errors. Removed duplicate entries.
23 January 2003
  GB   Words: 21544, Defs: 22346
  Big5 Words: 23820, Defs: 24400
  Added in some wordlists.  Fixed some errors in creation of GB
  version.
28 January 2003
  GB   Words: 23243, Defs: 24063
  Big5 Words: 25541, Defs: 26120
  Added in a huge wordlist from Ron Grenier (Thanks Ron!)
07 February 2003
  GB   Words: 23316, Defs: 24149
  Big5 Words: 25616, Defs: 26206
  Added in Chinese titles for various family relations.
27 February 2003
  GB   Words: 23451, Defs: 24285
  Big5 Words: 25746, Defs: 26343
  Added in fixes and new words from Eric Goodell (Thanks Eric!)
28 April 2003
  GB   Words: 23494, Defs: 24328
  Big5 Words: 25789, Defs: 26387
  Various corrections and new words from the news.
30 May 2003
  GB   Words: 23512, Defs: 24345
  Big5 Words: 25807, Defs: 26404
  Various corrections and new words from the news.
23 October 2004
  GB   Words: 23722, Defs: 24556
  Big5 Words: 26017, Defs: 26617
  Country and city names contributed by Jack Halpern.
8 January 2005
  GB   Words: 23825, Defs: 24665
  Big5 Words: 26120, Defs: 26726
  New words and corrections from Steven Daniels, EuroAsiaSoftware, David Rhys Jones, Matthew Fischer, and Xianwen Cao
11 April 2005
  UTF8 Defs: 27085
  GB   Words: 24210, Defs: 25027
  Big5 Words: 26452, Defs: 27085
  New words and corrections from Michael Burkhardt, Micah, Steven Daniels, Vincent Ramos, Matthew Fischer, Simon Rapp, John Jenkins, Erik Peterson, David Lancashire, Luiz Borges, Brian Schack.
11 June 2005
  UTF8 Defs:  28712
  GB   Words: 25802, Defs: 26650
  Big5 Words: 28029, Defs: 28694
  New words and corrections from Dennis Vierkant, Steven Daniels, Robert Wills, James Worsley, Erik Peterson.
9 August 2005
  UTF8 Defs:  28805
  GB   Words: 25897, Defs: 26743
  Big5 Words: 28124, Defs: 28787
  New entries and corrections from Erik Peterson, Andreas Graf, Michael Burkhardt, James Corey, Bryan Chen, Matthias Langer.
3 September 2005
  UTF8 Defs:  29079
  GB   Words: 26210, Defs: 26968
  Big5 Words: 28427, Defs: 29059
  Corrections and new entries from Erik Peterson, Ron Molenda, Tim Lindeman.  Tibetan place names from Jan Zapotocky.  Corrected some trad/simp issues due to new UTF-8 format.
1 October 2005
  UTF8 Defs:  30546
  GB   Words: 27610, Defs: 28411
  Big5 Words: 29869, Defs: 30499
  Various terms from the news and the sciences.  Corrections and new entries from Erik Peterson, Ron Molenda, Matthias Langer.
6 November 2005
  UTF8 Defs:  30649
  GB   Words: 27780, Defs: 28546
  Big5 Words: 29974, Defs: 30612
  New and corrected entries from Erik Peterson, Ron Molenda, and Stephan Hodges.  Added country and state names.  Introducing capitalized pinyin for proper names.
21 December 2005
  UTF8 Defs:  34608
  GB   Words: 31200, Defs: 31992
  Big5 Words: 33903, Defs: 34553
  Many thanks to Mark Swofford for a huge set of China and Taiwan place names.  New entries and corrections from Ron Molenda and Erik Peterson.  Many new entries from Brian Schack (assisted by Becky Chou, Carolina Chang, Cecilia Shih, Ching Lee, Christie Qiu, Eva Yan, Grace Tseng, Harriet Chou, Jill Cheng, Kathy Chen, Rebecca Ying, Teresa Liang and Zoe Way).
=
==========================================================================

CEDICT LICENCE STATEMENT

Copyright (C) 1997, 1998 Paul Andrew Denisowski

This  licence  statement  and  copyright  notice   applies   to   the   CEDICT 
Chinese/English   Dictionary   file,   the   associated  documentation  file 
CEDICT.DOC, and any data files which are derived from them. 

COPYING AND DISTRIBUTION 

Permission is granted to make and distribute verbatim copies of  these  files 
provided  this copyright notice and permission notice is distributed with all 
copies.  Any distribution of the files must take place  without  a  financial 
return, except a charge to cover the cost of the distribution medium. 

Permission is granted to make and distribute extracts or subsets of the CEDICT 
file under the same conditions applying to verbatim copies. 

Permission  is  granted  to  translate the English elements of the CEDICT file 
into other languages, and to make and distribute copies of those translations 
under the same conditions applying to verbatim copies. 

USAGE 

These files may be freely  used  by  individuals,  and  may  be  accessed  by 
software belonging to, or operated by, such individuals. 

The files, extracts from the files, and translations of the files must not be 
sold  as  part  of  any  commercial  software  package,   nor  must  they  be 
incorporated in any published dictionary or other  printed  document  without 
the specific permission of the copyright holder. 

COPYRIGHT 

Copyright  over  the  documents  covered  by  this statement is held by 
Paul Denisowski.


