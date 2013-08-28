#!/bin/bash
echo "generating all possible classfications and with possible combinations of binerization techniques"
for i in 0 1
 do
  for j in {0..15}
    do
      java -jar binerize.jar $i $j      
    done
 done
echo "finished binerization"

echo "formatting binerized values in to tab separated values"
for file in $(ls *.moses)
 do 
 sed -i 's/"//g' $file    #delete all "
 sed -i 's/\,/\t/g' $file #replace all , with tab
 done
echo "finished formatting"

echo "finished all tasks"
