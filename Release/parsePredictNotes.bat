SET inputbin="F:\LINBIN\Melax\releasetool\Release\data\input\"
set inputbin=%inputbin:"=%
SET predictbin="F:\LINBIN\Melax\releasetool\Release\data\predict\"
set predictbin=%predictbin:"=%
SET urlstring="http://127.0.01:8080/query/xmi"
set urlstring=%urlstring:"=%

java -Dfile.encoding=utf-8 -jar releasetest.jar -a parse -i %inputbin% -o %predictbin% -url %urlstring%




