if [ "$1" == "project" ]
then
        cd ~
        mkdir public_html
        cp binary_converter.c index.html public_html
        chmod 755 public_html
        cd public_html
        chmod 755 index.html
elif [ "$1" == "add" ]
then
        cd ~
        cd public_html
        git init
        git add binary_converter.c index.html
        git commit -m "add"
elif [ "$1" == "compile" ]
then
        cd ~
        cd public_html
        gcc -o binary_converter.cgi binary_converter.c
        chmod 755 binary_converter.cgi
else
        cd ~
        mkdir public_html
        cp binary_converter.c index.html public_html
        chmod 755 public_html
        cd public_html
        chmod 755 index.html
        cd ~
        cd public_html
        git init
        git add binary_converter.c index.html
        git commit -m "add"
        cd ~
        cd public_html
        gcc -o binary_converter.cgi binary_converter.c
        chmod 755 binary_converter.cgi
~                                                                                                                     
~                                                                                                                     
~                                    
