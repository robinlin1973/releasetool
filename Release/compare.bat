SET predictbin="F:\LINBIN\Melax\releasetool\Release\data\predict\"
set predictbin=%predictbin:"=%
SET goldbin="F:\LINBIN\Melax\releasetool\Release\data\gold\"
set goldbin=%goldbin:"=%
SET reportbin="F:\LINBIN\Melax\releasetool\Release\data\report\"
set reportbin=%reportbin:"=%

java -Dfile.encoding=utf-8 -jar releasetest.jar -a compare -i %predictbin% -o %goldbin% -r %reportbin% 