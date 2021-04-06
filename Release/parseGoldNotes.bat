SET inputbin="F:\LINBIN\Melax\releasetool\Release\data\input\"
set inputbin=%inputbin:"=%
SET goldbin="F:\LINBIN\Melax\releasetool\Release\data\gold\"
set goldbin=%goldbin:"=%
SET urlstring="http://3.129.5.22:8089/query/xmi"
set urlstring=%urlstring:"=%

java -Dfile.encoding=utf-8 -jar releasetest.jar -a parse -i %inputbin% -o %goldbin% -url %urlstring%




