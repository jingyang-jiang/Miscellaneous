#!/bin/bash
clear
cd $home
if [[ -d $1 ]]
then
        echo "Directory found."
else
        mkdir $1
        echo -e "New directory $1 created."
fi
cd $1
if [[  -f extractor.log ]]
then
        echo "extractor.log found"
else
       touch extractor.log
       echo "extractor.log created."
fi
if  [[  -f Information ]]
then
        echo "Information file found."
        echo "Information file already exists." >> extractor.log
else
        touch Information
        echo "Information file created." 
        echo "Information file is created." >> extractor.log
fi
chmod 640 Information
echo "Access right changed for Information file." >> extractor.log
who | grep '[abcd]'| grep -o '^[^ ]*' >> Information
echo "List of user names has been added to the Information file." >> extractor.log
Y=`who | grep -c '[abcd]'`
echo -e "The number of found users is: $Y." >> extractor.log
echo "Execution of InformationExtractor script has ended successfully!"
exit 1
