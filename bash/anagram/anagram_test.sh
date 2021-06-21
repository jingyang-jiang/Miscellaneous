#!/bin/bash
clear
echo -e  "silent\nlisten\n" > input1.txt
./anagram < input1.txt
if [ $? -eq 0 ]
then
        echo "Test succeeded."
else
        echo "Test failed."
fi
echo -e "listen\nsilence\n" > input2.txt
./anagram <input2.txt
if [ $? -eq 1 ]
then
        echo "Test succeeded."
else
        echo "Test failed." 
fi
echo "Execution of anagram program has ended successfully!"
exit 1
